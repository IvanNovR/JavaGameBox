package ru.ivannovr.games.minesweeper.entities;

import java.awt.Color;

public abstract class Cell {
    private final int x;
    private final int y;
    private boolean revealed;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.revealed = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void reveal() {
        this.revealed = true;
    }

    public abstract boolean isMine();
    public abstract boolean isFlag();
    public abstract Color getColor();
    public abstract String getDisplayText();
    public abstract int getScoreValue();
}