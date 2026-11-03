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

        loadTransactions();

        runHomeScreen();
    }

    private static void loadTransactions() {
        try {

//            System.out.println("Enter the name of the file to be accessed: ");
//            String fileName = scanner.nextLine();

            BufferedReader bufferedReader = new BufferedReader(new FileReader("transactions.csv"));
            // BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));

            bufferedReader.readLine();

            String field;

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);

            while ((field = bufferedReader.readLine()) != null) {
                String[] fields = bufferedReader.readLine().split("\\|");

                Transaction transaction = new Transaction(LocalDate.parse(fields[0], dateFormatter), LocalTime.parse(fields[1], timeFormatter), fields[2], fields[3], Double.parseDouble(fields[4]));

            }

            for (Transaction transaction : transactions) {
                System.out.println("DATE: " + transaction.getDate() + " | TIME: " + transaction.getTime() + " | DESC: " + transaction.getDescription() + " | VENDOR: " + transaction.getVendor() + " | AMOUNT: " + transaction.getAmount());
            }

            bufferedReader.close();
            //bufferedWriter.close();

        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
            System.out.println("Unable to read the file, please check the location and try again.");

        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println("Unable to write to the file, please check connection and try again.");
        }
    }

    private static void runHomeScreen() {
        String userChoice;

        do {

            System.out.println("""
                    HOME SCREEN
                    D) Add Deposit
                    P) Make Payment
                    L) Ledger
                    X) Exit
                    
                    Please enter your selection: """);

            userChoice = scanner.nextLine().trim().toLowerCase();

            switch (userChoice) {
                case "d" -> addDeposit();
                case "p" -> makePayment();
                case "l" -> runLedgerScreen();
                case "x" -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid selection, please try again from the list above.");
            }
        } while (!userChoice.equals("x"));

    }

    private static void runLedgerScreen() {
        String userChoice;

        do {

            System.out.println("""
                    LEDGER SCREEN
                    A) View All Transactions
                    D) View Deposits
                    P) View Payments
                    R) Reports
                    H) Return to Home
                    
                    Please enter your selection: """);

            userChoice = scanner.nextLine().trim().toLowerCase();

            switch (userChoice) {
                case "a" -> viewAllTransactions();
                case "d" -> viewDeposits();
                case "p" -> viewPayments();
                case "r" -> runReportsScreen();
                case "h" -> System.out.println("Returning to home screen...");
                default -> System.out.println("Invalid selection, please try again from the list above.");
            }
        } while (!userChoice.equals("h"));

    }

    private static void runReportsScreen() {
        String userChoice;

        do {

            System.out.println("""
                    REPORTS SCREEN
                    1) Month to Date
                    2) Previous Month
                    3) Year to Date
                    4) Previous Year
                    5) Search by Vendor
                    0) Return to Ledger
                    
                    Please enter your selection: """);

            userChoice = scanner.nextLine().trim().toLowerCase();

            switch (userChoice) {
                case "1" -> System.out.println("TODO: mtd");
                case "2" -> System.out.println("TODO: prev mth");
                case "3" -> System.out.println("TODO: ytd");
                case "4" -> System.out.println("TODO: prev yr");
                case "5" -> System.out.println("TODO: search by vend");
                case "0" -> System.out.println("Returning to ledger screen...");
                default -> System.out.println("Invalid selection, please try again from the list above.");
            }
        } while (!userChoice.equals("h"));

    }

    private static void addDeposit() {

        System.out.println("Flesh me out!");

    }

    private static void makePayment() {

        System.out.println("Don't forget me!");

    }

    private static ArrayList<Transaction> viewAllTransactions() {
        //FIXME: flesh out
//        for (Transaction transaction : transactions) {
//            return transaction;
//        }

        return transactions;
    }

    private static ArrayList<Transaction> viewDeposits() {
        ArrayList<Transaction> deposits = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                deposits.add(transaction);
            }
        }

        return deposits;

    }

    private static ArrayList<Transaction> viewPayments() {
        ArrayList<Transaction> payments = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                payments.add(transaction);
            }
        }

        return payments;

    }


}
