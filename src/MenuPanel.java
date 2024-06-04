import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel implements MouseListener,ActionListener,MouseMotionListener {
    private JFrame enclosingFrame;
    private BufferedImage buttons;
    private BufferedImage background;
    private BufferedImage[][] buttonAnimations;
    private int[] buttonState;
    private boolean play;

    public MenuPanel(JFrame frame) {
        enclosingFrame = frame;
        load();
        play = false;
        buttonState = new int[3];
        addMouseMotionListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }
    private void load(){
        try {
            buttons = ImageIO.read(new File("src/assets/buttons.png"));
            background = ImageIO.read(new File("src/assets/menu_background.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        buttonAnimations = new BufferedImage[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                buttonAnimations[i][j] = buttons.getSubimage(140 * j, 56 * i, 140,56);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        for (int i = 0; i < 3; i++) {
            g.drawImage(buttonAnimations[i][buttonState[i]], 75, 56 * i + 120, null);
        }
    }
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (playRect().contains(e.getPoint())){
            play = true;
            enclosingFrame.setVisible(false);
            new MainFrame("a");
        }
        if (quitRect().contains(e.getPoint())){
            System.exit(0);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    private Rectangle playRect(){
        int imageHeight = buttonAnimations[0][1].getHeight();
        int imageWidth = buttonAnimations[0][1].getWidth();
        Rectangle rect = new Rectangle(75, 120, imageWidth, imageHeight);
        return rect;
    }
    private Rectangle optionRect(){
        int imageHeight = buttonAnimations[0][1].getHeight();
        int imageWidth = buttonAnimations[0][1].getWidth();
        Rectangle rect = new Rectangle(75, 176, imageWidth, imageHeight);
        return rect;
    }
    private Rectangle quitRect(){
        int imageHeight = buttonAnimations[0][1].getHeight();
        int imageWidth = buttonAnimations[0][1].getWidth();
        Rectangle rect = new Rectangle(75, 232, imageWidth, imageHeight);
        return rect;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (playRect().contains(e.getPoint())){
            buttonState[0] = 1;
            System.out.println("YES");
        }else if (optionRect().contains(e.getPoint())){
            buttonState[1] = 1;
            System.out.println("YES");
        }else if (quitRect().contains(e.getPoint())){
            buttonState[2] = 1;
            System.out.println("YES");
        }
    }
}
