package ru.netology.moneytransferservice.model;

public enum TransferRejectionReason {

    INSUFFICIENT_FUNDS, FROM_CARD_NOT_FOUND, TO_CARD_NOT_FOUND, INCORRECT_CONFIRMATION_CODE, ALL_CARDS_NOT_FOUND

}
