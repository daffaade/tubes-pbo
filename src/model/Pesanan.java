package model;

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Pesanan {
    private String noPesanan;
    private List<ItemKeranjang> items;
    private String jenisAmbil; 
    private String alamat;
    private double ongkir;
    private double biayaPengemasan;
    private String metodePembayaran; 
    private Date tanggal;

    public Pesanan(Users user) {
        this.noPesanan = noPesanan;
        this.items = new ArrayList<>();
        this.jenisAmbil = "ambil"; 
        this.alamat = "";
        this.ongkir = 0;
        this.biayaPengemasan = 0;
        this.metodePembayaran = "online"; 
        this.tanggal = new Date();
    }

    public double getTotalBayar() {
        return getTotalObat() + ongkir + biayaPengemasan;
    }

    public double getTotalObat() { 
        double total = 0.0;
        for (ItemKeranjang item : items) {
            total += item.getSubtotal();
        }
        return total; 
    }

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

    public String getNoPesanan() {
        return noPesanan; 
    }
    
    public List<ItemKeranjang> getItems() {
        return items; 
    }
    
    public String getJenisAmbil() {
        return jenisAmbil; 
    }
    
    public String getAlamat() { 
        return alamat; 
    }
    
    public double getOngkir() { 
        return ongkir; 
    }

    public double getBiayaPengemasan() { 
        return biayaPengemasan; 
    }
    
    public String getMetodeBayar() { 
        return metodePembayaran; 
    }

    public Date getTanggal() { 
        return tanggal; 
    }

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
            ItemKeranjang item = items.get(i); 
            sb.append(item.getObat().getKode()).append(","); 
            sb.append(item.getJumlah()); 
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

               double newSubtotal = item.getObat().getHarga() * item.getJumlah();
               item.setSubtotal(newSubtotal);
               return;
           }
       }

       this.items.add(newItem);
    }

    public void removeItem(String kodeObat) {

        if (this.items != null) {
            Iterator<ItemKeranjang> iterator = items.iterator();

            while (iterator.hasNext()) {
                ItemKeranjang item = iterator.next();

                if (item.getObat().getKode().equals(kodeObat)) {
                    iterator.remove(); 
                    return; 
                }
                
            }
        }
    }

    public void updateJumlahItem(String kodeObat, int jumlahBaru, double subtotalBaru) {

        for (ItemKeranjang item : items) { 
            if (item.getObat().getKode().equals(kodeObat)) {
                item.setJumlah(jumlahBaru);
                item.setSubtotal(subtotalBaru);
                
                return;
            }
        }
    }
}