package ru.ivannovr.games.snake.entities.food;

import ru.ivannovr.games.common.Score;
import ru.ivannovr.games.snake.entities.Snake;

import java.awt.*;

public class NormalFood extends Food {
    public NormalFood(Point position, long lifetime) {
        super(position, lifetime);
        this.color = Color.RED;
    }

    @Override
    public void applyEffect(Snake snake, Score score) {
        snake.grow();
        score.addPoints(10);
    }
}