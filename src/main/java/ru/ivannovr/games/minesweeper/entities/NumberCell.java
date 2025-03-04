package ru.ivannovr.games.minesweeper.entities;

import java.awt.Color;

public class NumberCell extends Cell {
    private final int mineCount;

    public NumberCell(int x, int y, int mineCount) {
        super(x, y);
        this.mineCount = mineCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    @Override
    public boolean isMine() {
        return false;
    }

    @Override
    public boolean isFlag() {
        return false;
    }

    @Override
    public Color getColor() {
        return switch (mineCount) {
            case 1 -> Color.BLUE;
            case 2 -> Color.GREEN;
            case 3 -> Color.RED;
            case 4 -> Color.MAGENTA;
            case 5 -> Color.ORANGE;
            case 6 -> Color.CYAN;
            case 7 -> Color.PINK;
            case 8 -> Color.BLACK;
            default -> Color.GRAY;
        };
    }

    @Override
    public String getDisplayText() {
        return isRevealed() && mineCount > 0 ? String.valueOf(mineCount) : "";
    }

    @Override
    public int getScoreValue() {
        return isRevealed() ? 10 : 0;
    }
}