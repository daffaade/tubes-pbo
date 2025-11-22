// File: src/ui/LoginFrame.java
package ui;

import data.DataManager;
import model.Users;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private DataManager dataManager;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    
    public LoginFrame() {
        dataManager = DataManager.getInstance();
        
        setTitle("Login - Sistem Apotek");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/logo.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Logo tidak ditemukan ");
        }
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(450, 120));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        // label 
        JLabel lblTitle = new JLabel("SISTEM APOTEK");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Aplikasi Manajemen Apotek");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitle.setForeground(Color.WHITE);
        
        headerPanel.add(Box.createVerticalStrut(25));
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(lblSubtitle);
        headerPanel.add(Box.createVerticalStrut(15));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 30, 50));
        
        // Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
        lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setMaximumSize(new Dimension(400, 35));
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setMaximumSize(new Dimension(400, 35));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setMaximumSize(new Dimension(400, 50));
        
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(60, 179, 113));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(120, 35));
        
        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setBackground(new Color(255, 140, 0));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setPreferredSize(new Dimension(120, 35));
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(240, 248, 255));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel lblInfo = new JLabel("<html><center>Default Login:<br>Username: admin | Password: admin123<br>Username: user | Password: user123</center></html>");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(lblInfo);
        
        // Add components to form panel
        formPanel.add(lblUsername);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtUsername);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(lblPassword);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtPassword);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalStrut(20));
        
        
        // Event listeners
        btnLogin.addActionListener(e -> login());
        btnRegister.addActionListener(e -> showRegisterDialog());
        
        // Enter key untuk login
        txtPassword.addActionListener(e -> login());
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        //  tulisan info di bawah login form
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);   
        mainPanel.add(infoPanel, BorderLayout.SOUTH); 
        
        add(mainPanel);
    }
    
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username dan password harus diisi!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (dataManager.login(username, password)) {
            JOptionPane.showMessageDialog(this,
                "Login berhasil!\nSelamat datang, " + dataManager.getCurrentUser().getNama(),
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Buka main frame
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            
            // Tutup login frame
            dispose(); // melepaskan memori yang dialokasikan ke variabel saat objek status dihapus.
        } else {
            JOptionPane.showMessageDialog(this,
                "Username atau password salah!",
                "Login Gagal",
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }
    
    private void showRegisterDialog() {
        JDialog dialog = new JDialog(this, "Register User Baru", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(Color.WHITE);
        
        // Fields
        JTextField txtRegUsername = new JTextField();
        JPasswordField txtRegPassword = new JPasswordField();
        JPasswordField txtRegConfirm = new JPasswordField();
        JTextField txtRegNama = new JTextField();
        JTextArea txtRegAlamat = new JTextArea(3, 20);
        txtRegAlamat.setLineWrap(true);
        txtRegAlamat.setWrapStyleWord(true);
        JTextField txtRegTelp = new JTextField();
        
        txtRegUsername.setMaximumSize(new Dimension(400, 30));
        txtRegPassword.setMaximumSize(new Dimension(400, 30));
        txtRegConfirm.setMaximumSize(new Dimension(400, 30));
        txtRegNama.setMaximumSize(new Dimension(400, 30));
        txtRegTelp.setMaximumSize(new Dimension(400, 30));
        
        panel.add(new JLabel("Username:"));
        panel.add(txtRegUsername);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Password:"));
        panel.add(txtRegPassword);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Konfirmasi Password:"));
        panel.add(txtRegConfirm);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Nama Lengkap:"));
        panel.add(txtRegNama);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Alamat:"));
        panel.add(new JScrollPane(txtRegAlamat));
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("No. Telepon:"));
        panel.add(txtRegTelp);
        panel.add(Box.createVerticalStrut(20));
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnDaftar = new JButton("Daftar");
        btnDaftar.setBackground(new Color(60, 179, 113));
        btnDaftar.setForeground(Color.WHITE);
        btnDaftar.setFocusPainted(false);
        
        JButton btnBatal = new JButton("Batal");
        btnBatal.setBackground(new Color(220, 20, 60));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFocusPainted(false);
        
        btnDaftar.addActionListener(e -> {
            String username = txtRegUsername.getText().trim();
            String password = new String(txtRegPassword.getPassword());
            String confirm = new String(txtRegConfirm.getPassword());
            String nama = txtRegNama.getText().trim();
            String alamat = txtRegAlamat.getText().trim();
            String telp = txtRegTelp.getText().trim();
            
            if (username.isEmpty() || password.isEmpty() || nama.isEmpty() || 
                alamat.isEmpty() || telp.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Semua field harus diisi!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(dialog,
                    "Password dan konfirmasi tidak cocok!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Users newUser = new Users(username, password, nama, telp);
            if (dataManager.register(newUser)) {
                JOptionPane.showMessageDialog(dialog,
                    "Registrasi berhasil!\nSilakan login dengan akun baru Anda.",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Username sudah digunakan!",
                    "Gagal",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnBatal.addActionListener(e -> dialog.dispose());
        
        btnPanel.add(btnDaftar);
        btnPanel.add(btnBatal);
        panel.add(btnPanel);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}