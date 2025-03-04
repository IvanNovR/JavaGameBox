package ru.ivannovr.games.pacman.entities.dots;

import ru.ivannovr.games.pacman.PacmanGame;

import java.awt.Color;
import java.awt.Point;

public abstract class Dot {
    private final Point position;
    private final Color color;

    protected Dot(int x, int y, Color color) {
        this.position = new Point(x, y);
        this.color = color;
    }

    public Point getPosition() {
        return new Point(position);
    }

    public Color getColor() {
        return color;
    }

    public abstract int getPoints();
    public abstract void applyEffect(PacmanGame game);
}
