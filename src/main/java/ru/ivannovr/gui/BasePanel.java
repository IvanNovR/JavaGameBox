package ru.ivannovr.gui;

import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class BasePanel extends JPanel {
    protected final JFrame parentFrame;
    protected final DatabaseManager dbManager;

    public BasePanel(JFrame parentFrame, DatabaseManager dbManager) {
        this.parentFrame = parentFrame;
        this.dbManager = dbManager;
        setLayout(null);
        setBackground(new Color(30, 30, 50));
    }

    protected JButton createStyledButton(String text, Color baseColor, ActionListener actionListener) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(baseColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }

    protected JTable createStyledTable(DefaultTableModel tableModel) {
        JTable table = new JTable(tableModel);
        table.setBackground(new Color(40, 40, 60));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setSelectionBackground(new Color(60, 60, 90));
        table.setSelectionForeground(Color.YELLOW);
        table.setOpaque(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setOpaque(true);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getTableHeader().setBackground(new Color(50, 50, 80));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setReorderingAllowed(false);

        return table;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(50, 50, 80), 0, getHeight(), new Color(20, 20, 40));
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    protected abstract void initializeComponents();

    protected void switchPanel(JPanel newPanel) {
        parentFrame.setContentPane(newPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}