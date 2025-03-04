package ru.ivannovr.games.snake.entities.food;

import ru.ivannovr.games.common.Score;
import ru.ivannovr.games.snake.entities.Snake;

import java.awt.*;

public abstract class Food {
    protected Point position;
    protected long spawnTime;
    protected long lifetime;
    protected Color color;

    public Food(Point position, long lifetime) {
        this.position = position;
        this.lifetime = lifetime;
        this.spawnTime = System.currentTimeMillis();
    }

    public Point getPosition() {
        return position;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - spawnTime >= lifetime;
    }

    public Color getColor() {
        return color;
    }

    public abstract void applyEffect(Snake snake, Score score);
}