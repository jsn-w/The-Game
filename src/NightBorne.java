import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class NightBorne {
    private static final int IMAGE_WIDTH = 480;
    private static final int IMAGE_HEIGHT = 480;
    private static final double MOVE_AMT = 2.2;
    private static final int FRAMES_PER_UPDATE = 25;
    private static final int CHARGE_FRAMES = 20;
    private static final int SLASH_FRAMES = 12;

    private BufferedImage[][] enemyAnimationsLeft, enemyAnimationsRight;

    private BufferedImage spritesheet;
    private double xCoord;
    private final double yCoord;
    private boolean isLeft;
    private int i;
    private int health;

    private enum State {
        DASHING, CHARGING, SLASHING, DYING
    }

    private State state;

    public NightBorne(String img, int x, int y) {
        xCoord = x;
        yCoord = y;
        state = State.DASHING;
        health = 101;
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

    public void render(Graphics g, Player p,ArrayList<Object> mobs) {
        if (health <= 0){
            state = State.DYING;
        }

        int margin = -150;
        isLeft = getXCoord() + IMAGE_WIDTH / 2 > p.getxCoord() + 128;

        switch (state) {
            case DASHING -> {

                if ((getXCoord() + 480) + margin < p.getxCoord()) {
                    dash(g);
                    xCoord += MOVE_AMT;
                } else if ((getXCoord() - 256) - margin > p.getxCoord()) {
                    dash(g);
                    xCoord -= MOVE_AMT;
                } else if ((getXCoord() - 256) - margin <= p.getxCoord() && (getXCoord() + 480) + margin >= p.getxCoord()) {
                    state = State.CHARGING;
                    i = 0;
                }
            }
            case CHARGING -> charge(g);
            case SLASHING -> slash(g, p);
            case DYING -> deathAnimation(g,mobs);
        }
        drawLines(g);
        takeDamage(p);
    }

    private void drawLines(Graphics g) {
        int middle = 240;
        g.setColor(Color.red);
        g.drawLine(getXCoord() + middle, 0, getXCoord() + middle, 1280);

        g.setColor(Color.magenta);
        int num1 = getXCoord();
        int num2 = getXCoord() + 480;
        g.drawLine(num1, 0, num1, 1280);
        g.drawLine(num2, 0, num2, 1280);
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

    private void charge(Graphics g) {
        if (i >= 9 * CHARGE_FRAMES) {
            state = State.SLASHING;
            i = 0;
            return;
        }

        if (!isLeft) {
            g.drawImage(enemyAnimationsRight[0][i/CHARGE_FRAMES], getXCoord(), (int) yCoord, 480, 480, null);
        } else {
            g.drawImage(enemyAnimationsLeft[0][i/CHARGE_FRAMES], getXCoord(), (int) yCoord, 480, 480, null);
        }

        i++;
    }

    private void slash(Graphics g, Player p) {
        if (i >= 12 * SLASH_FRAMES) {
            state = State.DASHING;
            i = 0;
            return;
        }

        if (i > SLASH_FRAMES * 9) {
            Rectangle slashRect;
            if (isLeft) {
                slashRect = new Rectangle(getXCoord(), getYCoord(), 240, 480);
            } else {
                slashRect = new Rectangle(getXCoord() + 240, getYCoord(), 240, 480);
            }
            if (slashRect.intersects(p.playerRect())) {
                p.takeDamage(1);
            }
        }

        if (!isLeft) {
            g.drawImage(enemyAnimationsRight[2][i/SLASH_FRAMES], getXCoord(), (int) yCoord, null);
        } else {
            g.drawImage(enemyAnimationsLeft[2][i/SLASH_FRAMES], getXCoord(), (int) yCoord, null);
        }

        i++;
    }

    public void deathAnimation(Graphics g, ArrayList<Object> mobs){
        g.drawImage(enemyAnimationsLeft[4][i / 20], getXCoord(), getYCoord(), null);
        i++;
        if (i == 23 * 20) {
            i = 0;
        }
        mobs.remove(this);
    }

    private void takeDamage(Player p){
        if (p.attackRect() != null && enemyRect().intersects(p.attackRect())){
            health -= Player.PLAYER_DAMAGE;
        }
    }

    public Rectangle enemyRect() {
        return new Rectangle(getXCoord(), getYCoord(), IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    public int getXCoord() {
        return (int) (xCoord + GraphicsPanel.backgroundPosition);
    }
    public int getYCoord() {
        return (int) yCoord;
    }
}