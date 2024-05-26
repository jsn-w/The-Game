import javax.swing.*;

public class MainFrame implements Runnable {
    private GraphicsPanel panel;
    public final static int screenWidth = 1280;
    public final static int screenHeight = 720;

    public MainFrame() {
        JFrame frame = new JFrame("a boring game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        panel = new GraphicsPanel();
        frame.add(panel);
        frame.setVisible(true);
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(true) {
            panel.repaint();
        }
    }
}
