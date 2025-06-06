package view;

import model.GameState;
import model.Pipe;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    GameState gameState;
    Image backgroundImg;

    public GamePanel(GameState gameState, Image backgroundImg) {
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
        g.drawImage(gameState.hero.img, gameState.hero.x, gameState.hero.y, gameState.hero.width, gameState.hero.height, null);

        for (Pipe pipe : gameState.pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));

        if (gameState.gameOver) {
            g.drawString("Game Over: " + (int) gameState.score, 10, 35);
        } else {
            g.drawString(String.valueOf((int) gameState.score), 10, 35);
        }
    }
}
