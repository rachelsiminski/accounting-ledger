package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {

        try {

            displayBanner();
            loadTransactions();
            runHomeScreen();

        } finally {

            scanner.close();

        }

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

                    if (fields.length != 5) {

                        System.out.println("Skipping malformed line: " + line);

                    }

                    try {

                        Transaction transaction = new Transaction(LocalDate.parse(fields[0]), LocalTime.parse(fields[1]), fields[2], fields[3], Double.parseDouble(fields[4]));
                        transactions.add(transaction);

                    } catch (DateTimeParseException | NumberFormatException e) {

                        System.out.println("Skipping invalid transaction line: " + line);
                        //throw new RuntimeException(e);

                    }

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
                 - PERSONAL LEDGER -
                
                Welcome to the personal budgeting ledger!
                Enter the name of the transactions file to be accessed by the program.
                Each menu will list the options available.""");

        System.out.println();

    }

    private static void runHomeScreen() {
        String userChoice;

        do {

            System.out.println("""
                     - HOME SCREEN -
                     
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
                default -> System.out.println("Invalid selection, please try again from the list above.\n");
            }

        } while (!userChoice.equals("x"));

    }

    private static void runLedgerScreen() {
        String userChoice;

        do {

            System.out.println("""
                     - LEDGER SCREEN -
                     
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
                case "h" -> System.out.println("Returning to home screen...\n");
                default -> System.out.println("Invalid selection, please try again from the list above.\n");
            }
        } while (!userChoice.equals("h"));

    }

    private static void runReportsScreen() {
        String userChoice;

        do {

            System.out.println("""
                     - REPORTS SCREEN -
                     
                    1) Month to Date
                    2) Previous Month
                    3) Year to Date
                    4) Previous Year
                    5) Search by Vendor
                    0) Return to Ledger
                    
                    Please enter your selection: """);

            userChoice = scanner.nextLine().trim();

            switch (userChoice) {
                case "1" -> viewMonthToDate();
                case "2" -> viewPreviousMonth();
                case "3" -> viewYearToDate();
                case "4" -> viewPreviousYear();
                case "5" -> searchByVendor();
                case "0" -> System.out.println("Returning to ledger screen...\n");
                default -> System.out.println("Invalid selection, please try again from the list above.\n");
            }

        } while (!userChoice.equals("0"));

    }

    private static void addDeposit() {

        try {

            LocalDate dateNow = LocalDate.now();
            LocalTime timeNow = LocalTime.now();

            System.out.println("Enter the description: ");
            String description = promptForString();

            System.out.println("Enter the vendor: ");
            String vendor = promptForString();

            System.out.println("Enter the amount: ");
            double amount = promptForDouble();

            Transaction transaction = new Transaction(dateNow, LocalTime.parse(timeNow.format(formatter)), description, vendor, amount);

            transactions.add(transaction);

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));

            bufferedWriter.write(dateNow + "|" + timeNow.format(formatter) + "|" + description + "|" + vendor + "|" + amount + "\n");

            bufferedWriter.close();

            System.out.printf("Deposit recorded: %s | %s | $ %.2f%n", transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            System.out.println();

        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println("Unable to write to the file, please check connection and try again.\n");

        }

        pressEnterToContinue();

    }

    private static void makePayment() {

        try {

            LocalDate dateNow = LocalDate.now();
            LocalTime timeNow = LocalTime.now();

            System.out.println("Enter the description: ");
            String description = promptForString();

            System.out.println("Enter the vendor: ");
            String vendor = promptForString();

            System.out.println("Enter the amount: ");
            double amount = promptForDouble();

            amount *= -1;

            Transaction transaction = new Transaction(dateNow, LocalTime.parse(timeNow.format(formatter)), description, vendor, amount);

            transactions.add(transaction);

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));

            bufferedWriter.write(dateNow + "|" + timeNow.format(formatter) + "|" + description + "|" + vendor + "|" + amount + "\n");

            bufferedWriter.close();

            System.out.printf("Payment recorded: %s | %s | $ %.2f%n", transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println("Unable to write to the file, please check connection and try again.");

        }

        pressEnterToContinue();

    }

    private static void viewAllTransactions() {

        System.out.println("                                     - Transactions -");
        displayTransactionsAsTable(transactions);

        pressEnterToContinue();

    }

    private static void viewDeposits() {

        System.out.println("                                     - Deposits -");

        displayTransactionsAsTable(filterDeposits());

        pressEnterToContinue();

    }

    private static void viewPayments() {

        System.out.println("                                     - Payments -");

        displayTransactionsAsTable(filterPayments());

        pressEnterToContinue();

    }

    private static void viewMonthToDate() {

        System.out.println("                                     - Month to Date -");

        displayTransactionsAsTable(filterMonthToDate());

        pressEnterToContinue();

    }

    private static void viewPreviousMonth() {

        System.out.println("                                     - Previous Month -");

        displayTransactionsAsTable(filterPreviousMonth());

        pressEnterToContinue();

    }

    private static void viewYearToDate() {

        System.out.println("                                     - Year to Date -");

        displayTransactionsAsTable(filterYearToDate());

        pressEnterToContinue();

    }

    private static void viewPreviousYear() {

        System.out.println("                                     - Previous Year -");

        displayTransactionsAsTable(filterPreviousYear());

        pressEnterToContinue();

    }

    private static void searchByVendor() {

        System.out.println("                                     - Search by Vendor -");

        displayTransactionsAsTable(filterByVendor());

        pressEnterToContinue();

    }

    private static String promptForString() {
        String userString = null;

        while (true) {

            userString = scanner.nextLine().trim();

            if (userString.isEmpty()) {

                System.out.println("Input not recorded, please try again: ");
                continue;

            }
            if (userString.contains("|")) {

                System.out.println("Please do not use '|', try again.");
                continue;

            }

            return userString;

        }

    }

    private static double promptForDouble() {
        String amount = null;

        while (true) {

            try {

                amount = scanner.nextLine();

                if (amount.isEmpty()) {

                    System.out.println("Input not recorded, please try again: ");
                    continue;

                }

                if (amount.equals("0") || amount.contains("-")) {

                    System.out.println("Please enter a positive number.");
                    continue;

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

        if (deposits.isEmpty()) {

            System.out.println("No transactions found");

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

        if (payments.isEmpty()) {

            System.out.println("No transactions found");

        }

        return payments;

    }

    private static ArrayList<Transaction> filterMonthToDate() {

        ArrayList<Transaction> monthToDateTransactions = new ArrayList<>();
        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now();

        for (Transaction transaction : transactions) {

            if (dateNow.getMonth() == transaction.getDate().getMonth() && dateNow.getYear() == transaction.getDate().getYear()) {

                monthToDateTransactions.add(transaction);

            }

        }

        if (monthToDateTransactions.isEmpty()) {

            System.out.println("No transactions found");

        }

        return monthToDateTransactions;

    }

    private static ArrayList<Transaction> filterPreviousMonth() {

        ArrayList<Transaction> previousMonthTransactions = new ArrayList<>();

        LocalDate previousMonth = LocalDate.now().minusMonths(1);

        for (Transaction transaction : transactions) {

            if (previousMonth.getMonth() == transaction.getDate().getMonth() && previousMonth.getYear() == transaction.getDate().getYear()) {

                previousMonthTransactions.add(transaction);

            }

        }

        if (previousMonthTransactions.isEmpty()) {

            System.out.println("No transactions found");

        }

        return previousMonthTransactions;
    }

    private static ArrayList<Transaction> filterYearToDate() {

        ArrayList<Transaction> yearToDateTransactions = new ArrayList<>();

        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now();

        for (Transaction transaction : transactions) {

            if (dateNow.getYear() == transaction.getDate().getYear()) {

                yearToDateTransactions.add(transaction);

            }

        }

        if (yearToDateTransactions.isEmpty()) {

            System.out.println("No transactions found");

        }

        return yearToDateTransactions;

    }

    private static ArrayList<Transaction> filterPreviousYear() {

        ArrayList<Transaction> previousYearTransactions = new ArrayList<>();

        LocalDate previousYear = LocalDate.now().minusYears(1);

        for (Transaction transaction : transactions) {

            if (previousYear.getYear() == transaction.getDate().getYear()) {

                previousYearTransactions.add(transaction);

            }

        }

        if (previousYearTransactions.isEmpty()) {

            System.out.println("No transactions found");

        }

        return previousYearTransactions;
    }

    private static ArrayList<Transaction> filterByVendor() {

        ArrayList<Transaction> vendors = new ArrayList<>();

        System.out.println("Enter the vendor you'd like to search by: ");
        String vendor = promptForString();

        for (Transaction transaction : transactions) {

            if (transaction.getVendor().equalsIgnoreCase(vendor)) {

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

            System.out.println();
            System.out.println();
            System.out.printf("|%s|%s|%s|%s|%s|%n", center("DATE", 12), center("TIME", 12), center("DESCRIPTION", 26), center("VENDOR", 20), center("AMOUNT", 14));

            for (Transaction transaction : transactions) {

                System.out.printf("|%s|%s|%s|%s| $%s|%n", center(transaction.getDate().toString(), 12), center(transaction.getTime().toString(), 12), center(transaction.getDescription(), 26), center(transaction.getVendor(), 20), center(String.format("%.2f", transaction.getAmount()), 12));

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
