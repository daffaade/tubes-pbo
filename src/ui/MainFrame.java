package ui;

import data.DataManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import model.Pesanan;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel contentContainer;
    
    public static final String MENU_UTAMA = "MENU_UTAMA";
    public static final String BELI_OBAT = "BELI_OBAT";
    public static final String KERANJANG = "KERANJANG";
    public static final String CHECKOUT = "CHECKOUT";
    public static final String KONSULTASI = "KONSULTASI";
    public static final String HISTORY = "HISTORY";
    
    private Pesanan pesananAktif;
    private BeliObatPanel beliObatPanel;
    private KeranjangPanel keranjangPanel;
    private CheckoutPanel checkoutPanel;
    private KonsultasiPanel konsultasiPanel;
    private HistoryPanel historyPanel;
    
    public MainFrame() {
        setTitle("Aplikasi Apotek Sehat");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inisialisasi pesanan aktif
        pesananAktif = new Pesanan(DataManager.getInstance().getCurrentUser());
        
        // Panel utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(1100, 70));
        
        JLabel titleLabel = new JLabel("Sistem Manajemen Apotek");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(70, 130, 180));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        
        JButton menuUtamaBtn = new JButton("Menu Utama");
        JButton logoutBtn = new JButton("Logout");
        
        menuUtamaBtn.setFont(new Font("Poppins", Font.PLAIN, 14));
        logoutBtn.setFont(new Font("Poppins", Font.PLAIN, 14));
        menuUtamaBtn.setPreferredSize(new Dimension(130, 40));
        logoutBtn.setPreferredSize(new Dimension(100, 40));
        
        buttonPanel.add(menuUtamaBtn);
        buttonPanel.add(logoutBtn);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Content Container dengan CardLayout
        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);
        
        // Content Panel Menu Utama
        JPanel menuUtamaPanel = new JPanel();
        menuUtamaPanel.setLayout(new BorderLayout());
        menuUtamaPanel.setBackground(new Color(240, 240, 240));
        
        JLabel welcomeLabel = new JLabel("Selamat Datang, Silakan Pilih Layanan", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(50, 50, 50));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        
        // Menu Grid Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2, 2, 30, 30));
        menuPanel.setBackground(new Color(240, 240, 240));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 100, 100));
        
        // Buat 4 menu button
        JButton beliObatBtn = createMenuButton("Beli Obat", new Color(60, 150, 100));
        JButton konsultasiBtn = createMenuButton("Konsultasi Dokter", new Color(70, 130, 180));
        JButton cekKeranjangBtn = createMenuButton("Cek Keranjang", new Color(255, 140, 0));
        JButton infoApotekBtn = createMenuButton("History", new Color(100, 150, 230));
        
        menuPanel.add(beliObatBtn);
        menuPanel.add(konsultasiBtn);
        menuPanel.add(cekKeranjangBtn);
        menuPanel.add(infoApotekBtn);
        
        menuUtamaPanel.add(welcomeLabel, BorderLayout.NORTH);
        menuUtamaPanel.add(menuPanel, BorderLayout.CENTER);
        
        // Tambahkan panel ke CardLayout
        contentContainer.add(menuUtamaPanel, MENU_UTAMA);
        
        beliObatPanel = new BeliObatPanel(this, pesananAktif);
        contentContainer.add(beliObatPanel, BELI_OBAT);
        
        keranjangPanel = new KeranjangPanel(this, pesananAktif);
        contentContainer.add(keranjangPanel, KERANJANG);
        
        checkoutPanel = new CheckoutPanel(this, pesananAktif);
        contentContainer.add(checkoutPanel, CHECKOUT);
        
        konsultasiPanel = new KonsultasiPanel();
        contentContainer.add(konsultasiPanel, KONSULTASI);
        
        historyPanel = new HistoryPanel();
        contentContainer.add(historyPanel, HISTORY);
        
        // Add event listeners
        beliObatBtn.addActionListener(e -> showPanel(BELI_OBAT));
        konsultasiBtn.addActionListener(e -> showPanel(KONSULTASI));
        cekKeranjangBtn.addActionListener(e -> {
            keranjangPanel.refreshData();
            showPanel(KERANJANG);
        });
        infoApotekBtn.addActionListener(e -> {
            historyPanel.refreshData();
            showPanel(HISTORY);
        });
        
        menuUtamaBtn.addActionListener(e -> showPanel(MENU_UTAMA));
        
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin logout?", 
                "Konfirmasi Logout", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentContainer, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    // Method untuk berpindah panel
    public void showPanel(String panelName) {
        cardLayout.show(contentContainer, panelName);
    }
    
    public KeranjangPanel getKeranjangPanel() {
        return keranjangPanel;
    }
    
    public CheckoutPanel getCheckoutPanel() {
        return checkoutPanel;
    }
    
    public BeliObatPanel getBeliObatPanel() {
        return beliObatPanel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}