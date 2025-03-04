package ru.ivannovr.games.snake.entities;

import ru.ivannovr.games.common.Direction;

import java.awt.*;
import java.util.LinkedList;

public class Snake {
    private final LinkedList<Point> body;
    private Direction direction;

    public Snake(int startX, int startY) {
        body = new LinkedList<>();
        body.add(new Point(startX, startY));
        direction = Direction.RIGHT;
    }

    public void move() {
        Point head = new Point(getHead());
        switch (direction) {
            case UP: head.y--; break;
            case DOWN: head.y++; break;
            case LEFT: head.x--; break;
            case RIGHT: head.x++; break;
        }
        body.addFirst(head);
        body.removeLast();
    }

    public void grow() {
        Point tail = body.getLast();
        Point newTail = new Point(tail);

        switch (direction) {
            case UP: newTail.y++; break;
            case DOWN: newTail.y--; break;
            case LEFT: newTail.x++; break;
            case RIGHT: newTail.x--; break;
        }

        body.addLast(newTail);
    }

    public Point getHead() {
        return body.getFirst();
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public void setDirection(Direction direction) {
        if (this.direction.opposite() != direction) {
            this.direction = direction;
        }
    }

    public boolean collidesWith(Point point) {
        return body.contains(point);
    }

    public boolean selfCollision() {
        Point head = getHead();
        if (body.size() < 2) {
            return false;
        }
        return body.stream()
                .skip(1)
                .anyMatch(p -> p.equals(head));
    }

    public void wrapAround(int width, int height) {
        for (Point p : body) {
            if (p.x < 0) p.x = width - 1;
            if (p.x >= width) p.x = 0;
            if (p.y < 0) p.y = height - 1;
            if (p.y >= height) p.y = 0;
        }
    }
}