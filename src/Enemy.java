import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.Buffer;

public class Enemy {
    private final double MOVE_AMT = 0.4;
    private BufferedImage enemyAnimations[][];
    private BufferedImage spritesheet;
    private double xCoord;
    private double yCoord;
    private boolean isLeft;
    private boolean idle = true, dash, slash;
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

    public void move(Player p, Graphics g) {
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
            System.out.println(e.getMessage());
        }
        loadAnimations();
    }

    private void loadAnimations() {
        enemyAnimations = new BufferedImage[5][23];
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 23; j++){
                enemyAnimations[i][j] = spritesheet.getSubimage(spritesheet.getWidth() / 23 * j, spritesheet.getHeight() / 5 * i, 160*3, 160*3);
            }
        }
    }

    public void dash(Graphics g, Player p){
        int frame = 100;
        int idleFrames = 9;
//        if (idle) {
//            g.drawImage(enemyAnimations[0][i / 100], getxCoord() + (int) GraphicsPanel.backgroundPosition, (int) yCoord, null);
//        }
        if (!dash){
            g.drawImage(enemyAnimations[0][i / 100], getxCoord() + (int) GraphicsPanel.backgroundPosition, (int) yCoord, null);
        } else {
            System.out.println(getxCoord());
            if (getxCoord() + (int) GraphicsPanel.backgroundPosition + 120 < p.getxCoord()) {
                g.drawImage(enemyAnimations[1][i / 100], getxCoord() + (int) GraphicsPanel.backgroundPosition, (int) yCoord, null);
                xCoord += 2;
            } else {
                g.drawImage(enemyAnimations[2][i / 50], getxCoord() + (int)GraphicsPanel.backgroundPosition, (int) yCoord, null);
            }
        }
        i++;

        if (i == 9 * 100 && !dash){
            dash = true;
        } else if (dash && i > 599){
            i = 0;
//            dashReady = false;
        }
    }

    public void deathAnimation(Graphics g, ArrayList<Enemy> e){
        g.drawImage(enemyAnimations[4][i / 20], getxCoord(), getyCoord(), null);
        i++;
        if (i == 23 * 20) {
            i = 0;
            e.remove(this);
        }
    }
    public Bullet shoot(Player p, Graphics g){
        return null;
    }


    // we use a "bounding Rectangle" for detecting collision
    public Rectangle enemyRect() {
        int imageHeight = 40;
        int imageWidth = 64;
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}