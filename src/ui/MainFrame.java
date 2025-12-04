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

    private BeliObatPanel beliObatPanel;
    private KeranjangPanel keranjangPanel;
    private CheckoutPanel checkoutPanel;
    //petaPanel gausah diinstansiiasi disini karena dia JDialog

    private Pesanan keranjangSekarang;

    public CheckoutPanel getCheckoutPanel() {
        return checkoutPanel;
    }
    
    public MainFrame(String title){
        super(title);

        this.keranjangSekarang = new Pesanan(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        //inisialisasi panel-panel
        beliObatPanel = new BeliObatPanel(this, keranjangSekarang);
        keranjangPanel = new KeranjangPanel(this, keranjangSekarang);
        checkoutPanel = new CheckoutPanel(this, keranjangSekarang);

        //tambah panel-panel ke CardLayout
        mainPanel.add(beliObatPanel, BeliObat_Panel);
        mainPanel.add(keranjangPanel, Keranjang_Panel);
        mainPanel.add(checkoutPanel, CheckOut_Panel);

        //pengaturan JFrame
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); //muncul di tengah layar
        
        //tampilkan panel awal
        showPanel(BeliObat_Panel); //harusnya login 
    }

    //Metode Navigasi
    public void showPanel(String panelName){
        if (panelName.equals(MainFrame.Keranjang_Panel)) {
        Pesanan pesananAktif = beliObatPanel.getPesananSekarang(); 
        
        keranjangPanel.refreshPanel(pesananAktif);
        }
        cardLayout.show(mainPanel, panelName);
    }

    public void goToCheckoutPanel(){
        checkoutPanel.refreshData();
        showPanel(CheckOut_Panel);
    };

    public Pesanan getKeranjangSaatIni() {
        return keranjangSekarang;
    }

    public BeliObatPanel getBeliObatPanel() {
        return beliObatPanel;
    }
}
