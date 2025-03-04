package ru.ivannovr.games.pacman;

import ru.ivannovr.games.pacman.entities.dots.Dot;
import ru.ivannovr.games.pacman.entities.dots.NormalDot;
import ru.ivannovr.games.pacman.entities.dots.PowerPellet;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Maze {
    public static final int WALL = 0;
    public static final int PATH = 1;
    public static final int POWER_PELLET = 2;

    private final int[][] grid;
    private final int width;
    private final int height;
    private final List<Dot> dots;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new MazeGenerator(width, height).generateMaze();
        this.dots = new ArrayList<>();
        placeDots();
    }

    private void placeDots() {
        Point pacmanStart = new Point(1, height - 2);
        Point ghostHouseTopLeft = new Point(width / 2 - 1, height / 2);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!isPacmanStart(x, y, pacmanStart) && !isInGhostHouse(x, y, ghostHouseTopLeft)) {
                    if (grid[x][y] == POWER_PELLET) {
                        dots.add(new PowerPellet(x, y));
                    } else if (grid[x][y] == PATH) {
                        dots.add(new NormalDot(x, y));
                    }
                }
            }
        }
    }

    private boolean isPacmanStart(int x, int y, Point pacmanStart) {
        return x == pacmanStart.x && y == pacmanStart.y;
    }

    private boolean isInGhostHouse(int x, int y, Point topLeft) {
        return x >= topLeft.x && x <= topLeft.x + 1 && y >= topLeft.y && y <= topLeft.y + 1;
    }

    public boolean isWalkable(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height && grid[x][y] != WALL;
    }

    public Dot collectDot(int x, int y) {
        for (int i = 0; i < dots.size(); i++) {
            Dot dot = dots.get(i);
            if (dot.getPosition().x == x && dot.getPosition().y == y) {
                dots.remove(i);
                return dot;
            }
        }
        return null;
    }

    public int[][] getGrid() {
        return grid.clone();
    }

    public List<Dot> getDots() {
        return new ArrayList<>(dots);
    }

    public int getDotCount() {
        return dots.size();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}