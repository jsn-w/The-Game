import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Boss{
    private BufferedImage image;
    private int x;
    private int y;
    private boolean phaseOneBeat;
    private boolean phaseTwoBeat;
    private boolean win;
    private int hp;
    private boolean hitAvailability;
    private ArrayList<Enemy> enemies;
    private int maxHp;

    public Boss(){
        hp = 1000;
        maxHp = 1000;
        phaseOneBeat = false;
        phaseTwoBeat = false;
        win = false;
        hitAvailability = true;
//        try {
//            image = ImageIO.read(new File("src/assets/boss.png"));
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
    }

    public BufferedImage getImage() {
        return image;
    }
    public int getX(){
        return x;
    }
    public int getY(){
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
