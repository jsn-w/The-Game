import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class BossBullet extends Bullet{
    private BufferedImage[] images;
    private int i;
    public BossBullet(int x, int y, double angle, double moveAmt) {
        super(x, y, angle, moveAmt);
        i = 0;
        loadImages();
    }
    private void loadImages(){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("src/assets/bossbullet.png"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        images = new BufferedImage[7];
        for(int i = 0; i < 7; i++){
            assert img != null;
            images[i] = img.getSubimage(img.getWidth() / 11 * i, img.getHeight() / 9,img.getWidth() / 11,img.getHeight() / 9);
        }
    }
    public boolean move(Graphics g, ArrayList<BossBullet> bullets){
        setImg(images[i/120]);
        super.move(g);
        i++;
        if(i >= 7 * 120){
            bullets.remove(this);
            return true;
        }
        return false;
    }
}
