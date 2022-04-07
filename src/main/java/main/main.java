package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class main {

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            
            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setTitle("Final Fantasy Fan Game | Harllem Nascimento");
            
            GamePanel gp = new GamePanel();
            window.add(gp);
            window.pack();
            
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            
            gp.startGameThread();
        });
    }
}
