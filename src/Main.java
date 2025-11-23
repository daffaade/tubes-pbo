
import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.MainFrame;
import ui.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {

    //waerna header yang konsisten di seluruh aplikasi
    public static final Color WARNA_HEADER = new Color(30, 90, 80); 

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
                // Membuat instance dari MainFrame
                MainFrame mainFrame = new MainFrame("Sistem Apotek");

                // Menampilkan frame
                mainFrame.setVisible(true);

                
            }
        }); 

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}