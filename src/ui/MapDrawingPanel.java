package ui;

import data.DataManager;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import model.Peta;

public class MapDrawingPanel extends JPanel {

    private final List<Peta> lokasiList;
    //Map ini hanya digunakan untuk menyimpan koordinat hasil hitungan TERAKHIR
    private final Map<Peta, Point> lokasiKoordinat = new HashMap<>(); 
    private Peta lokasiTerpilih = null;
    private PetaPanel parentDialog;

    private static final int APOTEK_SIZE_SCALE = 3; // Skala Ukuran Apotek
    private static final int NODE_SIZE = 15;
    private static final int MAX_DISTANCE_KM = 10; //jarak Maksimum KM
    private static final int MAX_RADIUS_PIXEL = 150; //jarak Maksimum Piksel dari Pusat

    private static final Color WARNA_LOKASI_DEFAULT = new Color(30, 90, 80);
    private static final Color WARNA_APOTEK = new Color(53, 94, 59); 
    private static final Color WARNA_LOKASI_TERPILIH = new Color(70, 130, 180);
    
    private final Point[] DEFAULT_ABSTRACT_COORDS = {
        new Point(290, 50),      // 0. Jebres
        new Point(220, 130),     // 1. RS Muwardi
        new Point(100, 190),     // 2. Slamet Riyadi
        new Point(210, 290),    // 3.  Alun-alun 
        new Point(300, 250),    // 4. Pasar Kliwon
        new Point(245, 200)     // 5.  Pasar Gedhe
    };
    
    //pusat fiktif untuk Perhitungan Vektor Arah
    private static final int FAKE_CENTER_X = 200; 
    private static final int FAKE_CENTER_Y = 200; 

    public MapDrawingPanel(PetaPanel parentDialog) {
        this.parentDialog = parentDialog;
        this.lokasiList = DataManager.getInstance().getLokasi();
        
        //Mouse Listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getPoint());
            }
        });
    }

    //fungsi untuk menggambar dan menghitung posisi dinamis
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Apotek adalah pusat panel
        Point apotek = new Point(getWidth() / 2, getHeight() / 2); 
        
        if (lokasiList.size() != DEFAULT_ABSTRACT_COORDS.length) {
             g2d.drawString("Error: Data lokasi tidak sama dengan koordinat awal!", 10, 10);
             return;
        }

        //1. hitung koordinat baru
        lokasiKoordinat.clear(); 
        
        for (int i = 0; i < lokasiList.size(); i++) {
            Peta loc = lokasiList.get(i);
            Point initialP = DEFAULT_ABSTRACT_COORDS[i];
            int jarakKM = loc.getJarak();
            
            //hitung Vektor Arah dari Pusat Fiktif
            double dx = initialP.x - FAKE_CENTER_X;
            double dy = initialP.y - FAKE_CENTER_Y;
            
            //hitung panjang vektor awal (Jarak Euclidean)
            double initialDistance = Math.sqrt(dx * dx + dy * dy);
            
            //normalisasi vektor
            double unitX = dx / initialDistance;
            double unitY = dy / initialDistance;
            
            //hitung Radius Proporsional berdasarkan KM
            double ratio = (double) jarakKM / MAX_DISTANCE_KM;
            int newRadiusPixel = (int) (ratio * MAX_RADIUS_PIXEL);
            
            //hitung Koordinat Akhir (Pusat Real-time + Vektor Satuan * Radius Proporsional)
            int p_x = apotek.x + (int) (unitX * newRadiusPixel);
            int p_y = apotek.y + (int) (unitY * newRadiusPixel);
            Point p = new Point(p_x, p_y);

            //simpan koordinat hasil hitungan untuk handleClick
            lokasiKoordinat.put(loc, p);

            //2. Gambar Garis dan Jarak
            g2d.setColor(Color.BLACK);
            g2d.drawLine(apotek.x, apotek.y, p.x, p.y);
            
            g2d.setColor(Color.DARK_GRAY);
            String jarakText = loc.getJarak() + " KM";
            int midX = (apotek.x + p.x) / 2;
            int midY = (apotek.y + p.y) / 2;
            g2d.drawString(jarakText, midX + 5, midY - 5);
        }
        
        //3. Gambar Node Lokasi
        for (Map.Entry<Peta, Point> entry : lokasiKoordinat.entrySet()) {
            Peta loc = entry.getKey();
            Point p = entry.getValue();

            //set warna node
            if (loc.equals(lokasiTerpilih)) {
                g2d.setColor(WARNA_LOKASI_TERPILIH); 
            } else {
                g2d.setColor(WARNA_LOKASI_DEFAULT); 
            }

            //gambar lingkaran (Node)
            g2d.fillOval(p.x - NODE_SIZE / 2, p.y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE);
            
            //tulis nama lokasi
            g2d.setColor(Color.BLACK);
            int textX = p.x + NODE_SIZE;
            int textY = p.y + 5;

            String namaLokasi = loc.getNamaLokasi();
    
            if (namaLokasi.equals("RS. Muwardi")) {
                //pindah ke kiri Titik
                textX = p.x - 70; //geser ke kiri, 70 piksel
                textY = p.y + 5;
            } else if (namaLokasi.equals("Slamet Riyadi")) {
                //pindah ke bawah Titik
                textX = p.x - 10;
                textY = p.y + 20; //geser ke bawah
            }
            
            g2d.drawString(namaLokasi, textX, textY);   
        }   
        
        //4. Gambar Node Apotek (Pusat) ---
        g2d.setColor(WARNA_APOTEK); 

        //Ukuran Kotak (Lebar dan Tinggi): 2 * APOTEK_SIZE_SCALE * NODE_SIZE
        int apotekSquareSize = NODE_SIZE * APOTEK_SIZE_SCALE;

        //gambar lingkaran apotek
        g2d.fillOval(apotek.x - apotekSquareSize / 2, apotek.y - apotekSquareSize / 2, 
                     apotekSquareSize, apotekSquareSize);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.drawString("APOTEK", apotek.x - 22, apotek.y + 4);
    }
    
    private void handleClick(Point clickPoint) {
        final int CLICK_AREA_SIZE = 30;
        final int offset = CLICK_AREA_SIZE / 2;

        for (Map.Entry<Peta, Point> entry : lokasiKoordinat.entrySet()) {
            Peta loc = entry.getKey();
            Point p = entry.getValue();

            //gunakan area klik yang diperbesar
            Rectangle areaNode = new Rectangle(p.x - offset, p.y - offset, CLICK_AREA_SIZE, CLICK_AREA_SIZE);
            
            if (areaNode.contains(clickPoint)) {
                this.lokasiTerpilih = loc;
                repaint(); 
                parentDialog.setLokasiTerpilih(loc);
                return;
            }
        }
    }
    
    public Peta getLokasiTerpilih() {
        return lokasiTerpilih;
    }
}