import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener {
    private BufferedImage background;
    private Player player;
    private Enemy e;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private boolean[] pressedKeys;
    private Boss b;
    public static double backgroundPosition;
    private boolean test = true;
    int f = 1000; // test variable (remove later)

    public GraphicsPanel() {
        loadAssets();
        pressedKeys = new boolean[128];
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        backgroundPosition = (double) -MainFrame.screenWidth /2;
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    private void loadAssets() {
        try {
            background = ImageIO.read(new File("src/assets/background.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player = new Player("src/assets/player.png", "src/assets/player.png", 640, 435);
        e = new Bee(100, 100);
        b = new Boss();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (test) {
            enemies.add(e);
            test = false;
        }
        super.paintComponent(g);
        g.drawImage(background, (int) backgroundPosition, 0, null);
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString("player health: ",MainFrame.screenWidth/2 - 50,64);

        player.render(g);


        if (f == 1000){
            for (int i = 0; i < enemies.size(); i++) {
                bullets.add(enemies.get(i).shoot(player, g));
            }
            f = 0;
        }
        f++;
        e.move(player, g);
        for (int b = 0; b < bullets.size(); b++){
            bullets.get(b).move(g);
        }

        for(int i = 0; i < player.getHp(); i ++){
            g.drawImage(player.getHeart(),40,64+10*i,null);
        }
        checkKeyboardInput();
        if (!b.isPhaseOneBeat()){
            b.phaseOne();
        }else if (!b.isPhaseTwoBeat()){
            b.phaseTwo(enemies);
        }else{
            b.phaseThree();
        }
    }

    private void checkKeyboardInput() {
        if (pressedKeys[65]) { // 37
            player.faceLeft();
            player.moveLeft();
        }
        if (pressedKeys[68]) { // 39
            player.faceRight();
            player.moveRight();

        }
        if (pressedKeys[87]) { // 38
            player.moveUp();
        }
        if (pressedKeys[83]) { // 40
            player.moveDown();
        }
    }



    // ----- KeyListener interface methods -----
    public void keyTyped(KeyEvent e) { } // unimplemented

    public void keyPressed(KeyEvent e) {
        // see this for all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    // ----- MouseListener interface methods -----
    public void mouseClicked(MouseEvent e) { }

    public void mousePressed(MouseEvent e) { } // unimplemented

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            Point mouseClickLocation = e.getPoint();


        }
        if (e.getButton() == MouseEvent.BUTTON3) { // right mouse click
//            player.setScore(0);
//            luigi.setScore(0);
        }
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}