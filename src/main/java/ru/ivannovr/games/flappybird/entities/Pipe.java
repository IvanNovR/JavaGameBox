package ru.ivannovr.games.flappybird.entities;

import java.util.Random;

public class Pipe {
    private int x;
    private final int gapY;
    private final int width = 50;
    private final int gapHeight = 150;
    private boolean passed;

    public Pipe(int screenWidth, int screenHeight) {
        this.x = screenWidth;
        Random random = new Random();
        this.gapY = random.nextInt(screenHeight - gapHeight - 100) + 50;
        this.passed = false;
    }

    public void update() {
        x -= 3;
    }

    public int getX() {
        return x;
    }

    public int getGapY() {
        return gapY;
    }

    public int getWidth() {
        return width;
    }

    public int getGapHeight() {
        return gapHeight;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public boolean isOffScreen() {
        return x + width < 0;
    }
}
