package ru.netology.moneytransferservice.model;

import java.util.Objects;

import static ru.netology.moneytransferservice.repository.TransferRepository.COMMISSION_RATE;

public class TransferOperation {
    protected final OperationIdentifier operationIdentifier;
    protected String confirmationCode;
    protected final String fromBankCardNumber;
    protected final String toBankCardNumber;
    protected final Amount amount;
    protected final int commission;
    protected boolean isVerified;
    protected boolean isConfirmed;
    protected TransferRejectionReason transferRejectionReasonEnum;


    public TransferOperation(OperationIdentifier operationIdentifier, String fromBankCardNumber, String toBankCardNumber, Amount amount) {
        this.operationIdentifier = operationIdentifier;
        this.fromBankCardNumber = fromBankCardNumber;
        this.toBankCardNumber = toBankCardNumber;
        this.amount = amount;
        commission = (int) Math.round(amount.getValue() - amount.getValue() * (1 - COMMISSION_RATE));
        transferRejectionReasonEnum = TransferRejectionReason.NON;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public void setTransferRejectionReasonEnum(TransferRejectionReason transferRejectionReasonEnum) {
        this.transferRejectionReasonEnum = transferRejectionReasonEnum;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public OperationIdentifier getOperationIdentifier() {
        return operationIdentifier;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public String getFromBankCardNumber() {
        return fromBankCardNumber;
    }

    public String getToBankCardNumber() {
        return toBankCardNumber;
    }

    public Amount getAmount() {
        return amount;
    }

    public TransferRejectionReason getTransferRejectionReasonEnum() {
        return transferRejectionReasonEnum;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public int getCommission() {
        return commission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferOperation that = (TransferOperation) o;
        return operationIdentifier.equals(that.operationIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationIdentifier);
    }

    @Override
    public String toString() {
        String resultStatus;
        if (isConfirmed()) {
            resultStatus = "completed";
        } else {
            if (isVerified()) {
                resultStatus = "in process";
            } else {
                resultStatus = "uncompleted";
            }
        }

        return "Operation ID: " + operationIdentifier.getOperationId() +
                "\nFrom: bank card number: " + fromBankCardNumber +
                "\nTo: bank card number: " + toBankCardNumber +
                "\nAmount value: " + amount.getValue() +
                "\nincluding commission value: " + commission +
                "\nStatus: " + resultStatus +
                "\nRejection reason: " + transferRejectionReasonEnum.getTitle();
    }
}
