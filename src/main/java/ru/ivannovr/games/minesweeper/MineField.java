package ru.ivannovr.games.minesweeper;

import ru.ivannovr.games.minesweeper.entities.Cell;
import ru.ivannovr.games.minesweeper.entities.Flag;
import ru.ivannovr.games.minesweeper.entities.Mine;
import ru.ivannovr.games.minesweeper.entities.NumberCell;

import java.util.Random;

public class MineField {
    private final Cell[][] grid;
    private final int width;
    private final int height;
    private final int mineCount;
    private boolean lose;
    private int closedCount;

    public MineField(int width, int height, int mineCount) {
        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
        this.grid = new Cell[width][height];
        this.lose = false;
        this.closedCount = width * height;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new NumberCell(x, y, 0);
            }
        }
    }

    public void placeMines(int safeX, int safeY) {
        Random random = new Random();
        int placed = 0;
        while (placed < mineCount) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (!(grid[x][y] instanceof Mine) && (x != safeX || y != safeY)) {
                grid[x][y] = new Mine(x, y);
                placed++;
            }
        }
        calculateNumbers();
    }

    private void calculateNumbers() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!(grid[x][y] instanceof Mine)) {
                    int minesAround = countMinesAround(x, y);
                    grid[x][y] = new NumberCell(x, y, minesAround);
                }
            }
        }
    }

    private int countMinesAround(int x, int y) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < width && ny >= 0 && ny < height && grid[nx][ny].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    public int reveal(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height || grid[x][y].isRevealed() || grid[x][y].isFlag()) {
            return 0;
        }
        Cell cell = grid[x][y];
        cell.reveal();
        closedCount--;

        if (cell.isMine()) {
            lose = true;
            revealAllMines();
            return 0;
        }

        int revealed = cell.getScoreValue();
        if (cell instanceof NumberCell numberCell && numberCell.getMineCount() == 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    revealed += reveal(x + dx, y + dy);
                }
            }
        }
        return revealed;
    }

    private void revealAllMines() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y].isMine()) {
                    grid[x][y].reveal();
                }
            }
        }
    }

    public void toggleFlag(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height || grid[x][y].isRevealed()) {
            return;
        }
        if (grid[x][y].isFlag()) {
            grid[x][y] = ((Flag) grid[x][y]).removeFlag();
        } else {
            grid[x][y] = new Flag(x, y, grid[x][y]);
        }
    }

    public boolean isWin() {
        return closedCount == mineCount && !lose;
    }

    public boolean isLose() {
        return lose;
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}