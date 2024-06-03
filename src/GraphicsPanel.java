import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener {

    public static double backgroundPosition;

    private BufferedImage background;
    private Player player;
    private NightBorne e;

    private ArrayList<NightBorne> enemies;
    private ArrayList<Bullet> bullets;

    private boolean[] pressedKeys;

    public GraphicsPanel() {
        pressedKeys = new boolean[128];
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        backgroundPosition = (double) -MainFrame.screenWidth / 2;
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
        loadAssets();
    }

    private void loadAssets() {
        try {
            background = ImageIO.read(new File("src/assets/background.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player = new Player("src/assets/playerAnimations.png", 640, 135);
        e = new NightBorne("src/assets/NightBorne.png",100, 220);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        boolean mainMenu = false;

        if (mainMenu) {
            mainMenu(g);
        } else {
            phase1(g);
        }
    }

    private void mainMenu(Graphics g) {

    }

    private void phase1(Graphics g) {
        g.drawImage(background, (int) backgroundPosition, 0, null);
        player.render(g, this);
        e.render(g, player);
        for (Bullet bullet : bullets) {
            bullet.move(g);
        }
    }


    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    public void mouseClicked(MouseEvent e) { }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            Point mouseClickLocation = e.getPoint();

        }
        if (e.getButton() == MouseEvent.BUTTON3) { // right mouse click
        }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public boolean[] getPressedKeys() {
        return pressedKeys;
    }

    public ArrayList<NightBorne> getEnemies() {
        return enemies;
    }
}