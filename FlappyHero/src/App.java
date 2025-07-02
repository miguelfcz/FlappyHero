import controller.GameController;
import model.GameState;
import model.Hero;
import view.GamePanel;

import javax.swing.*; //Janela e Painel
import java.awt.*; //Desenho e eventos

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Hero"); //Janela Principal

        // Carregamento das imagens
        Image backgroundImg = new ImageIcon(App.class.getResource("/JogoBG.png")).getImage();
        Image heroImg = new ImageIcon(App.class.getResource("/BonecoJogo.png")).getImage();
        Image topPipeImg = new ImageIcon(App.class.getResource("/toppipe.png")).getImage();
        Image bottomPipeImg = new ImageIcon(App.class.getResource("/bottompipe.png")).getImage();
        Image startImg = new ImageIcon(App.class.getResource("/Start.png")).getImage();
        Image statusImg = new ImageIcon(App.class.getResource("/Status.png")).getImage();

        //Guarda todas as informações sobre o estado do Jogo
        GameState state = new GameState();
        Hero hero = new Hero(state.boardWidth / 8, state.boardHeight / 3, 58, 40, heroImg);
        state.hero = hero;

        //GamePanel recebe state para saber o que vai ser desenhado na tela e a imagem de fundo
        GamePanel panel = new GamePanel(state, backgroundImg, startImg, statusImg);

        //Cérebro que controla o jogo
        new GameController(state, panel, topPipeImg, bottomPipeImg);

        //Preparação da janela
        frame.add(panel);
        frame.pack();//Ajuste para o painel caber perfeitamente
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
