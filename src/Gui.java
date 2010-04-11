import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.htmlparser.util.ParserException;

public class Gui extends JFrame {
	private JButton button;
	private JButton buttonPause;
	private JLabel solution;
	private JLabel link;

	private JComboBox yearStart;
	private JComboBox yearEnd;
	private JComboBox positionStart;
	private JComboBox positionEnd;

	private static enum Status {
		GUESSING, SHOWING_SOLUTION, PAUSE
	}

	private Status currentStatus = Status.GUESSING;

	private ChartsParser parser;
	private Player player;

	public Gui(final ChartsParser parser) {
		this.parser = parser;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Guesscharts - Philippe Eberli & Thomas Bruhin");
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension(900, 240));

		JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.LINE_AXIS));
		north.setBorder(new EmptyBorder(15, 15, 15, 15));

		north.add(new JLabel("Year"));

		yearStart = new JComboBox(parser.getYears());
		yearStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (yearEnd.getSelectedIndex() < yearStart.getSelectedIndex()) {
					yearEnd.setSelectedIndex(yearStart.getSelectedIndex());
				}
			}
		});
		north.add(yearStart);

		north.add(new JLabel("to"));

		yearEnd = new JComboBox(parser.getYears());
		yearEnd.setSelectedIndex(parser.getYears().length - 1);
		yearEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (yearStart.getSelectedIndex() > yearEnd.getSelectedIndex()) {
					yearStart.setSelectedIndex(yearEnd.getSelectedIndex());
				}
			}
		});
		north.add(yearEnd);

		north.add(Box.createRigidArea(new Dimension(30, 0)));

		north.add(new JLabel("Position"));

		positionStart = new JComboBox(parser.getPositions());
		positionStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (positionEnd.getSelectedIndex() < positionStart.getSelectedIndex()) {
					positionEnd.setSelectedIndex(positionStart.getSelectedIndex());
				}
			}
		});
		north.add(positionStart);

		north.add(new JLabel("to"));

		positionEnd = new JComboBox(parser.getPositions());
		positionEnd.setSelectedIndex(parser.getPositions().length - 1);
		positionEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (positionStart.getSelectedIndex() > positionEnd.getSelectedIndex()) {
					positionStart.setSelectedIndex(positionEnd.getSelectedIndex());
				}
			}
		});
		north.add(positionEnd);

		north.add(Box.createHorizontalGlue());

		add(north, BorderLayout.NORTH);

		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
		center.setBorder(new EmptyBorder(0, 15, 15, 15));
		
		center.add(Box.createVerticalGlue());

		solution = new JLabel();
		solution.setFont(solution.getFont().deriveFont(20f));

		center.add(solution);
		
		link = new JLabel("<html><u>Details</u></html>");
		link.setVisible(false);
		link.setForeground(Color.blue);
		link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		link.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (currentStatus == Status.SHOWING_SOLUTION) {
						Desktop.getDesktop().browse(parser.getSongDetailsLink());
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		center.add(link);

		center.add(Box.createVerticalGlue());

		button = new JButton("Show the solution");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (currentStatus) {
				// GUESSING -> Show solution & prepare for next Song
				case GUESSING:
					solution.setText(parser.getSong());
					link.setVisible(true);

					button.setText("Next Song");
					button.setEnabled(true);
					buttonPause.setText("Play again");
					buttonPause.setEnabled(true);
					currentStatus = Status.SHOWING_SOLUTION;
					break;
				case PAUSE:
					solution.setText(parser.getSong());
					link.setVisible(true);

					button.setText("Next Song");
					button.setEnabled(true);
					buttonPause.setText("Play again");
					buttonPause.setEnabled(true);

					currentStatus = Status.SHOWING_SOLUTION;
					break;
				// Showing solution -> Stop Playing, Load next song
				case SHOWING_SOLUTION:
					playNewSong();
					currentStatus = Status.GUESSING;
					break;
				default:
					break;
				}
			}
		});
		getRootPane().setDefaultButton(button);

		add(center, BorderLayout.CENTER);

		JPanel south = new JPanel();
		south.setLayout(new BoxLayout(south, BoxLayout.LINE_AXIS));
		south.setBorder(new EmptyBorder(15, 15, 15, 15));
		south.add(button);
		buttonPause = new JButton("Stop playing");
		buttonPause.setEnabled(false);
		buttonPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (currentStatus) {
				// GUESSING -> Stop Playing
				case GUESSING:
					pauseSong();
					buttonPause.setText("Play again");
					buttonPause.setEnabled(true);
					currentStatus = Status.PAUSE;
					break;
				// PAUSE: -> Play
				case PAUSE:
					playSong();
					buttonPause.setText("Stop playing");
					buttonPause.setEnabled(true);
					currentStatus = Status.GUESSING;
					break;
				// Showing solution -> Play again
				case SHOWING_SOLUTION:
					playSongAgain();
					break;
				default:
					break;
				}
			}
		});
		south.add(buttonPause);

		add(south, BorderLayout.SOUTH);
		pack();
		setVisible(true);

		playNewSong();
	}

	private void playSong() {
		if (player != null) {
			player.close();
		}
		solution.setText("Loading song...");
		link.setVisible(false);

		Thread thread = new Thread() {
			public void run() {
				try {
					button.setEnabled(false);
					buttonPause.setEnabled(false);
					// System.out.println("Play: "+parser.getSongURL() );
					player = new Player(parser.getMp3(parser.getSongURL()));

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							solution.setText("Guess...");
							button.setText("Show the solution");
							button.setEnabled(true);
							buttonPause.setEnabled(true);
							buttonPause.setText("Stop playing");
						}
					});
					player.play();
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			}
		};
		thread.start();
	}

	private void playSongAgain() {
		if (player != null) {
			player.close();
		}

		Thread thread = new Thread() {
			public void run() {
				try {
					player = new Player(parser.getMp3(parser.getSongURL()));

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							button.setEnabled(true);
							buttonPause.setEnabled(true);
						}
					});
					player.play();
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			}
		};
		thread.start();
	}

	private void playNewSong() {
		if (player != null) {
			player.close();
		}
		solution.setText("Loading song...");
		link.setVisible(false);

		Thread thread = new Thread() {
			public void run() {
				try {
					button.setEnabled(false);
					buttonPause.setEnabled(false);
					parser.getNewSong((Integer) yearStart.getSelectedItem(), (Integer) yearEnd.getSelectedItem(),
							(Integer) positionStart.getSelectedItem(), (Integer) positionEnd.getSelectedItem());
					player = new Player(parser.getMp3());

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							solution.setText("Guess...");
							button.setText("Show the solution");
							button.setEnabled(true);
							buttonPause.setEnabled(true);
							buttonPause.setText("Stop playing");
						}
					});
					player.play();
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			}
		};
		thread.start();
	}

	private void pauseSong() {
		// System.out.println("Pause: "+parser.getSongURL() );
		if (player != null) {
			player.close();
		}
		solution.setText("Pause ...");
		link.setVisible(false);

	}

	public static void main(String[] args) throws ParserException, IOException, JavaLayerException {
		setNativeLookAndFeel();
		setExceptionHandler();
		new Gui(new SwissChartsParser());
	}

	private static void setExceptionHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				String message = e.getMessage();
				if (e instanceof NullPointerException) {
					message = "NullPointerException";
				}
				JOptionPane.showMessageDialog(null, message, "System error", JOptionPane.ERROR_MESSAGE);

				e.printStackTrace();

				System.exit(1);
			}
		});
	}

	private static void setNativeLookAndFeel() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.startsWith("windows")) {
			trySetLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} else if (osName.startsWith("linux")) {
			trySetLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		}
	}

	private static void trySetLookAndFeel(String laf) {
		try {
			UIManager.setLookAndFeel(laf);
		} catch (Exception e) {
		}
	}
}
