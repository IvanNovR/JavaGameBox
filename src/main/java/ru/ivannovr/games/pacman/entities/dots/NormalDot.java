package ru.ivannovr.games.pacman.entities.dots;

import ru.ivannovr.games.pacman.PacmanGame;

import java.awt.*;

public class NormalDot extends Dot {
    private static final int POINTS = 10;
    private static final Color NORMAL_COLOR = Color.ORANGE;

    public NormalDot(int x, int y) {
        super(x, y, NORMAL_COLOR);
    }

    @Override
    public int getPoints() {
        return POINTS;
    }

    @Override
    public void applyEffect(PacmanGame game) {}
}
