package bankingandfraudsystem.domain.account;

import bankingandfraudsystem.util.Money;

public abstract class Account {
    private String AccountName;
    private Money Balance;

    
    public String getName() {
        return this.AccountName;
    }

    public Money getBalance() {
        return this.Balance;
    }



}
