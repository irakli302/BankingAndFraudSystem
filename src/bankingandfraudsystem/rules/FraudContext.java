package bankingandfraudsystem.rules;

import bankingandfraudsystem.domain.customer.Customer;
import bankingandfraudsystem.domain.transaction.Transaction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FraudContext {
    private final Customer customer;
    private final List<Transaction>postedHistory;
    private final Instant now;

    public FraudContext(Customer cus, List<Transaction>postedHistory, Instant now) {
        if(cus == null) throw new IllegalArgumentException("Customer cannot be null!");

        if(now == null) throw new IllegalArgumentException("Time cannot be null!");

        if(postedHistory == null) throw new IllegalArgumentException("Posted history cannot be null!");

        this.customer = cus;
        this.now = now;

        List<Transaction>new_lst = new ArrayList<>(postedHistory);
        new_lst.sort(Comparator.comparing(Transaction::getCreatedAt));
        this.postedHistory = List.copyOf(new_lst);
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public Instant getNow() {
        return this.now;
    }

    public List<Transaction> getPostedHistory() {
        return Collections.unmodifiableList(this.postedHistory);
    }
}
