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
// getter
    public String getKode() {return kode;}
    public String getNama() {return nama;}
    public double getHarga() {return harga;}
    public int getStok() {return stok;}
    public String getKategori() {return kategori;}
    

// setter
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
    // menubah objek obat menjadi string 
    public String toString() {
        return String.format(Locale.US, "%s|%s|%.2f|%d|%s", kode, nama, harga, stok, kategori);
    }

    // mengubah string kembali menjadi objek obat
    public static Obat fromString(String str) {
        String[] parts = str.split("\\|");

        if (parts.length < 5) {
            System.err.println("Format data obat tidak lengkap: " + str);
            return new Obat("ERR", "Data Error", 0.0, 0, "Error");
        }

        try {
            String hargaString = parts[2].trim(); 
            
            // Perbaikan agar dapat membaca koma (,) atau titik (.)
            hargaString = hargaString.replace(',', '.'); 
            
            double harga = Double.parseDouble(hargaString);
            
            return new Obat(parts[0], 
                            parts[1], 
                            harga, // Sudah benar dibaca
                            Integer.parseInt(parts[3]), 
                            parts[4]);
                            
        } catch (NumberFormatException e) {
            System.err.println("Gagal membaca format angka di baris: " + str);
            // Pilihan: return null atau lemparkan exception lagi
            return new Obat(parts[0], parts[1], 0.0, 0, parts[4]); // Fallback aman jika parsing harga gagal
        }
    }
}