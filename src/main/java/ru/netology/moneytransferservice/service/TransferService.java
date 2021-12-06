package ru.netology.moneytransferservice.service;

import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.exception.MoneyTransferConfirmationException;
import ru.netology.moneytransferservice.exception.MoneyTransferInitiationException;
import ru.netology.moneytransferservice.model.*;
import ru.netology.moneytransferservice.repository.TransferRepository;

import java.util.Optional;

@Service
public class TransferService {
    TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public OperationIdentifier initiateMoneyTransfer(TransferRequest transferRequest) {
        TransferOperation transferOperation = transferRepository.initiateMoneyTransfer(transferRequest);
        if (!transferOperation.isVerified()) {
            if (transferOperation.getTransferRejectionReasonEnum() == TransferRejectionReason.FROM_CARD_NOT_FOUND)
                throw new MoneyTransferInitiationException("Bank details failed validation: " +
                        "sender bank card does not exist, money transfer can not be made.");
            if (transferOperation.getTransferRejectionReasonEnum() == TransferRejectionReason.TO_CARD_NOT_FOUND)
                throw new MoneyTransferInitiationException("Bank details failed validation: " +
                        "receiver bank card does not exist, money transfer can not be made.");
            if (transferOperation.getTransferRejectionReasonEnum() == TransferRejectionReason.ALL_CARDS_NOT_FOUND)
                throw new MoneyTransferInitiationException("Bank details failed validation: " +
                        "sender bank card and receiver bank card do not exist, money transfer can not be made.");
        }
        return transferOperation.getOperationIdentifier();
    }

    public OperationIdentifier confirmMoneyTransfer(OperationConfirmation operationConfirmation) {
        Optional<TransferOperation> optionalTransferOperation = transferRepository.confirmMoneyTransfer(operationConfirmation);
        if (optionalTransferOperation.isPresent()) {
            TransferOperation transferOperation = optionalTransferOperation.get();
            if (!transferOperation.isConfirmed()) {
                if (transferOperation.getTransferRejectionReasonEnum() == TransferRejectionReason.INCORRECT_CONFIRMATION_CODE)
                    throw new MoneyTransferConfirmationException("Confirmation code is incorrect, money transfer can not be made.");
                if (transferOperation.getTransferRejectionReasonEnum() == TransferRejectionReason.INSUFFICIENT_FUNDS)
                throw new MoneyTransferConfirmationException("There are not enough funds on the sender's card for transfer, money transfer can not be made.");
            } else {
                return transferOperation.getOperationIdentifier();
            }
        } else {
            throw new MoneyTransferConfirmationException("Operation ID is incorrect, money transfer can not be made.");
        }
        throw new MoneyTransferConfirmationException("Server error: Money transfer can not be made.");
    }
}
