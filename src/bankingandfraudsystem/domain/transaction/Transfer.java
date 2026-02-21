package bankingandfraudsystem.domain.transaction;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.account.Account;
import bankingandfraudsystem.domain.account.AccountStatus;
import bankingandfraudsystem.util.Money;

public class Transfer extends Transaction {
    private final Account from;
    private final Account to;

    public Transfer(Money amount, String description, Account fromAcc, Account toAcc) throws CurrencyMismatchException {
        super(amount,description);

        if(fromAcc == null) throw new IllegalArgumentException("Account transferrin money from can't be null!");

        if(toAcc == null) throw  new IllegalArgumentException("Target account cannot be null!");

        if(fromAcc.equals(toAcc)) throw new IllegalStateException("Cannot transfer to same account!");

        if(amount.getCurrency() != toAcc.getCurrency() || amount.getCurrency() != fromAcc.getCurrency())
            throw new CurrencyMismatchException("Currency mismatch!");

        this.from = fromAcc;
        this.to = toAcc;
    }

    @Override
    public TransactionType type() {
        return TransactionType.TRANSFER;
    }

    @Override
    protected void apply() throws CurrencyMismatchException {
        if(this.getStatus() != TransactionStatus.APPROVED) throw new IllegalStateException("Transaction status must be APPROVED!");

        from.withDraw(getAmount());
        to.deposit(getAmount());
    }
}
