package bankingandfraudsystem.domain.transaction;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.account.Account;
import bankingandfraudsystem.domain.account.AccountStatus;
import bankingandfraudsystem.domain.customer.Customer;
import bankingandfraudsystem.util.Money;

import java.time.Instant;
import java.util.UUID;

public abstract class Transaction {
    private final UUID ID;
    private final Instant createdAt;
    private Money amount;
    private TransactionStatus status;
    private String description;

    public Transaction(Money money, String desc) {

        if(money == null) throw new IllegalArgumentException("Amount cannot be null!");

        if(!money.isPositive()) throw new IllegalArgumentException("Amount must be positive!");

        this.ID = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.status = TransactionStatus.CREATED;
        this.description = desc;
        this.amount = money;
    }

    public abstract TransactionType type();
    public abstract void apply() throws CurrencyMismatchException;
    public abstract boolean involves(Account acc);

    public UUID getId() {
        return this.ID;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Money getAmount() {
        return this.amount;
    }

    public TransactionStatus getStatus() {
        return this.status;
    }

    public String getDescription() {
        return this.description;
    }

    public void setStatus(TransactionStatus st) {
        this.status = st;
    }

    public void approve() {
        checkPosted();
        if(status != TransactionStatus.CREATED) throw new IllegalStateException("Only Created transaction can be APPROVED!");
        this.status = TransactionStatus.APPROVED;
    }

    public void markReview() {
        checkPosted();
        if(status != TransactionStatus.CREATED) throw new IllegalStateException("Only Created transaction can be  marked REVIEW!");
        this.status = TransactionStatus.REVIEW;
    }

    public void decline() {
        checkPosted();
        if(status != TransactionStatus.CREATED) throw new IllegalStateException("Only Created transaction can be  DECLINED!");
        this.status = TransactionStatus.DECLINED;
    }

    public void markPosted() {
        checkPosted();
        if(status != TransactionStatus.APPROVED) throw new IllegalStateException("Only APPROVED transaction can be mark POSTED!");
        this.status = TransactionStatus.POSTED;
    }

    private void checkPosted() {
        if(status == TransactionStatus.POSTED) throw new IllegalStateException("Transaction cannot be modified!");
    }

    public boolean involvesAnyAccountOf(Customer customer) {
        if(customer == null) throw new IllegalArgumentException("Customer cannot be null!");

        for(Account account : customer.getAccounts()) {
            if(this.involves(account)) return true;
        }
        return false;
    }

}
