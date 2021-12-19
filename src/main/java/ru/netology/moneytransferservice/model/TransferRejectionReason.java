package ru.netology.moneytransferservice.model;

public enum TransferRejectionReason {

    INSUFFICIENT_FUNDS("Insufficient funds"),
    FROM_CARD_NOT_FOUND("Sender bank card is not found"),
    TO_CARD_NOT_FOUND("Receiver bank card is not found"),
    INCORRECT_CONFIRMATION_CODE("Incorrect confirmation code"),
    ALL_CARDS_NOT_FOUND("All bank cards are not found"),
    NON("No");

    private final String title;

    TransferRejectionReason(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
