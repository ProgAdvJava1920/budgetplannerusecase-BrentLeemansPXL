package be.pxl.student;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.Payment;
import be.pxl.student.util.BudgetPlannerImporter;

import java.util.List;

public class BudgetPlanner {

    public static void main(String[] args) {
        BudgetPlannerImporter budgetPlannerImporter = new BudgetPlannerImporter();
        budgetPlannerImporter.readFile("account_payments.csv");
        List<Account> accounts = budgetPlannerImporter.getAccounts();
            for (Payment payment: accounts.get(0).getPayments()) {
                System.out.println(accounts.get(0).getName() + " " + payment);
            }
    }
}
