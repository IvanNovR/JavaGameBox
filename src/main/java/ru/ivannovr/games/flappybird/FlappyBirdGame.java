package ru.ivannovr.games.flappybird;

import ru.ivannovr.games.common.AbstractGame;
import ru.ivannovr.games.flappybird.entities.Bird;
import ru.ivannovr.games.flappybird.entities.Pipe;
import ru.ivannovr.utils.DatabaseManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FlappyBirdGame extends AbstractGame {
    private final Bird bird;
    private final LinkedList<Pipe> pipes;
    private final int width = 600;
    private final int height = 600;
    private long lastPipeSpawnTime = 0;
    private static final long PIPE_SPAWN_INTERVAL = 100;

    public FlappyBirdGame(String username, DatabaseManager dbManager) {
        super("FlappyBird", username, dbManager);
        bird = new Bird(100, height / 2);
        pipes = new LinkedList<>();
        spawnPipe();
    }

    private void spawnPipe() {
        pipes.add(new Pipe(width, height));
    }

    @Override
    public void update() {
        if (gameOver) return;

        bird.update();

        for (Pipe pipe : pipes) {
            pipe.update();
            if (bird.collidesWith(pipe) || bird.isOutOfBounds(height)) {
                endGame();
                return;
            }
            if (!pipe.isPassed() && pipe.getX() + pipe.getWidth() < bird.getPosition().x) {
                pipe.setPassed(true);
                score.addPoints(10);
            }
        }

        pipes.removeIf(Pipe::isOffScreen);

        lastPipeSpawnTime = (lastPipeSpawnTime + 1) % PIPE_SPAWN_INTERVAL;
        if (lastPipeSpawnTime == 0) {
            spawnPipe();;
        }
    }

    @Override
    public void setControl(Object control) {
        if (control instanceof String) {
            switch ((String) control) {
                case "JUMP": bird.jump(); break;
                case "LEFT": bird.moveLeft(); break;
                case "RIGHT": bird.moveRight(); break;
                case "STOP": bird.stopHorizontal(); break;
            }
        }
    }

    @Override
    public List<Object> getRenderData() {
        List<Object> data = new ArrayList<>();
        data.add(bird);
        data.addAll(pipes);
        return data;
    }
}