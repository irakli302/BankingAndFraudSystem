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
import bankingandfraudsystem.domain.transaction.Withdrawal;
import bankingandfraudsystem.rules.FraudContext;
import bankingandfraudsystem.rules.FraudEngine;
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
        FraudContext fraudContext = new FraudContext(account.getOwner(),ledger.getHistory());
        attempts.add(tx);
    }
}
