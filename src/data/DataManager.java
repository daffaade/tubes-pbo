package data;

import java.util.*;
import model.*;
import java.text.SimpleDateFormat; 

public class DataManager {
    
    private static DataManager instance;
    private List<Obat> daftarObat;
    private List<Pesanan> daftarPesanan;
    private List<Konsultasi> daftarKonsultasi;
    private List<Users> daftarUsers; 
    private List<History> daftarHistory;
    private Users currentUsers;      

    private int noPesananCounter = 1;
    private int noKonsultasiCounter = 1;
    private int noHistoryCounter = 1;
    
    private DataManager() {
        daftarObat = new ArrayList<>();
        daftarPesanan = new ArrayList<>();
        daftarKonsultasi = new ArrayList<>();
        daftarUsers = new ArrayList<>();
        daftarHistory = new ArrayList<>();
        
        inisialisasiData();  
        inisialisasiUsers(); 
        inisialisasiHistory();
    }
    
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void inisialisasiHistory() {

        List<String> data = FileHandler.bacaDariFile("history.txt");
        
        if (!data.isEmpty()) {
            for (String line : data) {
                History h = History.fromString(line);
                daftarHistory.add(h);

                if (h.getNoHistory() >= noHistoryCounter) {
                    noHistoryCounter = h.getNoHistory() + 1;
                }
            }
        }
    }

    public void simpanHistory(History history) {
        daftarHistory.add(history);
        FileHandler.simpanKeFile("history.txt", history.toString());
        noHistoryCounter++;
    }

    public int generateNoHistory() {
        return noHistoryCounter;
    }

    public List<History> getDaftarHistory() {
        return daftarHistory;
    }

    private String getTanggal() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(new Date());
    }
    
    private void inisialisasiData() {

        List<String> dataObat = FileHandler.bacaDariFile("obat.txt");
       
        if (dataObat.isEmpty()) {
            daftarObat.add(new Obat("OBT001", "Paracetamol 500mg", 5000, 100, "Demam"));
            daftarObat.add(new Obat("OBT002", "Amoxicillin 500mg", 15000, 50, "Antibiotik"));
            daftarObat.add(new Obat("OBT003", "Antimo", 8000, 75, "Mual"));
            daftarObat.add(new Obat("OBT004", "Promag", 10000, 60, "Maag"));
            daftarObat.add(new Obat("OBT005", "Panadol Extra", 12000, 80, "Demam"));
            daftarObat.add(new Obat("OBT006", "OBH Combi", 18000, 40, "Batuk"));
            daftarObat.add(new Obat("OBT007", "Decolgen", 7000, 90, "Flu"));
            daftarObat.add(new Obat("OBT008", "Bodrex", 6000, 85, "Sakit Kepala"));
            daftarObat.add(new Obat("OBT009", "Sangobion", 25000, 30, "Vitamin"));
            daftarObat.add(new Obat("OBT010", "Betadine", 15000, 45, "Antiseptik"));
            simpanSemuaObat();
        } else {
            for (String line : dataObat) {
                daftarObat.add(Obat.fromString(line));
            }
        }
    }
    
    public List<Obat> getDaftarObat() {
        return new ArrayList<>(daftarObat);
    }
    
    public Obat cariObat(String kode) {

        for (Obat obat : daftarObat) {
            if (obat.getKode().equalsIgnoreCase(kode)) {
                return obat;
            }
        }

        return null;
    }
    
    public void simpanPesanan(Pesanan pesanan) {

        daftarPesanan.add(pesanan);
        FileHandler.simpanKeFile("pesanan.txt", pesanan.toString());
        
        for (ItemKeranjang item : pesanan.getItems()) {
            Obat obat = cariObat(item.getObat().getKode());
            if (obat != null) {
                obat.kurangiStok(item.getJumlah());
            }
        }
        simpanSemuaObat();

        History h = new History(generateNoHistory(), currentUsers.getUsername(), getTanggal(), "Pembelian", pesanan.toString());
        simpanHistory(h);
    }
    
    public String generateNoPesanan() {
        return String.format("PSN%04d", noPesananCounter++);
    }
    
    public void simpanKonsultasi(Konsultasi konsultasi) {
        daftarKonsultasi.add(konsultasi);
        FileHandler.simpanKeFile("konsultasi.txt", konsultasi.toString());

        History h = new History(generateNoHistory(), currentUsers.getUsername(), getTanggal(), "Konsultasi", konsultasi.toString());
        simpanHistory(h);
    }
    
    public String generateNoKonsultasi() {
        return String.format("KSL%04d", noKonsultasiCounter++);
    }
    
    public String getRekomendasi(String gejala) {

        String gejalaLower = gejala.toLowerCase();
        
        if (gejalaLower.contains("demam") || gejalaLower.contains("panas")) {
            return "Paracetamol 500mg atau Panadol Extra";
        } else if (gejalaLower.contains("batuk")) {
            return "OBH Combi";
        } else if (gejalaLower.contains("flu") || gejalaLower.contains("pilek")) {
            return "Decolgen";
        } else if (gejalaLower.contains("sakit kepala") || gejalaLower.contains("pusing")) {
            return "Bodrex atau Paracetamol";
        } else if (gejalaLower.contains("maag") || gejalaLower.contains("perut")) {
            return "Promag";
        } else if (gejalaLower.contains("mual") || gejalaLower.contains("mabuk")) {
            return "Antimo";
        } else if (gejalaLower.contains("luka")) {
            return "Betadine";
        } else {
            return "Silakan konsultasi dengan dokter untuk gejala ini";
        }
    }
    
    private void simpanSemuaObat() {

        List<String> data = new ArrayList<>();

        for (Obat obat : daftarObat) {
            data.add(obat.toString());
        }

        FileHandler.simpanSemuaKeFile("obat.txt", data);
    }
    
    private void inisialisasiUsers() {

        List<String> dataUser = FileHandler.bacaDariFile("users.txt");
      
        if (dataUser.isEmpty()) {

            daftarUsers.add(new Users("admin", "admin123", "Administrator", "081234567890"));
            daftarUsers.add(new Users("user", "user123", "User Test", "081234567891"));
            simpanSemuaUsers();

        } else {

            for (String line : dataUser) {
                daftarUsers.add(Users.fromString(line));
            }
            
        }
    }
    
    public boolean login(String username, String password) {

        for (Users user : daftarUsers) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUsers = user;
                return true;
            }
        }
        return false;
    }
    
    public boolean register(Users user) {

        for (Users u : daftarUsers) {
            if (u.getUsername().equals(user.getUsername())) {
                return false; 
            }
        }

        daftarUsers.add(user);
        FileHandler.simpanKeFile("users.txt", user.toString());
        
        return true;
    }
    
    public void logout() {
        currentUsers = null;
    }
    
    public Users getCurrentUser() {
        return currentUsers;
    }
    
    public boolean isLoggedIn() {
        return currentUsers != null;
    }
    
    private void simpanSemuaUsers() {
        List<String> data = new ArrayList<>();
        for (Users user : daftarUsers) {
            data.add(user.toString());
        }
        FileHandler.simpanSemuaKeFile("users.txt", data);
    }
    
    public List<Pesanan> getHistoryPembelian() {
        return daftarPesanan;
    }

    public List<Konsultasi> getHistoryKonsultasi() {
        return daftarKonsultasi;
    }

    public List<Peta> getLokasi() {
        List<Peta> lokasiList = new ArrayList<>();

        lokasiList.add(new Peta("Jebres", 10));
        lokasiList.add(new Peta("RS. Moewardi", 5));
        lokasiList.add(new Peta("Slamet Riyadi", 8));
        lokasiList.add(new Peta("Alun-alun", 6));
        lokasiList.add(new Peta("Pasar Kliwon", 8));
        lokasiList.add(new Peta("Pasar Gede", 3));
    
        return lokasiList;
    }
}