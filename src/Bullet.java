import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Bullet {
    private double moveAmt;
    private double angle;
    private BufferedImage img;
    private double xCoord;
    private double yCoord;

    public Bullet(int x, int y, double angle, double moveAmt){
        xCoord = x;
        yCoord = y;
        this.moveAmt = moveAmt;
        this.angle = angle;
        loadImages();

        loadImages();
    }

    public int getxCoord(){
        return (int) xCoord;
    }

    public int getyCoord(){
        return (int) yCoord;
    }

    private void loadImages() {
        try {
            img = ImageIO.read(new File("src/assets/bullet.png"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void move(Graphics g){
        xCoord += moveAmt * Math.cos(angle);
        yCoord += moveAmt * Math.sin(angle);
        g.drawImage(img, getxCoord(), getyCoord(), null);
    }
}