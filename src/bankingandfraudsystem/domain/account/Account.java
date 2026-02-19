package bankingandfraudsystem.domain.account;

import bankingandfraudsystem.domain.customer.Customer;
import bankingandfraudsystem.util.Currency;
import bankingandfraudsystem.util.Money;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class Account {
    protected final UUID ID;
    protected Customer Owner;
    protected Money Balance;
    protected Currency currency;
    protected AccountStatus status;
    protected AccountType type;

    public Account(Customer owner, Money balance, Currency curr, AccountStatus st, AccountType typ) {
        if(owner == null) throw  new IllegalArgumentException("Owner can't be null!");
        if(curr == null) throw new IllegalArgumentException("Currency can't be null!");
        if(st == null) throw new IllegalArgumentException("Account status can't be null!");

        this.ID = UUID.randomUUID();
        this.Owner = owner;
        this.Balance = balance;
        this.currency = curr;
        this.status = st;
        this.type = typ;
    }

    public Account(Customer owner, Currency curr, AccountStatus st, AccountType typ) {
        if(owner == null) throw  new IllegalArgumentException("Owner can't be null!");
        if(curr == null) throw new IllegalArgumentException("Currency can't be null!");
        if(st == null) throw new IllegalArgumentException("Account status can't be null!");

        this.ID = UUID.randomUUID();
        this.Owner = owner;
        this.Balance = new Money(curr, BigDecimal.ZERO);
        this.currency = curr;
        this.status = st;
        this.type = typ;
    }

    public UUID getID() {
        return this.ID;
    }

    public Customer getOwner() {
        return this.Owner;
    }

    public Money getBalance() {
        return  this.Balance;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public AccountStatus getStatus() {
        return this.status;
    }

    public AccountType getType() {
        return this.type;
    }

    public void setBalance(Money balance) {
        this.Balance = balance;
    }

    public void setStatus(AccountStatus st) {
        this.status = st;
    }

    public void setType(AccountType typ) {
        this.type = typ;
    }
}
