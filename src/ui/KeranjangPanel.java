package ui;

import model.ItemKeranjang;
import model.Pesanan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class KeranjangPanel extends JPanel {
    private MainFrame parentFrame;
    private Pesanan pesananSekarang;
    private JTable tabelKeranjang;
    private DefaultTableModel modelTabel;
    private JLabel totalLabel;

    private JButton removeButton;
    private JButton checkoutButton;

    public KeranjangPanel (MainFrame parent, Pesanan pesananAktif) {
        this.parentFrame = parent;
        this.pesananSekarang = pesananAktif;
        setLayout(new BorderLayout(10, 10));

        // 1. Inisialiasi tabel
        String[] namaKolom = {"Kode", "Nama Obat", "Harga", "Jumlah", "Subtotal"};
        modelTabel = new DefaultTableModel(namaKolom, 0) {
            @Override
            public boolean isCellEditable(int baris, int kolom) {
                return kolom == 3;
            }
        };
        
        tabelKeranjang = new JTable(modelTabel);
        tabelKeranjang.setShowGrid(true);
        tabelKeranjang.setGridColor(Color.LIGHT_GRAY); 
        tabelKeranjang.setIntercellSpacing(new Dimension(1, 1));
        JScrollPane scrollPane = new JScrollPane(tabelKeranjang);
        add(scrollPane, BorderLayout.CENTER);
           
        totalLabel = new JLabel("Total Belanja: Rp 0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("<< Kembali Belanja");
        backButton.setBackground(new Color(30, 90, 80));
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setPreferredSize(new Dimension(120, 20));

        backButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                parentFrame.showPanel(MainFrame.BeliObat_Panel);
            }
        });
       
        removeButton = new JButton("Hapus Item");
        removeButton.setBackground(new Color(180, 50, 50));
        removeButton.setForeground(Color.WHITE);
        removeButton.setOpaque(true);
        removeButton.setBorderPainted(false);
        removeButton.setPreferredSize(new Dimension(100, 20));

        checkoutButton = new JButton("CheckOut Item >>");
        checkoutButton.setBackground(new Color(46, 139, 87));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setOpaque(true);
        checkoutButton.setBorderPainted(false);
        checkoutButton.setPreferredSize(new Dimension(120, 20));

        actionPanel.add(backButton);
        actionPanel.add(totalLabel);
        actionPanel.add(removeButton);
        actionPanel.add(checkoutButton);

        add(actionPanel, BorderLayout.SOUTH);

        //Memuat Data
        loadKeranjangData();
        
        //tambahkan Event Listeners (Klik)
        addListeners();
    }

    private void loadKeranjangData() {
        modelTabel.setRowCount(0);
        if (pesananSekarang.getItems() != null) {
            for (ItemKeranjang item : pesananSekarang.getItems()){
                Object[] rowData = {
                    item.getObat().getKode(),
                    item.getObat().getNama(),
                    formatRupiah(item.getObat().getHarga()),
                    item.getJumlah(),
                    formatRupiah(item.getSubtotal())
                };
                modelTabel.addRow(rowData);
            }
        }

        totalLabel.setText(String.format("Total Belanja: Rp %.2f", pesananSekarang.getTotalObat()));
    }

    private void addListeners() {
        //hapus Item
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                removeItem();
            }
        });

        //checkout Item
        checkoutButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (pesananSekarang.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(KeranjangPanel.this, "Keranjang Anda kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            parentFrame.goToCheckoutPanel();
            }
        });
    }

    private void removeItem() {
        int selectedRow = tabelKeranjang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih item yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //ambil kode obat dr kolom pertama
        String kodeObat = (String) modelTabel.getValueAt(selectedRow, 0);

        pesananSekarang.removeItem(kodeObat);
        
        loadKeranjangData();
        JOptionPane.showMessageDialog(this, "Item berhasil dihapus dari keranjang.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    public void refreshPanel(Pesanan pesananTerbaru) {
    this.pesananSekarang = pesananTerbaru; 
    loadKeranjangData();
    }
    
    private String formatRupiah(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("in", "ID")); // Locale Indonesia
        return nf.format(amount).replace("Rp", "Rp "); // Menambahkan spasi setelah Rp (opsional)
    }
}
