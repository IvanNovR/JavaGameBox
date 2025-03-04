package ru.ivannovr.gui.gamepanels;

import ru.ivannovr.games.common.AbstractGame;
import ru.ivannovr.gui.menu.GameOverPanel;
import ru.ivannovr.gui.menu.PausePanel;
import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class AbstractGamePanel extends JPanel {
    protected AbstractGame game;
    protected Timer timer;
    protected JFrame parentFrame;
    protected String username;
    protected DatabaseManager dbManager;
    protected boolean paused;
    protected final int DELAY;

    public AbstractGamePanel(JFrame parentFrame, String username, DatabaseManager dbManager, AbstractGame game, int delay) {
        this.parentFrame = parentFrame;
        this.username = username;
        this.dbManager = dbManager;
        this.game = game;
        this.paused = false;
        this.DELAY = delay;

        setFocusable(true);
        addKeyListener(new GameKeyListener());
        startGame();

        parentFrame.setResizable(false);
        parentFrame.pack();
        requestFocusInWindow();
    }

    public AbstractGame getGame() {
        return game;
    }

    private void startGame() {
        timer = new Timer(DELAY, e -> {
            if (!paused) {
                game.update();
                repaint();
                if (game.isGameOver()) {
                    timer.stop();
                    endGame();
                }
            }
        });
        timer.start();
    }

    public void resume() {
        paused = false;
        requestFocusInWindow();
    }

    private void pauseGame() {
        paused = true;
        parentFrame.setContentPane(new PausePanel(parentFrame, this, dbManager, username, getGameName()));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void endGame() {
        parentFrame.setContentPane(new GameOverPanel(parentFrame, this, dbManager, username, getGameName()));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderGame(g);
    }

    protected abstract void renderGame(Graphics g);
    protected abstract String getGameName();

    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                pauseGame();
            } else if (!paused) {
                handleKeyPress(e);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (!paused) {
                handleKeyRelease(e);
            }
        }
    }

    protected abstract void handleKeyPress(KeyEvent e);
    protected abstract void handleKeyRelease(KeyEvent e);
}
