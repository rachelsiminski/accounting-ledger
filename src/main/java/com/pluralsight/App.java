package com.pluralsight;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static LocalDate now = LocalDate.now();

    public static void main(String[] args) {

        loadTransactions();
        runHomeScreen();

    }

    private static void loadTransactions() {
        try {

//            System.out.println("Enter the name of the file to be accessed: ");
//            String fileName = scanner.nextLine();

            BufferedReader bufferedReader = new BufferedReader(new FileReader("transactions.csv"));

            bufferedReader.readLine();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] fields = line.split("\\|");

                Transaction transaction = new Transaction(LocalDate.parse(fields[0]), LocalTime.parse(fields[1]), fields[2], fields[3], Double.parseDouble(fields[4]));

                transactions.add(transaction);

            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
            System.out.println("Unable to read the file, please check the location and try again.");

        } catch (IOException e) {
            System.out.println("Error reading file");
            //throw new RuntimeException(e);
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
                case "1" -> viewMonthToDate();
                case "2" -> viewPreviousMonth();
                case "3" -> viewYearToDate();
                case "4" -> viewPreviousYear();
                case "5" -> System.out.println("TODO: search by vend");
                case "0" -> System.out.println("Returning to ledger screen...");
                default -> System.out.println("Invalid selection, please try again from the list above.");
            }
        } while (!userChoice.equals("0"));

    }

    private static void addDeposit() {

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));

            System.out.println("Enter the date of the transaction: (yyyy-mm-dd)");
            LocalDate date = promptForDate();

            System.out.println("Enter the time of the transaction: (hh:mm:ss)");
            LocalTime time = promptForTime();

            System.out.println("Enter the description: ");
            String description = promptForString();

            System.out.println("Enter the vendor: ");
            String vendor = promptForString();

            System.out.println("Enter the amount: ");
            double amount = promptForDouble();

            Transaction transaction = new Transaction(date, time, description, vendor, amount);

            transactions.add(transaction);

            bufferedWriter.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");


            bufferedWriter.close();

        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println("Unable to write to the file, please check connection and try again.");

        }


    }

    private static void makePayment() {

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));

            System.out.println("Enter the date of the transaction: (yyyy-mm-dd)");
            LocalDate date = promptForDate();

            System.out.println("Enter the time of the transaction: (hh:mm:ss)");
            LocalTime time = promptForTime();

            System.out.println("Enter the description: ");
            String description = promptForString();

            System.out.println("Enter the vendor: ");
            String vendor = promptForString();

            System.out.println("Enter the amount: ");
            double amount = promptForDouble();

            amount *= -1;

            Transaction transaction = new Transaction(date, time, description, vendor, amount);

            transactions.add(transaction);

            bufferedWriter.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");


            bufferedWriter.close();

        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println("Unable to write to the file, please check connection and try again.");

        }

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

    private static void viewMonthToDate() {

        displayTransactions(filterMonthToDate());

    }

    private static void viewPreviousMonth() {

        displayTransactions(filterPreviousMonth());

    }

    private static void viewYearToDate() {

        displayTransactions(filterYearToDate());

    }

    private static void viewPreviousYear() {

        displayTransactions(filterPreviousYear());

    }

    private static void searchByVendor() {

        displayTransactions(filterByVendor());

    }

    private static String promptForString() {
        String userString = scanner.nextLine();

        if (userString.isEmpty()) {
            System.out.println("Input not recorded, please try again: ");
            userString = scanner.nextLine();
        }

        return userString;

    }

    private static double promptForDouble() {
        String userDouble = null;

        try {
            userDouble = scanner.nextLine();

            if (userDouble.isEmpty()) {

                System.out.println("Input not recorded, please try again: ");
                userDouble = scanner.nextLine();

            }

            if (userDouble.equals("0") || userDouble.contains("-")) {

                System.out.println("Please enter a positive number.");
                userDouble = scanner.nextLine();

            }
        } catch (NumberFormatException e) {

            System.out.println("Please enter a number");
            //throw new RuntimeException(e);

        }

        return Double.parseDouble(userDouble);

    }

    private static LocalDate promptForDate() {

        DateTimeFormatter dateFormatter = null;
        String userDate = null;

        try {

            dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            userDate = scanner.nextLine();

            if (userDate.isEmpty()) {

                System.out.println("Input not recorded, please try again: ");
                userDate = scanner.nextLine();

            }

        } catch (DateTimeException e) {

            System.out.println("Please enter a date in the format yyyy-mm-dd");
            //throw new RuntimeException(e);

        }

        return LocalDate.parse(userDate, dateFormatter);
    }

    private static LocalTime promptForTime() {

        DateTimeFormatter timeFormatter = null;
        String userTime = null;

        try {

            timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            userTime = scanner.nextLine();

            if (userTime.isEmpty()) {

                System.out.println("Input not recorded, please try again: ");
                userTime = scanner.nextLine();

            }

        } catch (Exception e) {

            System.out.println("Please enter the time in the format hh:mm:ss");
            //throw new RuntimeException(e);

        }

        return LocalTime.parse(userTime, timeFormatter);
    }

    private static ArrayList<Transaction> filterMonthToDate() {

        ArrayList<Transaction> monthToDateTransactions = new ArrayList<>();

        System.out.println(" - Month to Date - ");


        for (Transaction transaction : transactions) {

            if (now.getMonth() == transaction.getDate().getMonth() && now.getYear() == transaction.getDate().getYear()) {

                monthToDateTransactions.add(transaction);

            }

        }

        return monthToDateTransactions;

    }

    private static ArrayList<Transaction> filterPreviousMonth() {

        ArrayList<Transaction> previousMonthTransactions = new ArrayList<>();

        LocalDate previousMonth = LocalDate.now().minusMonths(1);

        System.out.println(" - Previous Month - ");

        for (Transaction transaction : transactions) {

            if (previousMonth.getMonth() == transaction.getDate().getMonth() && previousMonth.getYear() == transaction.getDate().getYear()) {

                previousMonthTransactions.add(transaction);

            }
        }

        return previousMonthTransactions;
    }

    private static ArrayList<Transaction> filterYearToDate() {

        ArrayList<Transaction> yearToDateTransactions = new ArrayList<>();

        System.out.println(" - Year to Date - ");


        for (Transaction transaction : transactions) {

            if (now.getYear() == transaction.getDate().getYear()) {

                yearToDateTransactions.add(transaction);

            }

        }

        return yearToDateTransactions;

    }

    private static ArrayList<Transaction> filterPreviousYear() {

        ArrayList<Transaction> previousYearTransactions = new ArrayList<>();

        LocalDate previousYear = LocalDate.now().minusYears(1);

        System.out.println(" - Previous Year - ");

        for (Transaction transaction : transactions) {

            if (previousYear.getYear() == transaction.getDate().getYear()) {

                previousYearTransactions.add(transaction);

            }

        }

        return previousYearTransactions;
    }

    private static ArrayList<Transaction> filterByVendor() {

        ArrayList<Transaction> vendors = new ArrayList<>();

        System.out.println("Enter the vendor you'd like to search by: ");
        String vendor = promptForString();

        for (Transaction transaction : transactions) {

            if (transaction.getVendor().equals(vendor)) {

                vendors.add(transaction);
            }
        }

        return vendors;

    }

    private static void displayTransactions(ArrayList<Transaction> transactions) {

        //

        pressEnterToContinue();
    }

    private static void pressEnterToContinue() {
        System.out.println();
        System.out.println("Press enter to return to the menu...");
        scanner.nextLine();
    }

}
