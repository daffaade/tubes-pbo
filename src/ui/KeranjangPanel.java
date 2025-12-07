package ui;

import model.ItemKeranjang;
import model.Pesanan;

import java.text.NumberFormat;
import java.util.Locale;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

public class KeranjangPanel extends JPanel {
    private MainFrame mainFrame;
    private Pesanan pesananSekarang;
    private JTable tabelKeranjang;
    private DefaultTableModel modelTabel;
    private JLabel totalLabel;

    private JButton removeButton;
    private JButton checkoutButton;

    public KeranjangPanel (MainFrame mainFrame, Pesanan pesananAktif) {
        this.mainFrame = mainFrame;
        this.pesananSekarang = pesananAktif;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));

        // 1. Inisialiasi tabel
        String[] namaKolom = {"Kode", "Nama Obat", "Harga", "Jumlah", "Subtotal"};
        modelTabel = new DefaultTableModel(namaKolom, 0) {
            @Override
            public boolean isCellEditable(int baris, int kolom) {
                return kolom == 3;
            }
        };

        modelTabel.addTableModelListener(new javax.swing.event.TableModelListener() {
        @Override
        public void tableChanged(TableModelEvent e) {

            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                
                // Kolom 0: Kode, 1: Nama Obat, 2: Harga, 3: Jumlah, 4: Subtotal
                final int JUMLAH_COLUMN_INDEX = 3;
                
                if (column == JUMLAH_COLUMN_INDEX) {
                    recalculateRowAndTotal(row);
                }
            }
        }
    });

        tabelKeranjang = new JTable(modelTabel);
        tabelKeranjang.setShowGrid(true);
        tabelKeranjang.setGridColor(Color.LIGHT_GRAY); 
        tabelKeranjang.setIntercellSpacing(new Dimension(1, 3));
        tabelKeranjang.setFont(new Font("Arial", Font.PLAIN, 13));
        tabelKeranjang.setRowHeight(25);
        tabelKeranjang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(tabelKeranjang);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);
           
        totalLabel = new JLabel("Total Belanja: Rp 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(50, 50, 50));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionPanel.setBackground(new Color(240, 240, 240));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        
        JButton backButton = new JButton("← Kembali Belanja");
        backButton.setBackground(new Color(100, 100, 100));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setPreferredSize(new Dimension(160, 40));

        backButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mainFrame.showPanel(MainFrame.BELI_OBAT);
            }
        });
       
        removeButton = new JButton("Hapus Item");
        removeButton.setBackground(new Color(220, 53, 69));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeButton.setFocusPainted(false);
        removeButton.setBorderPainted(false);
        removeButton.setPreferredSize(new Dimension(130, 40));

        checkoutButton = new JButton("Checkout →");
        checkoutButton.setBackground(new Color(60, 150, 100));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutButton.setFocusPainted(false);
        checkoutButton.setBorderPainted(false);
        checkoutButton.setPreferredSize(new Dimension(130, 40));

        actionPanel.add(backButton);
        actionPanel.add(totalLabel);
        actionPanel.add(removeButton);
        actionPanel.add(checkoutButton);

        add(actionPanel, BorderLayout.SOUTH);

        loadKeranjangData();
        
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

        totalLabel.setText("Total Belanja: " + formatRupiah(pesananSekarang.getTotalObat()));
    }

    private void addListeners() {
        
        // Hapus Item
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                removeItem();
            }
        });

        checkoutButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                System.out.println("=== CHECKOUT BUTTON CLICKED ===");
                System.out.println("Items count: " + pesananSekarang.getItems().size());
                
                if (pesananSekarang.getItems().isEmpty()) {
                    JOptionPane.showMessageDialog(KeranjangPanel.this, "Keranjang Anda kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                System.out.println("Calling updateTotalBelanjaUI...");
                updateTotalBelanjaUI();
                
                System.out.println("Calling refreshData...");
                mainFrame.getCheckoutPanel().refreshData();
                
                System.out.println("Showing CHECKOUT panel...");
                mainFrame.showPanel(MainFrame.CHECKOUT);
                
                System.out.println("DONE");
            }
        });
    }

    private void removeItem() {
        int selectedRow = tabelKeranjang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih item yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

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

    private void recalculateRowAndTotal(int row) { // Menghapus parameter model dan subtotalColIndex agar lebih bersih
    try {
        final int JUMLAH_COLUMN_INDEX = 3;
        final int SUBTOTAL_COLUMN_INDEX = 4;
        
        String kodeObat = (String) modelTabel.getValueAt(row, 0); 
        String hargaStr = (String) modelTabel.getValueAt(row, 2); 
        
        //cek validitas input jumlah
        int jumlahBaru;
        try {
            jumlahBaru = Integer.parseInt(modelTabel.getValueAt(row, JUMLAH_COLUMN_INDEX).toString());
            if (jumlahBaru <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0.", "Error Input", JOptionPane.ERROR_MESSAGE);
                throw new NumberFormatException(); 
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka yang valid dan positif.", "Error Input", JOptionPane.ERROR_MESSAGE);
            loadKeranjangData(); 
            return;
        }

        double hargaSatuan = parseRupiahToDouble(hargaStr); // Menggunakan double karena formatRupiah() menggunakan double
        
        double subtotalBaru = hargaSatuan * jumlahBaru;
        
        pesananSekarang.updateJumlahItem(kodeObat, jumlahBaru, subtotalBaru); 
        
        String subtotalStr = formatRupiah(subtotalBaru); 
        modelTabel.setValueAt(subtotalStr, row, SUBTOTAL_COLUMN_INDEX);
        
        updateTotalBelanjaUI();
        
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengupdate keranjang: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            loadKeranjangData();
        }
    }


    private void updateTotalBelanjaUI() {

        double newTotalObat = 0.0;
        for(ItemKeranjang item : pesananSekarang.getItems()) {
            newTotalObat += item.getSubtotal();
        }
        
        totalLabel.setText("Total Belanja: " + formatRupiah(newTotalObat));
    
        if (mainFrame.getCheckoutPanel() != null) {
            mainFrame.getCheckoutPanel().refreshData();
        }
    }

    public void refreshData() {
        loadKeranjangData();
    }

   
    private double parseRupiahToDouble(String rupiah) {
        try {
        
        String cleanedString = rupiah.replace("Rp", "").trim();
        
        cleanedString = cleanedString.replace(".", ""); 
        
        cleanedString = cleanedString.replace(",", "."); 
        
        return Double.parseDouble(cleanedString);

        } catch (NumberFormatException e) {
            return 0.0; 
        }
    }
}