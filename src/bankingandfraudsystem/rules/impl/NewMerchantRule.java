package bankingandfraudsystem.rules.impl;

import bankingandfraudsystem.rules.Decision;
import bankingandfraudsystem.rules.FraudRule;

public class NewMerchantRule implements FraudRule {
    private Decision decisionOnHit;

    public NewMerchantRule(Decision decision) {
        if(decision == null)
            throw new IllegalArgumentException("Decision cannot be null!");
        if(decision == Decision.ALLOW)
            throw new IllegalArgumentException("Decision cannot be null!");

        this.decisionOnHit = decision;
    }
}
