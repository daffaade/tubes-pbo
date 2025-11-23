package ui;

import data.DataManager;
import model.Peta;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;


public class PetaPanel extends JDialog {
    private JTable tabelLokasi;
    private DefaultTableModel modelTabel;
    private Peta lokasiTerpilih = null; //output yang diambil

    public PetaPanel(Frame parent) {
        // JDialog modal, memblokir interaksi dengan parent frame sampai ditutup
        super(parent, "Pilih Lokasi Pengiriman", true); 
        setLayout(new BorderLayout(10, 10));

        // 1. Inisisalisasi Tabel
        String[] columnNames = {"Nama Lokasi", "Jarak (KM)"};
        modelTabel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak bisa diedit
            }
        };

        tabelLokasi = new JTable(modelTabel);
        tabelLokasi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tabelLokasi);
        add(scrollPane, BorderLayout.CENTER);

        // 2. Memuat Data
        loadDataLokasi();

        // 3. Listener klik Ganda (Double Click)
        tabelLokasi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    pilihLokasi();
                }
            }
        });

        // 4. Tombol OK
        JButton okButton = new JButton("Pilih Lokasi Ini");
        okButton.setBackground(Main.WARNA_HEADER); 
        okButton.setForeground(Color.WHITE);
        okButton.setOpaque(true);
        okButton.setBorderPainted(false);

        okButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                pilihLokasi();
            }
        });
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.add(okButton);
        add(southPanel, BorderLayout.SOUTH);

        // Pengaturan JDialog
        setSize(400, 300);
        setLocationRelativeTo(parent);
    
    }

    private void loadDataLokasi() {
        modelTabel.setRowCount(0);
        List<Peta> daftarLokasi = DataManager.getInstance().getLokasi();
        for (Peta loc : daftarLokasi) {
            Object[] rowData = {
                loc.getNamaLokasi(),
                loc.getJarak()
            };
            modelTabel.addRow(rowData);
        }
    }
    
    private void pilihLokasi() {
        int selectedRow = tabelLokasi.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih salah satu lokasi terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Dapatkan objek peta yang dipilih
        String namaLokasi = (String) modelTabel.getValueAt(selectedRow, 0);
        //memastikan casting ke Integer dan uboxing ke int
        // ui/PetaPanel.java (GANTI baris di dalam private void pilihLokasi())
        // Pastikan data yang dibaca adalah tipe angka (Integer/Double) sebelum dikonversi ke int
        Object jarakObj = modelTabel.getValueAt(selectedRow, 1);
        int jarak;

        if (jarakObj instanceof Integer) {
            jarak = (Integer) jarakObj;
        } else if (jarakObj instanceof Double) {
            jarak = ((Double) jarakObj).intValue();
        } else {
            // Jika tipe data String (terburuk), gunakan:
            try {
                jarak = Integer.parseInt(jarakObj.toString());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Data jarak tidak valid!", "Error Data", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        this.lokasiTerpilih = new Peta(namaLokasi, jarak);
        
        // Tutup Dialog
        dispose(); 
    }

    /**
     * Metode untuk memanggil dan mendapatkan hasil pemilihan lokasi.
     */
    public Peta getLokasiTerpilih() {
        return lokasiTerpilih;
    }
}