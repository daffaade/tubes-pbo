package model;

public class peta {
    private String namaLokasi; 
    private int jarak; // Jarak dalam kilo meter


    public peta(String namaLokasi, int jarak) {
        this.namaLokasi = namaLokasi;
        this.jarak = jarak;
    }

    public String getNamaLokasi() { return namaLokasi; }
    public int getJarak() { return jarak; }

    @Override
    public String toString() {
        return namaLokasi + " (" + jarak + " KM)";
    }
}

