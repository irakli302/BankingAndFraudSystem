package bankingandfraudsystem.domain.account;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.customer.Customer;
import bankingandfraudsystem.util.Currency;
import bankingandfraudsystem.util.Money;

public class CheckingAccount extends Account {
    private Money overdraftLimit;

    public CheckingAccount(Customer owner, Currency currency, AccountStatus status, Money Limit) {
        super(owner, currency, status, AccountType.CHECKING);

        if(overdraftLimit.isNegative()) throw new IllegalArgumentException("OverdraftLimit can't be negative!");

        this.overdraftLimit = Limit;
    }

    public CheckingAccount(Customer owner,Money balance, Currency currency, AccountStatus status, Money Limit) {
        super(owner, balance, currency, status, AccountType.CHECKING);

        if(overdraftLimit.isNegative()) throw new IllegalArgumentException("OverdraftLimit can't be negative!");

        this.overdraftLimit = Limit;
    }

    public Money getOverdraftLimit() {
        return this.overdraftLimit;
    }

    @Override
    public boolean canWithDraw(Money amount) throws CurrencyMismatchException {
        if(amount==null) throw new IllegalArgumentException("Amount can't be null!");
        Money new_money = Balance.add_Money(overdraftLimit);
        return new_money.compareTo(amount) >=0;
    }
}
