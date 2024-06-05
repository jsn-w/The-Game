import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Boss{
    private boolean phaseOneBeat;
    private boolean phaseTwoBeat;
    private boolean win;
    private int hp;
    private boolean hitAvailability;
    private ArrayList<NightBorne> enemies;
    private int maxHp;
    private BufferedImage[][] bossAnimationsLeft,bossAnimationsRight;
    private BufferedImage spritesheet;
    private int i;
    private boolean isLeft;
    private static final int IMAGE_WIDTH = 400;
    private static final int IMAGE_HEIGHT = 233;
    private static final double MOVE_AMT = 1.8;
    private static final int FRAMES_PER_UPDATE = 25;
    private double xCoord;
    private double yCoord;
    private boolean fly;
    private boolean heavyAttack;
    private boolean attack;
    private boolean attackDone;
    private final int ATTACK_FRAMES = 35;

    public Boss(){
        hp = 1000;
        maxHp = 1000;
        phaseOneBeat = false;
        phaseTwoBeat = false;
        win = false;
        hitAvailability = true;
        i = 0;
        isLeft = true;
        xCoord = 300;
        yCoord = 400;
        fly = false;
        heavyAttack = false;
        attack = false;
        attackDone = true;
        loadImages();
    }
    private void loadImages() {
        try {
            spritesheet = ImageIO.read(new File("src/assets/cthulu.png"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        loadAnimations();
    }

    private void loadAnimations() {
        bossAnimationsLeft = new BufferedImage[7][15];
        bossAnimationsRight = new BufferedImage[7][15];
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 15; j++){
                 bossAnimationsRight[i][j] = spritesheet.getSubimage(spritesheet.getWidth() / 15 * j, spritesheet.getHeight() / 7 * i, 400, 233);
            }
        }
        bossAnimationsLeft = Utility.flipEvery(bossAnimationsRight);
    }
    public void render(Graphics g, Player p) {
        drawHealthBar(g);
        int margin = -70;
        isLeft = getXCoord() + IMAGE_WIDTH / 2 > p.getxCoord() + 50;

        if ((getXCoord() + 480) + margin < p.getxCoord()) {
            isLeft = false;
            walk(g, FRAMES_PER_UPDATE);
            xCoord += MOVE_AMT;
        } else if ((getXCoord() - 100) - margin > p.getxCoord()) {
            isLeft = true;
            walk(g, FRAMES_PER_UPDATE);
            xCoord -= MOVE_AMT;
        } else if ((getXCoord() - 100) - margin <= p.getxCoord() && (getXCoord() + 480) + margin >= p.getxCoord()) {
            if (attackDone) {
                if (Math.random() < .25) {
                    heavyAttack = true;
                    attackDone = false;
                    attack = false;
                } else {
                    attack = true;
                    attackDone = false;
                    heavyAttack = false;
                }
            }
            if (heavyAttack) {
                heavyAttack(g, ATTACK_FRAMES);
            }else{
                punch(g, ATTACK_FRAMES);
            }
        } else {
            deathAnimation(g);
        }
    }
    private void deathAnimation(Graphics g){
        g.drawImage(bossAnimationsLeft[7][i / 20], getXCoord(), getYCoord(), null);
        i++;
        if (i == 11 * 20) {
            i = 0;
        }
    }
    private void punch(Graphics g, int frames){
        if (i >= 7 * frames) {
            i = 0;
            attackDone = true;
        }
        if (!isLeft) {
            g.drawImage(bossAnimationsRight[3][i/frames], getXCoord(), getYCoord(), null);
        } else {
            g.drawImage(bossAnimationsLeft[3][i/frames], getXCoord(), getYCoord(), null);
        }
        i++;
    }
    private void heavyAttack(Graphics g, int frames){
        if (i >= 9 * frames) {
            i = 0;
            attackDone = true;
        }
        if (!isLeft) {
            g.drawImage(bossAnimationsRight[4][i/frames], getXCoord(), getYCoord(), null);
        } else {
            g.drawImage(bossAnimationsLeft[4][i/frames], getXCoord(), getYCoord(), null);
        }
        i++;
    }
    private void walk(Graphics g, int frames){
        if (i >= 6 * frames) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(bossAnimationsRight[1][i/frames], getXCoord(), getYCoord(), null);
        } else {
            g.drawImage(bossAnimationsLeft[1][i/frames], getXCoord(), getYCoord(), null);
        }
        i++;
    }
    public boolean isPhaseOneBeat(){
        return phaseOneBeat;
    }
    public boolean isPhaseTwoBeat() {
        return phaseTwoBeat;
    }
    public boolean canHit(){
        return hitAvailability;
    }

    public void drawHealthBar(Graphics g){
        g.setColor(Color.black);
        g.fillRoundRect(320, 100, 500, 40, 40, 40);
        g.setColor(Color.RED);
        g.fillRoundRect(325, 105, 490, 30, 40, 40);

//        g.fillRect(320,100,640 * (hp/maxHp), 50);
//        g.setColor(Color.WHITE);
//        g.fillRect(320+640 * (hp/maxHp),100,640 * ((maxHp-hp)/maxHp),50);
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        g.drawString("boss health: " + hp + "/" + maxHp,335,125);
    }
    public void phaseOne(){
        if (!phaseOneBeat) {
            if (hp <= 0) {
                phaseOneBeat = true;
                hp = 5000;
                maxHp = 5000;
            }
        }
    }
    public void phaseTwo(ArrayList<NightBorne> e){
        if (!phaseTwoBeat) {
            hitAvailability = false;
            if (e.isEmpty()){
                phaseTwoBeat = true;
                hitAvailability = true;
            }
        }
    }
    public void phaseThree(){
        if (hp <= 0) {
            win = true;
        }
    }
    public int getXCoord() {
        return (int) (xCoord + GraphicsPanel.backgroundPosition);
    }
    public int getYCoord() {
        return (int) (yCoord);
    }
}
