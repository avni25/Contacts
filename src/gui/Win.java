package gui;

import com.Contact;
import com.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
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
    DB db;

    public Win(){
        setLayout(new FlowLayout());
        setTitle("my Window");
        setLocationRelativeTo(this);
        setSize(700,600);
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
            }else{
                System.out.println(new Contact(name, surname, phone));
                try{
                    db.add(new Contact(name, surname, phone));
                    c=db.load();

                }catch (Exception ee){
                    System.out.println("cant add contact. "+ee.getMessage());
                }

            }
            loadTable(c, columns);
            clearTextFields();

        }else if(e.getSource() == loadButton){
            try{
                c = db.load();
                loadTable(c, columns);
            }catch(Exception er){
                System.out.println(er.getMessage());
            }


        }else if(e.getSource() == updateButton){
            int[] selections = table1.getSelectedRows();
            for (int i = 0; i < selections.length; i++) {
                System.out.println(selections[i]);
            }

        }else if(e.getSource() == removeButton){
            int index = table1.getSelectedRow();
            int[] selections = table1.getSelectedRows();
            try{
                for (int i = 0; i < selections.length; i++) {
                    int s;
                    if(i!=0) db.remove(c.get(selections[i]-1));
                    else db.remove(c.get(selections[i]));
//                    c.remove(selections[s]);
                }

            }catch(Exception er){
                System.out.println(er.getMessage());
            }

            loadTable(c,columns);
        }
    }

    public void loadTable(ArrayList<Contact> c, String[] cols){
        data = convertListTo2DArray(c);
        table1.setModel(new DefaultTableModel(data, cols));
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





}

