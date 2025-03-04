package ru.ivannovr.games.snake.entities.food;

import ru.ivannovr.games.common.Score;
import ru.ivannovr.games.snake.entities.Snake;

import java.awt.*;

public class NegativeFood extends Food {
    public NegativeFood(Point position, long lifetime) {
        super(position, lifetime);
        this.color = Color.BLACK;
    }

    @Override
    public void applyEffect(Snake snake, Score score) {
        if (snake.getBody().size() > 1) {
            snake.getBody().removeLast();
        }
        score.addPoints(-10);
    }
}
