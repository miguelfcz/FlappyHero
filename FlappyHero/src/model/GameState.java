package model;

import java.util.ArrayList;

public class GameState {

    public enum Status {
        MENU,
        PLAYING,
        GAME_OVER
    }

    public int boardWidth = 360;
    public int boardHeight = 640;

    public Hero hero;
    public ArrayList<Pipe> pipes = new ArrayList<>();
    public double score = 0;
    public int highScore = 0;

    public int gravity = 1;
    public int velocityY = 0;
    public int velocityX = -4;

    public int pipeWidth = 64;
    public int pipeHeight = 512;
    public int pipeY = 0;

    public Status currentStatus;

    public GameState() {
        this.pipes = new ArrayList<>();
        this.currentStatus = Status.MENU;
    }

    public void reset(Hero newHero) {
        this.hero = newHero;
        pipes.clear();
        score = 0;
        velocityY = 0;
        currentStatus = Status.PLAYING;
    }

    public void moveHero() {
        velocityY += gravity;
        hero.move(velocityY);
        if (hero.y > boardHeight) {
            currentStatus = Status.GAME_OVER;
        }
    }

    public void movePipes() {
        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && hero.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }

            if (collision(hero, pipe)) {
                currentStatus = Status.GAME_OVER;
            }
        }
    }

    public void placePipes(Pipe top, Pipe bottom) {
        pipes.add(top);
        pipes.add(bottom);
    }

    public boolean collision(Hero a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }
}