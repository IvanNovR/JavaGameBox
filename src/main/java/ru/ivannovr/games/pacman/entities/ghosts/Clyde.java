package ru.ivannovr.games.pacman.entities.ghosts;

import ru.ivannovr.games.common.Direction;
import java.awt.*;
import java.util.ArrayList;

public class Clyde extends Ghost {
    public Clyde(int x, int y) {
        super(x, y, Color.ORANGE, 4);
    }

    @Override
    protected Direction chooseDirection(ArrayList<Direction> possibleDirs, Point pacmanPos) {
        return chasePacman(possibleDirs, pacmanPos);
    }
}