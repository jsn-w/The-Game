import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Bullet {
    private double moveAmt;
    private double angle;
    private BufferedImage img;
    private double xCoord;
    private double yCoord;

    public Bullet(int x, int y, double angle, double moveAmt){
        xCoord = x - GraphicsPanel.backgroundPosition;
        yCoord = y;
        this.moveAmt = moveAmt;
        this.angle = angle;
        loadImages();
    }

    public int getxCoord(){
        return (int) (xCoord + GraphicsPanel.backgroundPosition);
    }

    public int getyCoord(){
        return (int) yCoord;
    }

    private void loadImages() {
        try {
            img = ImageIO.read(new File("src/assets/bullet.png"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void move(Graphics g){
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        if (cos != 0) {
            xCoord += moveAmt * Math.cos(angle);
        }
        if (sin != 0) {
            yCoord += moveAmt * Math.sin(angle);
        }
        g.drawImage(img, getxCoord(), getyCoord(), null);
    }

    public Rectangle enemyRect(){
        int imageHeight = img.getHeight();
        int imageWidth = img.getWidth();
        return new Rectangle((int) xCoord, (int)yCoord, imageWidth, imageHeight);
    }
}
