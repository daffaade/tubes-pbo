package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Konsultasi {
    private String noKonsultasi;
    private String namaPasien;
    private String gejala;
    private String rekomendasiObat;
    private Date tanggal;
    // konstruktr
    public Konsultasi(String noKonsultasi, String namaPasien, String gejala, String rekomendasiObat) {
        this.noKonsultasi = noKonsultasi;
        this.namaPasien = namaPasien;
        this.gejala = gejala;
        this.rekomendasiObat = rekomendasiObat;
        this.tanggal = new Date();
    }
    
    // Getters
    public String getNoKonsultasi() { return noKonsultasi; }
    public String getNamaPasien() { return namaPasien; }
    public String getGejala() { return gejala; }
    public String getRekomendasiObat() { return rekomendasiObat; }
    public Date getTanggal() { return tanggal; }
    // mengubah objek konsultasi menjadi string agar bisa disimpan di file txt
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return String.format("%s|%s|%s|%s|%s", 
            noKonsultasi, sdf.format(tanggal), namaPasien, gejala, rekomendasiObat);
    }
}