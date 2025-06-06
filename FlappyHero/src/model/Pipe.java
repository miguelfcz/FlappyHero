package model;

import java.awt.Image;

public class Pipe {
    public int x, y, width, height;
    public Image img;
    public boolean passed = false;

    public Pipe(int x, int y, int width, int height, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }
}
