package controller;

import util.AudioPlayer;
import model.*;
import view.GamePanel;

import java.awt.Image;
import java.awt.Rectangle; // Importe a classe Rectangle
import javax.swing.*;
import java.awt.event.*;
import java.io.*; // Importe para leitura/escrita de arquivos
import java.util.Scanner; // Importe para leitura de arquivos

// 1. A classe agora também implementa MouseListener para ouvir cliques do mouse.
public class GameController implements ActionListener, KeyListener, MouseListener {
    // ... os campos da classe continuam os mesmos ...
    GameState gameState;
    GamePanel panel;
    Timer gameLoop;
    Timer placePipeTimer;
    Image topPipeImg;
    Image bottomPipeImg;
    private AudioPlayer jumpSound;
    private AudioPlayer backgroundMusic;
    private AudioPlayer gameOverSound;

    public GameController(GameState state, GamePanel panel, Image topPipeImg, Image bottomPipeImg) {
        this.gameState = state;
        this.panel = panel;
        this.topPipeImg = topPipeImg;
        this.bottomPipeImg = bottomPipeImg;

        // 2. Adicione este objeto (o controller) como ouvinte de mouse do painel.
        panel.addKeyListener(this);
        panel.addMouseListener(this);

        gameLoop = new Timer(1000 / 60, this);
        placePipeTimer = new Timer(1500, e -> placePipes());

        jumpSound = new AudioPlayer("/sounds/jump.wav");
        backgroundMusic = new AudioPlayer("/sounds/background.wav");
        gameOverSound = new AudioPlayer("/sounds/gameover.wav");

        // 3. O jogo não começa mais automaticamente. Carregamos o recorde e esperamos o jogador.
        loadHighScore();

        // As linhas abaixo foram removidas daqui. O jogo só começa com o clique no botão "Start".
        // gameLoop.start();
        // placePipeTimer.start();
        // backgroundMusic.loop();
    }

    private void startGame() {
        // Reinicia o estado do jogo para um novo jogo.
        Hero newHero = new Hero(gameState.boardWidth / 8, gameState.boardHeight / 3, 58, 40, gameState.hero.img);
        gameState.reset(newHero);
        gameState.currentStatus = GameState.Status.PLAYING;

        // Inicia os timers e a música de fundo.
        gameLoop.start();
        placePipeTimer.start();
        backgroundMusic.loop();
    }
    
    // O método placePipes continua o mesmo.
    void placePipes() {
        int randomY = (int) (gameState.pipeY - gameState.pipeHeight / 4 - Math.random() * (gameState.pipeHeight / 2));
        int opening = gameState.boardHeight / 4;//Define uma abertura fixa entre os canos

        Pipe top = new Pipe(gameState.boardWidth, randomY, gameState.pipeWidth, gameState.pipeHeight, topPipeImg);
        Pipe bottom = new Pipe(gameState.boardWidth, randomY + gameState.pipeHeight + opening, gameState.pipeWidth, gameState.pipeHeight, bottomPipeImg);

        gameState.placePipes(top, bottom);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 4. A lógica principal do jogo só é executada se o estado for PLAYING.
        if (gameState.currentStatus == GameState.Status.PLAYING) {
            gameState.moveHero();
            gameState.movePipes();

            // Verifica se o jogo acabou nesta iteração.
            if (gameState.currentStatus == GameState.Status.GAME_OVER) {
                gameLoop.stop();
                placePipeTimer.stop();
                backgroundMusic.stop();
                gameOverSound.play();

                // Compara a pontuação atual com o recorde e salva se for maior.
                if (gameState.score > gameState.highScore) {
                    gameState.highScore = gameState.score;
                    saveHighScore();
                }
            }
        }
        // O repaint acontece em todos os estados, para manter a tela sempre atualizada.
        panel.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // O pulo com espaço só funciona quando o jogo está em andamento.
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameState.currentStatus == GameState.Status.PLAYING) {
            gameState.velocityY = -9;
            jumpSound.stop();
            jumpSound.play();
        }
    }
    
    // 5. Este é o novo método que lida com os cliques do mouse.
    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        switch (gameState.currentStatus) {
            case MENU:
                // Define as áreas clicáveis dos botões.
                // Estes valores (x, y, largura, altura) devem corresponder ao que foi desenhado no GamePanel.
                Rectangle startButtonArea = new Rectangle(80, 270, 200, 55);
                Rectangle scoreButtonArea = new Rectangle(80, 370, 200, 55);

                if (startButtonArea.contains(mouseX, mouseY)) {
                    startGame(); // Inicia o jogo
                } else if (scoreButtonArea.contains(mouseX, mouseY)) {
                    gameState.currentStatus = GameState.Status.SCORE_VIEW; // Muda para a tela de recorde
                }
                break;
            
            case GAME_OVER:
            case SCORE_VIEW:
                // Em ambas as telas, um clique em qualquer lugar retorna ao menu.
                gameState.currentStatus = GameState.Status.MENU;
                break;
        }
    }
    
    // 6. Métodos para salvar e carregar o recorde em um arquivo "highscore.txt".
    private void saveHighScore() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("highscore.txt"))) {
            writer.println((int) gameState.highScore);
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

    // Métodos não utilizados que precisam ser implementados por causa das interfaces.
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void closeAudioPlayers() { // Este método já existia.
        if (jumpSound != null) jumpSound.close();
        if (backgroundMusic != null) backgroundMusic.close();
        if (gameOverSound != null) gameOverSound.close();
    }
}
