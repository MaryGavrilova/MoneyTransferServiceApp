package ru.netology.moneytransferservice.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.netology.moneytransferservice.model.*;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransferRepository {
    private final Map<String, BankCard> bankCardsDataBase = new ConcurrentHashMap<>();
    private final Map<String, TransferOperation> transferOperationsDataBase = new ConcurrentHashMap<>();
    private long numberOfAllTransferOperations;

    private final Random random = new Random();

    @Value("${transfer.repository.verificationCodeRandomBound:10000}")
    protected int verificationCodeRandomBound;

    public TransferOperation initiateMoneyTransfer(TransferRequest transferRequest) {
        TransferOperation transferOperation = recordTransferOperation(transferRequest);
        transferOperationsDataBase.put(transferOperation.getOperationIdentifier().getOperationId(), transferOperation);

        if (!checkBankDetailsCardFrom(transferRequest) && checkBankDetailsCardTo(transferRequest)) {
            transferOperation.setTransferRejectionReasonEnum(TransferRejectionReason.FROM_CARD_NOT_FOUND);
            return transferOperation;
        }
        if (checkBankDetailsCardFrom(transferRequest) && !checkBankDetailsCardTo(transferRequest)) {
            transferOperation.setTransferRejectionReasonEnum(TransferRejectionReason.TO_CARD_NOT_FOUND);
            return transferOperation;
        }
        if (!checkBankDetailsCardFrom(transferRequest) && !checkBankDetailsCardTo(transferRequest)) {
            transferOperation.setTransferRejectionReasonEnum(TransferRejectionReason.ALL_CARDS_NOT_FOUND);
            return transferOperation;
        }
        transferOperation.setConfirmationCode(String.valueOf(random.nextInt(verificationCodeRandomBound)));
        transferOperation.setVerified(true);
        return transferOperation;
    }

    public Optional<TransferOperation> confirmMoneyTransfer(OperationConfirmation operationConfirmation) {
        if (transferOperationsDataBase.containsKey(operationConfirmation.getOperationId())) {
            TransferOperation transferOperation = transferOperationsDataBase.get(operationConfirmation.getOperationId());
            if (transferOperation.getConfirmationCode().equals(operationConfirmation.getCode())) {
                if (checkBalanceCardFrom(transferOperation)) {
                    transferMoney(transferOperation);
                    transferOperation.setConfirmed(true);
                } else {
                    transferOperation.setTransferRejectionReasonEnum(TransferRejectionReason.INSUFFICIENT_FUNDS);
                }
            } else {
                transferOperation.setTransferRejectionReasonEnum(TransferRejectionReason.INCORRECT_CONFIRMATION_CODE);
            }
            return Optional.of(transferOperation);
        } else {
            return Optional.empty();
        }
    }

    //регистрация попытки перевода путем создания операции и присвоения ей уникального номера
    public TransferOperation recordTransferOperation(TransferRequest transferRequest) {
        String operationId = String.valueOf(++numberOfAllTransferOperations);
        return new TransferOperation(
                new OperationIdentifier(operationId),
                transferRequest.getCardFromNumber(),
                transferRequest.getCardToNumber(),
                transferRequest.getAmount());
    }

    //проверка на существование банковской карты отправителя в базе и получение полных данных о его счете
    public boolean checkBankDetailsCardFrom(TransferRequest transferRequest) {
        if (bankCardsDataBase.containsKey(transferRequest.getCardFromNumber())) {
            BankCard bankCardFrom = bankCardsDataBase.get(transferRequest.getCardFromNumber());
            return bankCardFrom.getNumber().equals(transferRequest.getCardFromNumber()) &&
                    bankCardFrom.getValidTill().equals(transferRequest.getCardFromValidTill()) &&
                    bankCardFrom.getCVV().equals(transferRequest.getCardFromCVV());
        } else return false;
    }


    //проверка на существование банковской карты получателя в базе и получение полных данных о его счете
    public boolean checkBankDetailsCardTo(TransferRequest transferRequest) {
       return bankCardsDataBase.containsKey(transferRequest.getCardToNumber());
    }

    //проверка баланса банковской карты отправителя
    public boolean checkBalanceCardFrom(TransferOperation transferOperation) {
        return bankCardsDataBase.get(transferOperation.getFromBankCardNumber()).getBalance() >= transferOperation.getAmount().getValue() * (1 + transferOperation.getCommissionRate());
    }

    //перевод средств c карты на карту
    public void transferMoney(TransferOperation transferOperation) {
        BankCard fromBankCard = bankCardsDataBase.get(transferOperation.getFromBankCardNumber());
        fromBankCard.setBalance(fromBankCard.getBalance() - transferOperation.getAmount().getValue() * (1 + transferOperation.getCommissionRate()));
        BankCard toBankCard = bankCardsDataBase.get(transferOperation.getToBankCardNumber());
        toBankCard.setBalance(toBankCard.getBalance() + transferOperation.getAmount().getValue());
    }
}

