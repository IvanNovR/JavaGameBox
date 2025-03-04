package ru.ivannovr.games.flappybird.entities;

import java.awt.*;

public class Bird {
    private Point position;
    private double velocity;
    private int horizontalSpeed;
    private static final double GRAVITY = 1.0;
    private static final double JUMP_STRENGTH = -12.0;
    private static final int HORIZONTAL_SPEED = 5;
    private static final int SCREEN_WIDTH = 600;

    public Bird(int x, int y) {
        this.position = new Point(x, y);
        this.velocity = 0;
        this.horizontalSpeed = 0;
    }

    public void update() {
        velocity += GRAVITY;
        position.y += (int) velocity;

        position.x += horizontalSpeed;

        if (position.x < 0) position.x = 0;
        if (position.x > SCREEN_WIDTH - 20) position.x = SCREEN_WIDTH - 20;
    }

    public void jump() {
        velocity = JUMP_STRENGTH;
    }

    public void moveLeft() {
        horizontalSpeed = -HORIZONTAL_SPEED;
    }

    public void moveRight() {
        horizontalSpeed = HORIZONTAL_SPEED;
    }

    public void stopHorizontal() {
        horizontalSpeed = 0;
    }

    public Point getPosition() {
        return position;
    }

    public boolean collidesWith(Pipe pipe) {
        int birdX = position.x;
        int birdY = position.y;
        int birdSize = 20;

        int pipeX = pipe.getX();
        int pipeWidth = pipe.getWidth();
        int gapY = pipe.getGapY();
        int gapHeight = pipe.getGapHeight();

        return (birdX + birdSize > pipeX && birdX < pipeX + pipeWidth) &&
                (birdY < gapY || birdY + birdSize > gapY + gapHeight);
    }

    public boolean isOutOfBounds(int height) {
        return position.y < 0 || position.y > height - 20;
    }
}