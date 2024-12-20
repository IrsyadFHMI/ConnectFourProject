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
 * The Cell class models each individual cell of the game board.
 */
public class Cell {
    // Define properties (package-visible)
    /** Content of this cell (Seed.NO_SEED, Seed.RED, or Seed.YELLOW) */
    Seed content;
    /** Row and column of this cell */
    int row, col;

    // Constructor to initialize this cell with the specified row and col
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    /** Reset this cell's content to NO_SEED, ready for new game */
    public void newGame() {
        content = Seed.NO_SEED;
    }

    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g, int cellWidth, int cellHeight) {
        // Use Graphics2D which allows us to set the pen's stroke
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(8,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Calculate dynamic positions based on the cell size
        int x1 = col * cellWidth;
        int y1 = row * cellHeight;

        if (content == Seed.RED || content == Seed.YELLOW) {
            g.drawImage(content.getImage(), x1, y1, cellWidth, cellHeight, null);
        }
    }
}
