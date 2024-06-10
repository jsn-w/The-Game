import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {

    private static final double MOVE_AMT = 2;
    private static final double SPRINT_AMT = 4;
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 100;
    private static final int FRAMES_PER_UPDATE = 30;
    private static final int FLOOR_Y = 600;
    private static final double GRAVITY = 0.04;
    private static final int MAX_STAMINA = 100;
    private static final double STAMINA_REGEN_RATE = 0.07;
    private static final double STAMINA_DEPLETION_RATE = 0.2;
    private static final double MAX_HP = 100;
    private static final double HEAL_RATE = 0.02; // Healing rate per update

    private boolean isLeft, jumping, falling, sprinting, doubleJumpAvailable, jumpKeyPressed;
    private double xCoord, yCoord, score, jumpVelocity;
    private double hp;
    private int i;
    private double stamina;
    private BufferedImage[] animations;
    private BufferedImage playerAnimations, healthbar;

    public Player(String imagePath, int x, int y) {
        xCoord = x;
        yCoord = y;
        score = 0;
        hp = MAX_HP;
        stamina = MAX_STAMINA;
        doubleJumpAvailable = false;
        jumpKeyPressed = false;
        loadImages(imagePath);
    }

    private void loadImages(String imagePath) {
        try {
            playerAnimations = ImageIO.read(new File(imagePath));
            healthbar = ImageIO.read(new File("src/assets/healthbar.png"));
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

        if (pressedKeys[65] || pressedKeys[37]) {
            moveLeft();
        }
        if (pressedKeys[68] || pressedKeys[39]) {
            moveRight();
        }
        if (pressedKeys[87] || pressedKeys[38]) {
            if (!jumpKeyPressed) {
                jumpKeyPressed = true;
                jump();
            }
        } else {
            jumpKeyPressed = false;
        }
        if (pressedKeys[83] || pressedKeys[40]) {
            moveDown();
        }
        if (pressedKeys[16] && stamina > 0) {
            sprinting = true;
        } else {
            sprinting = false;
        }

        updatePosition();
        updateStamina(pressedKeys[16]);
        heal(); // Heal the player gradually

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
        int barWidth = 258;
        int barHeight = 20;
        int x = 51 + 40;
        int y = 40 + 6;

        g.setColor(Color.RED);
        g.fillRect(x, y, barWidth, barHeight);

        g.setColor(new Color(100, 240, 100));
        g.fillRect(x, y, (int) ((hp / (double) MAX_HP) * barWidth), barHeight);
        g.drawImage(healthbar, 40, 40, null);
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
        return (int) hp;
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
            jumpVelocity = 4;
            doubleJumpAvailable = true;
        } else if (doubleJumpAvailable && stamina >= STAMINA_DEPLETION_RATE * 200) {
            jumping = true;
            jumpVelocity = 4;
            stamina -= STAMINA_DEPLETION_RATE * 200;
            doubleJumpAvailable = false;
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
                doubleJumpAvailable = false;
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

    public void takeDamage(double damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }

    private void heal() {
        if (hp < MAX_HP) {
            hp += HEAL_RATE;
            if (hp > MAX_HP) {
                hp = MAX_HP;
            }
        }
    }

    public Rectangle playerRect() {
        return new Rectangle((int) xCoord, (int) yCoord, IMAGE_WIDTH, IMAGE_HEIGHT);
    }
}