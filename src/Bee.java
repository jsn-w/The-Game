import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Bee extends Enemy{
    private final double MOVE_AMT = 0.6;
    private BufferedImage spritesheet;

    private double xCoord;
    private double yCoord;
    private boolean isLeft;

    public Bee(int x, int y) {
        super("src/assets/bee.png", 200, 200);
        loadImages();
    }

    public int getxCoord() {
        return (int) xCoord;
    }

    public int getyCoord() {
        return (int) yCoord;
    }

    public void move() {
        if (xCoord > 800 && xCoord < 810) {
            isLeft = true;
        }

        if (xCoord > 100 && xCoord < 110) {
            isLeft = false;
        }

        if (!isLeft && xCoord + MOVE_AMT <= 880) {
            xCoord += MOVE_AMT;
        }
        if (isLeft && xCoord - MOVE_AMT >= 0) {
            xCoord -= MOVE_AMT;
        }
    }

    private void loadImages() {
        try {
            spritesheet = ImageIO.read(new File("src/assets/bee.png"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void move(Player p, Graphics g){
        if (getxCoord() + 305 < p.getxCoord()){
            xCoord += MOVE_AMT;
        } else {
            xCoord -= MOVE_AMT;
        }

        if (getyCoord() + 251.5 < p.getyCoord()){
            yCoord += MOVE_AMT;
        } else {
            yCoord -= MOVE_AMT;
        }
        g.drawImage(spritesheet, getxCoord() + 200, getyCoord() + 150, null);
    }

    // something is wrong with the math lol
    @Override
    public Bullet shoot(Player p, Graphics g){
        int y = p.getyCoord() - getyCoord();
        int x = p.getxCoord() - getxCoord();
        double angle = Math.atan((double) y / x);
        System.out.println(angle);
        if ((angle > 0 && p.getxCoord() < getxCoord() && p.getyCoord() < getyCoord()) || (angle < 0 && p.getxCoord() < getxCoord() && p.getyCoord() > getyCoord())){
            angle += Math.PI;
        }
        System.out.println(angle);
        return new Bullet(getxCoord() + 305, getyCoord() + 252, angle, 1);
    }
}
