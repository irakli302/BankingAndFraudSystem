package bankingandfraudsystem.rules;

import bankingandfraudsystem.domain.transaction.Transaction;

public interface FraudRule {
    RuleResult evaluate(Transaction transaction, FraudContext ctx);
}
