package bankingandfraudsystem.domain.customer;

import bankingandfraudsystem.domain.account.Account;
import bankingandfraudsystem.domain.card.Card;

import java.util.*;

public class Customer {
    private final UUID id;
    private final String fullName;
    private final List<Account>accounts;
    private final List<Card> cards = new ArrayList<Card>();

    public Customer(String fullname) {
        if(fullname == null) throw new IllegalArgumentException("FullName cannot be null, please try again!");
        if(fullname.isBlank()) throw new IllegalArgumentException("FullName cannot be blank, please try again!");


        this.id = UUID.randomUUID();
        this.fullName = fullname;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        if(account == null) throw new IllegalArgumentException("Account cannot be null!");

        if(!account.getOwner().equals(this)) throw new IllegalArgumentException("Account owner doesn't match, please try again!");

        if(accounts.contains(account)) throw new IllegalArgumentException("Account already added!");

        accounts.add(account);
    }

    public Account findAccount(UUID accountID) {
        if(accountID == null) throw new IllegalArgumentException("Account ID cannot be null!");

        for(Account account : accounts) {
            if(account.getID().equals(accountID)) return account;
        }
        throw new IllegalStateException("Account with ID: " + accountID + " has not be found!");
    }

    public String getFullName() {
        return this.fullName;
    }

    public UUID getId() {
        return this.id;
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(this.accounts);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(this.cards);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return this.id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Customer ID: " + this.id + "\nFull Name: " + this.fullName;
    }
}
