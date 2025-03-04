package ru.ivannovr.games.common;

public class Score {
    private int points;

    public Score() {
        this.points = 0;
    }

    public void addPoints(int points) {
        this.points += points;
        if (this.points < 0) this.points = 0;
    }

    public int getPoints() {
        return points;
    }
}