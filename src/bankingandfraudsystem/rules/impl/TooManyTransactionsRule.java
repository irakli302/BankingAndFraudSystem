package bankingandfraudsystem.rules.impl;

import bankingandfraudsystem.domain.transaction.Transaction;
import bankingandfraudsystem.rules.Decision;
import bankingandfraudsystem.rules.FraudContext;
import bankingandfraudsystem.rules.FraudRule;
import bankingandfraudsystem.rules.RuleResult;

public class TooManyTransactionsRule implements FraudRule {
    private final int maxCount;
    private final int windowMinutes;
    private final Decision decisionOnHit;

    public TooManyTransactionsRule(int maxCnt, int windowMnts, Decision decision) {
        if(maxCnt < 1) throw new IllegalArgumentException("MaxCount should be greater then 0!");

        if(windowMnts < 1) throw new IllegalArgumentException("WindowMinutes should be greater then 0!");

        if(decision == null) throw new IllegalArgumentException("Decision cannot be null!");

        if(decision == Decision.ALLOW) throw new IllegalArgumentException("Decision should not be ALLOW!");

        this.maxCount = maxCnt;
        this.windowMinutes = windowMnts;
        this.decisionOnHit = decision;
    }

    @Override
    public RuleResult evaluate(Transaction transaction, FraudContext ctx) {
        if(transaction == null) throw new IllegalArgumentException("Transaction cannot be null!");
        if(ctx == null) throw new IllegalArgumentException("FraudContext cannot be null!");

        int count = ctx.countWithInMinutes(windowMinutes) + 1;
        String reason = "High velocity: " + count + " transaction in " + this.windowMinutes + " minutes";

        if(count > maxCount) {
            return (this.decisionOnHit == Decision.BLOCK) ? RuleResult.block(reason) : RuleResult.review(reason);
        }

        return RuleResult.allow();
    }

    public int getMaxCount() {
        return this.maxCount;
    }

    public int getWindowMinutes() {
        return this.windowMinutes;
    }
}
