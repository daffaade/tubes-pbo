package ui;

import javax.swing.*;

public class MainFrame extends JFrame {
    
    public MainFrame() {
        setTitle("Menu Utama Apotek");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Tengah layar
        
        // Tulisan sementara biar ga kosong 
        add(new JLabel("Halaman Utama (MainFrame)", SwingConstants.CENTER));
    }
}