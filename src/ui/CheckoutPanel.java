package ui;

import data.DataManager;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.Border;
import model.Pesanan;
import model.Peta;

public class CheckoutPanel extends JPanel {

    private MainFrame parentFrame;
    private Pesanan finalPesanan;
    private final double BiayaPerKM = 3000.0;
    private final double BiayaPengemasan = 2000.0; // Biaya Pengemasan tetap
    
    // Komponen Input
    private JComboBox<String> jenisAmbilCombo;
    private JTextField lokasiDisplayField; 
    private JButton pilihLokasiButton;
    private JTextField alamatDetail; 
    private JComboBox<String> pembayaranCombo;
    
    // Komponen Display Biaya
    private JLabel totalObatLabel;
    private JLabel biayaKemasanLabel;
    private JLabel ongkirLabel;
    private JLabel totalBayarLabel;
    private JButton completeOrderButton;

    public CheckoutPanel(MainFrame parent, Pesanan pesananCheckout) {
        this.parentFrame = parent; // menyimpan referensi ke MainFrame
        this.finalPesanan = pesananCheckout;
        setLayout(new BorderLayout(15, 15));
        
        // 1. Panel Formulir & Detail Biaya (Pusat)
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // Kiri: Input Pengiriman/Pembayaran
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
        
        // Panel untuk Display Lokasi dan Tombol
        JPanel lokasiInputPanel = new JPanel(new BorderLayout(5, 0));
        lokasiDisplayField = new JTextField("--- Belum dipilih ---");
        lokasiDisplayField.setEditable(false);
        pilihLokasiButton = new JButton("Pilih Lokasi");

        pilihLokasiButton.setBackground(new Color(150, 150, 150)); // Abu-abu
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
        
        centerPanel.add(inputPanel);

        // Kanan: Rincian Biaya
        JPanel detailPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Rincian Biaya"));
        
        totalObatLabel = new JLabel(formatRupiah(finalPesanan.getTotalObat()));
        biayaKemasanLabel = new JLabel("Rp 0.00"); // Akan diupdate
        ongkirLabel = new JLabel("Rp 0.00"); // Akan diupdate
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
        

        centerPanel.add(detailPanel, BorderLayout.NORTH);
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // 2. Tombol Selesaikan Pesanan (Selatan)
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("<< Kembali Keranjang");

        backButton.setBackground(new Color(30, 90, 80)); // Hijau Tua
        backButton.setForeground(Color.WHITE); 
        backButton.setOpaque(true); // Wajib agar background terlihat!
        backButton.setBorderPainted(false);

        southPanel.add(backButton);

        backButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                parentFrame.showPanel(MainFrame.Keranjang_Panel);
            }
        });

        completeOrderButton = new JButton("SELESAIKAN PESANAN & BAYAR");
        completeOrderButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        completeOrderButton.setPreferredSize(new Dimension(250, 40));

        completeOrderButton.setBackground(new Color(46, 139, 87)); // Hijau gelap
        completeOrderButton.setForeground(Color.WHITE); // Teks putih
        completeOrderButton.setOpaque(true); // Pastikan warna background terlihat
        completeOrderButton.setBorderPainted(false);

        southPanel.add(completeOrderButton);
        add(southPanel, BorderLayout.SOUTH);

        completeOrderButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                processCheckout();
            }
        });
        

        // Initial update
        updateBiayaPengiriman(); 
    }

    private void bukaPilihLokasiDialog() {
        if ("Antar (Delivery)".equals(jenisAmbilCombo.getSelectedItem())) {
        // 1. Buat dan Tampilkan Dialog
        PetaPanel dialog = new PetaPanel(
            (Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true); // Memblokir hingga dialog ditutup

        // 2. Ambil Hasil
        Peta lokasiDipilih = dialog.getLokasiTerpilih();
        
        if (lokasiDipilih != null) {
            // Simpan lokasi terpilih ke Pesanan (kita harus menyimpan objek peta ini, 
            // tapi karena Pesanan hanya menyimpan String alamat, kita simpan string-nya saja dulu)

            // Simpan JARAK ke Pesanan (untuk Ongkir)
            finalPesanan.setOngkir(lokasiDipilih.getJarak() * BiayaPerKM);
            
            // Update Tampilan
            lokasiDisplayField.setText(lokasiDipilih.getNamaLokasi() + 
                                       " (" + lokasiDipilih.getJarak() + " KM)");
        } else {
            // Jika dialog dicancel, set kembali ke default/nol
            finalPesanan.setOngkir(0);
            lokasiDisplayField.setText("--- Belum dipilih ---");
        }
        
        // Perbarui total biaya setelah memilih lokasi
        updateBiayaPengiriman(); 
        }
    }

    // Tambahkan metode ini di BeliObatPanel atau KeranjangPanel:
    private String formatRupiah(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("in", "ID")); // Locale Indonesia
        return nf.format(amount).replace("Rp", "Rp "); // Menambahkan spasi setelah Rp (opsional)
    }

    private void updateBiayaPengiriman() {
        String jenis = (String) jenisAmbilCombo.getSelectedItem();
        double ongkirBaru = 0.0;
        double biayaKemasanBaru = 0.0;
        
        boolean isDelivery = "Antar (Delivery)".equals(jenis);
        String ongkirDisplay = formatRupiah(0.0);

        pilihLokasiButton.setEnabled(isDelivery);
        alamatDetail.setEnabled(isDelivery);

        if (isDelivery) {
            // --- LOGIKA UNTUK PENGANTARAN (DELIVERY) ---
            biayaKemasanBaru = BiayaPengemasan;
            
            // Ambil Ongkir dari data yang sudah tersimpan di Pesanan/Tampilan
            // Jika belum dipilih, Ongkir tetap 0
            if (lokasiDisplayField.getText().contains("KM") || finalPesanan.getOngkir() > 0) {
                // Jika lokasi sudah dipilih, gunakan nilai ongkir yang sudah di-set di Pesanan
                ongkirBaru = finalPesanan.getOngkir(); 

                // Dapatkan jarak (KM) untuk display rincian
                double jarak = ongkirBaru / BiayaPerKM; 
                ongkirDisplay = formatRupiah(ongkirBaru) + String.format(" (%.0f KM x %s)", jarak, formatRupiah(BiayaPerKM).trim());

            } else {
                // Lokasi BELUM dipilih atau direset. Set teks default.
                lokasiDisplayField.setText("--- Belum dipilih ---");
                ongkirBaru = 0.0;
                ongkirDisplay = formatRupiah(0.0);
            }

        } else {
            // --- LOGIKA UNTUK AMBIL LANGSUNG ---
            // Reset Ongkir & Biaya Pengemasan

            // 1. Reset Model data pengiriman
            finalPesanan.setOngkir(0);
            finalPesanan.setBiayaPengemasan(0);

            // 2. Update Tampilan
            lokasiDisplayField.setText("--- Ambil Langsung ---");
            ongkirBaru = 0.0;
            biayaKemasanBaru = 0.0;
            ongkirDisplay = formatRupiah(0.0);
        }
        
        // Final Update Model
        finalPesanan.setOngkir(ongkirBaru); 
        finalPesanan.setBiayaPengemasan(biayaKemasanBaru);
        
        // Update Tampilan
        ongkirLabel.setText(ongkirDisplay);
        biayaKemasanLabel.setText(formatRupiah(biayaKemasanBaru));
        totalObatLabel.setText(formatRupiah(finalPesanan.getTotalObat()));

        // Perhitungan Total Bayar (Total Obat + Ongkir + Kemasan)
        double totalAkhir = finalPesanan.getTotalObat() + finalPesanan.getOngkir() + finalPesanan.getBiayaPengemasan();
        finalPesanan.setTotalBayar(totalAkhir);
        totalBayarLabel.setText(formatRupiah(totalAkhir));
    }

    private void processCheckout() {
        if (finalPesanan.getItems() == null || finalPesanan.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang belanja kosong! Tidak bisa checkout.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Dapatkan data dari GUI
        String jenisAmbilPilihan = (String) jenisAmbilCombo.getSelectedItem();
        String metodeBayar = (String) pembayaranCombo.getSelectedItem();
        
        String jenisAmbilFinal = jenisAmbilPilihan.equals("Antar (Delivery)") ? "antar" : "ambil";
        String alamatFinal;

        // Pastikan total biaya sudah diupdate terakhir kali sebelum validasi/simpan
        updateBiayaPengiriman();

        if (jenisAmbilFinal.equals("antar")) {
            // mengecek apakah lokasi dan alamat detail sudah dipilih
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


        // 2. Finalisasi Pesanan di Model
        finalPesanan.setJenisAmbil(jenisAmbilFinal);
        finalPesanan.setAlamat(alamatFinal);
        // finalPesanan.setMetodePembayaran(metodeBayar.contains("COD") ? "cod" : "online");
        
        // Pastikan total biaya sudah diupdate terakhir kali
        updateBiayaPengiriman(); 

        // 3. Simpan Pesanan ke DataManager
        // A - Sebelum menyimpan, generate nomor pesanan baru
        finalPesanan.setNoPesanan(DataManager.getInstance().generateNoPesanan());

        // B - Simpan data yang akan ditampilkan di konfirmasi ke variabel lokal
        String noPesananFinal = finalPesanan.getNoPesanan(); 
        double totalBayarFinal = finalPesanan.getTotalBayar(); // Nilai ini sudah benar dari updateBiayaPengiriman()

        // C - Simpan pesanan ke DataManager
        DataManager.getInstance().simpanPesanan(finalPesanan); 

        // 4. Reset Keranjang di Model untuk transaksi baru
        // >>> KOREKSI: KOSONGKAN KERANJANG UNTUK PESANAN BARU
        finalPesanan.getItems().clear(); // Hapus semua item dari list keranjang
        // Anda juga perlu mereset semua biaya di finalPesanan menjadi 0, 
        // agar Pesanan ini siap digunakan kembali atau diganti dengan Pesanan baru.
        finalPesanan.setOngkir(0.0);
        finalPesanan.setBiayaPengemasan(0.0);
        finalPesanan.setTotalBayar(0.0);

        // 5. Konfirmasi
        JOptionPane.showMessageDialog(this, 
            "Pesanan berhasil diselesaikan!\nNomor Pesanan: " + noPesananFinal + 
            "\nTotal Bayar: Rp " + formatRupiah(totalBayarFinal) +
            "\nMohon tunggu konfirmasi lebih lanjut.", 
            "Pesanan Sukses", JOptionPane.INFORMATION_MESSAGE);

        // 5. Pindah Panel & Refresh
        // Dapatkan instance BeliObatPanel dan panggil refreshTable()
        parentFrame.getBeliObatPanel().refreshObatData(); // Anda harus membuat getter di MainFrame
        parentFrame.showPanel(MainFrame.BeliObat_Panel);
    }

    // ui/CheckoutPanel.java (TAMBAHKAN INI)
    public void refreshData() {
        // Reset tampilan input ke default saat pertama kali dibuka
        jenisAmbilCombo.setSelectedItem("Ambil Langsung");
        lokasiDisplayField.setText("--- Belum dipilih ---"); 
        finalPesanan.setOngkir(0); // Reset ongkir di model
        alamatDetail.setText("");
        
        // Pastikan label-label biaya diperbarui dengan data keranjang saat ini
        updateBiayaPengiriman(); 
    }
}