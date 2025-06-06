package controller;

import model.*;
import view.GamePanel;

import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class GameController implements ActionListener, KeyListener {
    GameState gameState;
    GamePanel panel;

    Timer gameLoop;
    Timer placePipeTimer;

    Image topPipeImg;
    Image bottomPipeImg;

    public GameController(GameState state, GamePanel panel, Image topPipeImg, Image bottomPipeImg) {
        this.gameState = state;
        this.panel = panel;
        this.topPipeImg = topPipeImg;
        this.bottomPipeImg = bottomPipeImg;

        panel.addKeyListener(this);

        gameLoop = new Timer(1000 / 60, this);
        placePipeTimer = new Timer(1500, e -> placePipes());

        gameLoop.start();
        placePipeTimer.start();
    }

    void placePipes() {
        int randomY = (int) (gameState.pipeY - gameState.pipeHeight / 4 - Math.random() * (gameState.pipeHeight / 2));
        int opening = gameState.boardHeight / 4;

        Pipe top = new Pipe(gameState.boardWidth, randomY, gameState.pipeWidth, gameState.pipeHeight, topPipeImg);
        Pipe bottom = new Pipe(gameState.boardWidth, randomY + gameState.pipeHeight + opening, gameState.pipeWidth, gameState.pipeHeight, bottomPipeImg);

        gameState.placePipes(top, bottom);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameState.moveHero();
        gameState.movePipes();
        panel.repaint();

        if (gameState.gameOver) {
            gameLoop.stop();
            placePipeTimer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            gameState.velocityY = -9;

            if (gameState.gameOver) {
                Hero newHero = new Hero(gameState.boardWidth / 8, gameState.boardHeight / 3, 58, 40, gameState.hero.img);
                gameState.reset(newHero);
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
