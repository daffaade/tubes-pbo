package ui;

import data.DataManager;
import model.Konsultasi;
import model.Obat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class KonsultasiPanel extends JFrame {

    private DataManager dm = DataManager.getInstance();

    private JTextField fieldNama;
    private JComboBox<String> comboGejala;
    private JTextArea areaOutput;

    public KonsultasiPanel() {
        setTitle("Panel Konsultasi");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // PANEL ATAS
        JPanel panelInput = new JPanel();
        panelInput.setLayout(new GridLayout(6, 1, 5, 5));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelInput.add(new JLabel("Nama Pasien:"));
        fieldNama = new JTextField();
        panelInput.add(fieldNama);

        panelInput.add(new JLabel("Pilih Gejala:"));
        comboGejala = new JComboBox<>();

        // Mengambil gejala dari daftar obat
        Set<String> daftarGejala = new LinkedHashSet<>();
        for (Obat o : dm.getDaftarObat()) {
            daftarGejala.add(o.getKategori());
        }
        for (String g : daftarGejala) {
            comboGejala.addItem(g);
        }

        panelInput.add(comboGejala);

        JButton btnSubmit = new JButton("Konsultasi");
        panelInput.add(btnSubmit);

        add(panelInput, BorderLayout.NORTH);

        // OUTPUT AREA
        areaOutput = new JTextArea();
        areaOutput.setEditable(false);
        areaOutput.setMargin(new Insets(10, 10, 10, 10));
        add(new JScrollPane(areaOutput), BorderLayout.CENTER);

        // ACTION BUTTON
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesKonsultasi();
            }
        });
    }

    private void prosesKonsultasi() {
        String nama = fieldNama.getText().trim();
        String gejala = (String) comboGejala.getSelectedItem();

        // Validasi nama
        if (dm.getCurrentUser() == null || 
            !nama.equalsIgnoreCase(dm.getCurrentUser().getNama())) {

            areaOutput.setText("User tidak ditemukan.");
            return;
        }

        String rekomendasi = dm.getRekomendasi(gejala);

        // Simpan konsultasi
        Konsultasi k = new Konsultasi(
                dm.generateNoKonsultasi(),
                nama,
                gejala,
                rekomendasi
        );
        dm.simpanKonsultasi(k);

        areaOutput.setText(
                "Rekomendasi obat untuk gejala '" + gejala + "':\n" +
                rekomendasi + "\n\n" +
                "Konsultasi berhasil disimpan."
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new KonsultasiPanel().setVisible(true);
        });
    }
}