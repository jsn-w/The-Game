import java.awt.image.BufferedImage;
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

    public Boss(){
        hp = 1000;
        phaseOneBeat = false;
        phaseTwoBeat = false;
        win = false;
        hitAvailability = true;
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
    public void phaseOne(){
        if (!phaseOneBeat) {
            if (hp <= 0) {
                phaseOneBeat = true;
                hp = 5000;
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
