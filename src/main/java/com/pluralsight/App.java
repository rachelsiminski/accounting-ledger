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
            System.out.println("Enter the name of the file to be accessed: ");
            String fileName = promptForString();

            System.out.println("Accessing file...");
            System.out.println();
            System.out.println();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
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
                    }
                }
            }
            bufferedReader.close();

            System.out.println("File read successful!");
            System.out.println();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to read the file, please check the location and try again.");
        } catch (IOException e) {
            System.out.println("Error reading file");
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
                    B) Balance
                    X) Exit
                    
                    Please enter your selection: """);
            userChoice = scanner.nextLine().trim().toLowerCase();

            switch (userChoice) {
                case "d" -> addDeposit();
                case "p" -> makePayment();
                case "l" -> runLedgerScreen();
                case "b" -> viewBalance();
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
                    6) Custom Search
                    0) Return to Ledger
                    
                    Please enter your selection: """);
            userChoice = scanner.nextLine().trim();

            switch (userChoice) {
                case "1" -> viewMonthToDate();
                case "2" -> viewPreviousMonth();
                case "3" -> viewYearToDate();
                case "4" -> viewPreviousYear();
                case "5" -> searchByVendor();
                case "6" -> customSearch();
                case "0" -> System.out.println("Returning to ledger screen...\n");
                default -> System.out.println("Invalid selection, please try again from the list above.\n");
            }
        } while (!userChoice.equals("0"));
    }

    private static void addTransaction(boolean isDeposit) {
        try {
            LocalDate dateNow = LocalDate.now();
            LocalTime timeNow = LocalTime.now();

            System.out.println("Enter the description: ");
            String description = promptForString();

            System.out.println("Enter the vendor: ");
            String vendor = promptForString();

            System.out.println("Enter the amount: ");
            double amount = promptForDouble();

            if (!isDeposit) {
                amount *= -1;
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));
            bufferedWriter.write(dateNow + "|" + timeNow.format(formatter) + "|" + description + "|" + vendor + "|" + amount + "\n");
            bufferedWriter.close();

            Transaction transaction = new Transaction(dateNow, LocalTime.parse(timeNow.format(formatter)), description, vendor, amount);
            transactions.add(transaction);

            System.out.printf("Transaction recorded: %s | %s | $ %.2f%n", transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            System.out.println();
        } catch (IOException e) {
            System.out.println("Unable to write to the file, please check connection and try again.\n");
        }
        pressEnterToContinue();
    }

    private static void addDeposit() {
        addTransaction(true);
    }

    private static void makePayment() {
        addTransaction(false);
    }

    private static void viewBalance() {
        System.out.println("Current Balance:");
        System.out.printf("$ %.2f%n", calculateBalance(transactions));

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

    private static void customSearch() {
        System.out.println("                                     - Custom Search -");
        displayTransactionsAsTable(filterCustomSearch());  // custom arraylist

        pressEnterToContinue();
    }

    private static String promptForString() {
        String userString;

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
        String amount;

        while (true) {
            try {
                amount = scanner.nextLine().trim();

                if (amount.isEmpty()) {
                    System.out.println("Input not recorded, please try again: ");
                    continue;
                }
                if (amount.equals("0") || amount.contains("-")) {
                    System.out.println("Please enter a positive number.");
                    continue;
                }
                if (amount.equalsIgnoreCase("skip")) {
                    amount = null;
                }

                return Double.parseDouble(amount);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number");
            }
        }
    }

    private static LocalDate promptForDate() {
        while (true) {
            String date = promptForString();
            if (date.equalsIgnoreCase("skip")) {
                return null;
            }

            try {
                return LocalDate.parse(date);
            } catch (DateTimeParseException e) {
                System.out.println("Unable to parse date, please use the format yyyy-MM-dd");
            }
        }
    }

    private static double calculateBalance(ArrayList<Transaction> transactions) {
        double sum = 0;

        for (Transaction transaction : transactions) {
            sum += transaction.getAmount();
        }

        return sum;
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
        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now();

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

        for (Transaction transaction : transactions) {
            if (previousMonth.getMonth() == transaction.getDate().getMonth() && previousMonth.getYear() == transaction.getDate().getYear()) {
                previousMonthTransactions.add(transaction);
            }
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

        return vendors;
    }

    private static ArrayList<Transaction> filterCustomSearch() {
        ArrayList<Transaction> custom = new ArrayList<>();

        System.out.println("""
                Custom Search Parameters
                Fill in the fields to search
                Enter "skip" to skip a field""");
        System.out.println();
        System.out.println("Date Range");
        System.out.println("Enter the start date: (yyyy-mm-dd)");
        LocalDate start = promptForDate();
        System.out.println("Enter the end date: (yyyy-mm-dd)");
        LocalDate end = promptForDate();

        System.out.println("Enter the description: ");
        String description = promptForString().toLowerCase();

        System.out.println("Enter the vendor: ");
        String vendor = promptForString().toLowerCase();

        System.out.println("Enter the amount: ");
        Double amount = promptForDouble();

        for (Transaction transaction : transactions) {
            boolean matches = true;

            if (start != null && transaction.getDate().isBefore(start)) {
                matches = false;
            }
            if (end != null && transaction.getDate().isAfter(end)) {
                matches = false;
            }
            if (!description.equalsIgnoreCase("skip") && !transaction.getDescription().toLowerCase().contains(description.toLowerCase())) {
                matches = false;
            }
            if (!vendor.equalsIgnoreCase("skip") && !transaction.getVendor().equalsIgnoreCase(vendor)) {
                matches = false;
            }
            if (amount != null && transaction.getAmount() != amount) {
                matches = false;
            }
            if (matches) {
                custom.add(transaction);
            }
        }

        return custom;
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
        } else {
           System.out.println("No transactions found");
        }

        System.out.println();
        System.out.printf("Total: $ %.2f%n", calculateBalance(transactions));
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
