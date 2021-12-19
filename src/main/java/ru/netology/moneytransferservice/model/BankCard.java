package ru.netology.moneytransferservice.model;

import java.util.Objects;

public class BankCard {
    protected int balance;
    protected final String number;
    protected final String validTill;
    protected final String CVV;
    protected final String name;
    protected final String surname;

    public BankCard(int balance, String number, String validTill, String CVV, String name, String surname) {
        this.balance = balance;
        this.number = number;
        this.validTill = validTill;
        this.CVV = CVV;
        this.name = name;
        this.surname = surname;
    }

    public BankCard(String number, String validTill, String CVV, String name, String surname) {
        this.number = number;
        this.validTill = validTill;
        this.CVV = CVV;
        this.name = name;
        this.surname = surname;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public String getNumber() {
        return number;
    }

    public String getValidTill() {
        return validTill;
    }

    public String getCVV() {
        return CVV;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankCard bankCard = (BankCard) o;
        return number.equals(bankCard.number) && validTill.equals(bankCard.validTill) && CVV.equals(bankCard.CVV) && name.equals(bankCard.name) && surname.equals(bankCard.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, validTill, CVV, name, surname);
    }
}
