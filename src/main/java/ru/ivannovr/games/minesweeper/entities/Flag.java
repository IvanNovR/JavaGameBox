package ru.ivannovr.games.minesweeper.entities;

import java.awt.Color;

public class Flag extends Cell {
    private final Cell underlyingCell;

    public Flag(int x, int y, Cell underlyingCell) {
        super(x, y);
        this.underlyingCell = underlyingCell;
    }

    public Cell removeFlag() {
        return underlyingCell;
    }

    @Override
    public boolean isMine() {
        return underlyingCell.isMine();
    }

    @Override
    public boolean isFlag() {
        return true;
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public String getDisplayText() {
        return "F";
    }

    @Override
    public int getScoreValue() {
        return 0;
    }
}