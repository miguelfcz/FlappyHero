package view;

import model.GameState;
import model.Pipe;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    GameState gameState;
    Image backgroundImg;
    Image startImg;
    Image statusImg;

    public GamePanel(GameState gameState, Image backgroundImg, Image startImg, Image statusImg) {
        this.gameState = gameState;
        this.backgroundImg = backgroundImg;
        this.startImg = startImg;
        this.statusImg = statusImg;
        setPreferredSize(new Dimension(gameState.boardWidth, gameState.boardHeight));
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, gameState.boardWidth, gameState.boardHeight, null);

        if (gameState.currentStatus == GameState.Status.MENU) {
            // Define a largura dos botões para ser 1/3 da largura da tela
            int buttonWidth = gameState.boardWidth / 3;

            // Calcula a altura dos botões mantendo a proporção original da imagem
            int startButtonHeight = (int) (startImg.getHeight(this) * (double) buttonWidth / startImg.getWidth(this));
            int statusButtonHeight = (int) (statusImg.getHeight(this) * (double) buttonWidth / statusImg.getWidth(this));

            // Centraliza os botões horizontalmente
            int centerX = gameState.boardWidth / 2 - buttonWidth / 2;

            // Define a posição vertical dos botões
            int startY = gameState.boardHeight / 2 - startButtonHeight; // Botão "Start" um pouco acima do centro
            int statusY = gameState.boardHeight / 2 + 20; // Botão "Status" um pouco abaixo do centro

            // Desenha os botões com o novo tamanho e posição
            g.drawImage(startImg, centerX, startY, buttonWidth, startButtonHeight, this);
            g.drawImage(statusImg, centerX, statusY, buttonWidth, statusButtonHeight, this);

        } else if (gameState.currentStatus == GameState.Status.STATUS) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 33));
            g.drawString("Maior Pontuação:", gameState.boardWidth / 2 - 160, gameState.boardHeight / 2 - 100);
            g.drawString(String.valueOf(gameState.highScore), gameState.boardWidth / 2 - 25, gameState.boardHeight / 2 - 50);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Clique para voltar", gameState.boardWidth / 2 - 90, gameState.boardHeight / 2);
        } else {
            g.drawImage(gameState.hero.img, gameState.hero.x, gameState.hero.y, gameState.hero.width, gameState.hero.height, null);

            for (Pipe pipe : gameState.pipes) {
                g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
            }

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 32));

            if (gameState.currentStatus == GameState.Status.GAME_OVER) {
                g.setFont(new Font("Arial", Font.BOLD, 40));
                g.drawString("Game Over", gameState.boardWidth / 2 - 100, gameState.boardHeight / 2 - 100);
                g.setFont(new Font("Arial", Font.PLAIN, 32));
                g.drawString("Pontuação: " + (int) gameState.score, gameState.boardWidth / 2 - 100, gameState.boardHeight / 2 - 50);
                g.drawString("Clique para voltar", gameState.boardWidth / 2 - 120, gameState.boardHeight / 2);
            } else {
                g.drawString(String.valueOf((int) gameState.score), 10, 35);
            }
        }
    }
}