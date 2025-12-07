package model;

public class History {

    private int noHistory;
    private String username;
    private String tanggal;
    private String jenis;
    private String detail;

    public History(int noHistory, String username, String tanggal, String jenis, String detail) {
        this.noHistory = noHistory;
        this.username = username;
        this.tanggal = tanggal;
        this.jenis = jenis;
        this.detail = detail;
    }

    public int getNoHistory() {
        return noHistory;
    }

    public String getUsername() {
        return username;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJenis() {
        return jenis;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public String toString() {
        return String.format("%d|%s|%s|%s|%s", noHistory, username, tanggal, jenis, detail);
    }

    public static History fromString(String str) {
        String[] listHistory = str.split("\\|");
        return new History(Integer.parseInt(listHistory[0]), listHistory[1], listHistory[2], listHistory[3], listHistory[4]);
    }
}