package bankingandfraudsystem.rules;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.customer.Customer;
import bankingandfraudsystem.domain.transaction.Transaction;
import bankingandfraudsystem.util.Currency;
import bankingandfraudsystem.util.Money;

import java.time.Duration;
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

    public List<Transaction>lastN(int n) {
        if(n <= 0) throw new IllegalArgumentException("N must be greater than 0, please try again!");

        int size = getPostedHistory().size();

        if(n >= size) return List.copyOf(this.postedHistory);

        return List.copyOf(this.postedHistory.subList(size-n,size));
    }

    public List<Transaction> withInMinutes(int minutes) {
        if(minutes <= 0) throw new IllegalArgumentException("Minutes must be positive, please try again!");

        Instant inTime = this.now.minus(Duration.ofMinutes(minutes));

        List<Transaction>lst = new ArrayList<>();

        for (Transaction tx : this.postedHistory) {
            if(tx.getCreatedAt().isAfter(inTime)) lst.add(tx);
        }

        return Collections.unmodifiableList(lst);
    }

    public int countWithInMinutes(int minutes) {
        return withInMinutes(minutes).size();
    }

    public Money sumWithinMinutes(int minutes, Currency currency) throws CurrencyMismatchException {
        if(currency == null) throw new IllegalArgumentException("Currency cannot be null!");

        List<Transaction>lst = new ArrayList<>(withInMinutes(minutes));

        Money sum = Money.zero(currency);

        for(Transaction tx : lst) {
            Money money = tx.getAmount();
            if(!money.getCurrency().equals(currency)) throw new CurrencyMismatchException("Currency mismatch!");
            sum=sum.add_Money(tx.getAmount());
        }

        return sum;
    }

    public boolean hasAnyTransaction() {
        return this.postedHistory.size() > 0;
    }

    public Transaction mostRecent() {
        if(this.postedHistory.isEmpty()) return null;

        return this.postedHistory.getLast();
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
