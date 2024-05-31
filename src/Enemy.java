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
    private boolean idle, dash, slash;
    private int i;

    public Enemy(String img, int x, int y) {
        xCoord = x; // starting pos: (50, 435)
        yCoord = y;

        i = 0;
        loadImages();
        idle = true;
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

    public void render(Graphics g, Player p) {
        int framesPerUpdate = 30;

        int margin = 20;
        if (!idle) {
            idle(g, framesPerUpdate);
        } else if ((getxCoord() + 480) + margin < p.getxCoord()) {
            dash(g, framesPerUpdate);
            xCoord += 1;
        } else if ((getxCoord() - 100) - margin > p.getxCoord()) {
            dash(g, framesPerUpdate);
            xCoord -= 1;
        } else if (slash) {
            slash(g, framesPerUpdate);
        }
        drawLines(g, margin);

    }

    private void drawLines(Graphics g, int margin) {
        int middle = 240;
        g.setColor(Color.red);
        g.drawLine(this.getxCoord() + middle, 0, this.getxCoord() + middle, 1280);

        g.setColor(Color.green);
        g.drawLine(this.getxCoord() + 100, 0, this.getxCoord() + 100, 1280);
        g.drawLine(this.getxCoord() + 480 - 100, 0, this.getxCoord() + 480 - 100, 1280);

        g.setColor(Color.cyan);
        g.drawLine(getxCoord() + 480 + margin, 0, getxCoord() + 480 + margin, 1280);
        g.drawLine(getxCoord() - margin, 0, getxCoord() - margin, 1280);
    }

    private void idle(Graphics g, int frames) {
        frames -= 10;
        g.drawImage(enemyAnimations[0][i/frames], (int) xCoord, (int) yCoord, 480, 480, null);
        i++;
        if (i == 9 * frames) {
            i = 0;
        }
    }

    private void dash(Graphics g, int frames) {
        g.drawImage(enemyAnimations[1][i/frames], (int) xCoord, (int) yCoord, null);
        i++;
        if (i == 6 * frames) {
            i = 0;
        }
    }

    private void slash(Graphics g, int frames) {
        frames -= 10;
        g.drawImage(enemyAnimations[2][i/frames], (int) xCoord, (int) yCoord, null);
        i++;
        if (i == 12 * frames) {
            i = 0;
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