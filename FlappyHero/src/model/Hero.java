package model;

import java.awt.Image;

public class Hero {
    public int x, y, width, height;
    public Image img;

    public Hero(int x, int y, int width, int height, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }

    public void move(int velocityY) {
        y += velocityY;
        y = Math.max(y, 0);
    }
}
