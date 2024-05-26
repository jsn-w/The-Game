import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Enemy {
    private final double MOVE_AMT = 0.4;
    private BufferedImage img[][];
    private double xCoord;
    private double yCoord;
    private boolean isLeft;

    public Enemy(String img, int x, int y) {
        xCoord = x; // starting position is (50, 435), right on top of ground
        yCoord = y;

    }

    public int getxCoord() {
        return (int) xCoord;
    }

    public int getyCoord() {
        return (int) yCoord;
    }

    public void move() {
        if (xCoord > 800 && xCoord < 810) {
            isLeft = true;
        }

        if (xCoord > 100 && xCoord < 110) {
            isLeft = false;
        }

        if (!isLeft && xCoord + MOVE_AMT <= 880) {
            xCoord += MOVE_AMT;
        }
        if (isLeft && xCoord - MOVE_AMT >= 0) {
            xCoord -= MOVE_AMT;
        }
    }

    public void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.ENEMY_SPRITES);
        this.img = new BufferedImage[5][23];
        for (int i = 0; i < this.img.length; i++){
            for (int j = 0; j < this.img[i].length; j++){
                this.img[i][j] = img.getSubimage(j * 64, i * 40, 64, 40);
            }
        }
    }

    public void deathAnimation(){
        for (BufferedImage f : img[4]){

        }
    }


    // we use a "bounding Rectangle" for detecting collision
    public Rectangle enemyRect() {
        int imageHeight = 40;
        int imageWidth = 64;
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}