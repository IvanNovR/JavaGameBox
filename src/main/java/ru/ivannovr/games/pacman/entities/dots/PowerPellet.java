package ru.ivannovr.games.pacman.entities.dots;

import ru.ivannovr.games.pacman.PacmanGame;

import java.awt.*;

public class PowerPellet extends Dot {
    private static final int POINTS = 50;
    private static final Color PELLET_COLOR = Color.PINK;
    private static final int EFFECT_DURATION = 100;

    public PowerPellet(int x, int y) {
        super(x, y, PELLET_COLOR);
    }

    @Override
    public int getPoints() {
        return POINTS;
    }

    @Override
    public void applyEffect(PacmanGame game) {
        game.activatePowerPellet(EFFECT_DURATION);
    }
}