import javax.swing.*;

public class MainMenu implements Runnable{
    private MenuPanel panel;

    public MainMenu() {
        JFrame frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 375);
        frame.setLocationRelativeTo(null);
        panel = new MenuPanel(frame);
        frame.add(panel);
        frame.setVisible(true);
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        panel.repaint();
    }
}
