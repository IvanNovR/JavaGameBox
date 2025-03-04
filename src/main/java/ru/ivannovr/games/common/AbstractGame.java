package ru.ivannovr.games.common;

import ru.ivannovr.utils.DatabaseManager;

import java.util.Map;

public abstract class AbstractGame implements Game, Controllable, Renderable {
    protected Score score;
    protected boolean gameOver;
    protected String gameName;
    protected String username;
    protected DatabaseManager dbManager;

    public AbstractGame(String gameName, String username, DatabaseManager dbManager) {
        this.score = new Score();
        this.gameOver = false;
        this.gameName = gameName;
        this.username = username;
        this.dbManager = dbManager;
    }

    @Override
    public int getScore() {
        return score.getPoints();
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    public void endGame() {
        Map<String, Object> userLeaderboardMap = dbManager.getUserLeaderboard(username, gameName);
        if (userLeaderboardMap == null || userLeaderboardMap.isEmpty() || score.getPoints() > (int) userLeaderboardMap.get("score"))
            dbManager.updateScore(username, gameName, score.getPoints());
        gameOver = true;
    }
}
