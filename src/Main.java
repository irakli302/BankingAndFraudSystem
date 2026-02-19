import bankingandfraudsystem.util.Currency;
import bankingandfraudsystem.util.CurrencyMismatchException;
import bankingandfraudsystem.util.Money;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        try {
            Money money1 = new Money(Currency.GEL, new BigDecimal("200.5"));
            Money money2 = new Money(Currency.GEL, new BigDecimal("100.5"));
            Money money3 = money1.AddMoney(money2);
            Money money4 = money3.Multiply(new BigDecimal("2"));
            boolean ans = money1.equals(money2);
            System.out.println(ans);
            System.out.println(money3);
        }catch(CurrencyMismatchException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}