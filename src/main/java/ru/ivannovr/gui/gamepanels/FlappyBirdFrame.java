package ru.ivannovr.gui.gamepanels;

import ru.ivannovr.games.flappybird.entities.Bird;
import ru.ivannovr.games.flappybird.FlappyBirdGame;
import ru.ivannovr.games.flappybird.entities.Pipe;
import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class FlappyBirdFrame extends AbstractGamePanel {
    private static final int WINDOW_SIZE = 600;
    private static final int FLAPPY_DELAY = 20;
    private int cloudOffset = 0;

    public FlappyBirdFrame(JFrame parentFrame, String username, DatabaseManager dbManager) {
        super(parentFrame, username, dbManager, new FlappyBirdGame(username, dbManager), FLAPPY_DELAY);
        setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
    }

    @Override
    protected void renderGame(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(new GradientPaint(0, 0, new Color(112, 197, 206), 0, WINDOW_SIZE, new Color(173, 216, 230)));
        g2.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);

        cloudOffset = (cloudOffset + 1) % (WINDOW_SIZE + 300);
        g2.setColor(Color.WHITE);
        g2.fillOval(cloudOffset - 100, 50, 80, 40);
        g2.fillOval(cloudOffset - 200, 100, 100, 50);
        g2.fillOval(cloudOffset - 300, 70, 60, 30);

        g2.setColor(new Color(147, 197, 114));
        int groundHeight = 100;
        g2.fillRect(0, WINDOW_SIZE - groundHeight, WINDOW_SIZE, groundHeight);

        List<Object> renderData = ((FlappyBirdGame) game).getRenderData();
        Bird bird = (Bird) renderData.get(0);

        g2.setColor(new Color(99, 155, 67));
        for (int i = 1; i < renderData.size(); i++) {
            Pipe pipe = (Pipe) renderData.get(i);
            int x = pipe.getX();
            int gapY = pipe.getGapY();
            int width = pipe.getWidth();
            int gapHeight = pipe.getGapHeight();

            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRect(x + 5, 0, width, gapY);
            g2.fillRect(x + 5, gapY + gapHeight, width, WINDOW_SIZE - (gapY + gapHeight));

            g2.setColor(Color.BLACK);
            g2.drawRect(x - 1, 0, width + 1, gapY);
            g2.drawRect(x - 6, gapY - 21, width + 11, 21);
            g2.drawRect(x - 1, gapY + gapHeight, width + 1, WINDOW_SIZE - (gapY + gapHeight));
            g2.drawRect(x - 6, gapY + gapHeight - 1, width + 11, 21);

            g2.setColor(new Color(99, 155, 67));
            g2.fillRect(x, 0, width, gapY);
            g2.fillRect(x - 5, gapY - 20, width + 10, 20);
            g2.fillRect(x, gapY + gapHeight, width, WINDOW_SIZE - (gapY + gapHeight));
            g2.fillRect(x - 5, gapY + gapHeight, width + 10, 20);
        }

        Point birdPos = bird.getPosition();
        int birdSize = 30;
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillOval(birdPos.x + 2, birdPos.y + 2, birdSize, birdSize);
        g2.setColor(Color.YELLOW);
        g2.fillOval(birdPos.x, birdPos.y, birdSize, birdSize);
        g2.setColor(Color.WHITE);
        g2.fillOval(birdPos.x + birdSize / 2, birdPos.y + 5, 10, 10);
        g2.setColor(Color.BLACK);
        g2.fillOval(birdPos.x + birdSize / 2 + 3, birdPos.y + 8, 4, 4);
        g2.setColor(new Color(255, 153, 0));
        g2.fillPolygon(
                new int[] {birdPos.x + birdSize, birdPos.x + birdSize + 10, birdPos.x + birdSize},
                new int[] {birdPos.y + 10, birdPos.y + 15, birdPos.y + 20},
                3
        );

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Score: " + game.getScore(), 12, 32);
        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + game.getScore(), 10, 30);

        requestFocusInWindow();
    }

    @Override
    protected String getGameName() {
        return "FlappyBird";
    }

    @Override
    protected void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE: game.setControl("JUMP"); break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT: game.setControl("LEFT"); break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT: game.setControl("RIGHT"); break;
        }
    }

    @Override
    protected void handleKeyRelease(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT ||
                e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            game.setControl("STOP");
        }
    }
}