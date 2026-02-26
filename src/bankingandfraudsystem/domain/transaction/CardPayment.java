package bankingandfraudsystem.domain.transaction;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.card.Card;
import bankingandfraudsystem.domain.card.CardStatus;
import bankingandfraudsystem.domain.merchant.Merchant;
import bankingandfraudsystem.util.Money;

public class CardPayment extends Transaction {
    private final Card card;
    private final Merchant merchant;

    public CardPayment(Money amount, String description, Card card, Merchant merchant) throws CurrencyMismatchException {
        super(amount,description);
        if(card == null)
            throw new IllegalArgumentException("Card cannot be null!");
        if(merchant == null)
            throw new IllegalArgumentException("Merchant cannot be null!");
        if(amount.getCurrency() != card.getLinkedAccount().getCurrency())
            throw new CurrencyMismatchException("Currency mismatch!");
        if(card.getStatus() != CardStatus.ACTIVE)
            throw new IllegalArgumentException("Card status must be ACTIVE!");

        this.card = card;
        this.merchant = merchant;
    }
    
}
