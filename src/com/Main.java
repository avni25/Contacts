package com;

import gui.Win;

import java.sql.*;
import java.util.List;


public class Main {

    public static void main(String[] args) throws SQLException {


        Win win = new Win();





    }


    public static void print(List<Contact> c){
        for (int i = 0; i < c.size(); i++) {
            System.out.println(c.get(i).toString());
        }
    }

}
