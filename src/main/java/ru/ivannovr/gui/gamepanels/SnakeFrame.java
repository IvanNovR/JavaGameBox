package ru.ivannovr.gui.gamepanels;

import ru.ivannovr.games.common.Direction;
import ru.ivannovr.games.snake.SnakeGame;
import ru.ivannovr.games.snake.entities.food.Food;
import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class SnakeFrame extends AbstractGamePanel {
    private static final int CELL_SIZE = 30;
    private static final int GRID_SIZE = 20;
    private static final int WINDOW_SIZE = CELL_SIZE * GRID_SIZE;
    private static final int SNAKE_DELAY = 150;

    public SnakeFrame(JFrame parentFrame, String username, DatabaseManager dbManager) {
        super(parentFrame, username, dbManager, new SnakeGame(username, dbManager), SNAKE_DELAY);
        setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
    }

    @Override
    protected void renderGame(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(new GradientPaint(0, 0, new Color(34, 139, 34), 0, WINDOW_SIZE, new Color(144, 238, 144)));
        g2.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);

        g2.setColor(new Color(50, 205, 50, 50));
        for (int i = 0; i < WINDOW_SIZE; i += 20) {
            for (int j = 0; j < WINDOW_SIZE; j += 20) {
                g2.fillOval(i + (int)(Math.random() * 10), j + (int)(Math.random() * 10), 5, 5);
            }
        }

        java.util.List<Object> renderData = game.getRenderData();
        LinkedList<Point> snakeBody = (LinkedList<Point>) renderData.get(0);

        for (int i = 0; i < snakeBody.size(); i++) {
            Point p = snakeBody.get(i);
            if (i == 0) {
                g2.setColor(new Color(50, 63, 205));
                g2.fillOval(p.x * CELL_SIZE + 2, p.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                g2.setColor(Color.WHITE);
                g2.fillOval(p.x * CELL_SIZE + 8, p.y * CELL_SIZE + 5, 8, 8);
                g2.fillOval(p.x * CELL_SIZE + 14, p.y * CELL_SIZE + 5, 8, 8);
                g2.setColor(Color.BLACK);
                g2.fillOval(p.x * CELL_SIZE + 10, p.y * CELL_SIZE + 7, 4, 4);
                g2.fillOval(p.x * CELL_SIZE + 16, p.y * CELL_SIZE + 7, 4, 4);
            } else {
                g2.setColor(new Color(50, 104, 205));
                g2.fillOval(p.x * CELL_SIZE + 2, p.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                g2.setColor(new Color(50, 63, 205));
                g2.drawOval(p.x * CELL_SIZE + 2, p.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            }

            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillOval(p.x * CELL_SIZE + 4, p.y * CELL_SIZE + 4, CELL_SIZE - 4, CELL_SIZE - 4);
        }

        for (int i = 1; i < renderData.size(); i++) {
            Food food = (Food) renderData.get(i);
            Point foodPos = food.getPosition();
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillOval(foodPos.x * CELL_SIZE + 7, foodPos.y * CELL_SIZE + 7, CELL_SIZE - 10, CELL_SIZE - 10);
            g2.setColor(food.getColor());
            if (food.getColor().equals(Color.RED)) {
                g2.fillOval(foodPos.x * CELL_SIZE + 5, foodPos.y * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                g2.setColor(Color.WHITE);
                g2.fillOval(foodPos.x * CELL_SIZE + 10, foodPos.y * CELL_SIZE + 8, 6, 6);
            } else if (food.getColor().equals(Color.YELLOW)) {
                g2.fillOval(foodPos.x * CELL_SIZE + 2, foodPos.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                g2.setColor(Color.WHITE);
                g2.fillOval(foodPos.x * CELL_SIZE + 10, foodPos.y * CELL_SIZE + 5, 8, 8);
            } else if (food.getColor().equals(Color.BLACK)) {
                g2.fillOval(foodPos.x * CELL_SIZE + 5, foodPos.y * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                g2.setColor(Color.RED);
                g2.fillOval(foodPos.x * CELL_SIZE + 5, foodPos.y * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            }
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Score: " + game.getScore(), 12, 22);
        g2.setColor(Color.YELLOW);
        g2.drawString("Score: " + game.getScore(), 10, 20);

        requestFocusInWindow();
    }

    @Override
    protected String getGameName() {
        return "Snake";
    }

    @Override
    protected void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: game.setControl(Direction.UP); break;
            case KeyEvent.VK_DOWN: game.setControl(Direction.DOWN); break;
            case KeyEvent.VK_LEFT: game.setControl(Direction.LEFT); break;
            case KeyEvent.VK_RIGHT: game.setControl(Direction.RIGHT); break;
        }
    }

    @Override
    protected void handleKeyRelease(KeyEvent e) {}
}