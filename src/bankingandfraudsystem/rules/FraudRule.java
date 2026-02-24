package bankingandfraudsystem.rules;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.transaction.Transaction;

public interface FraudRule {
    RuleResult evaluate(Transaction transaction, FraudContext ctx) throws CurrencyMismatchException;
}
