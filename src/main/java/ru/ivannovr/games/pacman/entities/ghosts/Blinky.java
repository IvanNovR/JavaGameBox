package ru.ivannovr.games.pacman.entities.ghosts;

import ru.ivannovr.games.common.Direction;
import java.awt.*;
import java.util.ArrayList;

public class Blinky extends Ghost {
    public Blinky(int x, int y) {
        super(x, y, Color.RED, 4);
    }

    @Override
    protected Direction chooseDirection(ArrayList<Direction> possibleDirs, Point pacmanPos) {
        return random.nextDouble() < 0.5 ? chasePacman(possibleDirs, pacmanPos) : chooseSmartDirection(possibleDirs);
    }
}