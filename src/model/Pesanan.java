package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Pesanan {
    private String noPesanan;
    private List<ItemKeranjang> items;
    private String jenisAmbil; // ambil atau antar
    private String alamat;
    private double ongkir;
    private double biayaPengemasan;
    private String metodePembayaran; //cod atau online 
    private Date tanggal;


    public Pesanan(String noPesanan, List<ItemKeranjang> item, String jenisAmbil, String alamat,
                   double ongkir, double biayaPengemasan, String metodePembayaran, Date tanggal) {
        this.noPesanan = noPesanan;
        this.items = items;
        this.jenisAmbil = jenisAmbil;
        this.alamat = alamat;
        this.ongkir = ongkir;
        this.biayaPengemasan = biayaPengemasan;
        this.metodePembayaran = metodePembayaran;
        this.tanggal = new Date();

        
    }
    // menghitung total obat dalam pesanan
    public double getTotalObat() {
    double total = 0;
        if (items != null) {
            for (ItemKeranjang item : items) {
                total = total + item.getSubtotal();
            }
        }
        return total;
    }

    // menghitung jumlah total 
    public double getTotalBayar() {
       return getTotalObat() + ongkir + biayaPengemasan; 
    } 

    //getter   
    public String getNoPesanan() { return noPesanan; }
    public List<ItemKeranjang> getItems() { return items; }
    public String getJenisAmbil() { return jenisAmbil; }
    public String getAlamat() { return alamat; }
    public double getOngkir() { return ongkir; }
    public double getBiayaPengemasan() { return biayaPengemasan; }
    public String getMetodeBayar() { return metodePembayaran; }
    public Date getTanggal() { return tanggal; }

// mengubah pesanan menjadi satu baris teks panjang 
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append(noPesanan).append("|");
        sb.append(sdf.format(tanggal)).append("|");
        sb.append(jenisAmbil).append("|");
        sb.append(alamat != null ? alamat : "-").append("|");
        sb.append(metodePembayaran).append("|");
        sb.append(String.format("%.2f", getTotalObat())).append("|");
        sb.append(String.format("%.2f", ongkir)).append("|");
        sb.append(String.format("%.2f", biayaPengemasan)).append("|");
        sb.append(String.format("%.2f", getTotalBayar())).append("|");


        for (int i = 0; i < items.size(); i++) {
            ItemKeranjang item = items.get(i); // ambil barang urutan ke i 
            sb.append(item.getObat().getKode()).append(","); // ambil kode obat
            sb.append(item.getJumlah()); // tempel jumlah barang yang dibeli
            if (i < items.size() - 1) sb.append(";");
        }
        
        return sb.toString();
    }


}

