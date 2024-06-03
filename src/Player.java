import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private static final double MOVE_AMT = 3;
    private static final double SPRINT_AMT = 6;
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 100;
    private static final int FRAMES_PER_UPDATE = 30;
    private static final int FLOOR_Y = 600;
    private static final double GRAVITY = 0.2;
    private static final int MAX_STAMINA = 100;
    private static final double STAMINA_REGEN_RATE = 0.07;
    private static final double STAMINA_DEPLETION_RATE = 0.4;
    private static final int MAX_HP = 100; // Assuming max HP is 5

    private boolean isLeft, jumping, falling, sprinting;
    private double xCoord, yCoord, score, jumpVelocity;
    private int hp;
    private int i;
    private double stamina;
    private BufferedImage[] animations;
    private BufferedImage playerAnimations;

    public Player(String imagePath, int x, int y) {
        xCoord = x;
        yCoord = y;
        score = 0;
        hp = MAX_HP;
        stamina = MAX_STAMINA;
        loadImages(imagePath);
    }

    private void loadImages(String imagePath) {
        try {
            playerAnimations = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        loadAnimations();
    }

    private void loadAnimations() {
        animations = new BufferedImage[15];
        for (int i = 0; i < animations.length; i++) {
            animations[i] = playerAnimations.getSubimage(i * IMAGE_WIDTH, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        }
    }

    public void render(Graphics g, GraphicsPanel panel) {
        int framesPerUpdate = 30;
        if (!isLeft) {
            g.drawImage(animations[i / framesPerUpdate], getxCoord(), getyCoord(), null);
        } else {
            g.drawImage(Utility.flipImageHorizontally(animations[i / framesPerUpdate]), getxCoord(), getyCoord(), null);
        }
        i++;
        if (i == animations.length * framesPerUpdate) {
            i = 0;
        }

        boolean[] pressedKeys = panel.getPressedKeys();

        if (pressedKeys[65] || pressedKeys[37]) { // A or Left Arrow
            moveLeft();
        }
        if (pressedKeys[68] || pressedKeys[39]) { // D or Right Arrow
            moveRight();
        }
        if (pressedKeys[87] || pressedKeys[38]) { // W or Up Arrow
            jump();
        }
        if (pressedKeys[83] || pressedKeys[40]) { // S or Down Arrow
            moveDown();
        }
        if (pressedKeys[16] && stamina > 0) { // Shift key and stamina > 0
            sprinting = true;
        } else {
            sprinting = false;
        }

        updatePosition();
        updateStamina(pressedKeys[16]);

        drawLines(g);
        drawStaminaBar(g);
        drawHealthBar(g);
    }

    private void drawLines(Graphics g) {
        g.drawLine(0, FLOOR_Y, 1280, FLOOR_Y);
        g.drawLine(0, FLOOR_Y - IMAGE_HEIGHT, 1280, FLOOR_Y - IMAGE_HEIGHT);
        g.drawLine(MainFrame.screenWidth / 4 - IMAGE_WIDTH / 2, 0, MainFrame.screenWidth / 4 - IMAGE_WIDTH / 2, 1280);
        g.drawLine(MainFrame.screenWidth * 3 / 4 + IMAGE_WIDTH / 2, 0, MainFrame.screenWidth * 3 / 4 + IMAGE_WIDTH / 2, 1280);
    }

    private void drawStaminaBar(Graphics g) {
        int barWidth = 200;
        int barHeight = 20;
        int x = 20;
        int y = MainFrame.screenHeight - 70;

        g.setColor(Color.BLACK);
        g.fillRect(x - 2, y - 2, barWidth + 4, barHeight + 4);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        g.setColor(Color.GRAY);
        g.fillRect(x, y, (int) ((stamina / (double) MAX_STAMINA) * barWidth), barHeight);
    }

    private void drawHealthBar(Graphics g) {
        int barWidth = 200;
        int barHeight = 20;
        int x = 20;
        int y = MainFrame.screenHeight - 100;

        g.setColor(Color.BLACK);
        g.fillRect(x - 2, y - 2, barWidth + 4, barHeight + 4);

        g.setColor(Color.RED);
        g.fillRect(x, y, barWidth, barHeight);

        g.setColor(Color.GREEN);
        g.fillRect(x, y, (int) ((hp / (double) MAX_HP) * barWidth), barHeight);
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

    public int getHp() {
        return hp;
    }

    public double getStamina() {
        return stamina;
    }

    public void moveLeft() {
        isLeft = true;
        double moveAmount = sprinting ? SPRINT_AMT : MOVE_AMT;
        int backgroundPosition = (int) GraphicsPanel.backgroundPosition;
        int margin = MainFrame.screenWidth / 4 - IMAGE_WIDTH / 2;
        if (xCoord >= margin || backgroundPosition == 0) {
            xCoord -= moveAmount;
            if (xCoord < 0) {
                xCoord = 0;
            }
        } else {
            GraphicsPanel.backgroundPosition += moveAmount;
            if (GraphicsPanel.backgroundPosition > 0) {
                GraphicsPanel.backgroundPosition = 0;
            }
        }
        if (sprinting) {
            stamina -= STAMINA_DEPLETION_RATE;
            if (stamina < 0) {
                stamina = 0;
                sprinting = false;
            }
        }
    }

    public void moveRight() {
        isLeft = false;
        double moveAmount = sprinting ? SPRINT_AMT : MOVE_AMT;
        int backgroundPosition = (int) GraphicsPanel.backgroundPosition;
        int margin = MainFrame.screenWidth * 3 / 4 - IMAGE_WIDTH / 2;
        if (xCoord <= margin || backgroundPosition == -1280 * 2 && (xCoord + IMAGE_WIDTH < MainFrame.screenWidth)) {
            xCoord += moveAmount;
            if (xCoord + IMAGE_WIDTH > MainFrame.screenWidth) {
                xCoord = MainFrame.screenWidth - IMAGE_WIDTH;
            }
        } else {
            GraphicsPanel.backgroundPosition -= moveAmount;
            if (GraphicsPanel.backgroundPosition < -1280 * 2) {
                GraphicsPanel.backgroundPosition = -1280 * 2;
            }
        }
        if (sprinting) {
            stamina -= STAMINA_DEPLETION_RATE;
            if (stamina < 0) {
                stamina = 0;
                sprinting = false;
            }
        }
    }

    public void jump() {
        if (!jumping && !falling) {
            jumping = true;
            jumpVelocity = 12;
        }
    }

    public void updatePosition() {
        if (jumping) {
            yCoord -= jumpVelocity;
            jumpVelocity -= GRAVITY;
            if (jumpVelocity <= 0) {
                jumping = false;
                falling = true;
            }
        } else if (falling) {
            yCoord += jumpVelocity;
            jumpVelocity += GRAVITY;
            if (yCoord >= FLOOR_Y - IMAGE_HEIGHT) {
                yCoord = FLOOR_Y - IMAGE_HEIGHT;
                falling = false;
            }
        }
    }

    public void moveDown() {
        yCoord += MOVE_AMT;
        int playerBottomY = FLOOR_Y - IMAGE_HEIGHT;
        if (yCoord > playerBottomY) {
            yCoord = playerBottomY;
        }
    }

    private void updateStamina(boolean shiftPressed) {
        if (!sprinting && stamina < MAX_STAMINA && !shiftPressed) {
            stamina += STAMINA_REGEN_RATE;
            if (stamina > MAX_STAMINA) {
                stamina = MAX_STAMINA;
            }
        }
    }

    public void updateScore(double amount) {
        score += amount;
    }

    public void setScore(double amount) {
        score = amount;
    }
}
