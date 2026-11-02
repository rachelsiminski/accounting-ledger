package com.pluralsight;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static ArrayList<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {

        try {

            System.out.println("Enter the name of the file to be accessed: ");
            String fileName = scanner.nextLine();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));

            bufferedReader.readLine();

            String field;

            while ((field = bufferedReader.readLine()) != null) {
                String[] fields = bufferedReader.readLine().split("\\|");

                Transaction transaction = new Transaction(LocalDateTime.parse(fields[0]), LocalDateTime.parse(fields[1]), fields[2], fields[3], Double.parseDouble(fields[4]));

            }

            for (Transaction transaction : transactions) {
                System.out.println("DATE: " + transaction.getDate() + " | TIME: " + transaction.getTime() + " | DESC: " + transaction.getDescription() + " | VENDOR: " + transaction.getVendor() + " | AMOUNT: " + transaction.getAmount());
            }

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
