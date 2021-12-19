package com;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class RandomContact extends Contact{

    public RandomContact() {
    }

    public RandomContact(String name, String surname, String phone_number) {
        super(name, surname, phone_number);
    }


    public static RandomContact generateRandomContact(){
        RandomContact rc = new RandomContact();
        ArrayList<String> names = readFile("src\\names.txt");
        ArrayList<String> surnames = readFile("src\\surnames.txt");

        rc.setName(names.get((int) (Math.random() * names.size())));
        rc.setSurname(surnames.get((int) (Math.random() * surnames.size())));
        rc.setPhone_number(generatePhoneNumber(7));
        return rc;
    }

    public static String generatePhoneNumber(int len){
        String s ="";
        for (int i = 0; i < len; i++) {
            s += (int)(Math.random()*10);
        }

        return s;
    }

    public static ArrayList<String> readFile(String filePath){
        ArrayList<String> arr = new ArrayList<>();
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                arr.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return arr;
    }


    public static void print(ArrayList<String> arr){
        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i));
        }
    }

}
