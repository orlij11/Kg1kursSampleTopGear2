package kiselev_i_e;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Top gear style retrowave");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        RacingPanel scene = new RacingPanel();
        frame.add(scene);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}