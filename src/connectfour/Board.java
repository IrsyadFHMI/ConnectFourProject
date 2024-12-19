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
    // Define named constants for drawing
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    // Define properties (package-visible)
    /** Composes of 2D array of ROWS-by-COLS Cell instances */
    Cell[][] cells;

    /** Constructor to initialize the game board */
    public Board() {
        initGame();
    }

    /** Initialize the game objects (run once) */
    public void initGame() {
        cells = new Cell[ROWS][COLS]; // allocate the array
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // Allocate element of the array
                cells[row][col] = new Cell(row, col);
                // Cells are initialized in the constructor
            }
        }
    }

    /** Reset the game board, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame(); // clear the cell content
            }
        }
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    /** Check if the player with the given seed has won after placing a piece at (rowSelected, colSelected) */
    public boolean hasWon(Seed theSeed, int rowSelected, int colSelected) {
        // 1. Check for 4-in-a-line on the same row
        int count = 0;
        for (int col = 0; col < COLS; ++col) {
            if (cells[rowSelected][col].content == theSeed) {
                ++count;
                if (count == WIN_CONDITION) return true; // found
            } else {
                count = 0; // reset and count again if not consecutive
            }
        }

        // 2. Check for 4-in-a-line on the same column
        count = 0;
        for (int row = 0; row < ROWS; ++row) {
            if (cells[row][colSelected].content == theSeed) {
                ++count;
                if (count == WIN_CONDITION) return true; // found
            } else {
                count = 0; // reset and count again if not consecutive
            }
        }

        // 3. Check for 4-in-a-line on the main diagonal (\) from top-left to bottom-right
        count = 0;
        int startRow = rowSelected;
        int startCol = colSelected;
        // Move to the top-left diagonal starting point
        while (startRow > 0 && startCol > 0) {
            startRow--;
            startCol--;
        }
        while (startRow < ROWS && startCol < COLS) {
            if (cells[startRow][startCol].content == theSeed) {
                ++count;
                if (count == WIN_CONDITION) return true; // found
            } else {
                count = 0; // reset and count again if not consecutive
            }
            startRow++;
            startCol++;
        }

        // 4. Check for 4-in-a-line on the anti-diagonal (/) from bottom-left to top-right
        count = 0;
        startRow = rowSelected;
        startCol = colSelected;
        // Move to the bottom-left diagonal starting point
        while (startRow < ROWS - 1 && startCol > 0) {
            startRow++;
            startCol--;
        }
        while (startRow >= 0 && startCol < COLS) {
            if (cells[startRow][startCol].content == theSeed) {
                ++count;
                if (count == WIN_CONDITION) return true; // found
            } else {
                count = 0; // reset and count again if not consecutive
            }
            startRow--;
            startCol++;
        }

        // If no 4-in-a-line found in any direction, return false
        return false;
    }

    /** Update the game state and check if the player has won */
    public State stepGame(Seed currentPlayer, int row, int col) {
        if (hasWon(currentPlayer, row, col)) {
            return (currentPlayer == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        } else {
            // Check for draw (when all cells are filled)
            boolean isDraw = true;
            for (int r = 0; r < ROWS; ++r) {
                for (int c = 0; c < COLS; ++c) {
                    if (cells[r][c].content == Seed.NO_SEED) {
                        isDraw = false; // If an empty cell is found, it's not a draw
                        break;
                    }
                }
            }
            if (isDraw) return State.DRAW;
        }
        return State.PLAYING; // Continue playing
    }


    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g) {
        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);  // ask the cell to paint itself
            }
        }
    }
}
