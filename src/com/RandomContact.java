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

    /**
     * rastgele contact nesnesi olusturur .txt dosyalarındaki isimlerden
     * */
    public static RandomContact generateRandomContact(){
        RandomContact rc = new RandomContact();                             // bos bir contact olusturulur
        ArrayList<String> names = readFile("src\\names.txt");       // names.txt dosaysındaki isimleri listeye aktarir
        ArrayList<String> surnames = readFile("src\\surnames.txt"); // surnames.txt dosaysındaki isimleri listeye aktarir

        rc.setName(names.get((int) (Math.random() * names.size())));        // names listesinden rastgele bir isim secilir ve olusturulan bos contact nesneinin ismine atanir
        rc.setSurname(surnames.get((int) (Math.random() * surnames.size())));   // surnames listesinden rastgele bir soyisim secilir ve olusturulan bos contact nesneinin soyismine atanir
        rc.setPhone_number(generatePhoneNumber(7));     //rastgele bir telefon numarasi urretirilir ve nesnenin telefon degiskenine atanir.
        return rc;
    }

    /**
     * rastgele bir telefon numarasi dondurur
     * @param len: istenilen telefon numarasinin uzunlugu
     * @return String: telefon numarasi
     * */
    public static String generatePhoneNumber(int len){
        String s ="";
        for (int i = 0; i < len; i++) {
            s += (int)(Math.random()*10);   // 0-9 da dahil arasında rastgele sayi uretir
        }

        return s;
    }

    /**
     * .txt dosyasındaki verileri arrayliset aktarir.
     * @param filePath: okunacak dosyanin dosya yolu
     * @return dosyadaki verileri barindiran arraylist dondurur.
     * @exception FileNotFoundException: belirtilen isimde dosya yoksa hata verir.
     *
     * */
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
