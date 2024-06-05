import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Death {
    private final double MOVE_AMT = 0.3;

    private final int SUMMON_FRAMES = 40;
    private final int SLASH_FRAMES = 20;
    private final int DEATH_FRAMES = 50;
    private final int MOVE_FRAMES = 30;

    private BufferedImage enemyAnimations[][], enemyAnimationsLeft[][];
    private BufferedImage spiritAnimations[][];
    private BufferedImage deathSpritesheet;
    private BufferedImage spiritSpritesheet;

    int health;
    private double xCoord;
    private double yCoord;
    private boolean isLeft;
    private int i;
    private int row;
    private int ability;

    public Death(int x, int y){
        xCoord = x;
        yCoord = y;
        i = 0;
        row = 0;
        ability = -1;
        health = 6666;

        loadImages();
    }

    public int getxCoord(){
        return (int) xCoord;
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
            if (ability < 7000) {
                move(g, p);
                ability = (int) (Math.random() * 10000);
            } else if (ability < 8000) {
                slash(g, p);
            } else if (ability < 9000) {
//                summon(g, GraphicsPanel.spirits);
            } else {
                move(g, p);
            }
        } else {
            deathAnimation(g);
        }
    }

    public void loadImages(){
        try {
            deathSpritesheet = ImageIO.read(new File("src/assets/Death/death.png"));
            spiritSpritesheet = ImageIO.read(new File("src/assets/Death/spirit.png"));
        } catch (IOException e){
            System.out.println(e);
        }
        loadAnimations();
    }

    public void loadAnimations(){
        enemyAnimations = new BufferedImage[4][18]; // row 1: slash, row 2: death, row 3: idle, row 4: summon
        enemyAnimationsLeft = new BufferedImage[4][18];

        spiritAnimations = new BufferedImage[3][6]; // row 1: spawn, row 2: death, row 3: idle

        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 18; j++){
                enemyAnimations[row][j] = deathSpritesheet.getSubimage(deathSpritesheet.getWidth() / 10 * j, deathSpritesheet.getHeight() / 10 * i, 100, 100);
            }
            if (i == 3){
                row = 1;
            } else if (i == 5){
                row = 2;
            } else if (i == 8){
                row = 3;
            }
        }
        enemyAnimationsLeft = Utility.flipEvery(enemyAnimations);
        row = 0;
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 4; j++){
                spiritAnimations[row][j] = spiritSpritesheet.getSubimage(spiritSpritesheet.getWidth() / 4 * j, spiritSpritesheet.getHeight() / 5 * i, 50, 50);
            }
            if (i == 2){
                row = 1;
            } else if (i == 4){
                row = 2;
            }
        }
        row = 0;
    }

    private void move(Graphics g, Player p){
        if (i >= 18 * MOVE_FRAMES){
            i = 0;
        }
        if (i % 4 * MOVE_FRAMES == 0){
            i += 6 * MOVE_FRAMES;
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
    private void slash(Graphics g, Player p){
        i++;
        if (i < 13 * SLASH_FRAMES){
            if (isLeft){
                g.drawImage(enemyAnimationsLeft[1][i / SLASH_FRAMES], getxCoord(), getyCoord(), null);
            } else {
                g.drawImage(enemyAnimations[1][i / SLASH_FRAMES], getxCoord(), getyCoord(), null);
            }
            xCoord += MOVE_AMT * 2;
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
            s.add(new Spirit());
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
        int imageHeight = 101;
        int imageWidth = 1002 / 10;
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}
