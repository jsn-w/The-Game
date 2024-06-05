import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener {
    private static final int MENU = 0;
    private static final int LOADING = 1;
    private static final int GAME = 2;
    private int state = MENU;

    public static double backgroundPosition;

    private BufferedImage background;
    private Player player;
    private NightBorne e;

    private ArrayList<NightBorne> enemies;
    private ArrayList<Bullet> bullets;

    private boolean[] pressedKeys;

    private BufferedImage menuBackground;
    private BufferedImage buttons;
    private BufferedImage[][] buttonAnimations;
    private int[] buttonState;
    private int startY = 400;
    private int buttonSpacing = 65;

    private Timer loadingTimer;
    private int loadingAnimationAngle;

    public GraphicsPanel() {
        pressedKeys = new boolean[128];
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        backgroundPosition = (double) -MainFrame.screenWidth / 2;
        buttonState = new int[3];

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        requestFocusInWindow();
        loadAssets();

        loadingAnimationAngle = 0;
        loadingTimer = new Timer(1, this);
    }

    private void loadAssets() {
        try {
            background = ImageIO.read(new File("src/assets/background.png"));
            buttons = ImageIO.read(new File("src/assets/buttons.png"));
            menuBackground = ImageIO.read(new File("src/assets/menuBackground.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        buttonAnimations = new BufferedImage[3][3];
        int width = buttons.getWidth()/3;
        int height = buttons.getHeight()/3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttonAnimations[i][j] = buttons.getSubimage(width * j, height * i, width, height);
            }
        }

        player = new Player("src/assets/playerAnimations.png", 640, 135);
        e = new NightBorne("src/assets/NightBorne.png", 100, 220);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (state) {
            case MENU -> renderMenu(g);
            case LOADING -> renderLoading(g);
            case GAME -> renderGame(g);
        }
    }

    private void renderMenu(Graphics g) {
        g.drawImage(menuBackground, 0, 0, null);
        int buttonX = (MainFrame.screenWidth - buttonAnimations[0][0].getWidth()) / 2;

        for (int i = 0; i < 3; i++) {
            g.drawImage(buttonAnimations[i][buttonState[i]], buttonX, startY + i * buttonSpacing, null);
        }
    }

    private void renderLoading(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.translate(MainFrame.screenWidth / 2, MainFrame.screenHeight / 2);
        g2d.rotate(Math.toRadians(loadingAnimationAngle));
        g2d.fillRect(-25, -25, 50, 50);
        g2d.rotate(-Math.toRadians(loadingAnimationAngle));
        g2d.translate(-MainFrame.screenWidth / 2, -MainFrame.screenHeight / 2);
    }

    private void renderGame(Graphics g) {
        g.drawImage(background, (int) backgroundPosition, 0, null);
        player.render(g, this);
        e.render(g, player);
        for (Bullet bullet : bullets) {
            bullet.move(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (state == MENU) {
            if (playRect().contains(e.getPoint())) {
                state = LOADING;
                loadingTimer.start();
            } else if (quitRect().contains(e.getPoint())) {
                System.exit(0);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (state == MENU) {
            boolean insideButton = false;
            for (int i = 0; i < 3; i++) {
                if (getButtonRect(i).contains(e.getPoint())) {
                    buttonState[i] = 1;
                    insideButton = true;
                } else {
                    buttonState[i] = 0;
                }
            }

            if (!insideButton) {
                for (int i = 0; i < 3; i++) {
                    buttonState[i] = 0;
                }
            }

            repaint();
        }
    }

    private Rectangle getButtonRect(int index) {
        int imageHeight = buttons.getHeight()/3;
        int imageWidth = buttons.getWidth()/3;
        int x = (MainFrame.screenWidth - imageWidth) / 2;
        int y = startY + index * buttonSpacing;  // Adjusted Y coordinate with spacing
        return new Rectangle(x, y, imageWidth, imageHeight);
    }

    private Rectangle playRect() {
        return getButtonRect(0);
    }

    private Rectangle optionRect() {
        return getButtonRect(1);
    }

    private Rectangle quitRect() {
        return getButtonRect(2);
    }

    // KeyListener methods
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    // MouseListener methods
    public void mouseClicked(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    // MouseMotionListener methods
    public void mouseDragged(MouseEvent e) {}

    public boolean[] getPressedKeys() {
        return pressedKeys;
    }

    public ArrayList<NightBorne> getEnemies() {
        return enemies;
    }

    // ActionListener method for the loading animation
    @Override
    public void actionPerformed(ActionEvent e) {
        if (state == LOADING) {
            loadingAnimationAngle += 1.7;
            repaint();
            if (loadingAnimationAngle >= 360) {
                loadingAnimationAngle = 0;
                state = GAME;
                loadingTimer.stop();
            }
        }
    }
}