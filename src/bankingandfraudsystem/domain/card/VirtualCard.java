package bankingandfraudsystem.domain.card;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.account.Account;
import bankingandfraudsystem.domain.customer.Customer;
import bankingandfraudsystem.util.Money;

public class VirtualCard extends Card{
    public VirtualCard(Customer owner, Account linkedAcc, Money dailylimit) throws CurrencyMismatchException {
        super(owner,linkedAcc,dailylimit);
    }
}
