import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Boss implements ActionListener {

    private static final double MOVE_AMT = 1.8;
    private static final int FRAMES_PER_UPDATE = 25;

    private boolean phaseOneBeat, phaseTwoBeat, onPhaseThree, win, hitAvailability;
    private int hp, maxHp, i, fly, width, height,growNumber;
    private double xCoord, yCoord;
    private boolean heavyAttack, attack, attackDone, falling, isLeft;
    private Timer flyTimer, bulletTimer;
    private ArrayList<BossBullet> bullets;
    private Player p;
    private ArrayList<Object> mobs;

    private BufferedImage[][] bossAnimationsLeft,bossAnimationsRight;
    private BufferedImage spritesheet;

    public Boss(Player p, Graphics g){
        this.p = p;
        hp = 1000;
        maxHp = 1000;
        hitAvailability = true;
        i = 0;
        growNumber = 0;
        isLeft = true;
        xCoord = 300;
        yCoord = 400;
        fly = 0;
        bullets = new ArrayList<>();
        attackDone = true;
        flyTimer = new Timer(5000, this);
        flyTimer.start();
        bulletTimer = new Timer(1000,this);
        bulletTimer.start();
        bullets = new ArrayList<>();
        mobs = new ArrayList<>();
        loadImages("src/assets/bossSpritesheet.png");
    }

    private void loadImages(String file) {
        try {
            spritesheet = ImageIO.read(new File(file));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        width = spritesheet.getWidth() / 15;
        height = spritesheet.getHeight() / 7;
        loadAnimations();
    }

    private void loadAnimations() {
        bossAnimationsLeft = new BufferedImage[7][15];
        bossAnimationsRight = new BufferedImage[7][15];
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 15; j++){
                 bossAnimationsRight[i][j] = spritesheet.getSubimage(width * j, height * i, width, height);
            }
        }
        bossAnimationsLeft = Utility.flipEvery(bossAnimationsRight);
    }
    private void render(Graphics g, Player p) {
        drawHealthBar(g);
        int margin = -70;
        isLeft = getXCoord() + width / 2 > p.getxCoord() + 50;
        if(!hitAvailability){
            idle(g);
            if (!mobs.isEmpty()) {
                if (mobs.getFirst() instanceof NightBorne) {
                    ((NightBorne) mobs.getFirst()).render(g, p, mobs);
                    if (((NightBorne) mobs.getFirst()).getIsdead()) {
                        mobs = new ArrayList<>();
                        mobs.add(new Death(500, 200));
                    }
                } else if (mobs.getFirst() instanceof Death) {
                    ((Death) mobs.getFirst()).render(g, p, mobs);
                    if (((Death) mobs.getFirst()).getIsDead()) {
                        mobs = new ArrayList<>();
                    }
                }
            }
        } else {
            takeDamage(p);
            if (attackDone) {
                if ((getXCoord() + width -100) + margin < p.getxCoord()) {
                    isLeft = false;
                    if (fly % 2 == 1) {
                        bulletTimer.start();
                        if (onPhaseThree){
                            if (yCoord >= -100) {
                                fly(g);
                                yCoord -= MOVE_AMT / 2;
                            }
                            if (yCoord < -100) {
                                yCoord = -100;
                            }
                        }else {
                            if (yCoord >= 200) {
                                fly(g);
                                yCoord -= MOVE_AMT / 2;
                            }
                            if (yCoord < 200) {
                                yCoord = 200;
                            }
                        }
                        fly(g);
                    } else if (falling && fly % 2 == 0) {
                        if (onPhaseThree){
                            if (yCoord <= 200) {
                                fly(g);
                                yCoord += MOVE_AMT / 2;
                            }
                            if (yCoord > 200) {
                                yCoord = 200;
                                falling = false;
                            }
                        }else {
                            if (yCoord <= 400) {
                                fly(g);
                                yCoord += MOVE_AMT / 2;
                            }
                            if (yCoord > 400) {
                                yCoord = 400;
                                falling = false;
                            }
                        }
                    } else {
                        walk(g);
                    }
                    xCoord += MOVE_AMT;
                } else if ((getXCoord() - 100) - margin > p.getxCoord()) {
                    isLeft = true;
                    if (fly % 2 == 1) {
                        bulletTimer.start();
                        if (onPhaseThree){
                            if (yCoord >= -100) {
                                fly(g);
                                yCoord -= MOVE_AMT / 2;
                            }
                            if (yCoord < -100) {
                                yCoord = -100;
                            }
                        }else {
                            if (yCoord >= 200) {
                                fly(g);
                                yCoord -= MOVE_AMT / 2;
                            }
                            if (yCoord < 200) {
                                yCoord = 200;
                            }
                        }
                        fly(g);
                    } else if (falling && fly % 2 == 0) {
                        if (onPhaseThree){
                            if (yCoord <= 200) {
                                fly(g);
                                yCoord += MOVE_AMT / 2;
                            }
                            if (yCoord > 200) {
                                yCoord = 200;
                                falling = false;
                            }
                        }else {
                            if (yCoord <= 400) {
                                fly(g);
                                yCoord += MOVE_AMT / 2;
                            }
                            if (yCoord > 400) {
                                yCoord = 400;
                                falling = false;
                            }
                        }
                    } else {
                        walk(g);
                    }
                    xCoord -= MOVE_AMT;
                } else if ((getXCoord() - 100) - margin <= p.getxCoord() && (getXCoord() + width) + margin >= p.getxCoord()) {
                    if (fly % 2 == 0 && !falling) {
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
                            heavyAttack(g, p);
                        } else {
                            punch(g, p);
                        }
                    } else {
                        if (fly % 2 == 1) {
                            if (onPhaseThree){
                                if (yCoord >= -100) {
                                    fly(g);
                                    yCoord -= MOVE_AMT / 2;
                                }
                                if (yCoord < -100) {
                                    yCoord = -100;
                                }
                            }else {
                                if (yCoord >= 200) {
                                    fly(g);
                                    yCoord -= MOVE_AMT / 2;
                                }
                                if (yCoord < 200) {
                                    yCoord = 200;
                                }
                            }
                            fly(g);
                        } else if (falling && fly % 2 == 0) {
                            if (onPhaseThree){
                                if (yCoord <= 200) {
                                    fly(g);
                                    yCoord += MOVE_AMT / 2;
                                }
                                if (yCoord > 200) {
                                    yCoord = 200;
                                    falling = false;
                                }
                            }else {
                                if (yCoord <= 400) {
                                    fly(g);
                                    yCoord += MOVE_AMT / 2;
                                }
                                if (yCoord > 400) {
                                    yCoord = 400;
                                    falling = false;
                                }
                            }
                        }
                    }
                } else {
                    deathAnimation(g);
                }
            } else {
                if (attack) {
                    punch(g, p);
                } else {
                    heavyAttack(g, p);
                }
            }
            for (int i = 0; i < bullets.size(); i++) {
                boolean removed = bullets.get(i).move(g, bullets);
                if (!removed) {
                    if ((bullets.get(i).getyCoord() > 550 || bullets.get(i).getyCoord() < -200) || bullets.get(i).enemyRect().intersects(p.playerRect())) {
                        if (bullets.get(i).enemyRect().intersects(p.playerRect())) {
                            p.takeDamage(10);
                        }
                        bullets.remove(i);
                        i--;
                    }
                }
            }
        }
    }

    private void deathAnimation(Graphics g){
        if (i == 11 * 40) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(bossAnimationsRight[7][i/40], getXCoord(), getYCoord(), null);
        } else {
            g.drawImage(bossAnimationsLeft[7][i/40], getXCoord(), getYCoord(), null);
        }
        i++;
    }

    private void idle(Graphics g){
        if (i == 15 * 40) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(bossAnimationsRight[0][i/40], getXCoord(), getYCoord(), null);
        } else {
            g.drawImage(bossAnimationsLeft[0][i/40], getXCoord(), getYCoord(), null);
        }
        i++;
    }
    private void punch(Graphics g, Player p){
        if (i >= 7 * 35) {
            i = 0;
            attackDone = true;
        }
        if (!isLeft) {
            g.drawImage(bossAnimationsRight[3][i/35], getXCoord(), getYCoord(), null);
        } else {
            g.drawImage(bossAnimationsLeft[3][i/35], getXCoord(), getYCoord(), null);
        }
        i++;
        Rectangle atkRect;
        if (isLeft){
            atkRect = new Rectangle(getXCoord(),getYCoord(),width/2,height);
        }else{
            atkRect = new Rectangle(getXCoord()+width/2,getYCoord(),width/2,height);
        }
        if (atkRect.intersects(p.playerRect())){
            p.takeDamage(.3);
        }else if (atkRect.intersects(p.playerRect())){
            p.takeDamage(.3);
        }
    }
    private void heavyAttack(Graphics g, Player p){
        if (i >= 9 * 35) {
            i = 0;
            attackDone = true;
        }
        if (!isLeft) {
            g.drawImage(bossAnimationsRight[4][i/35], getXCoord(), getYCoord(), null);
        } else {
            g.drawImage(bossAnimationsLeft[4][i/35], getXCoord(), getYCoord(), null);
        }
        i++;
        Rectangle atkRect;
        if (isLeft){
            atkRect = new Rectangle(getXCoord(),getYCoord(),width/2,height);
        }else{
            atkRect = new Rectangle(getXCoord()+width/2,getYCoord(),width/2,height);
        }
        if (atkRect.intersects(p.playerRect())){
            p.takeDamage(.5);
        }else if (atkRect.intersects(p.playerRect())){
            p.takeDamage(.5);
        }
    }
    private void walk(Graphics g){
        if (i >= 6 * Boss.FRAMES_PER_UPDATE) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(bossAnimationsRight[1][i/ Boss.FRAMES_PER_UPDATE], getXCoord(), getYCoord(), null);
        } else {
            g.drawImage(bossAnimationsLeft[1][i/ Boss.FRAMES_PER_UPDATE], getXCoord(), getYCoord(), null);
        }
        i++;
    }
    private void fly(Graphics g){
        if (i >= 6 * 55) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(bossAnimationsRight[2][i/55], getXCoord(), getYCoord(), null);
        } else {
            g.drawImage(bossAnimationsLeft[2][i/55], getXCoord(), getYCoord(), null);
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
        g.fillRoundRect(325, 105, (int)(490 * ((double)hp/maxHp)), 30, 40, 40);
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.setColor(Color.YELLOW);
        if (hitAvailability) {
            g.drawString("boss health: " + hp + "/" + maxHp, 335, 125);
        } else {
            g.drawString("boss health: INVULNERABLE", 335, 125);
        }
    }
    public void phaseOne(Graphics g, Player p){
        render(g,p);
        if (hp <= 0) {
            phaseOneBeat = true;
            hp = 5000;
            maxHp = 5000;
            mobs.add(new NightBorne("src/assets/NightBorne.png", 100, 220));
        }
    }
    public void phaseTwo(Graphics g, Player p){
        hitAvailability = false;
        if (mobs.isEmpty()) {
            grow(g);
            growNumber++;
            if (growNumber > 2800) {
                loadImages("src/assets/biggercthulu.png");
                yCoord -= height / 2 - 35;
                phaseTwoBeat = true;
                hitAvailability = true;
                onPhaseThree = true;
            }
        }else{
            render(g,p);
        }
    }
    public void phaseThree(Graphics g, Player p){
        render(g,p);
        if (hp <= 0) {
            hp = 0;
            win = true;
        }
    }
    public int getXCoord() {
        return (int) (xCoord + GraphicsPanel.backgroundPosition);
    }
    public int getYCoord() {
        return (int) (yCoord);
    }
    public boolean isWin(){
        return win;
    }
    private void grow(Graphics g){
        if (i >= 5 * 100) {
            i = 0;
        }
        if (!isLeft) {
            g.drawImage(bossAnimationsRight[5][i / 100], getXCoord(), getYCoord(), null);
        } else {
            g.drawImage(bossAnimationsLeft[5][i / 100], getXCoord(), getYCoord(), null);
        }
        i++;
    }

    public int getGrowNumber(){
        return growNumber;
    }

    private void shoot(Player p, ArrayList<BossBullet> b){
        double angle = Math.atan2(p.getyCoord() - (int) yCoord + 128, p.getxCoord() - getXCoord() + 128);
        if (isLeft) {
            b.add(new BossBullet(getXCoord()+width/4, (int) yCoord + height / 2, angle, 1));
        } else {
            b.add(new BossBullet(getXCoord() + 3 * width/4, (int) yCoord + height / 2, angle, 1));
        }
    }
    private Rectangle bossRect(){
        return new Rectangle(getXCoord(), getYCoord(), width, height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            if (e.getSource() == flyTimer) {
                fly++;
                if (fly % 2 == 0) {
                    falling = true;
                }
            }
            if(e.getSource() == bulletTimer && fly % 2 == 1){
                shoot(p,bullets);
            }
        }
    }

    private void takeDamage(Player p){
        if (p.attackRect() != null && bossRect().intersects(p.attackRect())){
            hp -= Player.PLAYER_DAMAGE;
        }
    }
}
