package ru.ivannovr.gui.menu;

import ru.ivannovr.gui.BasePanel;
import ru.ivannovr.gui.gamepanels.*;
import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainPanel extends BasePanel {
    private final String username;
    private JComboBox<String> gameSelector;

    public MainPanel(JFrame parentFrame, String username, DatabaseManager dbManager) {
        super(parentFrame, dbManager);
        this.username = username;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        JLabel titleLabel = new JLabel("JavaGameBox", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 50, 600, 50);
        add(titleLabel);

        String[] games = {"Snake", "FlappyBird", "Pacman", "Minesweeper"};
        gameSelector = new JComboBox<>(games);
        gameSelector.setBounds(150, 200, 300, 40);
        gameSelector.setFont(new Font("Arial", Font.PLAIN, 16));
        gameSelector.setBackground(new Color(50, 50, 80));
        gameSelector.setForeground(Color.WHITE);
        gameSelector.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 120), 1));
        gameSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected ? new Color(80, 80, 120) : new Color(50, 50, 80));
                label.setForeground(Color.WHITE);
                label.setBorder(new EmptyBorder(5, 10, 5, 10));
                return label;
            }
        });
        add(gameSelector);

        JButton startButton = createStyledButton("Start Game", new Color(80, 200, 120), e -> startGame());
        startButton.setBounds(150, 260, 300, 50);
        add(startButton);

        JButton leaderboardButton = createStyledButton("Leaderboard", new Color(100, 150, 255), e -> switchPanel(new LeaderboardPanel(parentFrame, username, dbManager)));
        leaderboardButton.setBounds(150, 330, 300, 50);
        add(leaderboardButton);

        JButton logoutButton = createStyledButton("Logout", new Color(255, 165, 0), e -> switchPanel(new LoginPanel(parentFrame, dbManager)));
        logoutButton.setBounds(150, 400, 140, 50);
        add(logoutButton);

        JButton exitButton = createStyledButton("Exit", new Color(255, 100, 100), e -> exitApplication());
        exitButton.setBounds(310, 400, 140, 50);
        add(exitButton);
    }

    private void startGame() {
        String selectedGame = (String) gameSelector.getSelectedItem();
        if (selectedGame == null) return;
        JPanel gamePanel = switch (selectedGame) {
            case "Snake" -> new SnakePanel(parentFrame, username, dbManager);
            case "FlappyBird" -> new FlappyBirdPanel(parentFrame, username, dbManager);
            case "Pacman" -> new PacmanPanel(parentFrame, username, dbManager);
            case "Minesweeper" -> new MinesweeperPanel(parentFrame, username, dbManager);
            default -> null;
        };
        if (gamePanel != null) {
            switchPanel(gamePanel);
            parentFrame.pack();
        }
    }

    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit JavaGameBox",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}