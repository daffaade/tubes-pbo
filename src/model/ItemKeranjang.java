package model;

public class ItemKeranjang {

    private Obat obat;
    private int jumlah;
    private double subtotal;

    public ItemKeranjang(Obat obat, int jumlah) {
        this.obat = obat;
        this.jumlah = jumlah;
        this.subtotal = obat.getHarga() * jumlah;
    }

    public Obat getObat() { 
        return obat; 
    }

    public int getJumlah() { 
        return jumlah; 
    }

    public double getSubtotal() {
        return this.subtotal;
    }

    public void setSubtotal(double subtotal) { 
        this.subtotal = subtotal;    
    }

    public void setJumlah(int jumlah) { 
        this.jumlah = jumlah; 
    }
    
    public void tambahJumlah(int tambahan) {
        this.jumlah += tambahan;
    }
    
}