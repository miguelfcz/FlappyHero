package model;

import java.awt.Image;

public class Hero {
    public int x, y, width, height;
    public Image img;

    //Construtor da classe
    public Hero(int x, int y, int width, int height, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }

    //Método de movimentação do boneco
    public void move(int velocityY) {
        /*Velocidade vertical, o boneco apenas se movimenta para cima,
        o que da a impressão que ele vai para frente são os canos*/
        y += velocityY;

        //Linha de segurança para impedir que o boneco passe da tela
        y = Math.max(y, 0);
    }
}
