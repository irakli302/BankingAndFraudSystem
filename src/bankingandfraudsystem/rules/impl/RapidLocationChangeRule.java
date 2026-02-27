package bankingandfraudsystem.rules.impl;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.transaction.CardPayment;
import bankingandfraudsystem.domain.transaction.Transaction;
import bankingandfraudsystem.rules.*;

import java.time.Duration;

public class RapidLocationChangeRule implements FraudRule {
    private Duration maxTimeBetween;
    private Decision decisionOnHit;

    public RapidLocationChangeRule(Duration maxTimeBetween, Decision decision) {
        if(maxTimeBetween == null)
            throw new IllegalArgumentException("Time cannot be null!");
        if(!maxTimeBetween.isPositive())
            throw new IllegalArgumentException("Time must be positive!");
        if(decision == null)
            throw new IllegalArgumentException("Decision cannot be null!");
        if(decision == Decision.ALLOW)
            throw new IllegalArgumentException("Decision cannot be ALLOW!");

        this.maxTimeBetween = maxTimeBetween;
        this.decisionOnHit = decision;
    }

    @Override
    public RuleResult evaluate(Transaction tx, FraudContext ctx) throws CurrencyMismatchException {
        if(tx == null)
            throw new IllegalArgumentException("Transaction cannot be null!");
        if(ctx == null)
            throw new IllegalArgumentException("FraudContext cannot be null!");

        if(!(tx instanceof CardPayment cp)) return RuleResult.allow();

        for(Transaction transaction : ctx.getPostedHistory()){
            if(transaction instanceof CardPayment cardPayment){
                if(!cardPayment.getMerchant().getCountry().equals(cp.getMerchant().getCountry())){
                    Duration between = Duration.between(cardPayment.getCreatedAt(),cp.getCreatedAt());

                    if(between.isNegative() && between.compareTo(this.maxTimeBetween) <= 0) {
                        String reason = "Rapid location change detected: " + cardPayment.getMerchant().getCountry() + " → " + cp.getMerchant().getCountry() +
                                " within " + between + " minutes!";
                        return this.decisionOnHit == Decision.REVIEW ? RuleResult.review(reason) : RuleResult.block(reason);
                    }
                }
            }
        }
        return RuleResult.allow();
    }

    public Decision getDecisionOnHit() {
        return this.decisionOnHit;
    }

    public Duration getMaxTimeBetween() {
        return this.maxTimeBetween;
    }
}
