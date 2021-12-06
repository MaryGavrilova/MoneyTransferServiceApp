package ru.netology.moneytransferservice.model;

import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

public class TransferOperation {
    private final OperationIdentifier operationIdentifier;
    private String confirmationCode;
    private final String fromBankCardNumber;
    private final String toBankCardNumber;
    private final Amount amount;
    private boolean isVerified;
    private boolean isConfirmed;
    private Enum<TransferRejectionReason> transferRejectionReasonEnum;

    @Value("${transfer.operation.commissionRate:0}")
    private double commissionRate;

    public TransferOperation(OperationIdentifier operationIdentifier, String fromBankCardNumber, String toBankCardNumber, Amount amount) {
        this.operationIdentifier = operationIdentifier;
        this.fromBankCardNumber = fromBankCardNumber;
        this.toBankCardNumber = toBankCardNumber;
        this.amount = amount;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }


    public void setTransferRejectionReasonEnum(Enum<TransferRejectionReason> transferRejectionReasonEnum) {
        this.transferRejectionReasonEnum = transferRejectionReasonEnum;
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

    public double getCommissionRate() {
        return commissionRate;
    }

    public Enum<TransferRejectionReason> getTransferRejectionReasonEnum() {
        return transferRejectionReasonEnum;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
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
}
