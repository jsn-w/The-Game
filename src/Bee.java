import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Bee extends Enemy{
    private final double MOVE_AMT = 0.3;
    private BufferedImage enemyAnimations[][];
    private BufferedImage spritesheet;

    private double xCoord;
    private double yCoord;
    private boolean isLeft;
    private boolean dashReady;
    private int i;

    public Bee(int x, int y) {
        super("src/assets/bee.png", 200, 200);
        i = 0;
        dashReady = false;
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
        loadAnimations();
    }

    private void loadAnimations() {
        enemyAnimations = new BufferedImage[5][23];
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 23; j++){
                enemyAnimations[i][j] = spritesheet.getSubimage(spritesheet.getWidth() / 23 * j, spritesheet.getHeight() / 5 * i, 160, 160);
            }
        }
    }

    public void move(Player p){
        if (getxCoord() < p.getxCoord()){
            xCoord += MOVE_AMT;
        } else {
            xCoord -= MOVE_AMT;
        }

        if (getyCoord() < p.getyCoord()){
            yCoord += MOVE_AMT;
        } else {
            yCoord -= MOVE_AMT;
        }
    }

    public void shoot(Player p){
        double angle = Math.atan((double) p.getyCoord() / p.getxCoord());
        Bullet b = new Bullet(getxCoord(), getyCoord(), angle, 0.2);
    }
}
