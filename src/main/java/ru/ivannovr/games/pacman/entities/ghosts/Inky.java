package ru.ivannovr.games.pacman.entities.ghosts;

import ru.ivannovr.games.common.Direction;
import java.awt.*;
import java.util.ArrayList;

public class Inky extends Ghost {
    public Inky(int x, int y) {
        super(x, y, Color.CYAN, 2);
    }

    @Override
    protected Direction chooseDirection(ArrayList<Direction> possibleDirs, Point pacmanPos) {
        return random.nextDouble() < 0.5 ? chasePacman(possibleDirs, pacmanPos) : chooseSmartDirection(possibleDirs);
    }
}