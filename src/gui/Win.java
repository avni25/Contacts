package gui;

import com.Contact;
import com.DB;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;


public class Win extends JFrame implements ActionListener{
    private JTextField text_name;
    private JTextField text_surname;
    private JTextField text_phone;
    private JButton addButton;

    private JButton loadButton;
    private JButton updateButton;
    private JButton removeButton;
    private JPanel mypanel;
    private JScrollPane sp;
    JButton[] buttons = {addButton, loadButton, updateButton, removeButton};
    JTextField[] textFields = {text_name, text_surname, text_phone};
    public String[] columns = {"contac_id", "name", "surname", "phone_number"};
    public String[][] data;
    public ArrayList<Contact> c;
    public JTable table1;
    private JLabel label_result;
    private JLabel label_result2;
    DB db;

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
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==addButton){
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


        }else if(e.getSource() == loadButton){
            try{
                c = db.load();
                loadTable(c, columns);
                showResultOnLabel(label_result2, "Database loaded successfully", Color.GREEN);
            }catch(Exception er){
                System.out.println(er.getMessage());
            }


        }else if(e.getSource() == updateButton){
            int index = table1.getSelectedRow();
            c.get(index).setName(table1.getValueAt(index, 1).toString());
            c.get(index).setSurname(table1.getValueAt(index, 2).toString());
            c.get(index).setPhone_number(table1.getValueAt(index, 3).toString());
            try {
                db.update(c.get(index));
                System.out.println(c.get(index).toString());
                showResultOnLabel(label_result2, "Contact "+c.get(index).getName()+" updated", Color.GREEN);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else if(e.getSource() == removeButton){
            int index = table1.getSelectedRow();
//            int[] selections = table1.getSelectedRows();
            Contact co = c.get(index);
            try{
                db.remove(c.get(index));
                c.remove(index);
                showResultOnLabel(label_result2, "Contact "+co.getName()+" removed from Database", Color.RED);
            }catch(Exception er){
                System.out.println(er.getMessage());
            }

            loadTable(c,columns);
        }
    }

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

    public void clearTextFields(){
        for (int i = 0; i < textFields.length; i++) {
            textFields[i].setText("");
        }
    }

    public String[][] convertListTo2DArray(ArrayList<Contact> c){
        String[][] s = new String[c.size()][columns.length];
        for (int i = 0; i < c.size(); i++) {
            s[i][0] = c.get(i).getId().toString();
            s[i][1] = c.get(i).getName();
            s[i][2] = c.get(i).getSurname();
            s[i][3] = c.get(i).getPhone_number();
        }
        return s;
    }

    public void showResultOnLabel(JLabel l, String message, Color c){
        l.setText(message);
        l.setForeground(c);
        l.setVisible(true);
    }



}

