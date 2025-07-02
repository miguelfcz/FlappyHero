package view;

import model.GameState;
import model.Pipe;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    GameState gameState;
    Image backgroundImg;

    public GamePanel(GameState gameState, Image backgroundImg, Image startImg, Image statusImg) {
        this.gameState = gameState;
        this.backgroundImg = backgroundImg;
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
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Clique para Iniciar", gameState.boardWidth / 2 - 170, gameState.boardHeight / 2 - 50);

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