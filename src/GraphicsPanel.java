import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener {
    private BufferedImage background;
    private Player mario;
    private Player luigi;
    private Enemy enemy;
    private boolean[] pressedKeys;
    private int enemyXValue;
    private String winner;
    private boolean pause;

    public GraphicsPanel() {
        try {
            background = ImageIO.read(new File("src/background.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        mario = new Player("src/marioleft.png", "src/marioright.png", background.getMinX() + 50, 435, true);
        luigi = new Player("src/luigileft.png", "src/luigiright.png", background.getWidth() - 100, 435, false);
        enemyXValue = background.getWidth()/2 - 25;
        enemy = new Enemy("src/enemy.png", enemyXValue, 475);
        pressedKeys = new boolean[128];
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
        pause = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        if (mario.getScore() >= 10) {
            winner = "Mario";
        }
        if (luigi.getScore() >= 10) {
            winner = "Luigi";
        }
        if (winner == null && !pause) {
            g.drawImage(enemy.getEnemyImage(), enemy.getxCoord(), enemy.getyCoord(), null);
            g.drawImage(mario.getPlayerImage(), mario.getxCoord(), mario.getyCoord(), null);
            g.drawImage(luigi.getPlayerImage(), luigi.getxCoord(), luigi.getyCoord(), null);

            if (mario.playerRect().intersects(enemy.enemyRect())) { // check for collision
                mario.updateScore(-0.01);
            }
            if (luigi.playerRect().intersects(enemy.enemyRect())) { // check for collision
                luigi.updateScore(-0.01);
            }

            g.drawString("Mario Score: " + mario.getScore(), 20, 40);
            g.drawString("Luigi Score: " + luigi.getScore(), 20, 80);

            if (pressedKeys[65]) {
                mario.faceLeft();
                mario.moveLeft();
            }
            if (pressedKeys[68]) {
                mario.faceRight();
                mario.moveRight();
            }
            if (pressedKeys[87]) {
                mario.moveUp();
            }
            if (pressedKeys[83]) {
                mario.moveDown();
            }

            if (pressedKeys[37]) {
                luigi.moveLeft();
                luigi.faceLeft();
            }
            if (pressedKeys[39]) {
                luigi.moveRight();
                luigi.faceRight();
            }
            if (pressedKeys[38]) {
                luigi.moveUp();
            }
            if (pressedKeys[40]) {
                luigi.moveDown();
            }

            enemy.move();
        } else if (pause) {
            g.drawImage(enemy.getEnemyImage(), enemy.getxCoord(), enemy.getyCoord(), null);
            g.drawImage(mario.getPlayerImage(), mario.getxCoord(), mario.getyCoord(), null);
            g.drawImage(luigi.getPlayerImage(), luigi.getxCoord(), luigi.getyCoord(), null);


            g.drawString("Move the mouse inside the window", 250, 220);
            g.drawString("to continue playing.", 350, 250);
        } else {
            g.drawString(winner + " has won the game!", 300, 250);
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
            mario.setScore(0);
            luigi.setScore(0);
        }
    }

    public void mouseEntered(MouseEvent e) {
        pause = false;
    }

    public void mouseExited(MouseEvent e) {
        pause = true;
    }
}