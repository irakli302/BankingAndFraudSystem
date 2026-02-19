package bankingandfraudsystem.util;

import java.math.BigDecimal;

public class Money implements Comparable<Money> {
    private final Currency currency;
    private final BigDecimal Amount;

    public Money(final Currency curr, final BigDecimal amount){

        if(curr==null){
            throw new IllegalArgumentException("Currency cannot be null!");
        }

        if(amount==null){
            throw new IllegalArgumentException("Amount cannot be null!");
        }

        this.currency = curr;
        this.Amount = amount;
    }

    public Currency Get_Currency(){
        return this.currency;
    }

    public BigDecimal Get_Amount() {
        return this.Amount;
    }

    public Money AddMoney(Money other){
        if(!this.currency.toString().equals(other.currency.toString())){
            throw new IllegalArgumentException("Currency mismatch, please try again with correct currency!");
        }
        return new Money(this.currency, this.Amount.add(other.Amount));
    }

    public Money Subtract(Money other) {
        if(!this.currency.toString().equals(other.currency.toString())){
            throw new IllegalArgumentException("Currency mismatch, please try again with correct currency!");
        }
        return new Money(this.currency, this.Amount.subtract(other.Amount));
    }

    @Override
    public int compareTo(Money other){
        if(!this.currency.toString().equals(other.currency.toString())){
            throw new IllegalArgumentException("Currency mismatch, please try again with correct currency!");
        }
        return this.Amount.compareTo(other.Amount);
    }

    @Override
    public String toString(){
        return "Amount: " + this.Amount + ", currency: " + this.currency + ".";
    }
}
