package ru.ivannovr.games.pacman.entities.ghosts;

import ru.ivannovr.games.common.Direction;
import ru.ivannovr.games.pacman.Maze;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public abstract class Ghost {
    private final Point position;
    protected final Random random;
    private boolean released;
    private Direction lastDirection;
    private int moveCounter;
    private final int moveDelay;
    private boolean edible;
    private final Color normalColor;
    private static final Color EDIBLE_COLOR = new Color(0, 102, 204);
    private boolean eaten;
    private int respawnTimer;
    private static final int RESPAWN_DELAY = 50;
    private final Point spawnPoint;

    protected Ghost(int x, int y, Color normalColor, int moveDelay) {
        this.position = new Point(x, y);
        this.spawnPoint = new Point(x, y);
        this.random = new Random();
        this.released = false;
        this.lastDirection = Direction.UP;
        this.moveCounter = 0;
        this.moveDelay = moveDelay;
        this.edible = false;
        this.eaten = false;
        this.respawnTimer = 0;
        this.normalColor = normalColor;
    }

    public void move(Maze maze, Point pacmanPos) {
        if (!released) return;

        if (eaten) {
            respawnTimer--;
            if (respawnTimer <= 0) {
                respawn();
            }
            return;
        }

        moveCounter++;
        if (moveCounter % moveDelay != 0) return;

        ArrayList<Direction> possibleDirs = getPossibleDirections(maze);
        if (possibleDirs.isEmpty()) return;

        Direction chosenDir = edible ? fleeFromPacman(possibleDirs, pacmanPos) : chooseDirection(possibleDirs, pacmanPos);
        updatePosition(chosenDir);
        lastDirection = chosenDir;
    }

    protected abstract Direction chooseDirection(ArrayList<Direction> possibleDirs, Point pacmanPos);

    private Direction fleeFromPacman(ArrayList<Direction> possibleDirs, Point pacmanPos) {
        int dx = pacmanPos.x - position.x;
        int dy = pacmanPos.y - position.y;
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 && possibleDirs.contains(Direction.LEFT) ? Direction.LEFT :
                    dx < 0 && possibleDirs.contains(Direction.RIGHT) ? Direction.RIGHT :
                            chooseSmartDirection(possibleDirs);
        } else {
            return dy > 0 && possibleDirs.contains(Direction.UP) ? Direction.UP :
                    dy < 0 && possibleDirs.contains(Direction.DOWN) ? Direction.DOWN :
                            chooseSmartDirection(possibleDirs);
        }
    }

    private ArrayList<Direction> getPossibleDirections(Maze maze) {
        ArrayList<Direction> dirs = new ArrayList<>();
        if (maze.isWalkable(position.x, position.y - 1)) dirs.add(Direction.UP);
        if (maze.isWalkable(position.x, position.y + 1)) dirs.add(Direction.DOWN);
        if (maze.isWalkable(position.x - 1, position.y)) dirs.add(Direction.LEFT);
        if (maze.isWalkable(position.x + 1, position.y)) dirs.add(Direction.RIGHT);
        return dirs;
    }

    protected Direction chooseSmartDirection(ArrayList<Direction> possibleDirs) {
        Direction opposite = getOppositeDirection(lastDirection);
        if (possibleDirs.size() > 1) {
            possibleDirs.remove(opposite);
        }
        return possibleDirs.get(random.nextInt(possibleDirs.size()));
    }

    protected Direction chasePacman(ArrayList<Direction> possibleDirs, Point pacmanPos) {
        int dx = pacmanPos.x - position.x;
        int dy = pacmanPos.y - position.y;
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 && possibleDirs.contains(Direction.RIGHT) ? Direction.RIGHT :
                    dx < 0 && possibleDirs.contains(Direction.LEFT) ? Direction.LEFT :
                            chooseSmartDirection(possibleDirs);
        } else {
            return dy > 0 && possibleDirs.contains(Direction.DOWN) ? Direction.DOWN :
                    dy < 0 && possibleDirs.contains(Direction.UP) ? Direction.UP :
                            chooseSmartDirection(possibleDirs);
        }
    }

    private Direction getOppositeDirection(Direction dir) {
        return switch (dir) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
            default -> dir;
        };
    }

    private void updatePosition(Direction dir) {
        switch (dir) {
            case UP -> position.y--;
            case DOWN -> position.y++;
            case LEFT -> position.x--;
            case RIGHT -> position.x++;
        }
    }

    private void respawn() {
        position.setLocation(spawnPoint);
        eaten = false;
        released = true;
    }

    public void setEaten() {
        this.eaten = true;
        this.edible = false;
        this.position.setLocation(spawnPoint);
        this.respawnTimer = RESPAWN_DELAY;
    }

    public Point getPosition() {
        return new Point(position);
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public boolean isReleased() {
        return released;
    }

    public void setEdible(boolean edible) {
        this.edible = edible;
    }

    public boolean isEdible() {
        return edible;
    }

    public boolean isEaten() {
        return eaten;
    }

    public Color getColor() {
        return edible ? EDIBLE_COLOR : normalColor;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }
}