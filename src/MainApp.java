import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ui.LoginFrame;

public class MainApp {
    public static void main(String[] args) {

        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            }
        });
    }
}