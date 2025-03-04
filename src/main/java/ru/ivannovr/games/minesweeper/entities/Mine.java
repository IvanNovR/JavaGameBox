package ru.ivannovr.games.minesweeper.entities;

import java.awt.Color;

public class Mine extends Cell {
    public Mine(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean isMine() {
        return true;
    }

    @Override
    public boolean isFlag() {
        return false;
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public String getDisplayText() {
        return isRevealed() ? "*" : "";
    }

    @Override
    public int getScoreValue() {
        return 0;
    }
}