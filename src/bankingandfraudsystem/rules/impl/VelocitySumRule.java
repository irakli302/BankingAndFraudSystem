package bankingandfraudsystem.rules.impl;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.transaction.Transaction;
import bankingandfraudsystem.rules.Decision;
import bankingandfraudsystem.rules.FraudContext;
import bankingandfraudsystem.rules.FraudRule;
import bankingandfraudsystem.rules.RuleResult;
import bankingandfraudsystem.util.Currency;
import bankingandfraudsystem.util.Money;

public class VelocitySumRule implements FraudRule {
    private final Money threshold;
    private final int windowMinutes;

    public VelocitySumRule(Money money, int windowMnts) {
        if(money == null) throw new IllegalArgumentException("Money cannot be null!");

        if(!money.isPositive()) throw new IllegalArgumentException("Money must be positive!");

        if(windowMnts < 1) throw new IllegalArgumentException("WindowMinutes should be greater then 0!");

        this.threshold = money;
        this.windowMinutes = windowMnts;
    }

    @Override
    public RuleResult evaluate(Transaction transaction, FraudContext ctx) throws CurrencyMismatchException {
        if(transaction == null) throw new IllegalArgumentException("Transaction cannot be null!");
        if(ctx == null) throw new IllegalArgumentException("FraudContext cannot be null!");

        if(transaction.getAmount().getCurrency() != this.threshold.getCurrency())
            return RuleResult.allow();

        Currency currency = transaction.getAmount().getCurrency();

        Money sum = ctx.sumWithinMinutes(windowMinutes,currency);

        Money projected = sum.add_Money(transaction.getAmount());

        if(projected.compareTo(this.threshold) > 0)
            return RuleResult.review("High spend: " + projected + " in last " + this.windowMinutes + " minutes!");

        return RuleResult.allow();
    }
}
