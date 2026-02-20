package bankingandfraudsystem.domain.transaction;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.account.AccountStatus;
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

        if(this.amount == null) throw new IllegalArgumentException("Amount cannot be null!");

        if(!this.amount.isPositive()) throw new IllegalArgumentException("Amount must be positive!");

        this.ID = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.status = TransactionStatus.CREATED;
        this.description = desc;
    }

    public abstract TransactionType type();
    protected abstract void apply() throws CurrencyMismatchException;

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

}
