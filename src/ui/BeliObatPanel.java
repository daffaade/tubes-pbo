package ui;

import data.DataManager;
import model.ItemKeranjang;
import model.Obat;
import model.Pesanan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.awt.BorderLayout;
import java.awt.Color; 
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class BeliObatPanel extends JPanel{

    private MainFrame mainFrame;
    private JButton checkoutButton;
    private JTable obatTabel;
    private DefaultTableModel modelTabel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addtoCartButton;
    private JTextField quantityField;
    private Pesanan pesananSekarang;

    public Pesanan getPesananSekarang() {
        return pesananSekarang;
    }   

    public BeliObatPanel(MainFrame mainFrame, Pesanan pesananAktif) {
        this.mainFrame = mainFrame;
        this.pesananSekarang = pesananAktif;
        setLayout(new BorderLayout(10, 10));

        // 1. Tabel Obat
        String[] namaKolom = {"Kode", "Nama Obat", "Harga", "Stok", "Kategori"};
        modelTabel = new DefaultTableModel(namaKolom, 0) {
            @Override
            public boolean isCellEditable(int baris, int kolom) {
                return false;
            }
        };

        obatTabel = new JTable(modelTabel);
        obatTabel.setShowGrid(true);
        obatTabel.setGridColor(Color.LIGHT_GRAY);
        obatTabel.setIntercellSpacing(new Dimension(1, 3));

        JScrollPane scrollPane = new JScrollPane(obatTabel);

        add(scrollPane, BorderLayout.CENTER);

        // Field dan tombol pencari
        searchField = new JTextField(10);
        searchButton = new JButton("Cari Obat");
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setPreferredSize(new Dimension(100, 20));

        // Field dan tombol tambah ke keranjang
        quantityField = new JTextField("1", 2);//defaultnya udh ada jumlah 1
        addtoCartButton = new JButton("Tambah ke Keranjang");
        addtoCartButton.setBackground(new Color(70, 130, 180));
        addtoCartButton.setForeground(Color.WHITE);
        addtoCartButton.setOpaque(true);
        addtoCartButton.setBorderPainted(false);
        addtoCartButton.setPreferredSize(new Dimension(120, 20));

        checkoutButton = new JButton("Lihat Keranjang & Checkout Sekarang");

        checkoutButton.setBackground(new Color(46, 139, 87));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setOpaque(true);
        checkoutButton.setBorderPainted(false);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(checkoutButton);
        add(controlPanel, BorderLayout.SOUTH);

        // 2. Panel Pencarian Obat
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(30, 90, 80));

        searchPanel.add(new JLabel("Cari Nama Obat:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Jumlah:"));
        searchPanel.add(quantityField);
        searchPanel.add(addtoCartButton);

        // 3. Tambahkan ke Layout Utama
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // 4. Muat Data Awal
        loadTableData(DataManager.getInstance().getDaftarObat());
        
        // 5. Tambahkan Event Listeners (Logika Klik)
        addListeners();
    }

    private void loadTableData(List<Obat> daftarObat) {
        modelTabel.setRowCount(0);
        for (Obat obat : daftarObat) {
            Object[] rowData = {
                obat.getKode(), 
                obat.getNama(), 
                formatRupiah(obat.getHarga()), 
                obat.getStok(), 
                obat.getKategori()
            };
            modelTabel.addRow(rowData);
        }
    }
    
    private void addListeners() {
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                performSearch();
            }
        });

        searchField.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                performSearch();
            }
        }); 
        
        addtoCartButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addItemToCart();
            }
        });

        checkoutButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (pesananSekarang.getItems() == null || pesananSekarang.getItems().isEmpty()) {
                    JOptionPane.showMessageDialog(BeliObatPanel.this, "Keranjang kosong! Tambahkan obat terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            mainFrame.showPanel(MainFrame.KERANJANG); 
            }
        });
    }

    private void performSearch() {
        String keywordLower = searchField.getText().toLowerCase();
        
        List<Obat> hasilCari = new ArrayList<>();
        List<Obat> allObat = DataManager.getInstance().getDaftarObat();
        
        for (Obat o : allObat) {
            String namaObatLower = o.getNama().toLowerCase();
            String kategoriObatLower = o.getKategori().toLowerCase();
            
            if (namaObatLower.contains(keywordLower) || kategoriObatLower.contains(keywordLower)) {   
                hasilCari.add(o); 
            }
        }
        loadTableData(hasilCari);
    }

    private void addItemToCart() {
        int selectedRow = obatTabel.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih obat yang ingin dibeli terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1. Ambil Kode Obat dari kolom pertama
            String kodeObat = (String) modelTabel.getValueAt(selectedRow, 0);
            
            // 2. Ambil jumlah
            int jumlah = Integer.parseInt(quantityField.getText().trim());
            
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 3. Ambil Objek Obat dari DataManager
            Obat obatDipilih = DataManager.getInstance().cariObat(kodeObat);
            
            if (obatDipilih != null) {
                if (obatDipilih.getStok() < jumlah) {
                    JOptionPane.showMessageDialog(this, "Stok " + obatDipilih.getNama() + " tidak cukup. Stok tersedia: " + obatDipilih.getStok(), "Error Stok", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 4. Buat Item Keranjang dan Tambahkan ke Pesanan
                ItemKeranjang itemBaru = new ItemKeranjang(obatDipilih, jumlah);
                pesananSekarang.addItem(itemBaru);
                
                JOptionPane.showMessageDialog(this, jumlah + "x " + obatDipilih.getNama() + " berhasil ditambahkan ke keranjang.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                loadTableData(DataManager.getInstance().getDaftarObat()); 

            } else {
                JOptionPane.showMessageDialog(this, "Obat tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input jumlah tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatRupiah(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID")); 
        return nf.format(amount).replace("Rp", "Rp ");
    }

    public void refreshObatData() {
        loadTableData(DataManager.getInstance().getDaftarObat());
    }
}
