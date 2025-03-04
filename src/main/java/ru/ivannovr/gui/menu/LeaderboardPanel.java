package ru.ivannovr.gui.menu;

import ru.ivannovr.gui.BasePanel;
import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LeaderboardPanel extends BasePanel {
    private final String username;
    private JComboBox<String> gameSelector;
    private DefaultTableModel tableModel;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public LeaderboardPanel(JFrame parentFrame, String username, DatabaseManager dbManager) {
        super(parentFrame, dbManager);
        this.username = username;
        initializeComponents();
        updateLeaderboard((String) gameSelector.getSelectedItem());
    }

    @Override
    protected void initializeComponents() {
        JLabel title = new JLabel("Leaderboard - JavaGameBox", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 30, 600, 40);
        add(title);

        String[] games = {"Snake", "FlappyBird", "Pacman"};
        gameSelector = new JComboBox<>(games);
        gameSelector.setBounds(150, 100, 300, 40);
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
        gameSelector.addActionListener(e -> updateLeaderboard((String) gameSelector.getSelectedItem()));
        add(gameSelector);

        String[] columnNames = {"Rank", "Username", "Score", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable leaderboardTable = createStyledTable(tableModel);
        if (leaderboardTable.getColumnCount() >= 4) {
            leaderboardTable.getColumnModel().getColumn(0).setPreferredWidth(75);
            leaderboardTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            leaderboardTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            leaderboardTable.getColumnModel().getColumn(3).setPreferredWidth(175);
        }

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setBounds(50, 160, 500, 300);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);

        JButton backButton = createStyledButton("Back to Menu", new Color(255, 100, 100), e -> switchPanel(new MainPanel(parentFrame, username, dbManager)));
        backButton.setBounds(150, 480, 300, 50);
        add(backButton);
    }

    public void updateLeaderboard(String game) {
        tableModel.setRowCount(0);
        List<Map<String, Object>> leaderboardList = dbManager.getLeaderboard(game);
        if (leaderboardList != null) {
            int rank = 1;
            for (Map<String, Object> leaderboardMap : leaderboardList) {
                String user = (String) leaderboardMap.get("username");
                int score = (int) leaderboardMap.get("score");
                Date date = (Date) leaderboardMap.get("record_time");
                String timeStr = (date != null) ? DATE_FORMAT.format(date) : "Not set";
                tableModel.addRow(new Object[]{rank++, user, score, timeStr});
            }
        }
    }
}