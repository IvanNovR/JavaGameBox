package ru.ivannovr.games.snake.entities.food;

import ru.ivannovr.games.common.Score;
import ru.ivannovr.games.snake.entities.Snake;

import java.awt.*;

public class BigFood extends Food {
    public BigFood(Point position, long lifetime) {
        super(position, lifetime);
        this.color = Color.YELLOW;
    }

    @Override
    public void applyEffect(Snake snake, Score score) {
        for (int i = 0; i < 3; i++) {
            snake.grow();
        }
        score.addPoints(30);
    }
}
