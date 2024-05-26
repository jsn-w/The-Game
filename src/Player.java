import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private final double MOVE_AMT = 6;
    private BufferedImage left, right;
    private boolean facingRight, jumping;
    private double xCoord, yCoord, score, jumpVelocity;
    private int flooryValue, hp, i;
    private BufferedImage heart, playerAnimations;
    private BufferedImage[] animations;

    public Player(String leftImg, String rightImg, int x, int y) {
        xCoord = x;
        yCoord = y;
        score = 0;
        hp = 5;
        flooryValue = 435;
        loadImages(leftImg, rightImg);
    }

    private void loadImages(String left, String right) {
        try {
            this.left = ImageIO.read(new File(left));
            this.right = ImageIO.read(new File(right));
            playerAnimations = ImageIO.read(new File("src/assets/playerAnimations.png"));
            heart = ImageIO.read(new File("src/assets/heart.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        loadAnimations();
    }

    private void loadAnimations() {
        animations = new BufferedImage[15];
        for (int i = 0; i < animations.length; i++) {
            animations[i] = playerAnimations.getSubimage(i * 100, 0, 100, 100);
        }
    }

    public void render(Graphics g) {
        g.drawImage(animations[i / 50], getxCoord(), getyCoord(), null);
        i++;
        if (i == animations.length * 50) {
            i = 0;
        }
    }

    public int getxCoord() {
        return (int) xCoord;
    }

    public int getyCoord() {
        return (int) yCoord;
    }

    public double getScore() {
        if (score < 0) {
            score = 0;
        }
        return (int) (score * 10) / 10.;
    }
    public int getHp(){
        return hp;
    }
    public BufferedImage getHeart(){
        return heart;
    }

    public void faceRight() {
        facingRight = true;
    }

    public void faceLeft() {
        facingRight = false;
    }

    public void moveRight() {
        int backgroundPosition = (int) GraphicsPanel.backgroundPosition;
        int margin = MainFrame.screenWidth *3/4 - right.getWidth()/2;
        System.out.println("[RIGHT] - xCoord:" + xCoord + " || margin:" + margin + " || background position:" + backgroundPosition);
        if (xCoord <= margin || backgroundPosition == -1280*2 ) {
            xCoord += MOVE_AMT;
            if (xCoord > MainFrame.screenWidth - right.getWidth()) {
                xCoord = MainFrame.screenWidth - right.getWidth();
            }
        } else {
            GraphicsPanel.backgroundPosition -= MOVE_AMT;
            if (GraphicsPanel.backgroundPosition < -1280 * 2) {
                GraphicsPanel.backgroundPosition = -1280 * 2;
            }
        }
    }

    public void moveLeft() {
        int backgroundPosition = (int) GraphicsPanel.backgroundPosition;
        int margin = MainFrame.screenWidth / 4 - right.getWidth()/2;
        System.out.println("[LEFT] - xCoord:" + xCoord + " || margin:" + margin + " || background position:" + backgroundPosition);
        if (xCoord >= margin || backgroundPosition == 0) {
            xCoord -= MOVE_AMT;
            if (xCoord < 0) {
                xCoord = 0;
            }
        } else {
            GraphicsPanel.backgroundPosition += MOVE_AMT;
            if (GraphicsPanel.backgroundPosition > 0) {
                GraphicsPanel.backgroundPosition = 0;
            }
        }
    }

    public void moveUp() {
        jumping = true;
        jumpVelocity = 4;
        if (jumping) {
            yCoord -= jumpVelocity;
            jumpVelocity -= 0.5;
        }
//        if (yCoord - MOVE_AMT >= 0) {
//            yCoord -= MOVE_AMT;
//        }
    }

    public void moveDown() {
        if (yCoord + MOVE_AMT <= flooryValue) {
            yCoord += MOVE_AMT;
        }
    }

    public void updateScore(double amount) {
        score += amount;
    }
    public void setScore(double amount) {
        score = amount;
    }

    public BufferedImage getPlayerImage() {
        if (facingRight) {
            return right;
        } else {
            return left;
        }
    }

    // we use a "bounding Rectangle" for detecting collision
    public Rectangle playerRect() {
        int imageHeight = getPlayerImage().getHeight();
        int imageWidth = getPlayerImage().getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}