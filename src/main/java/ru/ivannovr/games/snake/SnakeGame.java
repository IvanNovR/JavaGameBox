package ru.ivannovr.games.snake;

import ru.ivannovr.games.common.AbstractGame;
import ru.ivannovr.games.common.Direction;
import ru.ivannovr.games.snake.entities.Snake;
import ru.ivannovr.games.snake.entities.food.Food;
import ru.ivannovr.utils.DatabaseManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SnakeGame extends AbstractGame {
    private final Snake snake;
    private final LinkedList<Food> foods;
    private final FoodFactory foodFactory;
    private final int width = 20;
    private final int height = 20;
    private long lastSpawnTime;
    private static final long SPAWN_INTERVAL = 1000;

    public SnakeGame(String username, DatabaseManager dbManager) {
        super("Snake", username, dbManager);
        snake = new Snake(width / 2, height / 2);
        foods = new LinkedList<>();
        foodFactory = new FoodFactory();
        lastSpawnTime = System.currentTimeMillis();
        spawnInitialFood();
    }

    private void spawnInitialFood() {
        foods.add(foodFactory.createRandomFood(width, height, snake));
    }

    @Override
    public void update() {
        if (gameOver) return;

        snake.move();
        snake.wrapAround(width, height);

        Point head = snake.getHead();
        for (int i = 0; i < foods.size(); i++) {
            Food food = foods.get(i);
            if (head.equals(food.getPosition())) {
                food.applyEffect(snake, score);
                foods.remove(i);
                spawnNewFood();
                break;
            }
        }

        foods.removeIf(Food::isExpired);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSpawnTime >= SPAWN_INTERVAL && foods.size() < 3) {
            spawnNewFood();
            lastSpawnTime = currentTime;
        }

        if (snake.selfCollision()) {
            endGame();
        }
    }

    private void spawnNewFood() {
        if (foods.size() >= 3) {
            foods.removeFirst();
        }
        foods.add(foodFactory.createRandomFood(width, height, snake));
    }

    @Override
    public void setControl(Object control) {
        if (control instanceof Direction) {
            snake.setDirection((Direction) control);
        }
    }

    @Override
    public List<Object> getRenderData() {
        List<Object> data = new ArrayList<>();
        data.add(snake.getBody());
        data.addAll(foods);
        return data;
    }
}