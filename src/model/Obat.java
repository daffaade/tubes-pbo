package model;

import java.util.Locale;

public class Obat {

    private String kode;
    private String nama;
    private double harga;
    private int stok;
    private String kategori;

    public Obat(String kode, String nama, double harga, int stok, String kategori) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.kategori = kategori;
    }

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }
    
    public double getHarga() {
        return harga;
    }
    
    public int getStok() {
        return stok;
    }
    
    public String getKategori() {
        return kategori;
    }
    
    public void setStok(int stok) {
        this.stok = stok;
    }

    public void kurangiStok(int jumlah) {

        if (jumlah <= stok) {
            stok -= jumlah;
        } else {
            System.out.println("Stok tidak cukup!");
        }

    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s|%s|%.2f|%d|%s", kode, nama, harga, stok, kategori);
    }

    public static Obat fromString(String str) {
        
        String[] data = str.split("\\|");

        if (data.length < 5) {
            System.err.println("Format data obat tidak lengkap: " + str);
            return new Obat("ERR", "Data Error", 0.0, 0, "Error");
        }

        try {

            String hargaString = data[2].trim(); 
            
            hargaString = hargaString.replace(',', '.'); 
            
            double harga = Double.parseDouble(hargaString);
            
            return new Obat(data[0], data[1], harga, Integer.parseInt(data[3]), data[4]);
                            
        } catch (NumberFormatException e) {

            System.err.println("Gagal membaca format angka di baris: " + str);
            return new Obat(data[0], data[1], 0.0, 0, data[4]);
        }

    }

}