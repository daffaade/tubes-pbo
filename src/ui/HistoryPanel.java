package ui;

import data.DataManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.History;

public class HistoryPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private DataManager dm = DataManager.getInstance();

    public HistoryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Riwayat Transaksi & Konsultasi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(Color.WHITE);
        refreshButton.setForeground(new Color(70, 130, 180));
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(new Dimension(100, 35));
        refreshButton.addActionListener(e -> refreshData());
        headerPanel.add(refreshButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Tabel
        String[] columnNames = {"No", "Username", "Tanggal", "Jenis", "Detail"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // No
        table.getColumnModel().getColumn(1).setPreferredWidth(120);  // Username
        table.getColumnModel().getColumn(2).setPreferredWidth(150);  // Tanggal
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Jenis
        table.getColumnModel().getColumn(4).setPreferredWidth(300);  // Detail

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(scrollPane, BorderLayout.CENTER);

        // Info Panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        JLabel infoLabel = new JLabel("Total Riwayat: 0");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        infoPanel.add(infoLabel);
        
        add(infoPanel, BorderLayout.SOUTH);

        // Load data awal
        refreshData();
    }

    private void loadData(List<History> historyList) {
        tableModel.setRowCount(0);

        if (historyList == null || historyList.isEmpty()) {
            return;
        }

        for (History h : historyList) {
            Object[] row = {
                    h.getNoHistory(),
                    h.getUsername(),
                    h.getTanggal(),
                    h.getJenis(),
                    h.getDetail()
            };
            tableModel.addRow(row);
        }

        // Update info label
        Component[] components = ((JPanel) getComponent(2)).getComponents();
        if (components.length > 0 && components[0] instanceof JLabel) {
            ((JLabel) components[0]).setText("Total Riwayat: " + historyList.size());
        }
    }

    public void refreshData() {
        // Ambil semua history
        List<History> allHistory = dm.getDaftarHistory();
        
        // Filter berdasarkan username current user
        List<History> userHistory = new java.util.ArrayList<>();
        String currentUsername = dm.getCurrentUser().getUsername();
        
        for (History h : allHistory) {
            if (h.getUsername().equals(currentUsername)) {
                userHistory.add(h);
            }
        }
        
        loadData(userHistory);
    }
}