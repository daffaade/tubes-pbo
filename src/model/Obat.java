package model;

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
    public void setstok(int stok) {
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
        return String.format("%s|%s|%.2f|%d|%s", kode, nama, harga, stok, kategori);
    }
    // mengubah string kembali menjadi objek obat
    public static Obat fromString(String str) {
        String[] parts = str.split("\\|");
        return new Obat(parts[0], parts[1], Double.parseDouble(parts[2]), 
                       Integer.parseInt(parts[3]), parts[4]);
    }
}