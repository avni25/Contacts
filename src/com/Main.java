package com;

import gui.Win;

import java.sql.*;
import java.util.List;


public class Main {

    public static void main(String[] args) throws SQLException {

//        DB db = new DB();
//        db.connect(DB.DB_NAME);
//
//        Contact avni = new Contact("asd","zxc","0000");
////      db.add(avni);
//        ArrayList<Contact> contacts = db.load();
////      db.remove(contacts.get(2));
//        contacts.get(0).setName("eee");
//        System.out.println(contacts.get(0).toString());
//        db.update(contacts.get(0));
        Win win = new Win();

//        db.close();

    }


    public static void print(List<Contact> c){
        for (int i = 0; i < c.size(); i++) {
            System.out.println(c.get(i).toString());
        }
    }

}
