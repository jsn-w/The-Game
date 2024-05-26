import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class Enemy {
    private final double MOVE_AMT = 0.4;
    private BufferedImage enemyAnimations[][];
    private BufferedImage spritesheet;
    private double xCoord;
    private double yCoord;
    private boolean isLeft;
    private int i;

    public Enemy(String img, int x, int y) {
        xCoord = x; // starting position is (50, 435), right on top of ground
        yCoord = y;

        i = 0;
        loadImages();
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

    private void loadImages() {
        try {
            spritesheet = ImageIO.read(new File("src/assets/NightBorne.png"));
        }catch (Exception e){
            System.out.println(e);
        }
        loadAnimations();
    }

    private void loadAnimations() {
        enemyAnimations = new BufferedImage[4][23];
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 23; j++){
                enemyAnimations[i][j] = spritesheet.getSubimage(spritesheet.getWidth() / 23, 100 * i, 100, 100);
            }
        }
    }

    public void deathAnimation(Graphics g){
        g.drawImage(enemyAnimations[3][i / 50], getxCoord(), getyCoord(), null);
        i++;
        if (i == 23 * 50) {
            i = 0;
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