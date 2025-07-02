package controller;

import util.AudioPlayer;
import model.*;
import view.GamePanel;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class GameController implements ActionListener, KeyListener, MouseListener {
    GameState gameState;
    GamePanel gamePanel;
    Timer gameLoop;
    Timer placePipeTimer;
    Image topPipeImg;
    Image bottomPipeImg;
    private AudioPlayer jumpSound;
    private AudioPlayer backgroundMusic;
    private AudioPlayer gameOverSound;

    public GameController(GameState gameState, GamePanel gamePanel, Image topPipeImg, Image bottomPipeImg) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.topPipeImg = topPipeImg;
        this.bottomPipeImg = bottomPipeImg;

        gamePanel.addKeyListener(this);
        gamePanel.addMouseListener(this);

        gameLoop = new Timer(1000 / 60, this);
        placePipeTimer = new Timer(1500, e -> placePipes());

        jumpSound = new AudioPlayer("/sounds/jump.wav");
        backgroundMusic = new AudioPlayer("/sounds/background.wav");
        gameOverSound = new AudioPlayer("/sounds/gameover.wav");

        loadHighScore();
        gameLoop.start();
    }

    private void startGame() {
        Hero newHero = new Hero(gameState.boardWidth / 8, gameState.boardHeight / 3, 58, 40, gameState.hero.img);
        gameState.reset(newHero);
        placePipeTimer.start();
        backgroundMusic.loop();
    }

    void placePipes() {
        int randomPipeY = (int) (gameState.pipeY - gameState.pipeHeight / 4 - Math.random() * (gameState.pipeHeight / 2));
        int opening = gameState.boardHeight / 4;

        Pipe topPipe = new Pipe(gameState.boardWidth, randomPipeY, gameState.pipeWidth, gameState.pipeHeight, topPipeImg);
        Pipe bottomPipe = new Pipe(gameState.boardWidth, randomPipeY + gameState.pipeHeight + opening, gameState.pipeWidth, gameState.pipeHeight, bottomPipeImg);

        gameState.placePipes(topPipe, bottomPipe);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (gameState.currentStatus == GameState.Status.PLAYING) {
            gameState.moveHero();
            gameState.movePipes();

            if (gameState.currentStatus == GameState.Status.GAME_OVER) {
                placePipeTimer.stop();
                backgroundMusic.stop();
                gameOverSound.play();

                if (gameState.score > gameState.highScore) {
                    gameState.highScore = (int) gameState.score;
                    saveHighScore();
                }
            }
        }
        gamePanel.repaint();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_SPACE && gameState.currentStatus == GameState.Status.PLAYING) {
            gameState.velocityY = -9;
            jumpSound.stop();
            jumpSound.play();
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (gameState.currentStatus == GameState.Status.MENU) {
            startGame();
        } else if (gameState.currentStatus == GameState.Status.GAME_OVER) {
            gameState.currentStatus = GameState.Status.MENU;
        }
    }

    private void saveHighScore() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("highscore.txt"))) {
            writer.println(gameState.highScore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHighScore() {
        File file = new File("highscore.txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                if (scanner.hasNextInt()) {
                    gameState.highScore = scanner.nextInt();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public void closeAudioPlayers() {
        if (jumpSound != null) jumpSound.close();
        if (backgroundMusic != null) backgroundMusic.close();
        if (gameOverSound != null) gameOverSound.close();
    }
}