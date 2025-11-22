package model;

public class Users {
    private String username;
    private String password;
    private String nama;
    private String noTelp;
    
    public Users(String username, String password, String nama, String noTelp) {
        this.username = username;
        this.password = password;
        this.nama = nama;
        this.noTelp = noTelp;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNama() { return nama; }
    public String getNoTelp() { return noTelp; }
    
    // Setters
    public void setPassword(String password) { this.password = password; }
    public void setNama(String nama) { this.nama = nama; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }
    
    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s", username, password, nama, noTelp);
    }
    
    public static Users fromString(String str) {
        String[] parts = str.split("\\|");
        return new Users(parts[0], parts[1], parts[2], parts[3]);
    }
}