package ru.ivannovr.gui.menu;

import ru.ivannovr.gui.BasePanel;
import ru.ivannovr.gui.gamepanels.AbstractGamePanel;
import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PausePanel extends BasePanel {
    private final AbstractGamePanel gamePanel;
    private final String username;
    private final String gameName;
    private JLabel countdownLabel;
    private Timer countdownTimer;
    private JButton continueButton;
    private JButton surrenderButton;

    public PausePanel(JFrame parentFrame, AbstractGamePanel gamePanel, DatabaseManager dbManager,
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
        JLabel pauseLabel = new JLabel("Paused", SwingConstants.CENTER);
        pauseLabel.setFont(new Font("Arial", Font.BOLD, 36));
        pauseLabel.setForeground(Color.WHITE);
        pauseLabel.setBounds(0, 100, 600, 50);
        add(pauseLabel);

        continueButton = createStyledButton("Continue", new Color(80, 200, 120), e -> startCountdown());
        continueButton.setBounds(200, 200, 200, 60);
        add(continueButton);

        surrenderButton = createStyledButton("Surrender", new Color(255, 100, 100), e -> endGame());
        surrenderButton.setBounds(200, 280, 200, 60);
        add(surrenderButton);

        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 48));
        countdownLabel.setForeground(Color.WHITE);
        countdownLabel.setBounds(0, 200, 600, 60);
        add(countdownLabel);
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

    private void startCountdown() {
        continueButton.setVisible(false);
        surrenderButton.setVisible(false);
        countdownLabel.setText("3");
        countdownTimer = new Timer(1000, new ActionListener() {
            int count = 3;

            @Override
            public void actionPerformed(ActionEvent e) {
                count--;
                if (count > 0) {
                    countdownLabel.setText(String.valueOf(count));
                } else {
                    countdownLabel.setText("");
                    countdownTimer.stop();
                    resumeGame();
                }
            }
        });
        countdownTimer.start();
    }

    private void resumeGame() {
        switchPanel(gamePanel);
        gamePanel.requestFocusInWindow();
        gamePanel.resume();
    }

    private void endGame() {
        gamePanel.getGame().endGame();
    }
}