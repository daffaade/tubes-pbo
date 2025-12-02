package ui;

import data.DataManager;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import model.Pesanan;
import model.Peta;

public class CheckoutPanel extends JPanel {

    private MainFrame parentFrame;
    private Pesanan finalPesanan;
    private final double BiayaPerKM = 3000.0;
    private final double BiayaPengemasan = 2000.0;
    
    //Input
    private JComboBox<String> jenisAmbilCombo;
    private JTextField lokasiDisplayField; 
    private JButton pilihLokasiButton;
    private JTextField alamatDetail; 
    private JComboBox<String> pembayaranCombo;
    
    //Display Biaya
    private JLabel totalObatLabel;
    private JLabel biayaKemasanLabel;
    private JLabel ongkirLabel;
    private JLabel totalBayarLabel;
    private JButton completeOrderButton;

    public CheckoutPanel(MainFrame parent, Pesanan pesananCheckout) {
        this.parentFrame = parent;
        this.finalPesanan = pesananCheckout;
        setLayout(new BorderLayout(15, 15));
        
        //Panel Formulir & Detail Biaya
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        //Input Pengiriman/Pembayaran (sblh kiri)
        JPanel inputPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Pilihan Checkout"));
        
        inputPanel.add(new JLabel("1. Pilih Jenis Pengambilan:"));
        jenisAmbilCombo = new JComboBox<>(new String[]{"Ambil Langsung", "Antar (Delivery)"});
        jenisAmbilCombo.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                updateBiayaPengiriman();
            }
        });

        inputPanel.add(jenisAmbilCombo);
        
        //lokasi
        JPanel lokasiInputPanel = new JPanel(new BorderLayout(5, 0));
        lokasiDisplayField = new JTextField("--- Belum dipilih ---");
        lokasiDisplayField.setEditable(false);
        pilihLokasiButton = new JButton("Pilih Lokasi");

        pilihLokasiButton.setBackground(new Color(150, 150, 150));
        pilihLokasiButton.setForeground(Color.BLACK); 
        pilihLokasiButton.setOpaque(true);
        pilihLokasiButton.setBorderPainted(false);

        pilihLokasiButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                bukaPilihLokasiDialog();
            }
        });

        lokasiInputPanel.add(lokasiDisplayField, BorderLayout.CENTER);
        lokasiInputPanel.add(pilihLokasiButton, BorderLayout.EAST);

        inputPanel.add(new JLabel("2. Lokasi Pengiriman:"));
        inputPanel.add(lokasiInputPanel);
        
        inputPanel.add(new JLabel("   Alamat Detail:"));
        alamatDetail = new JTextField();
        inputPanel.add(alamatDetail);
        
        inputPanel.add(new JLabel("3. Pilih Metode Pembayaran:"));
        pembayaranCombo = new JComboBox<>(new String[]{"Transfer Bank", "QRIS", "E-Wallet"});
        inputPanel.add(pembayaranCombo);

        //Rincian Biaya (sblh kanan)
        JPanel detailPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Rincian Biaya"));
        
        totalObatLabel = new JLabel(formatRupiah(finalPesanan.getTotalObat()));
        biayaKemasanLabel = new JLabel("Rp 0.00");
        ongkirLabel = new JLabel("Rp 0.00");
        totalBayarLabel = new JLabel(formatRupiah(finalPesanan.getTotalBayar()));
        totalBayarLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        detailPanel.add(new JLabel("Total Harga Obat:"));
        detailPanel.add(totalObatLabel);
        detailPanel.add(new JLabel("Biaya Pengemasan:"));
        detailPanel.add(biayaKemasanLabel);
        detailPanel.add(new JLabel("Ongkos Kirim:"));
        detailPanel.add(ongkirLabel);
        
        detailPanel.add(new JLabel("-----------------"));
        detailPanel.add(new JLabel("-----------------"));

        detailPanel.add(new JLabel("TOTAL AKHIR:"));
        detailPanel.add(totalBayarLabel);
        
        centerPanel.add(inputPanel);
        centerPanel.add(detailPanel);
    
        add(centerPanel, BorderLayout.CENTER);

        //selesaikan pesanan (sblh kanan bwh)
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("<< Kembali Keranjang");

        backButton.setBackground(new Color(30, 90, 80));
        backButton.setForeground(Color.WHITE); 
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);

        southPanel.add(backButton);

        backButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                parentFrame.showPanel(MainFrame.Keranjang_Panel);
            }
        });

        completeOrderButton = new JButton("SELESAIKAN PESANAN & BAYAR");
        completeOrderButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        completeOrderButton.setPreferredSize(new Dimension(250, 30));

        completeOrderButton.setBackground(new Color(46, 139, 87));
        completeOrderButton.setForeground(Color.WHITE);
        completeOrderButton.setOpaque(true);
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
        //buka peta
        PetaPanel dialog = new PetaPanel(
            (Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);

        //ambil lokasi
        Peta lokasiDipilih = dialog.getLokasiTerpilih();
        
        if (lokasiDipilih != null) {
            //simpan jarak
            finalPesanan.setOngkir(lokasiDipilih.getJarak() * BiayaPerKM);
            
            lokasiDisplayField.setText(lokasiDipilih.getNamaLokasi() + 
                                       " (" + lokasiDipilih.getJarak() + " KM)");
        } else {
            //jika batal pilih lokasi, reset ongkir
            finalPesanan.setOngkir(0);
            lokasiDisplayField.setText("--- Belum dipilih ---");
        }
        
        updateBiayaPengiriman(); 
        }
    }

    private String formatRupiah(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
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
            //jika ambil langsung
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

        //sebelum menyimpan, generate nomor pesanan baru
        finalPesanan.setNoPesanan(DataManager.getInstance().generateNoPesanan());

        //impan data yang akan ditampilkan di konfirmasi ke variabel lokal
        String noPesananFinal = finalPesanan.getNoPesanan(); 
        double totalBayarFinal = finalPesanan.getTotalBayar(); 

        //simpan pesanan ke DataManager
        DataManager.getInstance().simpanPesanan(finalPesanan); 

        //Reset Keranjang di Model untuk transaksi baru
        finalPesanan.getItems().clear();
        finalPesanan.setOngkir(0.0);
        finalPesanan.setBiayaPengemasan(0.0);

        //Konfirmasi
        JOptionPane.showMessageDialog(this, 
            "Pesanan berhasil diselesaikan!\nNomor Pesanan: " + noPesananFinal + 
            "\nTotal Bayar: " + formatRupiah(totalBayarFinal) +
            "\nMohon tunggu konfirmasi lebih lanjut.", 
            "Pesanan Sukses", JOptionPane.INFORMATION_MESSAGE);

        //Pindah Panel & Refresh
        parentFrame.getBeliObatPanel().refreshObatData();
        parentFrame.showPanel(MainFrame.BeliObat_Panel);
    }

    public void refreshData() {
        jenisAmbilCombo.setSelectedItem("Ambil Langsung");
        lokasiDisplayField.setText("--- Belum dipilih ---"); 
        finalPesanan.setOngkir(0);
        alamatDetail.setText("");
        
        updateBiayaPengiriman(); 
    }
}