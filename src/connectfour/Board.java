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

/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
    // Define named constants
    public static final int ROWS = 6;  // ROWS x COLS cells
    public static final int COLS = 7;
    public static final int WIN_CONDITION = 4;

    // Define properties (package-visible)
    public Cell[][] cells;

    // Variables for dynamic resizing (non-static)
    public int canvasWidth;  // Dynamically calculated width of the canvas
    public int canvasHeight; // Dynamically calculated height of the canvas

    // Define named constants for drawing
    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // Grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    // Constructor to initialize the game board
    public Board() {
        initGame();
    }

    // Initialize the game objects (run once)
    public void initGame() {
        cells = new Cell[ROWS][COLS]; // Allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col); // Allocate element of the array
            }
        }
    }

    // Reset the game board, ready for a new game
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame(); // Clear the cell content
            }
        }
    }

    // The given player makes a move on (selectedRow, selectedCol).
    // Update cells[selectedRow][selectedCol]. Compute and return the
    // new game state (PLAYING, DRAW, RED_WON, YELLOW_WON).
    public boolean hasWon(Seed theSeed, int rowSelected, int colSelected) {
        int count;

        // 1. Row check
        count = 0;
        for (int col = 0; col < COLS; ++col) {
            if (cells[rowSelected][col].content == theSeed) {
                ++count;
                if (count == WIN_CONDITION) return true;
            } else {
                count = 0;
            }
        }

        // 2. Column check
        count = 0;
        for (int row = 0; row < ROWS; ++row) {
            if (cells[row][colSelected].content == theSeed) {
                ++count;
                if (count == WIN_CONDITION) return true;
            } else {
                count = 0;
            }
        }

        // 3. Diagonal check (top-left to bottom-right)
        count = 0;
        int startRow = rowSelected, startCol = colSelected;
        while (startRow > 0 && startCol > 0) { startRow--; startCol--; }
        while (startRow < ROWS && startCol < COLS) {
            if (cells[startRow][startCol].content == theSeed) {
                ++count;
                if (count == WIN_CONDITION) return true;
            } else {
                count = 0;
            }
            startRow++; startCol++;
        }

        // 4. Anti-diagonal check (bottom-left to top-right)
        count = 0;
        startRow = rowSelected; startCol = colSelected;
        while (startRow < ROWS - 1 && startCol > 0) { startRow++; startCol--; }
        while (startRow >= 0 && startCol < COLS) {
            if (cells[startRow][startCol].content == theSeed) {
                ++count;
                if (count == WIN_CONDITION) return true;
            } else {
                count = 0;
            }
            startRow--; startCol++;
        }

        return false;
    }

    // Update the game state and check if the player has won
    public State stepGame(Seed currentPlayer, int row, int col) {
        if (hasWon(currentPlayer, row, col)) {
            return (currentPlayer == Seed.RED) ? State.RED_WON : State.YELLOW_WON;
        } else {
            boolean isDraw = true;
            for (int r = 0; r < ROWS; ++r) {
                for (int c = 0; c < COLS; ++c) {
                    if (cells[r][c].content == Seed.NO_SEED) {
                        isDraw = false;
                        break;
                    }
                }
            }
            if (isDraw) return State.DRAW;
        }
        return State.PLAYING;
    }

    // Paint itself on the graphics canvas
    public void paint(Graphics g, int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        // Calculate dynamic cell size based on canvas size
        int cellWidth = canvasWidth / COLS;
        int cellHeight = canvasHeight / ROWS;

        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, cellHeight * row - GRID_WIDTH_HALF,
                    canvasWidth - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(cellWidth * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, canvasHeight - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g, cellWidth, cellHeight);
            }
        }
    }
}
