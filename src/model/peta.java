package model;

public class Peta {
    private String namaLokasi; 
    private int jarak; 


    public Peta(String namaLokasi, int jarak) {
        this.namaLokasi = namaLokasi;
        this.jarak = jarak;
    }

    public String getNamaLokasi() {
        return namaLokasi; 
    }
    
    public int getJarak() { 
        return jarak; 
    }

    @Override
    public String toString() {
        return namaLokasi + " (" + jarak + " KM)";
    }
}

