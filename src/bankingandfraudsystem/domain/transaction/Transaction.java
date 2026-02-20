package bankingandfraudsystem.domain.transaction;

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
    protected abstract void apply();

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

}
