package ru.netology.moneytransferservice.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Amount {
    @Min(1)
    protected final int value;

    @NotNull(message = "Currency is null")
    protected final String currency;

    public Amount(int value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public int getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }
}
