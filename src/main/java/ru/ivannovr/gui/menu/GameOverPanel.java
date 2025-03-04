package ru.ivannovr.gui.menu;

import ru.ivannovr.gui.BasePanel;
import ru.ivannovr.gui.gamepanels.AbstractGamePanel;
import ru.ivannovr.gui.gamepanels.FlappyBirdFrame;
import ru.ivannovr.gui.gamepanels.PacmanFrame;
import ru.ivannovr.gui.gamepanels.SnakeFrame;
import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends BasePanel {
    private final AbstractGamePanel gamePanel;
    private final String username;
    private final String gameName;

    public GameOverPanel(JFrame parentFrame, AbstractGamePanel gamePanel, DatabaseManager dbManager,
                         String username, String gameName) {
        super(parentFrame, dbManager);
        this.gamePanel = gamePanel;
        this.username = username;
        this.gameName = gameName;
        setBackground(new Color(40, 40, 60, 200));
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 48));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setBounds(0, 100, 600, 60);
        add(gameOverLabel);

        JLabel scoreLabel = new JLabel("Score: " + gamePanel.getGame().getScore(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setBounds(0, 180, 600, 40);
        add(scoreLabel);

        JButton playAgainButton = createStyledButton("Play Again", new Color(80, 200, 120), e -> playAgain());
        playAgainButton.setBounds(200, 250, 200, 60);
        add(playAgainButton);

        JButton menuButton = createStyledButton("Back to Menu", new Color(100, 150, 255), e -> switchPanel(new MainMenu(parentFrame, username, dbManager)));
        menuButton.setBounds(200, 330, 200, 60);
        add(menuButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(60, 60, 90, 200), 0, getHeight(), new Color(30, 30, 50, 200));
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void playAgain() {
        AbstractGamePanel newGamePanel = switch (gameName) {
            case "Snake" -> new SnakeFrame(parentFrame, username, dbManager);
            case "FlappyBird" -> new FlappyBirdFrame(parentFrame, username, dbManager);
            case "Pacman" -> new PacmanFrame(parentFrame, username, dbManager);
            default -> null;
        };
        if (newGamePanel != null) {
            switchPanel(newGamePanel);
            parentFrame.pack();
            newGamePanel.requestFocusInWindow();
        }
    }
}