package ru.netology.moneytransferservice.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.exception.MoneyTransferConfirmationException;
import ru.netology.moneytransferservice.exception.MoneyTransferInitiationException;
import ru.netology.moneytransferservice.model.*;
import ru.netology.moneytransferservice.repository.TransferRepository;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static ru.netology.moneytransferservice.model.TransferRejectionReason.NON;
import static ru.netology.moneytransferservice.repository.TransferRepository.*;

@Service
public class TransferService {

    private static final Logger LOGGER = Logger.getLogger(TransferService.class);

    private final TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public OperationIdentifier initiateMoneyTransfer(TransferRequest transferRequest) {
        // проверка - валидность срока действия карты отправителя
        if (!checkValidTill(transferRequest.getCardFromValidTill())) {
            throw new ValidationException("The date of validity of the sender's card has expired");
        }

        // регистрация попытки перевода
        TransferOperation transferOperation = recordTransferOperation(transferRequest);
        transferRepository.putInTransferOperationsDataBase(transferOperation.getOperationIdentifier().getOperationId(), transferOperation);
        LOGGER.info("Initiation: Transfer operation is recorded:\n" + transferOperation);

        // проверка - банковской карты отправителя нет в базе данных
        if (!checkBankDetailsCardFrom(transferRequest) && checkBankDetailsCardTo(transferRequest)) {
            transferOperation.setTransferRejectionReasonEnum(TransferRejectionReason.FROM_CARD_NOT_FOUND);
            LOGGER.info("Initiation: Transfer operation is rejected:\n" + transferOperation);
            throw new MoneyTransferInitiationException("Bank details failed validation: " +
                    "sender bank card does not exist, money transfer can not be made.");
        }

        // проверка - банковской карты получателя нет в базе данных
        if (checkBankDetailsCardFrom(transferRequest) && !checkBankDetailsCardTo(transferRequest)) {
            transferOperation.setTransferRejectionReasonEnum(TransferRejectionReason.TO_CARD_NOT_FOUND);
            LOGGER.info("Initiation: Transfer operation is rejected:\n" + transferOperation);
            throw new MoneyTransferInitiationException("Bank details failed validation: " +
                    "receiver bank card does not exist, money transfer can not be made.");
        }

        // проверка - банковской карты отправителя и получателя  нет в базе данных
        if (!checkBankDetailsCardFrom(transferRequest) && !checkBankDetailsCardTo(transferRequest)) {
            transferOperation.setTransferRejectionReasonEnum(TransferRejectionReason.ALL_CARDS_NOT_FOUND);
            LOGGER.info("Initiation: Transfer operation is rejected:\n" + transferOperation);
            throw new MoneyTransferInitiationException("Bank details failed validation: " +
                    "sender bank card and receiver bank card do not exist, money transfer can not be made.");
        }

        // успешный случай - банковская карта отправителя и получателя есть в базе данных
        transferOperation.setConfirmationCode(VERIFICATION_CODE);
        transferOperation.setVerified(true);
        LOGGER.info("Initiation: Bank details are verified successfully:\n" + transferOperation);

        return transferOperation.getOperationIdentifier();
    }

    public OperationIdentifier confirmMoneyTransfer(OperationConfirmation operationConfirmation) {
        // проверка - наличие операции с указанным ID
        if (transferRepository.containsTransferOperationsDataBase(operationConfirmation.getOperationId())) {
            TransferOperation transferOperation = transferRepository.getTransferOperation(operationConfirmation.getOperationId());
            transferOperation.setTransferRejectionReasonEnum(NON);
            LOGGER.info("Confirmation: Transfer operation is found:\n" + transferOperation);

            // проверка - совпадение кода подтверждения
            if (transferOperation.getConfirmationCode().equals(operationConfirmation.getCode())) {
                LOGGER.info("Confirmation: Code is verified");

                // проверка - хватает ли средств на карте отправителя для совершения перевода
                if (checkBalanceCardFrom(transferOperation)) {
                    LOGGER.info("Confirmation: sender's funds are enough for transfer");

                    // совершение перевода
                    return transferMoney(transferOperation).getOperationIdentifier();

                } else {
                    transferOperation.setTransferRejectionReasonEnum(TransferRejectionReason.INSUFFICIENT_FUNDS);
                    LOGGER.info("Confirmation: Transfer operation is rejected:\n" + transferOperation);
                    throw new MoneyTransferConfirmationException("There are not enough funds on the sender's card for transfer, money transfer can not be made.");
                }

            } else {
                transferOperation.setTransferRejectionReasonEnum(TransferRejectionReason.INCORRECT_CONFIRMATION_CODE);
                LOGGER.info("Confirmation: Transfer operation is rejected:\n" + transferOperation);
                throw new MoneyTransferConfirmationException("Confirmation code is incorrect, money transfer can not be made.");
            }

        } else {
            throw new MoneyTransferConfirmationException("Operation ID is incorrect, money transfer can not be made.");
        }
    }

    // проверка срока действия карты отправителя
    public boolean checkValidTill(String validTill) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        int month = Integer.parseInt(validTill.substring(0, 2));
        int year = Integer.parseInt("20" + validTill.substring(2));

        if (year > currentYear) {
            return true;
        }
        if (year < currentYear) {
            return false;
        }
        return month >= currentMonth;
    }

    //регистрация попытки перевода путем создания операции и присвоения ей уникального номера
    public TransferOperation recordTransferOperation(TransferRequest transferRequest) {
        String operationId = String.valueOf(NUMBER_OF_ALL_TRANSFER_OPERATIONS.incrementAndGet());
        return new TransferOperation(
                new OperationIdentifier(operationId),
                transferRequest.getCardFromNumber(),
                transferRequest.getCardToNumber(),
                transferRequest.getAmount());
    }

    //проверка на существование банковской карты отправителя в базе и получение полных данных о его счете
    public boolean checkBankDetailsCardFrom(TransferRequest transferRequest) {
        if (transferRepository.containsBankCardsDataBase(transferRequest.getCardFromNumber())) {
            BankCard bankCardFrom = transferRepository.getBankCard(transferRequest.getCardFromNumber());
            return bankCardFrom.getNumber().equals(transferRequest.getCardFromNumber()) &&
                    bankCardFrom.getValidTill().equals(transferRequest.getCardFromValidTill()) &&
                    bankCardFrom.getCVV().equals(transferRequest.getCardFromCVV());
        } else return false;
    }

    //проверка на существование банковской карты получателя в базе
    public boolean checkBankDetailsCardTo(TransferRequest transferRequest) {
        return transferRepository.containsBankCardsDataBase(transferRequest.getCardToNumber());
    }

    //проверка баланса банковской карты отправителя
    public boolean checkBalanceCardFrom(TransferOperation transferOperation) {
        return transferRepository.getBankCard(transferOperation.getFromBankCardNumber()).getBalance() >= transferOperation.getAmount().getValue();
    }

    //перевод средств c карты на карту
    public TransferOperation transferMoney(TransferOperation transferOperation) {
        int currentBalanceFromBankCard = transferRepository.getBankCard(transferOperation.getFromBankCardNumber()).getBalance();

        transferRepository
                .getBankCard(transferOperation.getFromBankCardNumber())
                .setBalance(currentBalanceFromBankCard - transferOperation.getAmount().getValue());

        int currentBalanceToBankCard = transferRepository.getBankCard(transferOperation.getToBankCardNumber()).getBalance();
        transferRepository
                .getBankCard(transferOperation.getToBankCardNumber())
                .setBalance(currentBalanceToBankCard + transferOperation.getAmount().getValue() - transferOperation.getCommission());

        transferOperation.setConfirmed(true);
        LOGGER.info("Confirmation: Transfer is completed successfully:\n" + transferOperation);
        return transferOperation;
    }
}
