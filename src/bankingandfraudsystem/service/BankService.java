package bankingandfraudsystem.service;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.account.Account;
import bankingandfraudsystem.domain.account.AccountStatus;
import bankingandfraudsystem.domain.account.CheckingAccount;
import bankingandfraudsystem.domain.account.SavingsAccount;
import bankingandfraudsystem.domain.customer.Customer;
import bankingandfraudsystem.domain.ledger.Ledger;
import bankingandfraudsystem.domain.transaction.Deposit;
import bankingandfraudsystem.domain.transaction.Transaction;
import bankingandfraudsystem.domain.transaction.Transfer;
import bankingandfraudsystem.domain.transaction.Withdrawal;
import bankingandfraudsystem.rules.*;
import bankingandfraudsystem.util.Money;
import bankingandfraudsystem.util.Currency;

import java.util.*;

public class BankService {
    private final Ledger ledger;
    private final FraudEngine fraudEngine;

    private final Map<UUID, Customer>customers = new HashMap<>();
    private final Map<UUID, Account>accounts = new HashMap<>();

    private final List<Transaction>attempts = new ArrayList<>();

    public BankService(Ledger ledger, FraudEngine fraudEngine) {
        if(ledger == null) throw new IllegalArgumentException("Ledger cannot be null!");
        if(fraudEngine == null) throw new IllegalArgumentException("FraudEngine cannot be null!");

        this.ledger = ledger;
        this.fraudEngine = fraudEngine;
    }

    private Customer requireCustomer(UUID id) {
        if(id == null) throw new IllegalArgumentException("Customer ID cannot be null!");
        if(!customers.containsKey(id)) throw new IllegalArgumentException("Customer not found!");

        return customers.get(id);
    }

    private Account requireAccount(UUID id) {
        if(id == null) throw new IllegalArgumentException("Account ID cannot be null!");
        if(!accounts.containsKey(id)) throw new IllegalArgumentException("Account not found!");

        return accounts.get(id);
    }

    public Customer createCustomer(String fullName) {
        Customer customer = new Customer(fullName);
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Customer getCustomer(UUID id) {
        return requireCustomer(id);
    }

    public List<Customer>listCustomers() {
        return List.copyOf(customers.values());
    }

    public CheckingAccount openChecking(UUID customerID, Currency currency, Money overdraftLimit) {
        Customer owner = requireCustomer(customerID);
        CheckingAccount checkingAccount = new CheckingAccount(owner,currency, AccountStatus.ACTIVE, overdraftLimit);
        accounts.put(checkingAccount.getID(), checkingAccount);
        owner.addAccount(checkingAccount);
        return checkingAccount;
    }

    public SavingsAccount openSavings(UUID customerID, Currency currency, Money minimumBalance, int monthlyLimit) {
        Customer owner = requireCustomer(customerID);
        SavingsAccount savingsAccount = new SavingsAccount(owner,currency,AccountStatus.ACTIVE,minimumBalance,monthlyLimit);
        accounts.put(savingsAccount.getID(), savingsAccount);
        owner.addAccount(savingsAccount);
        return savingsAccount;
    }

    public Account getAccount(UUID id) {
        return requireAccount(id);
    }

    public List<Account>listAccounts() {
        return List.copyOf(accounts.values());
    }

    public Transaction deposit(UUID accountID, Money amount, String description) throws CurrencyMismatchException {
        Account account = requireAccount(accountID);
        Transaction tx = new Deposit(account,amount,description);
        attempts.add(tx);
        tx.approve();
        ledger.post(tx);
        return tx;
    }

    public Transaction withdraw(UUID accountID, Money amount, String description) throws CurrencyMismatchException {
        Account account = requireAccount(accountID);
        Transaction tx = new Withdrawal(amount,description,account);
        FraudContext fraudContext = new FraudContext(account.getOwner(),customerHistory(account.getOwner()));
        RuleResult rule = this.fraudEngine.assess(tx,fraudContext);
        if(rule.isAllow()) {
            tx.approve();
            ledger.post(tx);
        }
        else if(rule.isReview()){
            tx.markReview();
            attempts.add(tx);
        }
        else if(rule.isBlock()){
            tx.decline();
            attempts.add(tx);
        }
        return tx;
    }

    public Transaction transfer(UUID fromID, UUID toID, Money amount, String description) throws CurrencyMismatchException {
        Account fromAcc = requireAccount(fromID);
        Account toAcc = requireAccount(toID);
        Transaction tx = new Transfer(amount,description,fromAcc,toAcc);
        FraudContext fraudContext = new FraudContext(fromAcc.getOwner(),customerHistory(fromAcc.getOwner()));
        RuleResult rule = this.fraudEngine.assess(tx,fraudContext);

        if(rule.isAllow()) {
            tx.approve();
            ledger.post(tx);
        }
        else if(rule.isReview()){
            tx.markReview();
            attempts.add(tx);
        }
        else if(rule.isBlock()){
            tx.decline();
            attempts.add(tx);
        }
        return tx;
    }

    public List<Transaction>listPostedTransactions() {
        return this.ledger.getHistory();
    }

    public List<Transaction>listAttempts() {
        return List.copyOf(this.attempts);
    }

    public List<Transaction>statement(UUID accountID) {
        Account account = requireAccount(accountID);
        return ledger.statementFor(account);
    }

    public void freezeAccount(UUID accountID) {
        try {
            requireAccount(accountID).freeze();
        }catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public void unfreezeAccount(UUID accountID) {
        try {
            requireAccount(accountID).unFreeze();
        }catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeAccount(UUID accountID) {
        try {
            requireAccount(accountID).Close();
        }catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Transaction>customerHistory(Customer customer) {
        if(customer == null) throw new IllegalArgumentException("Customer cannot be null!");

        List<Transaction>res = new ArrayList<>();
        for(Transaction transaction : ledger.getHistory()) {
            if(transaction.involvesAnyAccountOf(customer))
                res.add(transaction);
        }
        return List.copyOf(res);
    }

    public Ledger getLedger() {
        return this.ledger;
    }

    public FraudEngine getFraudEngine() {
        return this.fraudEngine;
    }
}
