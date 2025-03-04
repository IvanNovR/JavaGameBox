package ru.ivannovr.games.pacman.entities.ghosts;

import ru.ivannovr.games.common.Direction;
import java.awt.*;
import java.util.ArrayList;

public class Pinky extends Ghost {
    public Pinky(int x, int y) {
        super(x, y, Color.PINK, 4);
    }

    @Override
    protected Direction chooseDirection(ArrayList<Direction> possibleDirs, Point pacmanPos) {
        return chooseSmartDirection(possibleDirs);
    }
}