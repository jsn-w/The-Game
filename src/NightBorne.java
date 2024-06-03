import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class NightBorne {
    private static final int IMAGE_WIDTH = 480;
    private static final int IMAGE_HEIGHT = 480;
    private static final double MOVE_AMT = 1.4;
    private static final int FRAMES_PER_UPDATE = 25;
    private static final int CHARGE_FRAMES = 30;
    private static final int SLASH_FRAMES = 20;

    private BufferedImage[][] enemyAnimationsLeft, enemyAnimationsRight;

    private BufferedImage spritesheet;
    private double xCoord;
    private final double yCoord;
    private boolean isLeft;
    private int i;

    public NightBorne(String img, int x, int y) {
        xCoord = x;
        yCoord = y;

        i = 0;
        loadImages(img);
    }

    private void loadImages(String path) {
        try {
            spritesheet = ImageIO.read(new File(path));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        loadAnimations();
    }

    private void loadAnimations() {
        enemyAnimationsRight = new BufferedImage[5][23];
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 23; j++){
                enemyAnimationsRight[i][j] = spritesheet.getSubimage(IMAGE_WIDTH * j, IMAGE_HEIGHT * i, IMAGE_WIDTH, IMAGE_HEIGHT);
            }
        }
        enemyAnimationsLeft = Utility.flipEvery(enemyAnimationsRight);
    }

    public void render(Graphics g, Player p) {

        int margin = -70;
        isLeft = getXCoord() + IMAGE_WIDTH / 2 > p.getxCoord() + 50;

        if ((getXCoord() + 480) + margin < p.getxCoord()) {
            isLeft = false;
            dash(g);
            xCoord += MOVE_AMT;
        } else if ((getXCoord() - 100) - margin > p.getxCoord()) {
            isLeft = true;
            dash(g);
            xCoord -= MOVE_AMT;
        } else if ((getXCoord() - 100) - margin <= p.getxCoord() && (getXCoord() + 480) + margin >= p.getxCoord()) {
            slash(g);
        } else {
            deathAnimation(g);
        }
        drawLines(g, margin);

    }

    private void drawLines(Graphics g, int margin) {
        int middle = 240;
        g.setColor(Color.red);
        g.drawLine(getXCoord() + middle, 0, getXCoord() + middle, 1280);

        g.setColor(Color.green);
        g.drawLine(getXCoord() + 100, 0, getXCoord() + 100, 1280);
        g.drawLine(getXCoord() + 480 - 100, 0, getXCoord() + 480 - 100, 1280);

        g.setColor(Color.cyan);
        g.drawLine(getXCoord() + 480 + margin, 0, getXCoord() + 480 + margin, 1280);
        g.drawLine(getXCoord() - margin, 0, getXCoord() - margin, 1280);
    }


    private void dash(Graphics g) {
        if (i >= 6 * NightBorne.FRAMES_PER_UPDATE) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(enemyAnimationsRight[1][i/ NightBorne.FRAMES_PER_UPDATE], getXCoord(), getYCoord(), null);
        } else {
            g.drawImage(enemyAnimationsLeft[1][i/ NightBorne.FRAMES_PER_UPDATE], getXCoord(), getYCoord(), null);
        }
        i++;

    }

    private void slash(Graphics g) {

        if (i >= 9 * CHARGE_FRAMES + 12 * SLASH_FRAMES) {
            i = 9 * CHARGE_FRAMES;
        }

        if (i < 9 * CHARGE_FRAMES) {
            if (!isLeft) {
                g.drawImage(enemyAnimationsRight[0][i/CHARGE_FRAMES], getXCoord(), (int) yCoord, 480, 480, null);
            } else {
                g.drawImage(enemyAnimationsLeft[0][i/CHARGE_FRAMES], getXCoord(), (int) yCoord, 480, 480, null);
            }
        } else {
            if (!isLeft) {
                g.drawImage(enemyAnimationsRight[2][(i - 9 * CHARGE_FRAMES)/SLASH_FRAMES], getXCoord(), (int) yCoord, null);
            } else {
                g.drawImage(enemyAnimationsLeft[2][(i - 9 * CHARGE_FRAMES)/SLASH_FRAMES], getXCoord(), (int) yCoord, null);
            }
        }

        i++;
    }

    public void deathAnimation(Graphics g){
        g.drawImage(enemyAnimationsLeft[4][i / 20], getXCoord(), getYCoord(), null);
        i++;
        if (i == 23 * 20) {
            i = 0;
        }
    }

    public Bullet shoot(Player p, Graphics g){
        return null;
    }


    public Rectangle enemyRect() {
        int imageHeight = 40;
        int imageWidth = 64;
        return new Rectangle(getXCoord(), (int) yCoord, imageWidth, imageHeight);
    }

    public int getXCoord() {
        return (int) (xCoord + GraphicsPanel.backgroundPosition);
    }
    public int getYCoord() {
        return (int) yCoord;
    }
}