import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bloodsword extends Bullet{
    private BufferedImage img;
    public Bloodsword(int x, int y, double angle, double moveAmt){
        super(x, y, angle, moveAmt);
        loadImages();
    }

    private void loadImages(){
        try{
            img = ImageIO.read(new File("src/assets/Death/bloodswords.png"));
        } catch (IOException e){
            System.out.println(e);
        }
    }

    @Override
    public void move(Graphics g){
        super.changeY();
        g.drawImage(img, getxCoord(), getyCoord(), null);
    }

    @Override
    public Rectangle enemyRect(){
        int imageHeight = img.getHeight();
        int imageWidth = img.getWidth();
        return new Rectangle((int)(super.getxCoord() + GraphicsPanel.backgroundPosition), super.getyCoord(), imageWidth, imageHeight);
    }
}
