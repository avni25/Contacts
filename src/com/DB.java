package com;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bu sinif SQLİte ile database ile baglantilari olusturmak icin olusturulmustur.
 * İcerisinde connect, close, add, remove, update methodlari mevcuttur.
 * connect metodu ile ilgili database dosyasina baglanir.
 *
 * İlgili databse dosyasi c:/contacts.db olarak tanimlanmis ve konumlandirilmistir.
 * Ayrica SQLite baglantisi icin sqlite-jdbc-3.27.2.1.jar dosyasi projenin kutuphanesine eklenmistir.
 * add metodu ile contact objesi ilgili databse de belirtilen tabloya eklenir.
 * remove metodu ile istenen contact objesi databaseden silinir.
 * update metodu ile istenen contac objesinin verileri databasede guncellenir.
 *
 * ilgili databe dosyasi, dosya yolu, tablo adi ve
 * tablo icerisindeki kolonlarin isimleri ve
 * kurulacak baglanti objesi global olarak tanımlanmistir.
 *
 *
 *
 * */
public class DB {

    public static final String DB_NAME ="Contacts.db";
    public static final String fileURL="jdbc:sqlite:C://";

    public static final String TABLE_NAME ="contacts";
    public static final String COLUMN_ID ="contact_id";
    public static final String COLUMN_NAME ="name";
    public static final String COLUMN_SURNAME ="surname";
    public static final String COLUMN_PHONE_NUMBER ="phone_number";
    public static Connection conn;

    public DB(){

    }

    public void connect(String database_name){
        try{
            conn = DriverManager.getConnection(fileURL+database_name);
            System.out.println("connected to databse");
        }catch(Exception e){
            System.out.println("cant connect to database"+e.getMessage());
        }
    }

    public void close(){
        try{
            if(conn!=null) conn.close();
        }catch(SQLException e){
            System.out.println("cant close.. "+e.getMessage());
        }
    }


    /**
     * DB'e Contact nesnesi eklerken contac id girmeye gerek yoktur. Cunku olusturulan
     * db tablosunda contact_id kolonu primary key olarak atanmis ve otomatik olarak
     * (bir artırilarak) belirlenmektedir.
     * */
    public void add(Contact c)throws SQLException{
        Statement st = conn.createStatement();

        st.execute("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+
                " ("+COLUMN_NAME+" TEXT, "+COLUMN_SURNAME+" TEXT, "+COLUMN_PHONE_NUMBER+" TEXT)");

        try{
            st.execute("INSERT INTO "+TABLE_NAME+"("+COLUMN_NAME+", "+COLUMN_SURNAME+", "+COLUMN_PHONE_NUMBER+")"+
                    " values('"+c.getName()+"', '"+c.getSurname()+"', '"+c.getPhone_number()+"')");
            System.out.println("Conact "+c.getName()+" "+c.getSurname()+" has been added to DataBase");
        }catch (Exception e){
            System.out.println("contact "+c.getName()+" "+c.getSurname()+" can't be added to Database!!!");
        }

        st.close();
    }

    public ArrayList<Contact> load(){
        try(Statement st = conn.createStatement();
            ResultSet r = st.executeQuery("SELECT * FROM "+TABLE_NAME)){
            ArrayList<Contact> contactList = new ArrayList<>();
            while(r.next()){
                Contact c = new Contact();
                c.setId(r.getInt(COLUMN_ID));
                c.setName(r.getString(COLUMN_NAME));
                c.setSurname(r.getString(COLUMN_SURNAME));
                c.setPhone_number(r.getString(COLUMN_PHONE_NUMBER));
                contactList.add(c);
            }
            for (int i = 0; i < contactList.size(); i++) {
                System.out.println(contactList.get(i).toString());
            }

            return contactList;
        }catch(SQLException e){
            System.out.println("cant load list.." +e.getMessage());
            return null;
        }
    }

    public void update(Contact c) throws SQLException {
        /*
         * UPDATE table_name
         * SET column1 = value1, column2 = value2, ...
         * WHERE condition;
         *
         * UPDATE contacts
         * SET name = 'val1', surname = 'val2', phone_number = 'val3'
         * WHERE contact_id = 1;
         * */
        Statement st = conn.createStatement();
        try {
            st.execute( "UPDATE "+TABLE_NAME+" " +
                            "SET "+COLUMN_NAME+" = '"+c.getName()+"', " +
                                  ""+COLUMN_SURNAME+" = '"+c.getSurname()+"', " +
                                  ""+COLUMN_PHONE_NUMBER+" = '"+c.getPhone_number()+"' " +
                            "WHERE "+COLUMN_ID+"= "+c.getId()+"");
            System.out.println("contact has been updated...");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("coulnt update contact info");
        }
        st.close();
    }

    public void remove(Contact c) throws SQLException {
        //DELETE FROM table_name WHERE condition;
        //DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';
        Statement st = conn.createStatement();
        try{
            st.execute("DELETE FROM "+TABLE_NAME+" WHERE "+COLUMN_ID+" = "+c.getId()+"");
            System.out.println("contact "+c.getName()+" removed!");
        }catch (Exception e){
            System.out.println("cant remove contact!! "+e.getMessage());
        }

        st.close();
    }

    public void remove(int id) throws SQLException {
        //DELETE FROM table_name WHERE condition;
        //DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';
        Statement st = conn.createStatement();
        try{
            st.execute("DELETE FROM "+TABLE_NAME+" WHERE "+COLUMN_ID+" = "+id+"");
            System.out.println("contact no "+id+" removed!");
        }catch (Exception e){
            System.out.println("cant remove contact!! "+e.getMessage());
        }

        st.close();
    }


}
