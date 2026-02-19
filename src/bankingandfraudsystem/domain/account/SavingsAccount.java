package bankingandfraudsystem.domain.account;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.customer.Customer;
import bankingandfraudsystem.util.Currency;
import bankingandfraudsystem.util.Money;

public class SavingsAccount extends Account {
    private Money minimumBalance;
    private int withdrawalsThisMonth;
    private int monthlyWithdrawalLimit;

    public SavingsAccount(Customer owner, Currency currency, AccountStatus status, Money minBalance, int monthly){
        super(owner,currency, status, AccountType.SAVINGS);

        if(minBalance.isNegative()) throw new IllegalArgumentException("Minimum Balance can't be negative!");

        if(monthly < 0) throw new IllegalArgumentException("Monthly limit cannot be negative");

        this.minimumBalance = minBalance;
        this.monthlyWithdrawalLimit = monthly;
        this.withdrawalsThisMonth = 0;
    }


    public SavingsAccount(Customer owner, Money balance, Currency currency, AccountStatus status, Money minBalance, int monthly){
        super(owner,currency, status, AccountType.SAVINGS);

        if(minBalance.isNegative()) throw new IllegalArgumentException("Minimum Balance can't be negative!");

        if(monthly < 0) throw new IllegalArgumentException("Monthly limit cannot be negative");

        this.minimumBalance = minBalance;
        this.monthlyWithdrawalLimit = monthly;
        this.withdrawalsThisMonth = 0;
    }

    public Money getMinimumBalance() {
        return this.minimumBalance;
    }

    public int getWithdrawalsThisMonth() {
        return  this.withdrawalsThisMonth;
    }

    public int getMonthlyWithdrawalLimit() {
        return this.monthlyWithdrawalLimit;
    }

    public void setMonthlyWithdrawalLimit(int new_amount){
        this.monthlyWithdrawalLimit = new_amount;
    }

    @Override
    public boolean canWithDraw(Money amount) throws CurrencyMismatchException {
        if(amount==null) throw new IllegalArgumentException("Amount can't be null!");
        Money new_money = Balance.subtract1(minimumBalance);
        return new_money.compareTo(amount) >= 0;
    }

}
