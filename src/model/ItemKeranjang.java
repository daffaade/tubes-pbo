package model;

public class ItemKeranjang {
    private Obat obat;
    private int jumlah;
    // konstruktor
    public ItemKeranjang(Obat obat, int jumlah) {
        this.obat = obat;
        this.jumlah = jumlah;
    }
    // getter dan setter
    public Obat getObat() { 
        return obat; 
    }
    public int getJumlah() { 
        return jumlah; 
    }
    
    public void setJumlah(int jumlah) { 
        this.jumlah = jumlah; 
    }
    
    public void tambahJumlah(int tambahan) {
        this.jumlah += tambahan;
    }
    
    public double getSubtotal() {
        return obat.getHarga() * jumlah;
    }
}