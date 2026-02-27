package bankingandfraudsystem.rules.impl;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.transaction.CardPayment;
import bankingandfraudsystem.domain.transaction.Transaction;
import bankingandfraudsystem.rules.Decision;
import bankingandfraudsystem.rules.FraudContext;
import bankingandfraudsystem.rules.FraudRule;
import bankingandfraudsystem.rules.RuleResult;

public class NewMerchantRule implements FraudRule {
    private Decision decisionOnHit;

    public NewMerchantRule(Decision decision) {
        if(decision == null)
            throw new IllegalArgumentException("Decision cannot be null!");
        if(decision == Decision.ALLOW)
            throw new IllegalArgumentException("Decision cannot be null!");

        this.decisionOnHit = decision;
    }


    @Override
    public RuleResult evaluate(Transaction tx, FraudContext ctx) throws CurrencyMismatchException {
        if(tx == null)
            throw new IllegalArgumentException("Transaction cannot be null!");
        if(ctx == null)
            throw new IllegalArgumentException("FraudContext cannot be null!");

        if(!(tx instanceof CardPayment cp)) return RuleResult.allow();

        if (!ctx.hasSeenMerchant(cp.getMerchant().getId())) {
            String reason = "New Merchant: " + cp.getMerchant().getFullName();
            RuleResult result = this.decisionOnHit == Decision.BLOCK ? RuleResult.block(reason) : RuleResult.review(reason);
            return result;
        }
        return RuleResult.allow();
    }
}
