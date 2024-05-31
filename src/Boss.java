import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Boss{
    private BufferedImage image;
    private double x;
    private double y;
    private boolean phaseOneBeat;
    private boolean phaseTwoBeat;
    private boolean win;
    private int hp;
    private boolean hitAvailability;
    private ArrayList<Enemy> enemies;
    private int maxHp;
    private BufferedImage[][] bossAnimations;
    private BufferedImage spritesheet;
    private int i;

    public Boss(){
        hp = 1000;
        maxHp = 1000;
        phaseOneBeat = false;
        phaseTwoBeat = false;
        win = false;
        hitAvailability = true;
        loadImages();
        i = 0;
    }
    private void loadImages() {
        try {
            spritesheet = ImageIO.read(new File("src/assets/cthulu.png"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        loadAnimations();
    }

    private void loadAnimations() {
        bossAnimations = new BufferedImage[7][15];
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 15; j++){
                 bossAnimations[i][j] = spritesheet.getSubimage(spritesheet.getWidth() / 15 * j, spritesheet.getHeight() / 7 * i, spritesheet.getWidth() / 15, spritesheet.getHeight() / 7);
            }
        }
    }
    private void idle(Graphics g, int frames) {
        g.drawImage(bossAnimations[0][i/frames], (int) x, (int) y, null);
        i++;
        if (i == 9 * frames) {
            i = 0;
        }
    }


    public BufferedImage getImage() {
        return image;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public boolean isPhaseOneBeat(){
        return phaseOneBeat;
    }
    public boolean isPhaseTwoBeat() {
        return phaseTwoBeat;
    }
    public boolean canHit(){
        return hitAvailability;
    }
    public void drawHealthBar(Graphics g){
        g.setColor(Color.RED);
        g.fillRect(320,100,640 * (hp/maxHp), 50);
        g.setColor(Color.WHITE);
        g.fillRect(320+640 * (hp/maxHp),100,640 * ((maxHp-hp)/maxHp),50);
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("boss health: " + hp + "/" + maxHp,320,125);
    }
    public void phaseOne(){
        if (!phaseOneBeat) {
            if (hp <= 0) {
                phaseOneBeat = true;
                hp = 5000;
                maxHp = 5000;
            }
        }
    }
    public void phaseTwo(ArrayList<Enemy> e){
        if (!phaseTwoBeat) {
            hitAvailability = false;
            if (e.isEmpty()){
                phaseTwoBeat = true;
                hitAvailability = true;
            }
        }
    }
    public void phaseThree(){
        if (hp <= 0) {
            win = true;
        }
    }
}
