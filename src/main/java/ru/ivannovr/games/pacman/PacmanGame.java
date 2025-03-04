package ru.ivannovr.games.pacman;

import ru.ivannovr.games.common.AbstractGame;
import ru.ivannovr.games.common.Direction;
import ru.ivannovr.games.pacman.entities.*;
import ru.ivannovr.games.pacman.entities.dots.Dot;
import ru.ivannovr.games.pacman.entities.ghosts.*;
import ru.ivannovr.utils.DatabaseManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PacmanGame extends AbstractGame {
    private final Pacman pacman;
    private final List<Ghost> ghosts;
    private final Maze maze;
    private final int width;
    private final int height;
    private int releaseCounter;
    private int powerPelletTimer;

    public PacmanGame(String username, DatabaseManager dbManager) {
        super("Pacman", username, dbManager);
        this.width = 20;
        this.height = 20;
        this.maze = new Maze(width, height);
        this.pacman = new Pacman(1, height - 2);
        this.ghosts = initializeGhosts();
        this.releaseCounter = 0;
        this.powerPelletTimer = 0;
    }

    private List<Ghost> initializeGhosts() {
        List<Ghost> ghostList = new ArrayList<>();
        int houseX = width / 2 - 1;
        int houseY = height / 2;
        ghostList.add(new Blinky(houseX, houseY));
        ghostList.add(new Pinky(houseX + 1, houseY));
        ghostList.add(new Inky(houseX, houseY + 1));
        ghostList.add(new Clyde(houseX + 1, houseY + 1));
        return ghostList;
    }

    @Override
    public void update() {
        if (gameOver) return;

        pacman.move(maze);
        Dot dot = maze.collectDot(pacman.getPosition().x, pacman.getPosition().y);
        if (dot != null) {
            score.addPoints(dot.getPoints());
            dot.applyEffect(this);
        }

        if (powerPelletTimer > 0) {
            powerPelletTimer--;
            if (powerPelletTimer == 0) {
                deactivatePowerPellet();
            }
        }

        releaseCounter++;
        for (int i = 0; i < ghosts.size(); i++) {
            if (releaseCounter >= (i + 1) * 50 && !ghosts.get(i).isReleased() && !ghosts.get(i).isEaten()) {
                ghosts.get(i).setReleased(true);
            }
        }

        for (Ghost ghost : ghosts) {
            if (ghost.isReleased() || ghost.isEaten()) {
                ghost.move(maze, pacman.getPosition());
                if (!ghost.isEaten() && ghost.getPosition().equals(pacman.getPosition())) {
                    if (ghost.isEdible()) {
                        score.addPoints(200);
                        ghost.setEaten();
                    } else {
                        endGame();
                        return;
                    }
                }
            }
        }

        if (maze.getDotCount() == 0) {
            endGame();
        }
    }

    public void activatePowerPellet(int duration) {
        powerPelletTimer = duration;
        for (Ghost ghost : ghosts) {
            if (!ghost.isEaten()) {
                ghost.setEdible(true);
            }
        }
    }

    private void deactivatePowerPellet() {
        for (Ghost ghost : ghosts) {
            if (!ghost.isEaten()) {
                ghost.setEdible(false);
            }
        }
    }

    @Override
    public void setControl(Object control) {
        if (control instanceof Direction) {
            pacman.setDirection((Direction) control);
        }
    }

    @Override
    public List<Object> getRenderData() {
        List<Object> data = new ArrayList<>();
        data.add(pacman);
        data.addAll(ghosts);
        data.add(maze);
        return data;
    }
}