import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {

    private static final double MOVE_AMT = 2;
    private static final double SPRINT_AMT = 4;
    private static final int IMAGE_WIDTH = 256;
    private static final int IMAGE_HEIGHT = 256;
    private static final int FRAMES_PER_UPDATE = 30;
    private static final int FLOOR_Y = 600;
    private static final double GRAVITY = 0.04;
    private static final int MAX_STAMINA = 100;
    private static final double STAMINA_REGEN_RATE = 0.07;
    private static final double STAMINA_DEPLETION_RATE = 0.2;
    private static final double MAX_HP = 1000000000;
    private static final double HEAL_RATE = 0.02; // Healing rate per update

    private boolean isLeft, jumping, falling, sprinting, doubleJumpAvailable, jumpKeyPressed;
    private double xCoord, yCoord, score, jumpVelocity;
    private double hp;
    private int i;
    private double stamina;
    private BufferedImage[][] playerAnimationsLeft, playerAnimationsRight;
    private BufferedImage playerSpritesheet, healthbar, staminabar;

    private enum State {
        IDLE, RUN, JUMP, FALL, ATTACK, TAKING_DAMAGE, DEAD, SHIELD_BLOCKING
    }
    private State state;

    public Player(String imagePath, int x, int y) {
        xCoord = x;
        yCoord = y;
        score = 0;
        hp = MAX_HP;
        stamina = MAX_STAMINA;
        doubleJumpAvailable = false;
        jumpKeyPressed = false;
        loadImages(imagePath);
        state = State.IDLE;
    }

    private void loadImages(String imagePath) {
        try {
            playerSpritesheet = ImageIO.read(new File(imagePath));
            healthbar = ImageIO.read(new File("src/assets/healthbar.png"));
            staminabar = ImageIO.read(new File("src/assets/staminabar.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        loadAnimations();
    }

    private void loadAnimations() {
        playerAnimationsRight = new BufferedImage[8][8];
        for (int r = 0; r < 8; r++){
            for (int j = 0; j < 8; j++){
                playerAnimationsRight[r][j] = playerSpritesheet.getSubimage(IMAGE_WIDTH * j, IMAGE_HEIGHT * r, IMAGE_WIDTH, IMAGE_HEIGHT);
            }
        }
        playerAnimationsLeft = Utility.flipEvery(playerAnimationsRight);
    }

    public void render(Graphics g, GraphicsPanel panel) {

        boolean[] pKeys = panel.getPressedKeys();

        if (pKeys[65] || pKeys[37]) {
            moveLeft();
            state = State.RUN;
        }
        if (pKeys[68] || pKeys[39]) {
            moveRight();
            state = State.RUN;
        }
        if (pKeys[87] || pKeys[38]) {
            if (!jumpKeyPressed) {
                jumpKeyPressed = true;
                jump();
            }
        } else {
            jumpKeyPressed = false;
        }
        sprinting = pKeys[16] && stamina > 0;
        
        if (!pKeys[65] && !pKeys[37] &&!pKeys[68] && !pKeys[39] &&!pKeys[87] && !pKeys[38] && !jumping && !falling && state != State.ATTACK) {
            state = State.IDLE;
        }

        switch (state) {
            case IDLE -> idle(g);
            case RUN -> run(g);
            case JUMP -> jump(g);
            case FALL -> fall(g);
            case ATTACK -> attack(g);
            case DEAD -> dead(g);
//            case SHIELD_BLOCKING ->
        }

        updatePosition();
        updateStamina(pKeys[16]);
        heal(); // Heal the player gradually

        drawLines(g);
        drawStaminaBar(g);
        drawHealthBar(g);

        g.drawLine(getxCoord() + 128, 0, getxCoord() + 128, 500);
    }

    private void idle(Graphics g) {
        if (i >= 5 * FRAMES_PER_UPDATE) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(playerAnimationsRight[0][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        } else {
            g.drawImage(playerAnimationsLeft[0][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        }
        i++;
    }

    private void run(Graphics g) {
        if (i >= 8 * FRAMES_PER_UPDATE) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(playerAnimationsRight[1][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        } else {
            g.drawImage(playerAnimationsLeft[1][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        }
        i++;
    }

    private void jump(Graphics g) {
        if (i >= 3 * FRAMES_PER_UPDATE) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(playerAnimationsRight[2][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        } else {
            g.drawImage(playerAnimationsLeft[2][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        }
        i++;
    }

    private void fall(Graphics g) {
        if (i >= 2 * FRAMES_PER_UPDATE) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(playerAnimationsRight[3][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        } else {
            g.drawImage(playerAnimationsLeft[3][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        }
        i++;
    }

    private void attack (Graphics g) {
        if (i >= 6 * FRAMES_PER_UPDATE) {
            i = 0;
            state = State.IDLE;
        }
        if (!isLeft) {
            g.drawImage(playerAnimationsRight[4][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        } else {
            g.drawImage(playerAnimationsLeft[4][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        }
        i++;
    }

    private void dead (Graphics g) {
        if (i >= 7 * FRAMES_PER_UPDATE) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(playerAnimationsRight[6][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        } else {
            g.drawImage(playerAnimationsLeft[6][i/FRAMES_PER_UPDATE], getxCoord(), getyCoord(), null);
        }
        i++;
    }

    private void drawLines(Graphics g) {
        g.drawLine(0, FLOOR_Y, 1280, FLOOR_Y);
        g.drawLine(0, FLOOR_Y - IMAGE_HEIGHT, 1280, FLOOR_Y - IMAGE_HEIGHT);
        g.drawLine(MainFrame.screenWidth / 4 - IMAGE_WIDTH / 2, 0, MainFrame.screenWidth / 4 - IMAGE_WIDTH / 2, 1280);
        g.drawLine(MainFrame.screenWidth * 3 / 4 + IMAGE_WIDTH / 2, 0, MainFrame.screenWidth * 3 / 4 + IMAGE_WIDTH / 2, 1280);
    }

    private void drawStaminaBar(Graphics g) {
        int barWidth = 258;
        int barHeight = 20;
        int x = 51 + 40;
        int y = 40 + 6 + 60;

        g.setColor(Color.darkGray);
        g.fillRect(x, y, barWidth, barHeight);

        g.setColor(Color.gray);
        g.fillRect(x, y, (int) ((stamina / (double) MAX_STAMINA) * barWidth), barHeight);
        g.drawImage(staminabar, 40, 100, null);
    }

    private void drawHealthBar(Graphics g) {
        int barWidth = 258;
        int barHeight = 20;
        int x = 51 + 40;
        int y = 40 + 6;

        g.setColor(Color.red);
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
            state = State.JUMP;
            yCoord -= jumpVelocity;
            jumpVelocity -= GRAVITY;
            if (jumpVelocity <= 0) {
                jumping = false;
                falling = true;
            }
        } else if (falling) {
            state = State.FALL;
            yCoord += jumpVelocity;
            jumpVelocity += GRAVITY;
            if (yCoord >= FLOOR_Y - IMAGE_HEIGHT + 50) {
                yCoord = FLOOR_Y - IMAGE_HEIGHT + 50;
                falling = false;
                doubleJumpAvailable = false;
            }
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

    public void setState() {
        state = State.ATTACK;
        i = 0;
    }


    public Rectangle playerRect() {
        int margin = 100;
        return new Rectangle((int) xCoord + margin, (int) yCoord + margin, IMAGE_WIDTH - 2*margin, IMAGE_HEIGHT - 2*margin);
    }
}