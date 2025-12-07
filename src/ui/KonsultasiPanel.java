package ui;

import data.DataManager;
import model.Konsultasi;
import model.Obat;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.LinkedHashSet;
import java.util.Set;

public class KonsultasiPanel extends JPanel {

    private DataManager dm = DataManager.getInstance();

    private JTextField fieldNama;
    private JComboBox<String> comboGejala;
    private JTextArea areaOutput;

    public KonsultasiPanel() {
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
        btnSubmit.setBackground(new Color(70, 130, 180));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        
        panelInput.add(btnSubmit);

        add(panelInput, BorderLayout.NORTH);

        // OUTPUT AREA
        areaOutput = new JTextArea();
        areaOutput.setEditable(false);
        areaOutput.setMargin(new Insets(10, 10, 10, 10));
        areaOutput.setFont(new Font("SansSerif", Font.PLAIN, 13));
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
                "Rekomendasi obat untuk gejala '" + gejala + "':\n" + rekomendasi + "\n\n" + "Konsultasi berhasil disimpan."
        );
    }
}