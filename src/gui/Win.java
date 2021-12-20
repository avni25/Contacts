package gui;

import com.Contact;
import com.DB;
import com.RandomContact;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;
import java.util.Date;
import java.lang.*;
import java.text.*;


public class Win extends JFrame implements ActionListener{
    private JTextField text_name;
    private JTextField text_surname;
    private JTextField text_phone;
    private JButton addButton;

    private JButton loadButton;
    private JButton updateButton;
    private JButton removeButton;
    private JButton randAddButton;
    private JPanel mypanel;
    private JScrollPane sp;
    JButton[] buttons = {addButton, loadButton, updateButton, removeButton, randAddButton};
    JTextField[] textFields = {text_name, text_surname, text_phone};
    public String[] columns = {"contac_id", "name", "surname", "phone_number"};
    public String[][] data;
    public ArrayList<Contact> c;
    public JTable table1;
    private JLabel label_result;
    private JLabel label_result2;
    private JLabel label_time;

    DB db;

    /**
     * Constructor
     * Win nesnesi olusturuldugunda;
     * asagidaki ayarlarda pencere olusturulur ve goruntulenir
     * Buna ek olarak butun butonlara actionlistener atanir
     * Databse nesnesi olusturulur ve database baglantisi kurulur.
     * Database den Tabloya veriler yukarıda global olarak tanımlanmis olan
     * c arraylist araciligi ile aktarilir.
     *
     *
     * */
    public Win(){
        setLayout(new FlowLayout());
        setTitle("my Contacts");
        this.pack();
        setSize(800,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(mypanel);
        c = new ArrayList<>();
        loadTable(c, columns);

        for (JButton button : buttons) {
            button.addActionListener(this);
        }

        db = new DB();
        db.connect(DB.DB_NAME);

        ShowTime(label_time);

        setVisible(true);

    }


    /**
     * Butonlarin islevleri bu metodla tanimlanmistir
     *
     * Add butonu:
     * Textfield a girilen verilerden COntact nesnesi olusturur. contact nesnesini
     * c araylistine ve veritabanina ekler. Ekledikten sonra Tabloyu gunceller ve
     * yeni eklenen kisiyi tabloda gosterir.
     * Name ve phone textfieldlar bos olamaz. kaydedebilmesi icin mutlaka deger almalari gerekir.
     *
     *
     * Load Butonu:
     * Veritabnındaki tum verilerli okur ve tabloda gosterir. DB sınıfinin icerisindeki
     * load metodunu kullanir. Bu metod arraylist dondurur. Yani veriler c arraylistine
     * kaydedilir.
     *
     * Update BUtonu:
     * Tabloda uzeri secilen kisinin bilgilerini gunceller ve veritabanina kaydeder.
     * Guncellenmek istenen kisinin verileri tablodan cift tiklama ile girilerek degistirilir.
     * SOnrasinda kisi satiri hala seicli iken update butonu ile yeni veriler veritabanina eklenir.
     *
     * Remove Butonu:
     * Tabloda secilen kisiyi veritanabindan siler. Bunun icin ilgili kisi secilri ve
     * butona basilir.
     *
     * Generate Random Contact Butonu:
     * .txt dosyasindaki isim ve soyisimlerden rastgele contact nesnesi olusturur ve
     * textfieldlara contact verilerini yazar
     *
     * */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==addButton){           // Kaydet butonu
            System.out.println("add");
            String name = text_name.getText();  // not null
            String surname = text_surname.getText();
            String phone = text_phone.getText();  //not null
            if(name.equals("") || phone.equals("")){
                System.out.println("name or phone can not be empty!!");
                showResultOnLabel(label_result, "name or phone can not be empty!", Color.RED);
                JOptionPane.showMessageDialog(null, "name or phone can not be empty!!");
            }else{
                System.out.println(new Contact(name, surname, phone));
                try{
                    db.add(new Contact(name, surname, phone));
                    c=db.load();
                    showResultOnLabel(label_result, "Contact added to DataBase", Color.GREEN);
                    clearTextFields();
                }catch (Exception ee){
                    System.out.println("cant add contact. "+ee.getMessage());
                }

            }
            loadTable(c, columns);


        }else if(e.getSource() == loadButton){          // Okuma butonu
            try{
                c = db.load();                  // databaseden okunan veriler c arraylistine aktarilir
                loadTable(c, columns);          // c deki veriler tablo ya aktarilir
                showResultOnLabel(label_result2, "Database loaded successfully", Color.GREEN);
            }catch(Exception er){
                System.out.println(er.getMessage());
            }


        }else if(e.getSource() == updateButton){            // Guncelleme Butonu
            int index = table1.getSelectedRow();
            c.get(index).setName(table1.getValueAt(index, 1).toString());       //degisikliy yapilan kolonnun arraylistte karsilik gelen degeri guncellenir
            c.get(index).setSurname(table1.getValueAt(index, 2).toString());
            c.get(index).setPhone_number(table1.getValueAt(index, 3).toString());
            try {
                db.update(c.get(index));        // Db sinifindan update metodu ile ilgili satir guncellenir
                System.out.println(c.get(index).toString());
                showResultOnLabel(label_result2, "Contact "+c.get(index).getName()+" updated", Color.GREEN);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else if(e.getSource() == removeButton){            //Sİlme Butonu
            int[] selections = table1.getSelectedRows();
            for (int i = 0; i < selections.length; i++) {
                Contact co = c.get(selections[i]);
                try{
                    db.remove(c.get(selections[i]).getId());        // DB sinifnden remove metodu ile ilgili satir silinir
                    showResultOnLabel(label_result2, "Contact "+co.getName()+" removed from Database", Color.RED);
                }catch(Exception er){
                    System.out.println(er.getMessage());
                }
            }

            try{
                c.removeAll(c);
                c = db.load();
            }catch(Exception er){
                System.out.println(er.getMessage());
            }


            loadTable(c,columns);
        }else if(e.getSource() == randAddButton){   // Generate Random Contact Butonu:
            Contact c = RandomContact.generateRandomContact();  // rastegele bir contact nesnesi olusturulur
            text_name.setText(c.getName());             // isim textfielda contact nesnesinin ismi yazilir.
            text_surname.setText(c.getSurname());
            text_phone.setText(c.getPhone_number());
            System.out.println(c.toString());
        }
    }


    /**
     * Veri atabnından verileri okur ve arraylist dondurur.
     * Bu arraylisti de 2d diziye cevirir ve Tabloda verileri gosterir.
     * Tabloda 0.kolon(id) haric diger kolonlar duzenlenebilir. id kolonu degistirilemez.
     * */
    public void loadTable(ArrayList<Contact> c, String[] cols){
        data = convertListTo2DArray(c);                             // arraylisti diziye cevirir
        table1.setModel(new DefaultTableModel(data, cols){          // olsuturulan dizi tabloya eklenir sutun basliklarini iceren dizi ile birlikte.
            @Override
            public boolean isCellEditable(int row, int column)      // id column degerleri degistirilemez.
            {
                return column == 1 || column == 2 || column == 3;   // name, surname, tel kolonlari degistirilebirlir.
            }
        });
    }

    /**
     * Kayit sonrasi textfield lar temizlenir
     * */
    public void clearTextFields(){
        for (int i = 0; i < textFields.length; i++) {
            textFields[i].setText("");
        }
    }


    /**
     *Bu fonksiyon Contact nesnesi alan arraylisti 2d diziye cevirir.
     * Bu fonksiyonun olusturulmasinin nedeni Tablonun doldurulmasinda kullanilan
     * metodun(DefaultTableModel(Object[][] array, Object[] arr))
     * parametre olarak 2d dizi almasidir.
     * Kullanim kolayliği acisindan islemlerde arraylist kullanilmistir. Arraylist
     * verilerini tabloya aktarmak icin de bu metod aracılıgı ile diziye donusturulup
     * tabloda gösterilmistir.
     * */
    public String[][] convertListTo2DArray(ArrayList<Contact> c){
        String[][] s = new String[c.size()][columns.length];    // bos bir 2d dizi
        for (int i = 0; i < c.size(); i++) {                    // her contact nesnesinin degerlerini
            s[i][0] = c.get(i).getId().toString();              // diziye aktarir
            s[i][1] = c.get(i).getName();
            s[i][2] = c.get(i).getSurname();
            s[i][3] = c.get(i).getPhone_number();
        }
        return s;                                               // ilgili diziyi dondurur
    }


    /**
     *islemlerin gerceklesip gerceklesmedigini takip edebilmek icin
     * kullaniciyi bilgilendirmek amacli label olusturulmus ve bu etiketler araciligi
     * ile kullanici islemler hakkinda bilgilendirilmistir.
     * Ayni kodu tekrarlamamam ve kodu kisaltmak icin etiketlerin tasarimi bu metodla
     * belirtilmistir
     * */
    public void showResultOnLabel(JLabel l, String message, Color c){
        l.setText(message);     //etiket text belirler
        l.setForeground(c);     // yazi rengini belirler
        l.setVisible(true);     // gorunur yapar
    }

    public static void ShowTime(JLabel lbl) {
        new Timer(0, e -> {
            Date d = new Date();
            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
            lbl.setText(s.format(d));
        }).start();
    }

}

