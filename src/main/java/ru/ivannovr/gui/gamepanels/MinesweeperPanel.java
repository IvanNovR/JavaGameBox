package ru.ivannovr.gui.gamepanels;

import ru.ivannovr.games.minesweeper.MineField;
import ru.ivannovr.games.minesweeper.MinesweeperGame;
import ru.ivannovr.games.minesweeper.entities.Cell;
import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MinesweeperPanel extends AbstractGamePanel {
    private static final int CELL_SIZE = 30;
    private static final int GRID_SIZE = 20;
    private static final int WINDOW_SIZE = CELL_SIZE * GRID_SIZE;
    private static final int MINESWEEPER_DELAY = 100;

    public MinesweeperPanel(JFrame parentFrame, String username, DatabaseManager dbManager) {
        super(parentFrame, username, dbManager, new MinesweeperGame(username, dbManager), MINESWEEPER_DELAY);
        setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
        addMouseListener(new MouseHandler());
    }

    @Override
    protected void renderGame(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(169, 169, 169));
        g2.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);

        List<Object> renderData = game.getRenderData();
        MineField field = (MineField) renderData.get(0);
        long elapsedTime = (long) renderData.get(1);

        for (int x = 0; x < field.getWidth(); x++) {
            for (int y = 0; y < field.getHeight(); y++) {
                Cell cell = field.getCell(x, y);
                int px = x * CELL_SIZE;
                int py = y * CELL_SIZE;

                if (!cell.isRevealed() && !cell.isFlag()) {
                    g2.setColor(new Color(128, 128, 128));
                    g2.fillRect(px, py, CELL_SIZE, CELL_SIZE);
                } else {
                    g2.setColor(cell.isRevealed() ? new Color(211, 211, 211) : new Color(128, 128, 128));
                    g2.fillRect(px, py, CELL_SIZE, CELL_SIZE);
                    g2.setColor(cell.getColor());
                    if (cell.isFlag()) {
                        g2.setColor(Color.BLACK);
                        g2.fillRect(px + 18, py + 8, 2, 16);

                        g2.setColor(Color.RED);
                        int[] xPoints = {px + 18, px + 8, px + 18};
                        int[] yPoints = {py + 8, py + 12, py + 18};
                        g2.fillPolygon(xPoints, yPoints, 3);
                    } else if (cell.isMine() && cell.isRevealed()) {
                        g2.fillOval(px + 8, py + 8, 15, 15);
                    } else if (cell.isRevealed()) {
                        g2.setFont(new Font("Arial", Font.BOLD, 17));
                        g2.drawString(cell.getDisplayText(), px + 11, py + 21);
                    }
                }
                g2.setColor(Color.BLACK);
                g2.drawRect(px, py, CELL_SIZE, CELL_SIZE);
            }
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Score: " + game.getScore(), 12, 22);
        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + game.getScore(), 10, 20);

        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        String timeStr = String.format("Time: %02d:%02d", minutes, seconds);
        g2.setColor(Color.BLACK);
        g2.drawString(timeStr, 152, 22);
        g2.setColor(Color.WHITE);
        g2.drawString(timeStr, 150, 20);

        requestFocusInWindow();
    }

    @Override
    protected String getGameName() {
        return "Minesweeper";
    }

    @Override
    protected void handleKeyPress(KeyEvent e) {}

    @Override
    protected void handleKeyRelease(KeyEvent e) {}

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (paused || game.isGameOver()) return;

            int x = e.getX() / CELL_SIZE;
            int y = e.getY() / CELL_SIZE;

            if (SwingUtilities.isLeftMouseButton(e)) {
                ((MinesweeperGame) game).reveal(x, y);
            } else if (SwingUtilities.isRightMouseButton(e)) {
                ((MinesweeperGame) game).toggleFlag(x, y);
            }
            repaint();
        }
    }
}