package bankingandfraudsystem.domain.account;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
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

    public void deposit(Money amount) throws CurrencyMismatchException {
        if(this.status == AccountStatus.CLOSED) throw new IllegalStateException("Account is CLOSED!");

        if(amount.getCurrency() != this.currency) throw new IllegalArgumentException("Currency mismatch, try again!");

        this.Balance.AddMoney(amount);
    }


    public void withDraw(Money amount) throws CurrencyMismatchException {
        if(status!=AccountStatus.ACTIVE) throw new IllegalStateException("Account must be ACTIVE!");

        if(amount == null || (amount.getAmount().compareTo(BigDecimal.ZERO) <=0))  {
            throw new IllegalArgumentException("Amount must be more than 0!");
        }

        if(!canWithDraw(amount)) throw new IllegalStateException("Invalid operation!");

        this.Balance.Subtract(amount);
    }

    public void freeze() {
        if(status!=AccountStatus.ACTIVE) throw new IllegalStateException("Account already FROZEN!");

        status = AccountStatus.FROZEN;
    }

    public void unFreeze() {
        if(status!=AccountStatus.FROZEN) throw new IllegalStateException("Only FROZEN account can be UNFROZEN!");

        status = AccountStatus.ACTIVE;
    }

    public void Close() {
        if(status == AccountStatus.CLOSED) throw new IllegalStateException("Account already CLOSED!");

        if(!Balance.isZero()) throw new IllegalStateException("To CLOSE account, Balance must be zero!");

        status = AccountStatus.CLOSED;
    }

    public abstract boolean canWithDraw(Money amount);
}
