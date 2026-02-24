package bankingandfraudsystem.util;

import bankingandfraudsystem.Exception.CurrencyMismatchException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money implements Comparable<Money> {
    private final Currency currency;
    private BigDecimal Amount;

    public Money(final Currency curr, final BigDecimal amount){

        if(curr==null){
            throw new IllegalArgumentException("Currency cannot be null!");
        }

        if(amount==null || (amount.compareTo(BigDecimal.ZERO) <= 0)){
            throw new IllegalArgumentException("Amount cannot be null!");
        }

        this.currency = curr;
        this.Amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public Currency getCurrency(){
        return this.currency;
    }

    public BigDecimal getAmount() {
        return this.Amount;
    }

    private void CurrencyChecker(Money other) throws CurrencyMismatchException {
        if(!this.currency.toString().equals(other.currency.toString())){
            throw new CurrencyMismatchException("Currency mismatch, please try again with correct currency!");
        }
        if (other == null) throw new IllegalArgumentException("money cannot be null");
    }

    public void AddMoney(Money other) throws CurrencyMismatchException {
        CurrencyChecker(other);
        this.Amount = this.Amount.add(other.Amount);
    }

    public Money add_Money(Money other) throws CurrencyMismatchException{
        CurrencyChecker(other);
        return new Money(this.currency, this.Amount.add(other.Amount));
    }


    public void Subtract(Money other) throws CurrencyMismatchException {
        CurrencyChecker(other);
        this.Amount = this.Amount.add(other.Amount);
    }

    public Money subtract1(Money other) throws CurrencyMismatchException {
        CurrencyChecker(other);
        return new Money(this.currency, this.Amount.subtract(other.Amount));
    }

    public void Multiply(BigDecimal factor) {
        if(factor.compareTo(BigDecimal.ZERO)<=0) throw new IllegalArgumentException("Enter valid factor!");
        this.Amount = this.Amount.multiply(factor);
    }

    @Override
    public int compareTo(Money other){
        if(!this.currency.toString().equals(other.currency.toString())){
            throw new IllegalArgumentException("Currency mismatch, please try again with correct currency!");
        }
        return this.Amount.compareTo(other.Amount);
    }

    public static Money zero(Currency currency) {
        return new Money(currency, BigDecimal.ZERO);
    }

    public boolean isZero() {
        return this.Amount.compareTo(BigDecimal.ZERO)==0;
    }

    public boolean isPositive() {
        return this.Amount.compareTo(BigDecimal.ZERO)>0;
    }

    public boolean isNegative() {
        return this.Amount.compareTo(BigDecimal.ZERO)<0;
    }


    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;

        Money money = (Money) obj;
        return (this.Amount.equals(money.Amount)) && (this.currency==money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.Amount,this.currency);
    }

    @Override
    public String toString(){
        return this.Amount + " " + this.currency + ".";
    }
}
