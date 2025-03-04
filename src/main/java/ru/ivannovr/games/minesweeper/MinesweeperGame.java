package ru.ivannovr.games.minesweeper;

import ru.ivannovr.games.common.AbstractGame;
import ru.ivannovr.utils.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends AbstractGame {
    private final MineField field;
    private final int width = 20;
    private final int height = 20;
    private final int mineCount = 40;
    private boolean firstClick;
    private long startTime;
    private long elapsedTime;

    public MinesweeperGame(String username, DatabaseManager dbManager) {
        super("Minesweeper", username, dbManager);
        this.field = new MineField(width, height, mineCount);
        this.firstClick = true;
        this.startTime = 0;
        this.elapsedTime = 0;
    }

    @Override
    public void update() {
        if (gameOver) return;

        if (startTime > 0) {
            elapsedTime = System.currentTimeMillis() - startTime;
        }

        if (field.isWin()) {
            calculateFinalScore();
            endGame();
        } else if (field.isLose()) {
            endGame();
        }
    }

    public void reveal(int x, int y) {
        if (firstClick) {
            field.placeMines(x, y);
            startTime = System.currentTimeMillis();
            firstClick = false;
        }
        int revealedScore = field.reveal(x, y);
        score.addPoints(revealedScore);
    }

    public void toggleFlag(int x, int y) {
        field.toggleFlag(x, y);
    }

    private void calculateFinalScore() {
        int timeSeconds = (int) (elapsedTime / 1000);
        int maxTime = 600;
        int timeBonus = Math.max(0, (maxTime - timeSeconds) * 10);
        score.addPoints(timeBonus);
        score.addPoints(100);
    }

    @Override
    public void setControl(Object control) {}

    @Override
    public List<Object> getRenderData() {
        List<Object> data = new ArrayList<>();
        data.add(field);
        data.add(elapsedTime);
        return data;
    }

    public MineField getField() {
        return field;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
}