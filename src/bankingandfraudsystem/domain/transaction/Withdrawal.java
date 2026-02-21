package bankingandfraudsystem.domain.transaction;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.account.Account;
import bankingandfraudsystem.util.Money;

public class Withdrawal extends Transaction{
    private final Account from;

    public Withdrawal(Money amount, String description, Account fromAcc) {
        super(amount,description);

        if(fromAcc==null) throw new IllegalArgumentException("Target account cannot be null!");
        if(!fromAcc.getCurrency().equals(amount.getCurrency())) throw new IllegalStateException("Currency mismatch!");

        this.from = fromAcc;
    }

    public Account getFromAccount() {
        return this.from;
    }

    @Override
    public TransactionType type() {
        return TransactionType.WITHDRAWAL;
    }

    @Override
    public void apply() throws CurrencyMismatchException {
        from.withDraw(getAmount());
    }

    @Override
    public boolean involves(Account account) {
        if (account == null) throw new IllegalArgumentException("Account cannot be null!");
        return from.equals(account);
    }
}
