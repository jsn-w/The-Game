import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Death {
    private final double MOVE_AMT = 0.4;
    private BufferedImage enemyAnimations[][];
    private BufferedImage spiritAnimations[][];
    private BufferedImage deathSpritesheet;
    private BufferedImage spiritSpritesheet;
    private double xCoord;
    private double yCoord;
    private int frame;

    public Death(int x, int y){
        xCoord = x;
        yCoord = y;
        frame = 0;
    }

    public int getxCoord(){
        return (int) xCoord;
    }

    public int getyCoord(){
        return (int) yCoord;
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
        enemyAnimations = new BufferedImage[10][10];
        spiritAnimations = new BufferedImage[5][4];
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                enemyAnimations[i][j] = deathSpritesheet.getSubimage(deathSpritesheet.getWidth() / 10 * j, deathSpritesheet.getHeight() / 10 * i, deathSpritesheet.getWidth() / 10 * j, deathSpritesheet.getHeight() / 10 * i);
            }
        }
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 4; j++){
                spiritAnimations[i][j] = spiritSpritesheet.getSubimage(spiritSpritesheet.getWidth() / 4 * j, spiritSpritesheet.getHeight() / 5 * i, spiritSpritesheet.getWidth() / 4 * j, spiritSpritesheet.getHeight() / 5 * i);
            }
        }
    }

    public void summon(){

    }

    public Rectangle enemyRect() {
        int imageHeight = 101;
        int imageWidth = 1002 / 10;
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}
