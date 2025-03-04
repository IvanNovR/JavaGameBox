package ru.ivannovr.games.pacman.entities;

import ru.ivannovr.games.common.Direction;
import ru.ivannovr.games.pacman.Maze;

import java.awt.Point;

public class Pacman {
    private final Point position;
    private Direction direction;

    public Pacman(int x, int y) {
        this.position = new Point(x, y);
        this.direction = Direction.RIGHT;
    }

    public void move(Maze maze) {
        Point newPos = new Point(position);
        switch (direction) {
            case UP: newPos.y--; break;
            case DOWN: newPos.y++; break;
            case LEFT: newPos.x--; break;
            case RIGHT: newPos.x++; break;
        }
        if (maze.isWalkable(newPos.x, newPos.y)) {
            position.setLocation(newPos);
        }
    }

    public Point getPosition() {
        return new Point(position);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}