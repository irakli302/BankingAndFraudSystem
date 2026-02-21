package bankingandfraudsystem.domain.ledger;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.transaction.Transaction;
import bankingandfraudsystem.domain.transaction.TransactionStatus;

import java.util.*;

public class Ledger {
    private final List<Transaction>history = new ArrayList<Transaction>();

    public void post(Transaction tx) throws CurrencyMismatchException {
        if(tx == null) throw new IllegalArgumentException("Transaction cannot be null!");

        if(!tx.getStatus().equals(TransactionStatus.APPROVED)) throw new IllegalStateException("Transaction must be APPROVED!");

        if(isPosted(tx.getId())) throw new IllegalStateException("Transaction already exists in history!");

        tx.apply();
        tx.markPosted();
        history.add(tx);

    }

    public List<Transaction> getHistory() {
        return Collections.unmodifiableList(this.history);
    }

    public boolean isPosted(UUID id) {
        if(id == null) throw new IllegalArgumentException("Transaction ID cannot be null!");

        for(Transaction transaction : this.history) {
            if(transaction.getId().equals(id)) return true;
        }
        return false;
    }


}
