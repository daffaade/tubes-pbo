package ui;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Pesanan;

public class MainFrame extends JFrame{
    //variabel class (field)
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // nama Panelnya
    public static final String BeliObat_Panel = "BeliObat";
    public static final String Keranjang_Panel = "KeranjangObat";
    public static final String CheckOut_Panel = "CheckOut";

    //Panel yang sudah dibuat
    private BeliObatPanel beliObatPanel;
    private KeranjangPanel keranjangPanel;
    private CheckoutPanel checkoutPanel;
    // petaPanel gausah diinstansiiasi disini karena dia JDialog

    private Pesanan keranjangSekarang;

    public CheckoutPanel getCheckoutPanel() {
        return checkoutPanel;
    }
    
    public MainFrame(String title){
        super(title);

        //inisialisasi keranjnag
        this.keranjangSekarang = new Pesanan(null);

        //inisialisasi CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 1. inisialisasi panel-panel
        beliObatPanel = new BeliObatPanel(this, keranjangSekarang);
        keranjangPanel = new KeranjangPanel(this, keranjangSekarang);
        //COPanel butuh pesanan saat dibuat
        checkoutPanel = new CheckoutPanel(this, keranjangSekarang);

        // 2. Tambah panel-panel ke CardLayout
        mainPanel.add(beliObatPanel, BeliObat_Panel);
        mainPanel.add(keranjangPanel, Keranjang_Panel);
        mainPanel.add(checkoutPanel, CheckOut_Panel);

        // 3. Pengaturan JFrame
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(530, 400);
        setLocationRelativeTo(null); // muncul di tengah layar
        // setVisible(true); //biasanya dipanggil paling akhir di Main Java

        //tampilkan panel awal
        showPanel(BeliObat_Panel); //harusnya login 
    }

    // 4. Metode Navigasi
    public void showPanel(String panelName){
        if (panelName.equals(MainFrame.Keranjang_Panel)) {
        // Dapatkan Pesanan aktif dari BeliObatPanel
        Pesanan pesananAktif = beliObatPanel.getPesananSekarang(); 
        
        // Panggil metode untuk me-refresh KeranjangPanel dengan data baru
        keranjangPanel.refreshPanel(pesananAktif); // <-- Metode ini HARUS ada
        }
        cardLayout.show(mainPanel, panelName);
    }

    // 5. Metode Khusus untuk beralih ke Checkout
    public void goToCheckoutPanel(){
        // CheckoutPanel sudah ada, kita hanya perlu me-refresh data dan menampilkannya.
        checkoutPanel.refreshData();
        showPanel(CheckOut_Panel);
    };

    // --- 6. Metode Getter (Jika diperlukan) ---
    public Pesanan getKeranjangSaatIni() {
        return keranjangSekarang;
    }

    // MainFrame.java
    public BeliObatPanel getBeliObatPanel() {
        return beliObatPanel;
    }
}
