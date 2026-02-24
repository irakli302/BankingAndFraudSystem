package bankingandfraudsystem.rules;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class FraudEngine {
    private final List<FraudRule>rules;

    public FraudEngine(List<FraudRule>fraudRules) {
        if(fraudRules == null) throw new IllegalArgumentException("Rules cannot be null!");

        if(fraudRules.contains(null)) throw new IllegalArgumentException("Rules list contains null!");

        this.rules = List.copyOf(fraudRules);
    }

    public RuleResult assess(Transaction tx, FraudContext ctx) throws CurrencyMismatchException {
        if(tx == null) throw new IllegalArgumentException("Transaction cannot be null!");

        if(ctx == null) throw new IllegalArgumentException("Fraud Context cannot be null!");

        RuleResult best = RuleResult.allow();

        for(FraudRule rule : this.rules) {
            RuleResult result = rule.evaluate(tx,ctx);

            if(result.getDecision() == Decision.BLOCK) return result;

            if(result.getDecision() == Decision.REVIEW && best.getDecision() == Decision.ALLOW) best = result;
        }

        return best;
    }
}
