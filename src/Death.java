import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Death {
    private final double MOVE_AMT = 0.2;

    private final int SUMMON_FRAMES = 80;
    private final int SLASH_FRAMES = 40;
    private final int DEATH_FRAMES = 160;
    private final int MOVE_FRAMES = 60;
    private final int ABILITY_FRAMES = 80;

    private BufferedImage enemyAnimations[][], enemyAnimationsLeft[][];
    private BufferedImage deathSpritesheet;

    int health;
    private double xCoord;
    private double yCoord;
    private boolean isLeft;
    private int i;
    private int ability;

    public Death(int x, int y){
        xCoord = x;
        yCoord = y;
        i = 0;
        ability = -1;
        health = 6666;

        loadImages();
    }

    public int getxCoord(){
        return (int) (xCoord + GraphicsPanel.backgroundPosition);
    }

    public int getyCoord(){
        return (int) yCoord;
    }

    public void render(Graphics g, Player p){
        if (p.getxCoord() < getxCoord()){
            isLeft = true;
        } else{
            isLeft = false;
        }

        if (health > 0) {
            if (ability == -1) {
                move(g);
                int rando = (int) (Math.random() * 10);
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

            if (enemyRect().intersects(p.playerRect())){
                p.takeDamage(1);
            }
        } else {
            deathAnimation(g);
        }
    }

    public void loadImages(){
        try {
            deathSpritesheet = ImageIO.read(new File("src/assets/Death/death.png"));
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
            s.add(new Spirit((int) xCoord, (int)(yCoord - 300)));
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

    private void deathAnimation(Graphics g){
        i++;
        if (i < DEATH_FRAMES * 18){
            if (isLeft){
                g.drawImage(enemyAnimationsLeft[1][i/ DEATH_FRAMES], getxCoord(), getyCoord(), null);
            } else {
                g.drawImage(enemyAnimations[1][i / DEATH_FRAMES], getxCoord(), getyCoord(), null);
            }
        }
    }

    public Rectangle enemyRect() {
        int imageHeight = 492;
        int imageWidth = 480;
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}
