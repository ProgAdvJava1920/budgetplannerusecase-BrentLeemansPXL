package be.pxl.student.util;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.Payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Util class to import csv file
 */
public class BudgetPlannerImporter {
    private Map<String, List<Payment>> paymentByAccount = new HashMap<>();
    private List<Account> accounts = new ArrayList<>();

    public void readFile(String filename) {
        Path path = Paths.get(System.getProperty("user.dir")).resolve("src/main/resources/" + filename);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                checkLine(line);
                line = reader.readLine();
            }
            addPaymentsToAccount();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //creates account if the account does not exist in the map.
    private void checkLine(String line) {
        if (!line.startsWith("Error")) {
            createAccountAndPayment(line.split(","));
        }
    }

    private void createAccountAndPayment(String[] splittedLine) {
        Account account = null;
        //[0]Account name,[1]Account IBAN,[2]Counteraccount IBAN,[3]Transaction date,[4]Amount,[5]Currency,[6]Detail

        String accountName = splittedLine[0];
        String accountIBAN = splittedLine[1];
        LocalDateTime date = formatDate(splittedLine[3]);
        float amount = Float.parseFloat(splittedLine[4]);
        String currency = splittedLine[5];
        String detail = splittedLine[6];

        Payment payment = new Payment(date, amount, currency, detail);

        if (!paymentByAccount.containsKey(accountIBAN)) {
            account = new Account(accountIBAN, accountName);
            accounts.add(account);
            List<Payment> newPayment = new ArrayList<>();
            newPayment.add(payment);
            paymentByAccount.put(accountIBAN, newPayment);
        } else {
            paymentByAccount.get(accountIBAN).add(payment);
        }
    }

    private LocalDateTime formatDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        LocalDateTime date = LocalDateTime.parse(dateString, formatter);
        return date;
    }

    private void addPaymentsToAccount() {
        for (Map.Entry<String, List<Payment>> entry : paymentByAccount.entrySet()) {
            for (Account acc : accounts) {
                if (acc.getIBAN().equals(entry.getKey()))
                {
                    acc.setPayments(entry.getValue());
                }
            }
        }
    }

    public List<Account> getAccounts(){
        return accounts;
    }
}