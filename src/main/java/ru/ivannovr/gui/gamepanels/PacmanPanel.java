package ru.ivannovr.gui.gamepanels;

import ru.ivannovr.games.common.Direction;
import ru.ivannovr.games.pacman.Maze;
import ru.ivannovr.games.pacman.entities.Pacman;
import ru.ivannovr.games.pacman.PacmanGame;
import ru.ivannovr.games.pacman.entities.dots.Dot;
import ru.ivannovr.games.pacman.entities.dots.NormalDot;
import ru.ivannovr.games.pacman.entities.dots.PowerPellet;
import ru.ivannovr.games.pacman.entities.ghosts.Ghost;
import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PacmanPanel extends AbstractGamePanel {
    private static final int CELL_SIZE = 30;
    private static final int GRID_SIZE = 20;
    private static final int WINDOW_SIZE = CELL_SIZE * GRID_SIZE;
    private static final int PACMAN_DELAY = 150;
    private int animationFrame = 0;

    public PacmanPanel(JFrame parentFrame, String username, DatabaseManager dbManager) {
        super(parentFrame, username, dbManager, new PacmanGame(username, dbManager), PACMAN_DELAY);
        setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
    }

    @Override
    protected void renderGame(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(new GradientPaint(0, 0, new Color(20, 20, 40), WINDOW_SIZE, WINDOW_SIZE, new Color(40, 40, 80)));
        g2.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);

        List<Object> renderData = game.getRenderData();
        Pacman pacman = (Pacman) renderData.get(0);
        List<Ghost> ghosts = new ArrayList<>();
        for (int i = 1; i < renderData.size() - 1; i++) {
            ghosts.add((Ghost) renderData.get(i));
        }
        Maze maze = (Maze) renderData.get(renderData.size() - 1);

        int[][] grid = maze.getGrid();
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (grid[x][y] == Maze.WALL) {
                    g2.setColor(Color.BLUE);
                    g2.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g2.setColor(Color.WHITE);
                    g2.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        for (Dot dot : maze.getDots()) {
            g2.setColor(dot.getColor());
            if (dot instanceof NormalDot) {
                g2.fillOval(dot.getPosition().x * CELL_SIZE + 12, dot.getPosition().y * CELL_SIZE + 12, 6, 6);
            } else if (dot instanceof PowerPellet) {
                int size = 14 + (animationFrame % 2);
                g2.fillOval(dot.getPosition().x * CELL_SIZE + 8 - size / 4, dot.getPosition().y * CELL_SIZE + 8 - size / 4, size, size);
            }
        }

        drawPacman(g2, pacman);
        for (Ghost ghost : ghosts) {
            drawGhost(g2, ghost);
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Score: " + game.getScore(), 12, 22);
        g2.setColor(Color.YELLOW);
        g2.drawString("Score: " + game.getScore(), 10, 20);

        animationFrame = (animationFrame + 1) % 4;

        requestFocusInWindow();
    }

    private void drawPacman(Graphics2D g2, Pacman pacman) {
        Point pacmanPos = pacman.getPosition();
        int x = pacmanPos.x * CELL_SIZE;
        int y = pacmanPos.y * CELL_SIZE;
        Direction dir = pacman.getDirection();

        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillOval(x + 2, y + 2, CELL_SIZE + 4, CELL_SIZE + 4);

        g2.setColor(Color.YELLOW);
        g2.fillOval(x, y, CELL_SIZE, CELL_SIZE);

        boolean mouthOpen = animationFrame % 3 == 0;
        if (mouthOpen) {
            g2.setColor(Color.BLACK);
            int startAngle = switch (dir) {
                case RIGHT -> 45;
                case LEFT -> 225;
                case UP -> 135;
                case DOWN -> 315;
            };
            g2.fillArc(x, y, CELL_SIZE, CELL_SIZE, startAngle, -90);
        }
    }

    private void drawGhost(Graphics2D g2, Ghost ghost) {
        Point ghostPos = ghost.getPosition();
        int x = ghostPos.x * CELL_SIZE;
        int y = ghostPos.y * CELL_SIZE;
        Direction dir = ghost.isEaten() ? Direction.UP : ghost.getLastDirection();

        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillOval(x + 2, y + 2, CELL_SIZE + 4, CELL_SIZE + 4);

        g2.setColor(ghost.getColor());
        g2.fillOval(x, y, CELL_SIZE, CELL_SIZE);

        g2.setColor(Color.WHITE);
        int eyeSize = CELL_SIZE / 3;
        int eyeOffsetX = switch (dir) {
            case LEFT -> CELL_SIZE / 4;
            case RIGHT -> CELL_SIZE / 2;
            default -> CELL_SIZE * 3 / 8;
        };
        int eyeOffsetY = switch (dir) {
            case UP -> CELL_SIZE / 4;
            case DOWN -> CELL_SIZE / 2;
            default -> CELL_SIZE * 3 / 8;
        };
        g2.fillOval(x + eyeOffsetX - eyeSize / 2, y + eyeOffsetY - eyeSize / 2, eyeSize, eyeSize);
        g2.fillOval(x + eyeOffsetX + eyeSize / 2, y + eyeOffsetY - eyeSize / 2, eyeSize, eyeSize);

        g2.setColor(Color.BLACK);
        int pupilSize = eyeSize / 2;
        g2.fillOval(x + eyeOffsetX - eyeSize / 2 + pupilSize - 1, y + eyeOffsetY - pupilSize / 2, pupilSize, pupilSize);
        g2.fillOval(x + eyeOffsetX + eyeSize / 2 + pupilSize - 1, y + eyeOffsetY - pupilSize / 2, pupilSize, pupilSize);
    }

    @Override
    protected String getGameName() {
        return "Pacman";
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