import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener {
    private BufferedImage background;
    private Player player;
    private Enemy enemy;
    private boolean[] pressedKeys;
    private double backgroundPosition;

    public GraphicsPanel() {
        try {
            background = ImageIO.read(new File("src/assets/background.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player = new Player("src/assets/player.png", "src/assets/player.png", 640, 435);
//
//        enemy = new Enemy("src/enemy.png", enemyXValue, 475);
        pressedKeys = new boolean[128];
        backgroundPosition = -1920;
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, (int) backgroundPosition, 0, null);
        g.setFont(new Font("Courier New", Font.BOLD, 24));


//        g.drawImage(enemy.getEnemyImage(), enemy.getxCoord(), enemy.getyCoord(), null);
        g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), null);

        g.drawString("player Score: " + player.getScore(), 20, 40);
        g.drawString("player health: ",20,64);
        for(int i = 0; i < player.getHp(); i ++){
            g.drawImage(player.getHeart(),40,64+10*i,null);
        }


        if (pressedKeys[65]) { // 37
            player.faceLeft();
            if (player.moveLeft()) {
                backgroundPosition += 0.4;
            }
        }
        if (pressedKeys[68]) { // 39
            player.faceRight();
            if (player.moveRight()) {
                backgroundPosition -= 0.4;
            }

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