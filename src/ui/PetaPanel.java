package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.Peta;

public class PetaPanel extends JDialog {

    private Peta lokasiTerpilih = null;
    private MapDrawingPanel drawingPanel;

    public PetaPanel(Frame main) {
        super(main, "Pilih Lokasi Pengiriman", true); 
        setLayout(new BorderLayout(10, 10));

        drawingPanel = new MapDrawingPanel(this); 
        drawingPanel.setPreferredSize(new Dimension(500, 450));
        
        JScrollPane scrollPane = new JScrollPane(drawingPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton okButton = new JButton("Pilih Lokasi Ini");
        okButton.setBackground(new Color(30, 90, 80)); 
        okButton.setForeground(Color.WHITE);
        okButton.setOpaque(true);
        okButton.setBorderPainted(false);

        okButton.addActionListener(e -> pilihLokasi());
        
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.add(okButton);
        add(southPanel, BorderLayout.SOUTH);

        pack(); // Agar sesuai dengan preferred size drawingPanel
        setLocationRelativeTo(main);
    }
    
    // Set dari MapDrawingPanel
    public void setLokasiTerpilih(Peta lokasi) {
        this.lokasiTerpilih = lokasi;
    }
    
    private void pilihLokasi() {
        // Ambil lokasi terpilih dari drawingPanel
        lokasiTerpilih = drawingPanel.getLokasiTerpilih();
        
        if (lokasiTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Pilih salah satu lokasi pada peta terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Tutup dialog
        dispose(); 
    }

    public Peta getLokasiTerpilih() {
        return lokasiTerpilih;
    }
}