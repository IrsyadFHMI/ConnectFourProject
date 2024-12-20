/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #7
 * 1 - 5026221163 - Mohammad Geresidi Rachmadi
 * 2 - 5026221187 - Muhammad Irsyad Fahmi
 */
package connectfour;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Connect Four: Two-player Graphic version with minimal changes from Tic-Tac-Toe.
 */
public class ConnectFour extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final int ROWS = 6;   // 6 rows for Connect Four
    public static final int COLS = 7;   // 7 columns
    public static final String TITLE = "Connect Four";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_RED = new Color(239, 105, 80);  // Red #EF6950
    public static final Color COLOR_YELLOW = new Color(64, 154, 225); // Yellow #409AE1
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;

    public ConnectFour() {

        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int colSelected = mouseX / (getWidth() / COLS);

                if (currentState == State.PLAYING) {
                    SoundEffect.Playing.play();
                    if (colSelected >= 0 && colSelected < COLS) {
                        for (int row = ROWS - 1; row >= 0; row--) {
                            if (board.cells[row][colSelected].content == Seed.NO_SEED) {
                                board.cells[row][colSelected].content = currentPlayer;
                                currentState = board.stepGame(currentPlayer, row, colSelected);
                                currentPlayer = (currentPlayer == Seed.RED) ? Seed.YELLOW : Seed.RED;
                                break;
                            }
                        }
                    }
                } else {
                    SoundEffect.DIE.play();  // game over
                    newGame();  // restart
                }
                repaint();
            }
        });

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(800, 600));  // Default size; it will resize dynamically
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        initGame();
        newGame();

        // Add ComponentListener to handle resizing of the window
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Repaint the board when the window is resized
                repaint();
            }
        });
    }

    public void initGame() {
        board = new Board();
    }

    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.RED;
        currentState = State.PLAYING;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);

        // Calculate canvas width and height dynamically
        int canvasWidth = getWidth();
        int canvasHeight = getHeight() - statusBar.getHeight();  // Exclude the status bar height

        // Paint the board
        board.paint(g, canvasWidth, canvasHeight);

        // Display the current game state in the status bar
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.RED) ? "Red's Turn" : "Yellow's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.RED_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'Red' Won! Click to play again.");
        } else if (currentState == State.YELLOW_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'Yellow' Won! Click to play again.");
        }
    }

    public void play() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(new ConnectFour());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
