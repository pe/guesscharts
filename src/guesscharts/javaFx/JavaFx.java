package guesscharts.javaFx;

import guesscharts.ChartsParser;
import guesscharts.SwissChartsParser;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JavaFx extends Application {
	private static final String PLAY = "Play";
	private static final String PLAY_AGAIN = "Play again";
	private static final String PAUSE = "Pause";

	private MediaView mediaView = new MediaView();
	private Button showSolutionButton = new Button("Show Solution");
	private HBox solutions = new HBox(10);
	
	private static final BoxBlur BLUR_AND_MONOCHROME = new BoxBlur(20, 20, 3);
	private static final ColorAdjust MONOCHROME = new ColorAdjust(0, -1.0, 0, 0);
	static {
		BLUR_AND_MONOCHROME.setInput(MONOCHROME);
	}

	@Override
	public void start(Stage stage) throws NoSuchMethodException {
		ChartsParser<PropertyChartEntry> parser = new SwissChartsParser<>(PropertyChartEntry.class);
		PropertyChartEntry chartEntry = parser.chartEntry();
		chartEntry.audio.addListener((o, oldValue, newValue) -> {
			MediaPlayer mediaPlayer = new MediaPlayer(new Media(newValue));
			mediaPlayer.setAutoPlay(true);
			mediaView.setMediaPlayer(mediaPlayer);
		});

		BorderPane root = new BorderPane();
		root.setCenter(createCenter(chartEntry));
		root.setTop(createToolBar(parser));

		stage.setScene(new Scene(root));
		stage.setTitle("Chart Guesser");
		stage.setMinWidth(750);
		stage.setMinHeight(300);
		stage.setWidth(800);
		stage.show();
	}

	private Node createCenter(PropertyChartEntry chartEntry) throws NoSuchMethodException {
		ImageView image = new ImageView();
		chartEntry.cover.addListener((o, oldValue, newValue) -> image.setImage(new Image(newValue)));
		Text artist = new Text();
		chartEntry.artist.addListener((o, oldValue, newValue) -> artist.setText(newValue.toString()));
		Text title = new Text();
		chartEntry.title.addListener((o, oldValue, newValue) -> title.setText(newValue.toString()));
		Text year = new Text();
		chartEntry.year.addListener((o, oldValue, newValue) -> year.setText(newValue.toString()));
		Text position = new Text();
		chartEntry.position.addListener((o, oldValue, newValue) -> position.setText(newValue.toString()));
		Hyperlink moreDetails = new Hyperlink("Details");
		moreDetails.setOnAction(event -> getHostServices().showDocument(chartEntry.moreDetails.get()));

		TextFlow text = new TextFlow(artist, new Text(" – "), title, new Text("\n"), position, new Text(" in "), year,
				new Text("\n"), moreDetails);
		text.setStyle("-fx-font-size: 25pt;");

		showSolutionButton.setFont(new Font(20));
		showSolutionButton.setOnAction(event -> {
			showSolutionButton.setVisible(false);
			solutions.setEffect(null);
			solutions.setDisable(false);
		});

		solutions.getChildren().addAll(image, text);
		solutions.setEffect(BLUR_AND_MONOCHROME);
		solutions.setPadding(new Insets(10));
		return new StackPane(solutions, showSolutionButton);
	}

	private Node createToolBar(ChartsParser<?> parser) {
		ObservableList<Integer> years = FXCollections.observableList(parser.selectableYears());
		ComboBox<Integer> yearFrom = new ComboBox<>(years);
		yearFrom.getSelectionModel().select(0);
		ComboBox<Integer> yearTo = new ComboBox<>(years);
		yearTo.getSelectionModel().select(years.size() - 1);

		yearFrom.setOnAction(event -> {
			ensureToIsNotSmallerThanFrom(yearFrom.getSelectionModel(), yearTo.getSelectionModel());
		});
		yearTo.setOnAction(event -> {
			ensureFromIsNotBiggerThanTo(yearFrom.getSelectionModel(), yearTo.getSelectionModel());
		});

		ObservableList<Integer> positions = FXCollections.observableList(parser.selectablePositions());
		ComboBox<Integer> positionFrom = new ComboBox<>(positions);
		positionFrom.getSelectionModel().select(0);
		ComboBox<Integer> positionTo = new ComboBox<>(positions);
		positionTo.getSelectionModel().select(positions.size() - 1);
		
		positionFrom.setOnAction(event -> {
			ensureToIsNotSmallerThanFrom(positionFrom.getSelectionModel(), positionTo.getSelectionModel());
		});
		positionTo.setOnAction(event -> {
			ensureFromIsNotBiggerThanTo(positionFrom.getSelectionModel(), positionTo.getSelectionModel());
		});

		Button playButton = new Button(); // TODO : Länge muss längstem String entsprechen
		playButton.setOnAction(event -> {
			MediaPlayer mediaPlayer = mediaView.getMediaPlayer();
			switch (mediaPlayer.getStatus()) {
			case PAUSED:
			case READY:
			case STOPPED:
				mediaPlayer.play();
				break;
			case PLAYING:
				mediaPlayer.pause();
				break;
			default:
				break;
			}
		});

		Button nextButton = new Button("Next Song");
		nextButton.setOnAction(event -> {
			parser.nextSong(yearFrom.getValue(), yearTo.getValue(), positionFrom.getValue(), positionTo.getValue());
			showSolutionButton.setVisible(true);
			solutions.setDisable(true);
			solutions.setEffect(BLUR_AND_MONOCHROME);
		});

		ProgressBar progressBar = new ProgressBar(0.0);
		ChangeListener<Duration> progressChangeListener = (o, oldValue, newValue) -> {
			MediaPlayer player = mediaView.getMediaPlayer();
			progressBar.setProgress(player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis());
		};

		mediaView.mediaPlayerProperty().addListener((o, oldPlayer, newPlayer) -> {
			progressBar.setProgress(0.0);

			newPlayer.currentTimeProperty().addListener(progressChangeListener);
			newPlayer.setOnEndOfMedia(() -> {
				// Stopping the player resets playback to start time, but is doesn't reset currentTime to
				// zero. Seeking does.
					newPlayer.seek(new Duration(0.0));
					newPlayer.stop();
				});
			newPlayer.setOnPaused(() -> playButton.setText(PLAY));
			newPlayer.setOnReady(() -> playButton.setText(PLAY));
			newPlayer.setOnStopped(() -> playButton.setText(PLAY_AGAIN));
			newPlayer.setOnPlaying(() -> playButton.setText(PAUSE));

			if (oldPlayer != null) {
				oldPlayer.dispose();
			}
		});

		nextButton.fire(); // Load the first player

		return new ToolBar(new Label("Year"), yearFrom, new Label("to"), yearTo, new Separator(),
				new Label("Position"), positionFrom, new Label("to"), positionTo, new Separator(), playButton,
				nextButton, progressBar);
	}

	private void ensureToIsNotSmallerThanFrom(SingleSelectionModel<Integer> from, SingleSelectionModel<Integer> to) {
		int fromIndex = from.getSelectedIndex();
		int toIndex = to.getSelectedIndex();
		if (fromIndex > toIndex) {
			to.select(fromIndex);
		}
	}

	private void ensureFromIsNotBiggerThanTo(SingleSelectionModel<Integer> from, SingleSelectionModel<Integer> to) {
		int fromIndex = from.getSelectedIndex();
		int toIndex = to.getSelectedIndex();
		if (fromIndex > toIndex) {
			from.select(toIndex);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
