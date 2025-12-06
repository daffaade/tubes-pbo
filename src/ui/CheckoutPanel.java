package ui;

import data.DataManager;
import model.Pesanan;
import model.Peta;

import java.text.NumberFormat;
import java.util.Locale;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;

import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class CheckoutPanel extends JPanel {

    private MainFrame mainFrame;
    private Pesanan finalPesanan;
    private final double BiayaPerKM = 3000.0;
    private final double BiayaPengemasan = 2000.0;
    
    private JComboBox<String> jenisAmbilCombo;
    private JTextField lokasiDisplayField; 
    private JButton pilihLokasiButton;
    private JTextField alamatDetail; 
    private JComboBox<String> pembayaranCombo;
    
    private JLabel totalObatLabel;
    private JLabel biayaKemasanLabel;
    private JLabel ongkirLabel;
    private JLabel totalBayarLabel;
    private JButton completeOrderButton;

    public CheckoutPanel(MainFrame mainFrame, Pesanan pesananCheckout) {
        this.mainFrame = mainFrame;
        this.finalPesanan = pesananCheckout;
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(240, 240, 240));
        
        // Panel Formulir & Detail Biaya
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        centerPanel.setBackground(new Color(240, 240, 240));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Input Pengiriman/Pembayaran (sblh kiri)
        JPanel inputPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "Pilihan Checkout",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(70, 130, 180)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel label1 = new JLabel("1. Pilih Jenis Pengambilan:");
        label1.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(label1);
        
        jenisAmbilCombo = new JComboBox<>(new String[]{"Ambil Langsung", "Antar (Delivery)"});
        jenisAmbilCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        jenisAmbilCombo.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                updateBiayaPengiriman();
            }
        });

        inputPanel.add(jenisAmbilCombo);
        
        // Lokasi
        JPanel lokasiInputPanel = new JPanel(new BorderLayout(5, 0));
        lokasiInputPanel.setBackground(Color.WHITE);
        lokasiDisplayField = new JTextField("--- Belum dipilih ---");
        lokasiDisplayField.setEditable(false);
        lokasiDisplayField.setFont(new Font("Arial", Font.PLAIN, 13));
        
        pilihLokasiButton = new JButton("Pilih Lokasi");
        pilihLokasiButton.setBackground(new Color(70, 130, 180));
        pilihLokasiButton.setForeground(Color.WHITE);
        pilihLokasiButton.setFont(new Font("Arial", Font.BOLD, 12));
        pilihLokasiButton.setFocusPainted(false);
        pilihLokasiButton.setBorderPainted(false);

        pilihLokasiButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                bukaPilihLokasiDialog();
            }
        });

        lokasiInputPanel.add(lokasiDisplayField, BorderLayout.CENTER);
        lokasiInputPanel.add(pilihLokasiButton, BorderLayout.EAST);

        JLabel label2 = new JLabel("2. Lokasi Pengiriman:");
        label2.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(label2);
        inputPanel.add(lokasiInputPanel);
        
        JLabel label3 = new JLabel("   Alamat Detail:");
        label3.setFont(new Font("Arial", Font.PLAIN, 13));
        inputPanel.add(label3);
        alamatDetail = new JTextField();
        alamatDetail.setFont(new Font("Arial", Font.PLAIN, 13));
        inputPanel.add(alamatDetail);
        
        JLabel label4 = new JLabel("3. Pilih Metode Pembayaran:");
        label4.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(label4);
        pembayaranCombo = new JComboBox<>(new String[]{"Transfer Bank", "QRIS", "E-Wallet"});
        pembayaranCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        inputPanel.add(pembayaranCombo);

        // Rincian Biaya (sblh kanan)
        JPanel detailPanel = new JPanel(new GridLayout(5, 2, 5, 10));
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 150, 100), 2),
                "Rincian Biaya",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(60, 150, 100)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        totalObatLabel = new JLabel(formatRupiah(finalPesanan.getTotalObat()));
        totalObatLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        biayaKemasanLabel = new JLabel("Rp 0.00");
        biayaKemasanLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        ongkirLabel = new JLabel("Rp 0.00");
        ongkirLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        totalBayarLabel = new JLabel(formatRupiah(finalPesanan.getTotalBayar()));
        totalBayarLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalBayarLabel.setForeground(new Color(60, 150, 100));
        
        JLabel labelObat = new JLabel("Total Harga Obat:");
        labelObat.setFont(new Font("Arial", Font.PLAIN, 13));
        detailPanel.add(labelObat);
        detailPanel.add(totalObatLabel);
        
        JLabel labelKemasan = new JLabel("Biaya Pengemasan:");
        labelKemasan.setFont(new Font("Arial", Font.PLAIN, 13));
        detailPanel.add(labelKemasan);
        detailPanel.add(biayaKemasanLabel);
        
        JLabel labelOngkir = new JLabel("Ongkos Kirim:");
        labelOngkir.setFont(new Font("Arial", Font.PLAIN, 13));
        detailPanel.add(labelOngkir);
        detailPanel.add(ongkirLabel);
        
        JLabel separator1 = new JLabel("─────────────");
        separator1.setForeground(Color.GRAY);
        JLabel separator2 = new JLabel("─────────────");
        separator2.setForeground(Color.GRAY);
        detailPanel.add(separator1);
        detailPanel.add(separator2);

        JLabel labelTotal = new JLabel("TOTAL AKHIR:");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 14));
        detailPanel.add(labelTotal);
        detailPanel.add(totalBayarLabel);
        
        centerPanel.add(inputPanel);
        centerPanel.add(detailPanel);
    
        add(centerPanel, BorderLayout.CENTER);

        // Selesaikan pesanan (sblh kanan bwh)
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        southPanel.setBackground(new Color(240, 240, 240));
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        
        JButton backButton = new JButton("← Kembali ke Keranjang");
        backButton.setBackground(new Color(100, 100, 100));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setPreferredSize(new Dimension(200, 40));

        southPanel.add(backButton);

        backButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mainFrame.showPanel(MainFrame.KERANJANG);
            }
        });

        completeOrderButton = new JButton("SELESAIKAN PESANAN & BAYAR");
        completeOrderButton.setFont(new Font("Arial", Font.BOLD, 14));
        completeOrderButton.setPreferredSize(new Dimension(280, 40));
        completeOrderButton.setBackground(new Color(60, 150, 100));
        completeOrderButton.setForeground(Color.WHITE);
        completeOrderButton.setFocusPainted(false);
        completeOrderButton.setBorderPainted(false);

        southPanel.add(completeOrderButton);
        add(southPanel, BorderLayout.SOUTH);

        completeOrderButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                processCheckout();
            }
        });
        
        updateBiayaPengiriman(); 
    }

    private void bukaPilihLokasiDialog() {
        if ("Antar (Delivery)".equals(jenisAmbilCombo.getSelectedItem())) {
        // Buka peta
        PetaPanel dialog = new PetaPanel(
            (Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);

        // Ambil lokasi
        Peta lokasiDipilih = dialog.getLokasiTerpilih();
        
        if (lokasiDipilih != null) {
            // Simpan jarak
            finalPesanan.setOngkir(lokasiDipilih.getJarak() * BiayaPerKM);
            lokasiDisplayField.setText(lokasiDipilih.getNamaLokasi() + " (" + lokasiDipilih.getJarak() + " KM)");
        } else {
            // Jika batal pilih lokasi, reset ongkir
            finalPesanan.setOngkir(0);
            lokasiDisplayField.setText("--- Belum dipilih ---");
        }
        
        updateBiayaPengiriman(); 
        }
    }

    private String formatRupiah(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        return nf.format(amount).replace("Rp", "Rp ");
    }

    public void updateBiayaPengiriman() {
        String jenis = (String) jenisAmbilCombo.getSelectedItem();
        double ongkirBaru = 0.0;
        double biayaKemasanBaru = 0.0;
        
        boolean isDelivery = "Antar (Delivery)".equals(jenis);
        String ongkirDisplay = formatRupiah(0.0);

        pilihLokasiButton.setEnabled(isDelivery);
        alamatDetail.setEnabled(isDelivery);

        if (isDelivery) {
            biayaKemasanBaru = BiayaPengemasan;
            
            if (lokasiDisplayField.getText().contains("KM") || finalPesanan.getOngkir() > 0) {
                ongkirBaru = finalPesanan.getOngkir(); 

                double jarak = ongkirBaru / BiayaPerKM; 
                ongkirDisplay = formatRupiah(ongkirBaru) + String.format(" (%.0f KM x %s)", jarak, formatRupiah(BiayaPerKM).trim());

            } else {
                lokasiDisplayField.setText("--- Belum dipilih ---");
                ongkirBaru = 0.0;
                ongkirDisplay = formatRupiah(0.0);
            }

        } else {
    
            finalPesanan.setOngkir(0);
            finalPesanan.setBiayaPengemasan(0);

            lokasiDisplayField.setText("--- Ambil Langsung ---");
            ongkirBaru = 0.0;
            biayaKemasanBaru = 0.0;
            ongkirDisplay = formatRupiah(0.0);
        }
        
        finalPesanan.setOngkir(ongkirBaru); 
        finalPesanan.setBiayaPengemasan(biayaKemasanBaru);
        
        // Update Tampilan
        ongkirLabel.setText(ongkirDisplay);
        biayaKemasanLabel.setText(formatRupiah(biayaKemasanBaru));
        totalObatLabel.setText(formatRupiah(finalPesanan.getTotalObat()));

        // Perhitungan Total Bayar (Total Obat + Ongkir + Kemasan)
        double totalAkhir = finalPesanan.getTotalObat() + finalPesanan.getOngkir() + finalPesanan.getBiayaPengemasan();
        totalBayarLabel.setText(formatRupiah(totalAkhir));
    }

    private void processCheckout() {

        if (finalPesanan.getItems() == null || finalPesanan.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang belanja kosong! Tidak bisa checkout.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String jenisAmbilPilihan = (String) jenisAmbilCombo.getSelectedItem();
        String metodeBayar = (String) pembayaranCombo.getSelectedItem();
        
        String jenisAmbilFinal = jenisAmbilPilihan.equals("Antar (Delivery)") ? "antar" : "ambil";
        String alamatFinal;

        updateBiayaPengiriman();

        if (jenisAmbilFinal.equals("antar")) {
            if (!lokasiDisplayField.getText().contains("KM")) {
                JOptionPane.showMessageDialog(this, "Anda memilih pesanan Antar, mohon pilih lokasi pengiriman terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String lokasi = lokasiDisplayField.getText();
            String detail = alamatDetail.getText();

            if (detail.trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Alamat detail harus diisi untuk pengiriman!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                 return;
            }
            alamatFinal = lokasi + " - " + detail;
        } else {
            alamatFinal = "Diambil di Apotek";
        }

        finalPesanan.setJenisAmbil(jenisAmbilFinal);
        finalPesanan.setAlamat(alamatFinal);
        updateBiayaPengiriman(); 

        // Sebelum menyimpan, generate nomor pesanan baru
        finalPesanan.setNoPesanan(DataManager.getInstance().generateNoPesanan());

        // Simpan data yang akan ditampilkan di konfirmasi ke variabel lokal
        String noPesananFinal = finalPesanan.getNoPesanan(); 
        double totalBayarFinal = finalPesanan.getTotalBayar(); 

        // Simpan pesanan ke DataManager
        DataManager.getInstance().simpanPesanan(finalPesanan); 

        // Reset Keranjang di Model untuk transaksi baru
        finalPesanan.getItems().clear();
        finalPesanan.setOngkir(0.0);
        finalPesanan.setBiayaPengemasan(0.0);

        // Konfirmasi
        JOptionPane.showMessageDialog(this, 
            "Pesanan berhasil diselesaikan!\nNomor Pesanan: " + noPesananFinal + 
            "\nTotal Bayar: " + formatRupiah(totalBayarFinal) +
            "\nMohon tunggu konfirmasi lebih lanjut.", 
            "Pesanan Sukses", JOptionPane.INFORMATION_MESSAGE);

        // Pindah Panel & Refresh
        mainFrame.getBeliObatPanel().refreshObatData();
        mainFrame.showPanel(MainFrame.BELI_OBAT);
    }

    public void refreshData() {
        jenisAmbilCombo.setSelectedItem("Ambil Langsung");
        lokasiDisplayField.setText("--- Belum dipilih ---"); 
        finalPesanan.setOngkir(0);
        alamatDetail.setText("");
        
        updateBiayaPengiriman(); 
    }
}