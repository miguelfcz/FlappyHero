package model;

import java.util.ArrayList;
import java.util.Random;

public class GameState {
    public int boardWidth = 360;
    public int boardHeight = 640;

    public Hero hero;
    public ArrayList<Pipe> pipes = new ArrayList<>();
    public boolean gameOver = false;
    public double score = 0;

    public int gravity = 1;
    public int velocityY = 0;
    public int velocityX = -4;

    public int pipeWidth = 64;
    public int pipeHeight = 512;
    public int pipeY = 0;


    public void reset(Hero newHero) {
        this.hero = newHero;
        pipes.clear();
        score = 0;
        velocityY = 0;
        gameOver = false;
    }

    public void moveHero() {
        velocityY += gravity; //efeito da gravidade no boneco, 60 quadros por segundo
        hero.move(velocityY);
        if (hero.y > boardHeight) { //se o herói cair mais que a altura da tela, o jogo finaliza
            gameOver = true;
        }
    }

    public void movePipes() {
        for (Pipe pipe : pipes) {
            pipe.x += velocityX; //movimentação do cano para a esquerda

            if (!pipe.passed && hero.x > pipe.x + pipe.width) { //lógica da pontuação
                score += 0.5; //o valor é divido por 0.5, pois ele passa pelos dois canos o de cima e o de baixo, que ao somar, vai dar 1
                pipe.passed = true;
            }

            if (collision(hero, pipe)) {
                gameOver = true;
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
