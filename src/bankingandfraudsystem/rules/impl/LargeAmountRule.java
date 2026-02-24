package bankingandfraudsystem.rules.impl;

import bankingandfraudsystem.domain.transaction.Transaction;
import bankingandfraudsystem.rules.Decision;
import bankingandfraudsystem.rules.FraudContext;
import bankingandfraudsystem.rules.FraudRule;
import bankingandfraudsystem.rules.RuleResult;
import bankingandfraudsystem.util.Money;

public class LargeAmountRule implements FraudRule {
    private final Money threshold;
    private final Decision decisionOnHit;

    public LargeAmountRule(Money money, Decision decision) {
        if(money == null) throw new IllegalArgumentException("Money cannot be null!");

        if(!money.isPositive()) throw new IllegalArgumentException("Money must be positive!");

        if(decision == null) throw new IllegalArgumentException("Decision cannot be null!");

        if(decision == Decision.ALLOW) throw new IllegalArgumentException("Decision should not be ALLOW!");

        this.threshold = money;
        this.decisionOnHit = decision;
    }

    @Override
    public RuleResult evaluate(Transaction transaction, FraudContext ctx) {
        if(transaction == null) throw new IllegalArgumentException("Transaction cannot be null!");
        if(ctx == null) throw new IllegalArgumentException("FraudContext cannot be null!");

        if(transaction.getAmount().getCurrency() != this.threshold.getCurrency())
            return RuleResult.allow();

        if(transaction.getAmount().compareTo(this.threshold) > 0) {
            String reason = "Large Amount: " + transaction.getAmount() + " > " + this.threshold;
            RuleResult ans = (decisionOnHit == Decision.BLOCK) ? RuleResult.block(reason) : RuleResult.review(reason);
            return ans;
        }
        else
            return RuleResult.allow();
    }

    public Money getThreshold() {
        return this.threshold;
    }

    public Decision getDecisionOnHit() {
        return this.decisionOnHit;
    }
}
