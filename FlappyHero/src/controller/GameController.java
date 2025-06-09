package controller;

import util.AudioPlayer;
import model.*;
import view.GamePanel;

import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class GameController implements ActionListener, KeyListener {
    // Declarando variáveis
    GameState gameState;
    GamePanel panel;

    Timer gameLoop;
    Timer placePipeTimer;

    Image topPipeImg;
    Image bottomPipeImg;

    //Som do jogo
    private AudioPlayer jumpSound;
    private AudioPlayer backgroundMusic;
    private AudioPlayer gameOverSound;

    public GameController(GameState state, GamePanel panel, Image topPipeImg, Image bottomPipeImg) {
        this.gameState = state;
        this.panel = panel;
        this.topPipeImg = topPipeImg;
        this.bottomPipeImg = bottomPipeImg;

        panel.addKeyListener(this);

        gameLoop = new Timer(1000 / 60, this); //Diz que a tela do game atualizará 60 frames por segundo
        placePipeTimer = new Timer(1500, e -> placePipes()); //Esse temporizador indica que a cada 1500 milésimos(1,5 segundos) um cano irá surgir

        // Inicializa players de audio
        jumpSound = new AudioPlayer("/sounds/jump.wav");
        backgroundMusic = new AudioPlayer("/sounds/background.wav");
        gameOverSound = new AudioPlayer("/sounds/gameover.wav");

        gameLoop.start();//Inicia o looping do temporizador
        placePipeTimer.start();//Inicia o Looping que irá spawnar o cano
        backgroundMusic.loop(); // Inicia a música de fundo em looping
    }

    void placePipes() {
        int randomY = (int) (gameState.pipeY - gameState.pipeHeight / 4 - Math.random() * (gameState.pipeHeight / 2));
        int opening = gameState.boardHeight / 4;//Define uma abertura fixa entre os canos

        //Define uma altura aleatória aos canos (utilizando a variável atrelada a biblioteca Random)
        Pipe top = new Pipe(gameState.boardWidth, randomY, gameState.pipeWidth, gameState.pipeHeight, topPipeImg);
        Pipe bottom = new Pipe(gameState.boardWidth, randomY + gameState.pipeHeight + opening, gameState.pipeWidth, gameState.pipeHeight, bottomPipeImg);

        gameState.placePipes(top, bottom);
    }

    //Função que põe em prática os 60 frames por segundo, os jumps e a geração de canos
    @Override
    public void actionPerformed(ActionEvent e) {
        gameState.moveHero();
        gameState.movePipes();
        panel.repaint();

        if (gameState.gameOver) {
            gameLoop.stop();
            placePipeTimer.stop();
            gameOverSound.play(); // Toca o som de game over
        }
    }

    @Override

    // Essa função torna possível o botao ESPAÇO ativar o jump
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            gameState.velocityY = -9; // Altura do Jump
            jumpSound.stop(); // Para o som de pulo anterior
            jumpSound.play(); // Toca o novo som de pulo a partir do início

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

    // Método para fechar os players de áudio quando a aplicação for encerrada
    public void closeAudioPlayers() {
        if (jumpSound != null) jumpSound.close();
        if (backgroundMusic != null) backgroundMusic.close();
        if (gameOverSound != null) gameOverSound.close();
    }
}