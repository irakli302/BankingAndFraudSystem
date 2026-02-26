import bankingandfraudsystem.domain.account.AccountStatus;
import bankingandfraudsystem.domain.customer.Customer;
import bankingandfraudsystem.domain.ledger.Ledger;
import bankingandfraudsystem.domain.transaction.Transaction;
import bankingandfraudsystem.rules.Decision;
import bankingandfraudsystem.rules.FraudEngine;
import bankingandfraudsystem.rules.FraudRule;
import bankingandfraudsystem.rules.RuleResult;
import bankingandfraudsystem.rules.impl.LargeAmountRule;
import bankingandfraudsystem.rules.impl.TooManyTransactionsRule;
import bankingandfraudsystem.rules.impl.VelocitySumRule;
import bankingandfraudsystem.service.BankService;
import bankingandfraudsystem.util.Currency;
import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.util.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Ledger ledger = new Ledger();

            FraudRule rule1 = new LargeAmountRule(new Money(Currency.GEL, new BigDecimal("500")), Decision.REVIEW);
            FraudRule rule2 = new TooManyTransactionsRule(5,2,Decision.REVIEW);
            FraudRule rule3 = new VelocitySumRule(new Money(Currency.GEL, new BigDecimal("1000")),10);

            List<FraudRule>rules = new ArrayList<>() ;
            rules.add(rule1);
            rules.add(rule2);
            rules.add(rule3);

            FraudEngine engine = new FraudEngine(rules);

            BankService bank = new BankService(ledger,engine);

            var customer = bank.createCustomer("Test User");
            var checkingAccount = bank.openChecking(customer.getId(),Currency.GEL, new Money(Currency.GEL, new BigDecimal("200")));
            var savingAccount = bank.openSavings(customer.getId(),Currency.GEL,new Money(Currency.GEL,new BigDecimal("50")),10);

            System.out.println("\n== Deposit 300 EUR to Checking ==");
            var deposit = bank.deposit(checkingAccount.getID(),new Money(Currency.GEL,new BigDecimal("300")),"Initial Deposit");
            System.out.println(deposit.getStatus() + " | balance=" + checkingAccount.getBalance());

            System.out.println("\n== Withdraw 200 GEL (should REVIEW by LargeAmountRule) ==");
            var w = bank.withdraw(checkingAccount.getID(),
                    new Money(Currency.GEL, new BigDecimal("200")),
                    "Big withdraw");
            System.out.println(w.getStatus() + " | balance=" + checkingAccount.getBalance());
            System.out.println("attempts=" + bank.listAttempts().size());

            System.out.println("\n== Withdraw 600 GEL (should REVIEW by LargeAmountRule) ==");
            var w1 = bank.withdraw(checkingAccount.getID(),
                    new Money(Currency.GEL, new BigDecimal("600")),
                    "Big withdraw");
            System.out.println(w1.getStatus() + " | balance=" + checkingAccount.getBalance());
            System.out.println("attempts=" + bank.listAttempts().size());

            System.out.println("\n== Transfer 50 EUR Checking -> Savings ==");
            var transfer = bank.transfer(checkingAccount.getID(),savingAccount.getID(),new Money(Currency.GEL,new BigDecimal("50")),"move money");
            System.out.println(transfer.getStatus() + " | checking=" + checkingAccount.getBalance() + " | savings=" + savingAccount.getBalance());

            System.out.println("\n== Transfer 200 EUR Checking -> Savings ==");
            var transfer2 = bank.transfer(checkingAccount.getID(),savingAccount.getID(),new Money(Currency.GEL,new BigDecimal("500")),"move money");
            System.out.println(transfer2.getStatus() + " | checking=" + checkingAccount.getBalance() + " | savings=" + savingAccount.getBalance());

            System.out.println("\n== LEDGER (POSTED) ==");
            List<Transaction>postedTransactions = bank.getLedger().getHistory();

            for(Transaction tx : postedTransactions) {
                System.out.println(tx.type() + " | " + tx.getStatus() + " | " + tx.getAmount());
            }

            System.out.println("\n== ATTEMPTS (REVIEW/BLOCK) ==");
            List<Transaction>transactionsAttempts = bank.listAttempts();

            for(Transaction tx : transactionsAttempts) {
                System.out.println(tx.type() + " | " + tx.getStatus() + " | " + tx.getAmount());
            }

            System.out.println("\n== Statement for Checking ==");
            List<Transaction>statment = bank.statement(checkingAccount.getID());

            for(Transaction tx : statment) {
                System.out.println(tx.type() + " | " + tx.getStatus() + " | " + tx.getAmount());
            }

        }catch(CurrencyMismatchException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}