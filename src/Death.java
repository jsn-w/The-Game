import java.awt.image.BufferedImage;

public class Death {
    private final double MOVE_AMT = 0.4;
    private BufferedImage enemyAnimations[][];
    private BufferedImage deathSpritesheet;
    private BufferedImage summonsSpritesheet;
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
}
