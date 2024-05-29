import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Platform {
    private BufferedImage animations[];
    private BufferedImage spritesheet;
    private double xCoord;
    private double yCoord;
    private int i;


    public Platform(int x, int y, int duration) {
        xCoord = x;
        yCoord = y;
        i = 0;
        loadImages();
    }

    public int getxCoord() {
        return (int) xCoord;
    }

    public int getyCoord() {
        return (int) yCoord;
    }

    private void loadImages() {
        try {
            spritesheet = ImageIO.read(new File("src/assets/platform.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadAnimations() {
        int idx = 0;
        animations = new BufferedImage[10];
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 5; j++){
                animations[idx] = spritesheet.getSubimage(spritesheet.getWidth() / 5 * j, spritesheet.getHeight() / 2 * i, 216, 116);
            }
        }
    }

    public boolean appear(ArrayList<Platform> platforms, Graphics g){
        g.drawImage(animations[i / 50], getxCoord() + (int) GraphicsPanel.backgroundPosition, getyCoord() + (int)GraphicsPanel.backgroundPosition, null);
        i++;

        if (i == 50 * animations.length){
            i = 0;
            platforms.add(this);
            return true;
        }
        return false;
    }

    public boolean disappear(ArrayList<Platform> platforms, Graphics g){
        g.drawImage(animations[i / 50], getxCoord() + (int) GraphicsPanel.backgroundPosition, getyCoord() + (int)GraphicsPanel.backgroundPosition, null);
        i++;

        if (i == 50 * animations.length){
            i = 0;
            platforms.remove(this);
            return true;
        }
        return false;
    }

    public void render(Graphics g){
        g.drawImage(animations[0], getxCoord(), getyCoord(), null);
    }

    public Rectangle rect(){
        return new Rectangle((int)xCoord, (int)yCoord, 216, 116);
    }
}
