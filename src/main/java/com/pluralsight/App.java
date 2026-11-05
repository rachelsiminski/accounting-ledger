package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final LocalDate dateNow = LocalDate.now();
    private static final LocalTime timeNow = LocalTime.now();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {

        displayBanner();
        loadTransactions();
        runHomeScreen();

        scanner.close();

    }

    private static void loadTransactions() {
        try {

//            System.out.println("Enter the name of the file to be accessed: ");
//            String fileName = scanner.nextLine();

            BufferedReader bufferedReader = new BufferedReader(new FileReader("transactions.csv"));

            bufferedReader.readLine();

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                if (!line.isBlank()) {

                    String[] fields = line.split("\\|");

                    Transaction transaction = new Transaction(LocalDate.parse(fields[0]), LocalTime.parse(fields[1]), fields[2], fields[3], Double.parseDouble(fields[4]));

                    transactions.add(transaction);

                }

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

    private static void displayBanner() {

        System.out.println("""
                PERSONAL LEDGER
                
                Welcome to the personal budgeting ledger! Enter the name of the transactions file to be accessed by the program.
                Each menu will list the options available""");

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
                case "5" -> searchByVendor();
                case "0" -> System.out.println("Returning to ledger screen...");
                default -> System.out.println("Invalid selection, please try again from the list above.");
            }

        } while (!userChoice.equals("0"));

    }

    private static void addDeposit() {

        try {

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));

            System.out.println("Enter the description: ");
            String description = promptForString();

            System.out.println("Enter the vendor: ");
            String vendor = promptForString();

            System.out.println("Enter the amount: ");
            double amount = promptForDouble();

            Transaction transaction = new Transaction(dateNow, LocalTime.parse(timeNow.format(formatter)), description, vendor, amount);

            transactions.add(transaction);

            bufferedWriter.write(dateNow + "|" + timeNow + "|" + description + "|" + vendor + "|" + amount + "\n");

            System.out.printf("Deposit recorded: %s | %s | $ %.2f%n", transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

            bufferedWriter.close();

        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println("Unable to write to the file, please check connection and try again.");

        }

        pressEnterToContinue();

    }

    private static void makePayment() {

        try {

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));

            System.out.println("Enter the description: ");
            String description = promptForString();

            System.out.println("Enter the vendor: ");
            String vendor = promptForString();

            System.out.println("Enter the amount: ");
            double amount = promptForDouble();

            amount *= -1;

            Transaction transaction = new Transaction(dateNow, LocalTime.parse(timeNow.format(formatter)), description, vendor, amount);

            transactions.add(transaction);

            bufferedWriter.write(dateNow + "|" + timeNow + "|" + description + "|" + vendor + "|" + amount + "\n");

            System.out.printf("Payment recorded: %s | %s | $ %.2f%n", transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

            bufferedWriter.close();

        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println("Unable to write to the file, please check connection and try again.");

        }

        pressEnterToContinue();

    }

    private static void viewAllTransactions() {

        displayTransactionsAsTable(transactions);

        pressEnterToContinue();

    }

    private static void viewDeposits() {

        displayTransactionsAsTable(filterDeposits());

        pressEnterToContinue();

    }

    private static void viewPayments() {

        displayTransactionsAsTable(filterPayments());

        pressEnterToContinue();

    }

    private static void viewMonthToDate() {

        displayTransactionsAsTable(filterMonthToDate());

        pressEnterToContinue();

    }

    private static void viewPreviousMonth() {

        displayTransactionsAsTable(filterPreviousMonth());

        pressEnterToContinue();

    }

    private static void viewYearToDate() {

        displayTransactionsAsTable(filterYearToDate());

        pressEnterToContinue();

    }

    private static void viewPreviousYear() {

        displayTransactionsAsTable(filterPreviousYear());

        pressEnterToContinue();

    }

    private static void searchByVendor() {

        displayTransactionsAsTable(filterByVendor());

        pressEnterToContinue();

    }

    private static String promptForString() {
        String userString = scanner.nextLine();

        if (userString.isEmpty()) {

            System.out.println("Input not recorded, please try again: ");
            userString = scanner.nextLine().trim().toLowerCase();

        }
        if (userString.contains("|")) {

            System.out.println("Please do not use '|', try again.");
            userString = scanner.nextLine().trim().toLowerCase();

        }

        return userString;

    }

    private static double promptForDouble() {
        String amount = null;

        while (true) {
            try {
                amount = scanner.nextLine();

                if (amount.isEmpty()) {

                    System.out.println("Input not recorded, please try again: ");
                    amount = scanner.nextLine();

                }

                if (amount.equals("0") || amount.contains("-")) {

                    System.out.println("Please enter a positive number.");
                    amount = scanner.nextLine();

                }

                return Double.parseDouble(amount);

            } catch (NumberFormatException e) {

                System.out.println("Please enter a number");
                //throw new RuntimeException(e);

            }

        }

    }

    private static ArrayList<Transaction> filterDeposits() {

        ArrayList<Transaction> deposits = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                deposits.add(transaction);
            }
        }

        return deposits;

    }

    private static ArrayList<Transaction> filterPayments() {

        ArrayList<Transaction> payments = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                payments.add(transaction);
            }
        }

        return payments;

    }

    private static ArrayList<Transaction> filterMonthToDate() {

        ArrayList<Transaction> monthToDateTransactions = new ArrayList<>();

        System.out.println(" - Month to Date - ");


        for (Transaction transaction : transactions) {

            if (dateNow.getMonth() == transaction.getDate().getMonth() && dateNow.getYear() == transaction.getDate().getYear()) {

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

            if (dateNow.getYear() == transaction.getDate().getYear()) {

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

            if (transaction.getVendor().toLowerCase().equals(vendor)) {

                vendors.add(transaction);

            }

        }

        if (vendors.isEmpty()) {
            System.out.println("There are no transactions that match this vendor. Please try again.");
        }

        return vendors;

    }

    private static void displayTransactionsAsTable(ArrayList<Transaction> transactions) {
        // Used Claude to help me with formatting so each section is a set size
        // This method is drafted using Claude, revised by me for accuracy and judgment

        if (!transactions.isEmpty()) {

            System.out.printf("%40s", "TRANSACTIONS");
            System.out.println();
            System.out.println();
            System.out.printf("|%s|%s|%s|%s|%n", center("DATE", 12), center("DESCRIPTION", 26), center("VENDOR", 20), center("AMOUNT", 14));

            for (Transaction transaction : transactions) {

                System.out.printf("|%s|%s|%s| $%s|%n", center(transaction.getDate().toString(), 12), center(transaction.getDescription(), 26), center(transaction.getVendor(), 20), center(String.format("%.2f", transaction.getAmount()), 12));

            }

        }

    }

    private static String center(String text, int width) {
        // Used Claude to help me with formatting so each section is a set size
        // This method is drafted using Claude, revised by me for accuracy and judgment
        int leftover = width - text.length();

        if (leftover <= 0) {

            return text;

        }

        int left = leftover / 2;
        int right = leftover - left;

        return " ".repeat(left) + text + " ".repeat(right);

    }

    private static void pressEnterToContinue() {
        System.out.println();
        System.out.println("Press enter to return to the menu...");
        scanner.nextLine();
    }

}
