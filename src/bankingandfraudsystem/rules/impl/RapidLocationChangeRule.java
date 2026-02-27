package bankingandfraudsystem.rules.impl;

import bankingandfraudsystem.rules.Decision;

import java.time.Duration;

public class RapidLocationChangeRule {
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
}
