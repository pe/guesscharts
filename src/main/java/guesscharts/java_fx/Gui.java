package guesscharts.java_fx;

import guesscharts.java_fx.util.ImageViewPane;
import guesscharts.parser.Charts;
import guesscharts.parser.ChartsEntry;
import guesscharts.parser.RetryingRandomChartsParser;
import guesscharts.parser.german.GermanCharts;
import guesscharts.parser.swiss.SwissCharts;
import guesscharts.parser.uk.UKCharts;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Gui extends Application {
	private static final BoxBlur BLUR_AND_MONOCHROME = new BoxBlur(50, 50, 3);
	private static final ColorAdjust MONOCHROME = new ColorAdjust(0, -1.0, 0, 0);
	private static final List<Charts> CHARTS = List.of(new SwissCharts(), new GermanCharts(), new UKCharts());
	private static final List<Number> YEARS = IntStream.rangeClosed(1900, Calendar.getInstance().get(Calendar.YEAR))
			.boxed().collect(Collectors.toList());
	private static final List<Number> POSITIONS = List.of(1, 2, 3, 4, 5, 10, 20, 30, 40, 50, 100, 200, 300, 400, 500,
			1000);

	static {
		BLUR_AND_MONOCHROME.setInput(MONOCHROME);
	}

	private final ChartsSettings settings = new ChartsSettings();
	private final DoubleProperty volume = new SimpleDoubleProperty();
	private final BooleanProperty showSolution = new SimpleBooleanProperty(true);
	private final ObjectProperty<Status> playerState = new SimpleObjectProperty<>(Status.PAUSED);

	private static ListCell<Charts> renderCharts(ListView<Charts> lv) {
		return new ListCell<>() {
			@Override
			protected void updateItem(Charts charts, boolean empty) {
				super.updateItem(charts, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					setText(charts.toString());
					if (!charts.isConfigured()) {
						setDisable(true);
						setTextFill(Color.LIGHTGRAY);
					}
				}
			}
		};
	}

	private static ObjectBinding<Predicate<Number>> ensureBetween(final IntegerProperty min,
																  final IntegerProperty max) {
		return Bindings.createObjectBinding(() -> value -> {
			boolean greaterThanMin = value.intValue() >= min.intValue();
			boolean smallerThanMax = value.intValue() <= max.intValue();
			return greaterThanMin && smallerThanMax;
		}, min, max);
	}

	private static Text createTextFor(Property<?> property) {
		Text text = new Text();
		property.addListener((o, oldValue, newValue) -> text.setText(newValue.toString()));
		return text;
	}

	public static void main(String[] args) {
		useNativeMediaPlatformOnMacOs();
		launch(args);
	}

	private static void useNativeMediaPlatformOnMacOs() {
		if (System.getProperty("os.name").contains("Mac OS X")) {
			// There is a bug in the GST-Platform with German/UK-Charts. Songs are not
			// played on the first run.
			System.setProperty("jfxmedia.platforms", "OSXPlatform");
		}
	}

	@Override
	public void start(Stage stage) {
		settings.charts.set(CHARTS.get(0));

		ChartsEntryProperty chartsEntry = new ChartsEntryProperty();

		BorderPane root = new BorderPane();
		root.setTop(createSettings());
		root.setCenter(createSolution(chartsEntry));
		root.setBottom(createControls(chartsEntry));

		showSolution.set(false);
		chartsEntry.update(getRandomEntry());

		stage.setScene(new Scene(root));
		stage.setTitle("Charts Guesser");
		stage.show();
	}

	private Node createSettings() {
		ComboBox<Charts> chartsBox = new ComboBox<>(FXCollections.observableList(CHARTS));
		chartsBox.setCellFactory(Gui::renderCharts);
		chartsBox.valueProperty().bindBidirectional(settings.charts);

		FilteredList<Number> filteredYears = new FilteredList<>(FXCollections.observableList(YEARS));
		filteredYears.predicateProperty().bind(ensureBetween(settings.minYear, settings.maxYear));

		ComboBox<Number> yearFromBox = new ComboBox<>(filteredYears);
		yearFromBox.valueProperty().bindBidirectional(settings.yearFrom);

		ComboBox<Number> yearToBox = new ComboBox<>(filteredYears);
		yearToBox.valueProperty().bindBidirectional(settings.yearTo);

		FilteredList<Number> filteredPositions = new FilteredList<>(FXCollections.observableList(POSITIONS));
		filteredPositions.predicateProperty().bind(ensureBetween(settings.minPosition, settings.maxPosition));

		ComboBox<Number> positionFromBox = new ComboBox<>(filteredPositions);
		positionFromBox.valueProperty().bindBidirectional(settings.positionFrom);

		ComboBox<Number> positionToBox = new ComboBox<>(filteredPositions);
		positionToBox.valueProperty().bindBidirectional(settings.positionTo);

		Slider volumeSlider = new Slider(0.0, 1.0, 0.8);
		volume.bind(volumeSlider.valueProperty());

		return new ToolBar(new Label("Play the"), chartsBox, new Label("charts from"), yearFromBox, new Label("to"),
				yearToBox, new Label("and positions from"), positionFromBox, new Label("to"), positionToBox,
				new Separator(), volumeSlider);
	}

	private Node createControls(ChartsEntryProperty chartsEntry) {
		ObjectProperty<MediaPlayer> mediaPlayer = createMediaPlayer(chartsEntry);

		Button showSolution = createShowSolutionButton();
		Button next = createNextButton(chartsEntry);
		Button play = createPlayButton(mediaPlayer);
		ProgressBar progressBar = createProgressBar(mediaPlayer);

		return new ToolBar(createSpacer(), showSolution, next, play, progressBar, createSpacer());
	}

	private Pane createSpacer() {
		Pane spacer = new Pane();
		HBox.setHgrow(spacer, Priority.SOMETIMES);
		return spacer;
	}

	private ObjectProperty<MediaPlayer> createMediaPlayer(ChartsEntryProperty chartsEntry) {
		ObjectProperty<MediaPlayer> mediaPlayer = new SimpleObjectProperty<>();
		chartsEntry.audio.addListener((o, oldAudio, newAudio) -> {
			MediaPlayer player = new MediaPlayer(new Media(newAudio));
			player.setAutoPlay(true);
			player.volumeProperty().bind(volume);
			player.setOnEndOfMedia(() -> {
				// Stopping the player resets playback to start time, but is doesn't reset
				// currentTime to zero. Seeking does.
				player.seek(new Duration(0.0));
				player.stop();
			});
			playerState.bind(player.statusProperty());
			mediaPlayer.set(player);
		});
		mediaPlayer.addListener((o, oldPlayer, newPlayer) -> {
			if (oldPlayer != null) {
				oldPlayer.dispose();
			}
		});
		return mediaPlayer;
	}

	private Button createShowSolutionButton() {
		Button showSolutionButton = new Button();
		showSolutionButton.setOnAction(event -> showSolution.set(!showSolution.get()));
		showSolution.addListener((o, oldState, newState) -> showSolutionButton.setText(newState ? "Hide Solution" : "Show Solution"));
		return showSolutionButton;
	}

	private Button createPlayButton(ObjectProperty<MediaPlayer> mediaPlayer) {
		Button playButton = new Button();
		playButton.setOnAction(event -> togglePlayPause(mediaPlayer.get()));
		playerState.addListener((o, oldState, newState) -> {
			if (newState == Status.PAUSED || newState == Status.READY) {
				playButton.setText("Play");
			} else if (newState == Status.STOPPED) {
				playButton.setText("Play again");
			} else if (newState == Status.PLAYING) {
				playButton.setText("Pause");
			}
		});
		return playButton;
	}

	private void togglePlayPause(MediaPlayer player) {
		Status status = player.getStatus();
		if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
			player.play();
		} else if (status == Status.PLAYING) {
			player.pause();
		}
	}

	private Button createNextButton(ChartsEntryProperty chartsEntry) {
		Button nextButton = new Button("Next Song");
		nextButton.setOnAction(event -> {
			showSolution.set(false);
			chartsEntry.update(getRandomEntry());
		});
		return nextButton;
	}

	private ChartsEntry getRandomEntry() {
		return new RetryingRandomChartsParser(settings.charts.get().parser()).getRandomEntry(
				settings.yearFrom.getValue(), settings.yearTo.getValue(), settings.positionFrom.getValue(),
				settings.positionTo.getValue());
	}

	private ProgressBar createProgressBar(ObjectProperty<MediaPlayer> mediaPlayer) {
		ProgressBar progressBar = new ProgressBar(0.0);
		mediaPlayer.addListener((o1, oldPlayer, newPlayer) -> {
			progressBar.setProgress(0.0);
			newPlayer.currentTimeProperty().addListener(o2 -> {
				double currentTime = mediaPlayer.get().getCurrentTime().toMillis();
				double totalDuration = mediaPlayer.get().getTotalDuration().toMillis();
				progressBar.setProgress(currentTime / totalDuration);
			});
		});
		return progressBar;
	}

	private Node createSolution(ChartsEntryProperty chartsEntry) {
		ImageViewPane image = createCoverImage(chartsEntry.cover);
		VBox.setVgrow(image, Priority.ALWAYS);
		TextFlow text = createSolutionText(chartsEntry);

		VBox solution = new VBox(10, text, image);
		solution.setPadding(new Insets(10));
		showSolution.addListener((o, oldState, newState) -> {
			if (newState) {
				solution.setEffect(null);
				solution.setDisable(false);
			} else {
				solution.setEffect(BLUR_AND_MONOCHROME);
				solution.setDisable(true);
			}
		});

		return solution;
	}

	private ImageViewPane createCoverImage(StringProperty cover) {
		ImageView image = new ImageView();
		image.setSmooth(true);
		image.setPreserveRatio(true);
		cover.addListener((o, oldValue, newValue) -> image.setImage(new Image(newValue)));
		return new ImageViewPane(image);
	}

	private TextFlow createSolutionText(ChartsEntryProperty chartsEntry) {
		Text artist = createTextFor(chartsEntry.artist);
		Text title = createTextFor(chartsEntry.title);
		Text year = createTextFor(chartsEntry.year);
		Text position = createTextFor(chartsEntry.position);
		Hyperlink moreDetails = new Hyperlink("Details");
		moreDetails.setOnAction(event -> getHostServices().showDocument(chartsEntry.moreDetails.get()));

		TextFlow text = new TextFlow(artist, new Text(" – "), title, new Text(" – "), position, new Text(" in "), year,
				new Text(" –"), moreDetails);
		text.setStyle("-fx-font-size: 2em;");
		text.setTextAlignment(TextAlignment.CENTER);
		return text;
	}
}
