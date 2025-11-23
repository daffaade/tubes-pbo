package model;

import java.text.SimpleDateFormat;
import java.util.*;

public class Pesanan {
    private String noPesanan;
    private List<ItemKeranjang> items;
    private String jenisAmbil; // ambil atau antar
    private String alamat;
    private double ongkir;
    private double biayaPengemasan;
    private String metodePembayaran; //cod atau online 
    private Date tanggal;
    private double totalBayar;

    public Pesanan(String noPesanan, List<ItemKeranjang> items, String jenisAmbil, String alamat,
                   double jarak, double biayaPengemasan, String metodePembayaran, Date tanggal) {
        this.noPesanan = noPesanan;
        this.items = items;
        this.jenisAmbil = jenisAmbil;
        this.alamat = alamat;
        this.ongkir = jarak * 3000; // hitunfg ongkir berdasarkan jarak
        this.biayaPengemasan = biayaPengemasan;
        this.metodePembayaran = metodePembayaran;
        this.tanggal = new Date();        
    }

    public Pesanan(String noPesanan) {
        this.noPesanan = noPesanan;
        this.items = new ArrayList<>();
        this.jenisAmbil = "ambil"; // default
        this.alamat = "";
        this.ongkir = 0;
        this.biayaPengemasan = 0;
        this.metodePembayaran = "cod"; // default
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

    public void setTotalBayar(double totalBayar) {
        this.totalBayar = totalBayar;
    }

    //setter
    public void setBiayaPengemasan(double biayaPengemasan) {
        this.biayaPengemasan = biayaPengemasan;
    } 

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setJenisAmbil(String jenisAmbil) {
        this.jenisAmbil = jenisAmbil;
    }

    public void setOngkir(double ongkir) {
        this.ongkir = ongkir;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }
    
    public void setNoPesanan(String noPesanan) {
        this.noPesanan = noPesanan;
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

    public void addItem(ItemKeranjang newItem) {
       if (this.items == null) {
           this.items = new ArrayList<>();
       }

       for (ItemKeranjang item : items) {
           if (item.getObat().getKode().equals(newItem.getObat().getKode())) {
               item.setJumlah(item.getJumlah() + newItem.getJumlah());
               return;
           }
       }

       this.items.add(newItem);
    }

    public void removeItem(String kodeObat) {
        if (this.items != null) {
            Iterator<ItemKeranjang> iterator = items.iterator();

            while (iterator.hasNext()) {
                ItemKeranjang item = iterator.next(); //ambil item saat ini

                // cek apakah kode obat cocok
                if (item.getObat().getKode().equals(kodeObat)) {
                    iterator.remove(); //menghapus item yang saat ini ditunjuk oleh iterator
                    return; //keluar setelah menghapus item
                }
            }
        }
    }

}

