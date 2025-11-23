package ui;

import data.DataManager;
import model.ItemKeranjang;
import model.Obat;
import model.Pesanan;

import java.awt.BorderLayout;
import java.awt.FlowLayout; // Asumsi Anda memiliki model Pesanan
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List; // Asumsi Anda memiliki model ItemKeranjang
import java.util.Locale;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Dimension;

public class BeliObatPanel extends JPanel{
    private MainFrame parentFrame;
    private JButton checkoutButton;
    private JTable obatTabel;
    private DefaultTableModel modelTabel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addtoCartButton;
    private JTextField quantityField; //input jumlah obat
    private Pesanan pesananSekarang; // Pesanan yang sedang dibuat saat ini / yg sedang aktif

    public Pesanan getPesananSekarang() {
        return pesananSekarang;
    }   

    public BeliObatPanel(MainFrame parent, Pesanan pesananAktif) {
        this.parentFrame =parent; // menyimpan ke main frame
        this.pesananSekarang = pesananAktif;
        setLayout(new BorderLayout(10, 10));

        // 1. Inisialisasi komponen tabel GUI Obat
        String[] namaKolom = {"Kode", "Nama Obat", "Harga", "Stok", "Kategori"};
        modelTabel = new DefaultTableModel(namaKolom, 0) {
            @Override
            public boolean isCellEditable(int baris, int kolom) {
                return false; // membuat tabel tidak dapat diedit langsung
            }
        };

        obatTabel = new JTable(modelTabel);
        obatTabel.setShowGrid(true);
        obatTabel.setGridColor(Color.LIGHT_GRAY); // Gunakan warna abu-abu muda
        obatTabel.setIntercellSpacing(new Dimension(1, 1)); // Memastikan jarak antar sel

        JScrollPane scrollPane = new JScrollPane(obatTabel);

        add(scrollPane, BorderLayout.CENTER);

        //Field dan tombol pencari
        searchField = new JTextField(10);
        searchButton = new JButton("Cari Obat");
        searchButton.setBackground(new Color(70, 130, 180)); // Biru baja
        searchButton.setForeground(Color.WHITE);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setPreferredSize(new Dimension(100, 20));

        //Field dan tombol tambah ke keranjang
        quantityField = new JTextField("1", 2);//defaultnya udh ada jumlah 1
        addtoCartButton = new JButton("Tambah ke Keranjang");
        addtoCartButton.setBackground(new Color(70, 130, 180)); // Biru baja
        addtoCartButton.setForeground(Color.WHITE);
        addtoCartButton.setOpaque(true);
        addtoCartButton.setBorderPainted(false);
        addtoCartButton.setPreferredSize(new Dimension(120, 20));

        checkoutButton = new JButton("Lihat Keranjang & Checkout Sekarang");

        checkoutButton.setBackground(new Color(46, 139, 87)); // Hijau gelap
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setOpaque(true);
        checkoutButton.setBorderPainted(false);
        // tambahkan ke panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(checkoutButton);
        add(controlPanel, BorderLayout.SOUTH);

        // 2. Panel Pencarian Obat
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(30, 90, 80)); // WARNA_HEADER

        searchPanel.add(new JLabel("Cari Nama Obat:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Jumlah:"));
        searchPanel.add(quantityField);
        searchPanel.add(addtoCartButton);


        // 3. Tambahkan ke Layout Utama
        add(searchPanel, BorderLayout.NORTH); // Panel Pencarian di atas
        add(scrollPane, BorderLayout.CENTER); // Tabel di tengah
        
        // 4. Muat Data Awal
        loadTableData(DataManager.getInstance().getDaftarObat());
        
        // --- 5. Tambahkan Event Listeners (Logika Klik) ---
        addListeners();
    }

    // === Metode Controller (Logika)
    private void loadTableData(List<Obat> daftarObat) {
        modelTabel.setRowCount(0); // Bersihkan baris lama
        for (Obat obat : daftarObat) {
            // Tambahkan baris baru ke tabel model
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
        // Logika Pencarian
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
        }); // Juga bisa mencari saat menekan Enter
        
        // Logika Tambah ke Keranjang
        addtoCartButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addItemToCart();
            }
        });

        // Logika Checkout
        checkoutButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (pesananSekarang.getItems() == null || pesananSekarang.getItems().isEmpty()) {
                    JOptionPane.showMessageDialog(BeliObatPanel.this, "Keranjang kosong! Tambahkan obat terlebih dahulu.", 
                                                "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            // Panggil metode navigasi di MainFrame
            parentFrame.showPanel(MainFrame.Keranjang_Panel); // Gunakan goToCheckoutPanel
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
            
            // Logika Filter
            if (namaObatLower.contains(keywordLower) || 
                kategoriObatLower.contains(keywordLower)) {
                
                hasilCari.add(o); // Menambahkan ke list hasil
            }
        }
        loadTableData(hasilCari);
    }
    
    private void addItemToCart() {
        int selectedRow = obatTabel.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih obat yang ingin dibeli terlebih dahulu.", 
                                          "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1. Ambil Kode Obat dari kolom pertama
            String kodeObat = (String) modelTabel.getValueAt(selectedRow, 0);
            
            // 2. Ambil jumlah
            int jumlah = Integer.parseInt(quantityField.getText().trim());
            
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0.", 
                                              "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 3. Ambil Objek Obat dari DataManager
            Obat obatDipilih = DataManager.getInstance().cariObat(kodeObat);
            
            if (obatDipilih != null) {
                if (obatDipilih.getStok() < jumlah) {
                    JOptionPane.showMessageDialog(this, "Stok " + obatDipilih.getNama() + " tidak cukup. Stok tersedia: " + obatDipilih.getStok(), 
                                                  "Error Stok", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 4. Buat Item Keranjang dan Tambahkan ke Pesanan
                ItemKeranjang itemBaru = new ItemKeranjang(obatDipilih, jumlah);
                pesananSekarang.addItem(itemBaru); // Anda perlu menambahkan metode addItem() di Pesanan.java
                
                JOptionPane.showMessageDialog(this, jumlah + "x " + obatDipilih.getNama() + " berhasil ditambahkan ke keranjang.", 
                                              "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                // (Opsional) Refresh tampilan tabel untuk update stok langsung
                loadTableData(DataManager.getInstance().getDaftarObat()); 

            } else {
                JOptionPane.showMessageDialog(this, "Obat tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input jumlah tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tambahkan metode ini di BeliObatPanel atau KeranjangPanel:
    private String formatRupiah(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("in", "ID")); // Locale Indonesia
        return nf.format(amount).replace("Rp", "Rp "); // Menambahkan spasi setelah Rp (opsional)
    }

    public  void refreshObatData() {
        loadTableData(DataManager.getInstance().getDaftarObat());
    }
}
