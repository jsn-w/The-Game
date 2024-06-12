import assets.Sound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Death {
    private final double MOVE_AMT = 0.2;
    private final int MAX_HP = 1000;

    private final int SUMMON_FRAMES = 50;
    private final int SLASH_FRAMES = 40;
    private final int DEATH_FRAMES = 20;
    private final int MOVE_FRAMES = 30;
    private final int ABILITY_FRAMES = 50;

    private BufferedImage enemyAnimations[][], enemyAnimationsLeft[][];
    private BufferedImage deathSpritesheet, healthbar;

    private int health;
    private double xCoord;
    private double yCoord;
    private boolean isLeft, isHit, isdead;
    private int i;
    private int ability;

    public Death(int x, int y){
        xCoord = x;
        yCoord = y;
        i = 0;
        ability = -1;
        health = MAX_HP;
        isHit = false;

        loadImages();
    }

    public int getxCoord(){
        return (int) (xCoord + GraphicsPanel.backgroundPosition);
    }

    public int getyCoord(){
        return (int) yCoord;
    }

    public void render(Graphics g, Player p,ArrayList<Object> mobs){
        isLeft = p.getxCoord() < getxCoord();

        if (enemyRect().intersects(p.playerRect())){
            p.takeDamage(0.1);
        }

        if (health > 0) {
            if (ability == -1) {
                move(g);
                int rando = (int) (Math.random() * 1000);
                if (rando == 1){
                    ability = 1;
                } else if (rando == 2){
                    ability = 2;
                } else if (rando == 3) {
                    ability = 3;
                } else {
                    ability = -1;
                }
                if (ability != -1){
                    i = 0;
                }
            } else if (ability == 1) {
                slash(g);
            } else if (ability == 2) {
                summon(g, GraphicsPanel.spirits);
            } else {
                swordSlam(g, p, GraphicsPanel.bullets);
            }
            drawHealthBar(g);
            takeDamage(p);
        } else {
            deathAnimation(g,mobs);
            yCoord = -1000;
        }
    }

    public void loadImages(){
        try {
            deathSpritesheet = ImageIO.read(new File("src/assets/Death/death.png"));
            healthbar = ImageIO.read(new File("src/assets/Death/death_healthbar.png"));
        } catch (IOException e){
            System.out.println(e);
        }
        loadAnimations();
    }

    public void loadAnimations(){
        enemyAnimations = new BufferedImage[5][18]; // row 1: slash, row 2: death, row 3: idle, row 4: summon spirit, row 5: summon swords
        enemyAnimationsLeft = new BufferedImage[5][18];

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 18; j++){
                enemyAnimations[i][j] = deathSpritesheet.getSubimage(deathSpritesheet.getWidth() / 18 * j, deathSpritesheet.getHeight() / 5 * i, 480,480);
            }
        }
        enemyAnimationsLeft = Utility.flipEvery(enemyAnimations);
    }

    private void drawHealthBar(Graphics g) {
        int barWidth = 258;
        int barHeight = 20;
        int x = 470;
        int y = 62;

        g.setColor(Color.red);
        g.fillRect(x, y, barWidth, barHeight);

        g.setColor(new Color(100, 240, 100));
        g.fillRect(x, y, (int) ((health / (double) MAX_HP) * barWidth), barHeight);
        g.drawImage(healthbar, 400, 40, null);
    }

    private void move(Graphics g){
        if (i >= 12 * MOVE_FRAMES){
            i = 0;
        }
        if (isLeft){
            xCoord -= MOVE_AMT;
            g.drawImage(enemyAnimationsLeft[2][i / MOVE_FRAMES], getxCoord(), getyCoord(), null);
        } else {
            xCoord += MOVE_AMT;
            g.drawImage(enemyAnimations[2][i / MOVE_FRAMES], getxCoord(), getyCoord(), null);
        }
        i++;
    }

    private void slash(Graphics g){
        i++;
        if (i < 13 * SLASH_FRAMES){
            if (isLeft){
                g.drawImage(enemyAnimationsLeft[0][i / SLASH_FRAMES], getxCoord(), getyCoord(), null);
                xCoord -= MOVE_AMT * 5;
            } else {
                g.drawImage(enemyAnimations[0][i / SLASH_FRAMES], getxCoord(), getyCoord(), null);
                xCoord += MOVE_AMT * 5;
            }
        } else {
            ability = -1;
            i = 0;
        }
    }

    private void summon(Graphics g, ArrayList<Spirit> s){
        i++;
        if (i < SUMMON_FRAMES * 5) {
            if (isLeft){
                g.drawImage(enemyAnimationsLeft[3][i / SUMMON_FRAMES], getxCoord(), getyCoord(), null);
            } else {
                g.drawImage(enemyAnimations[3][i / SUMMON_FRAMES], getxCoord(), getyCoord(), null);
            }
        } else {
            s.add(new Spirit((int) xCoord, (int)(yCoord - 100)));
            i = 0;
            ability = -1;
        }
    }

    private void swordSlam(Graphics g, Player p, ArrayList<Bullet> b){
        i++;
        if (i < ABILITY_FRAMES * 12){
            if (isLeft){
                g.drawImage(enemyAnimationsLeft[4][i / ABILITY_FRAMES], getxCoord(), getyCoord(), null);
            } else{
                g.drawImage(enemyAnimations[4][i / ABILITY_FRAMES], getxCoord(), getyCoord(), null);
            }
        } else {
            b.add(new Bloodsword(p.getxCoord() - 60, -50, 0, 1));
            i = 0;
            ability = -1;
        }
    }

    private void deathAnimation(Graphics g,ArrayList<Object> mobs){
        i++;
        if (i < DEATH_FRAMES * 18){
            if (isLeft){
                g.drawImage(enemyAnimationsLeft[1][i/ DEATH_FRAMES], getxCoord(), getyCoord(), null);
            } else {
                g.drawImage(enemyAnimations[1][i / DEATH_FRAMES], getxCoord(), getyCoord(), null);
            }
        }
        isdead = true;
    }

    private void takeDamage(Player p){
        if (p.attackRect() != null && enemyRect().intersects(p.attackRect())){
            health -= Player.PLAYER_DAMAGE;
            if (!isHit) {
                Sound.enemyHit();
                isHit = true;
            }
        } else {
            isHit = false;
        }
    }

    public Rectangle enemyRect() {
        int imageHeight = deathSpritesheet.getHeight() / 5;
        int imageWidth = deathSpritesheet.getWidth() / 18;
        return new Rectangle(getxCoord(), getyCoord(), imageWidth, imageHeight);
    }

    public double getHealth() {
        return health;
    }
    public boolean getIsDead(){
        return isdead;
    }
}
