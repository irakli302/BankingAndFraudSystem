package bankingandfraudsystem.domain.card;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.account.Account;
import bankingandfraudsystem.domain.customer.Customer;
import bankingandfraudsystem.util.Money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.UUID;

public abstract class Card {
    protected final UUID id;
    protected final Customer owner;
    protected final Account linkedAccount;
    protected CardStatus status;

    private Money dailyLimit;
    private Money spentToday;
    private LocalDate spendDate;

    public Card(Customer owner, Account linkedAcc,Money dailylimit) throws CurrencyMismatchException {
        if(owner == null)
            throw new IllegalArgumentException("Card owner cannot be null!");
        if(linkedAcc == null)
            throw new IllegalArgumentException("Linked account cannot be null!");
        if(!linkedAcc.getOwner().equals(owner))
            throw new IllegalArgumentException("Account and card owners mismatch!");
        if(dailylimit == null)
            throw new IllegalArgumentException("DailyLimit cannot be null!");
        if(!dailylimit.isPositive())
            throw new IllegalArgumentException("DailyLimit must be positive!");
        if(dailylimit.getCurrency() != linkedAcc.getCurrency())
            throw new CurrencyMismatchException("Currency mismatch, please try again!");


        this.id = UUID.randomUUID();
        this.owner = owner;
        this.linkedAccount = linkedAcc;
        this.dailyLimit = dailylimit;
        this.status = CardStatus.ACTIVE;
        this.spentToday = new Money(linkedAcc.getCurrency(),new BigDecimal(BigInteger.ZERO));
        this.spendDate = LocalDate.now();
    }

    public void freeze() {
        if(this.status == CardStatus.ACTIVE)
            throw new IllegalStateException("CardStatus already FROZEN!");
        this.status = CardStatus.FROZEN;
    }

    public void unfreeze() {
        if(this.status != CardStatus.FROZEN)
            throw new IllegalStateException("CardStatus must be FROZEN to UNFREEZE!");
        this.status = CardStatus.ACTIVE;
    }

    public void close() {
        if(this.status == CardStatus.CLOSED)
            throw new IllegalStateException("CardStatus already CLOSED!");
        this.status = CardStatus.CLOSED;
    }

    public boolean canAuthorise(Money amount) throws CurrencyMismatchException {
        if(amount == null || amount.isNegative()) return false;
        if(this.status != CardStatus.ACTIVE) return false;
        if(amount.getCurrency() != this.linkedAccount.getCurrency()) return false;

        restoreDate();

        Money new_money = this.spentToday.add_Money(amount);
        return new_money.compareTo(this.dailyLimit) <= 0;
    }

    public void restoreDate() {
        LocalDate today = LocalDate.now();
        if(!today.equals(this.spendDate)){
            this.spentToday = Money.zero(this.linkedAccount.getCurrency());
            this.spendDate = today;
        }
    }
}
