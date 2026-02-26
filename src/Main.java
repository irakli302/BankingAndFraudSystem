import bankingandfraudsystem.domain.account.AccountStatus;
import bankingandfraudsystem.domain.customer.Customer;
import bankingandfraudsystem.domain.ledger.Ledger;
import bankingandfraudsystem.rules.Decision;
import bankingandfraudsystem.rules.FraudEngine;
import bankingandfraudsystem.rules.FraudRule;
import bankingandfraudsystem.rules.RuleResult;
import bankingandfraudsystem.rules.impl.LargeAmountRule;
import bankingandfraudsystem.rules.impl.TooManyTransactionsRule;
import bankingandfraudsystem.rules.impl.VelocitySumRule;
import bankingandfraudsystem.service.BankService;
import bankingandfraudsystem.util.Currency;
import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.util.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Ledger ledger = new Ledger();

            FraudRule rule1 = new LargeAmountRule(new Money(Currency.GEL, new BigDecimal("500")), Decision.REVIEW);
            FraudRule rule2 = new TooManyTransactionsRule(5,2,Decision.REVIEW);
            FraudRule rule3 = new VelocitySumRule(new Money(Currency.GEL, new BigDecimal("1000")),10);

            List<FraudRule>rules = new ArrayList<>() ;
            rules.add(rule1);
            rules.add(rule2);
            rules.add(rule3);

            FraudEngine engine = new FraudEngine(rules);

            BankService bank = new BankService(ledger,engine);

            var customer = bank.createCustomer("Test User");
            var checkingAccount = bank.openChecking(customer.getId(),Currency.GEL, new Money(Currency.GEL, new BigDecimal("200")));
            var savingAccount = bank.openSavings(customer.getId(),Currency.GEL,new Money(Currency.GEL,new BigDecimal("50")),10);

            System.out.println("\n== Deposit 300 EUR to Checking ==");
            var deposit = bank.deposit(checkingAccount.getID(),new Money(Currency.GEL,new BigDecimal("300")),"Initial Deposit");
            System.out.println(deposit.getStatus() + " | balance=" + checkingAccount.getBalance());

        }catch(CurrencyMismatchException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}