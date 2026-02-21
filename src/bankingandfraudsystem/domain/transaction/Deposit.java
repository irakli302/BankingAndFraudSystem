package bankingandfraudsystem.domain.transaction;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.account.Account;
import bankingandfraudsystem.util.Money;

public class Deposit extends Transaction {
    private final Account to;

    public Deposit(Account toAcc, Money amount, String description) {
        super(amount, description);

        if(toAcc==null) throw new IllegalArgumentException("Target account cannot be null!");
        if(!toAcc.getCurrency().equals(amount.getCurrency())) throw new IllegalStateException("Currency mismatch!");

        this.to = toAcc;
    }

    public Account getTargetAccount(){
        return this.to;
    }

    @Override
    public TransactionType type() {
        return TransactionType.DEPOSIT;
    }

    @Override
    public void apply() throws CurrencyMismatchException {
        to.deposit(getAmount());
    }

    @Override
    public boolean involves(Account account) {
        if (account == null) throw new IllegalArgumentException("Account cannot be null!");
        return to.equals(account);
    }
}
