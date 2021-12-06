package ru.netology.moneytransferservice.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class TransferRequest {

    @NotNull(message = "Card number is null")
    @Size(min = 16, message = "Card number contains less then 16 symbols")
    @Pattern(regexp = "[0-9]+", message = "Card number contains not only numeric characters")
    protected String cardFromNumber;

    @NotNull(message = "Data is null")
    @Pattern(regexp = "(0[1-9]|1[0-2])[0-9][0-9]", message = "Data is not correct")
    protected String cardFromValidTill;

    @NotNull(message = "CVV code is null")
    @Size(min = 3, message = "CVV code contains less then 3 symbols")
    @Pattern(regexp = "[0-9]+", message = "CVV code contains contains not only numeric characters")
    protected String cardFromCVV;

    @NotNull(message = "Card number is null")
    @Size(min = 16, message = "Card number contains less then 16 symbols")
    @Pattern(regexp = "[0-9]+", message = "Card number contains not only numeric characters")
    protected String cardToNumber;

    @NotNull(message = "Amount is null")
    protected Amount amount;

    public TransferRequest(String cardFromNumber, String cardFromValidTill, String cardFromCVV, String cardToNumber, Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardFromValidTill = cardFromValidTill;
        this.cardFromCVV = cardFromCVV;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
    }

    public String getCardFromNumber() {
        return cardFromNumber;
    }

    public String getCardFromValidTill() {
        return cardFromValidTill;
    }

    public String getCardFromCVV() {
        return cardFromCVV;
    }

    public String getCardToNumber() {
        return cardToNumber;
    }

    public Amount getAmount() {
        return amount;
    }
}
