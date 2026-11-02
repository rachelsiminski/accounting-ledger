package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static ArrayList<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {

        try {

//            System.out.println("Enter the name of the file to be accessed: ");
//            String fileName = scanner.nextLine();

            BufferedReader bufferedReader = new BufferedReader(new FileReader("transactions.csv"));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));

            bufferedReader.readLine();

            String field;

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);

            while ((field = bufferedReader.readLine()) != null) {
                String[] fields = bufferedReader.readLine().split("\\|");

                // Transaction transaction = new Transaction(LocalDateTime.parse(fields[0]), LocalDateTime.parse(fields[1]), fields[2], fields[3], Double.parseDouble(fields[4]));
                System.out.println(LocalDate.parse(fields[0], dateFormatter));
                System.out.println(LocalTime.parse(fields[1], timeFormatter));
                System.out.println(fields[2]);
                System.out.println(fields[3]);
                System.out.println(fields[4]);
                System.out.println("------");
            }

//            for (Transaction transaction : transactions) {
//                System.out.println("DATE: " + transaction.getDate() + " | TIME: " + transaction.getTime() + " | DESC: " + transaction.getDescription() + " | VENDOR: " + transaction.getVendor() + " | AMOUNT: " + transaction.getAmount());
//            }

            bufferedReader.close();
            bufferedWriter.close();

        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
            System.out.println("Unable to read the file, please check the location and try again.");

        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println("Unable to write to the file, please check connection and try again.");
        }
    }

}
