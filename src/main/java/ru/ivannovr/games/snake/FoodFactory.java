package ru.ivannovr.games.snake;

import ru.ivannovr.games.snake.entities.Snake;
import ru.ivannovr.games.snake.entities.food.BigFood;
import ru.ivannovr.games.snake.entities.food.Food;
import ru.ivannovr.games.snake.entities.food.NegativeFood;
import ru.ivannovr.games.snake.entities.food.NormalFood;

import java.awt.*;
import java.util.Random;

public class FoodFactory {
    private final Random random;
    private static final long DEFAULT_LIFETIME = 3000;

    public FoodFactory() {
        this.random = new Random();
    }

    public Food createRandomFood(int width, int height, Snake snake) {
        Point position;
        do {
            position = new Point(random.nextInt(width), random.nextInt(height));
        } while (snake.collidesWith(position));

        int type = random.nextInt(100);
        if (snake.getBody().size() == 1) {
            if (type < 80) {
                return new NormalFood(position, DEFAULT_LIFETIME);
            } else {
                return new BigFood(position, DEFAULT_LIFETIME);
            }
        } else {
            if (type < 70) {
                return new NormalFood(position, DEFAULT_LIFETIME);
            } else if (type < 90) {
                return new BigFood(position, DEFAULT_LIFETIME);
            } else {
                return new NegativeFood(position, DEFAULT_LIFETIME);
            }
        }
    }
}