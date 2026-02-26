package bankingandfraudsystem.domain.transaction;

import bankingandfraudsystem.Exception.CurrencyMismatchException;
import bankingandfraudsystem.domain.account.Account;
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

    @Override
    public void apply() throws CurrencyMismatchException {
        if(card.getStatus() != CardStatus.ACTIVE)
            throw new IllegalStateException("Card must be active!");

        if(!card.canAuthorise(getAmount()))
            throw new IllegalStateException("Card can't authorize transaction!");


        card.getLinkedAccount().withDraw(getAmount());
        card.recordSpend(getAmount());
    }

    @Override
    public TransactionType type() {
        return TransactionType.CARD_PAYMENT;
    }

    @Override
    public boolean involves(Account account) {
        if(account == null)
            throw new IllegalArgumentException("Account cannot be null!");

        return card.getLinkedAccount().getID().equals(account.getID());
    }

    public Card getCard() {
        return this.card;
    }

    public Merchant getMerchant() {
        return this.merchant;
    }
}
