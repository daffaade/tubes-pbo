import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ui.LoginFrame;

public class MainApp {
    public static void main(String[] args) {
        // Tambahkan di Main.java, sebelum MainFrame dibuat
        try {
            // Menggunakan tampilan sistem operasi (misalnya Windows, macOS)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // ATAU: Menggunakan L&F yang lebih modern (seperti FlatLaf, tapi butuh library eksternal)
            // Jika Anda tidak ingin library eksternal, System L&F adalah pilihan terbaik.
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            }
        });
    }
}