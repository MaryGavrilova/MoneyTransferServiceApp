package ru.netology.moneytransferservice.repository;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.netology.moneytransferservice.model.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransferRepository {

    private static final Logger LOGGER = Logger.getLogger(TransferRepository.class);

    // код подтверждения операции перевода
    public static final String VERIFICATION_CODE = "0000";

    // размер коммиссии за перевод
    public static final double COMMISSION_RATE = 0.01;

    // счетчик банковских операций - нужен для присвоения уникального ID операции
    public static long numberOfAllTransferOperations;

    // база данных всех банковских карт
    private final Map<String, BankCard> bankCardsDataBase = new ConcurrentHashMap<>(Map.of(
            "1234567812345678", new BankCard(300, "1234567812345678", "0522", "123", "Mary", "Gavrilova"),
            "5678123456781234", new BankCard(200, "5678123456781234", "0324", "567", "Denis", "Gavrilov")
    ));

    // база данных всех банковских операций по переводу средств (все попытки переводов, как удачные, так и неудачные)
    private final Map<String, TransferOperation> transferOperationsDataBase = new ConcurrentHashMap<>();

    public Map<String, BankCard> getBankCardsDataBase() {
        return bankCardsDataBase;
    }

    public Map<String, TransferOperation> getTransferOperationsDataBase() {
        return transferOperationsDataBase;
    }

    public TransferOperation putInTransferOperationsDataBase(String operationId, TransferOperation transferOperation) {
        transferOperationsDataBase.put(operationId, transferOperation);
        return transferOperation;
    }

    public boolean containsTransferOperationsDataBase(String operationId) {
        return transferOperationsDataBase.containsKey(operationId);
    }

    public TransferOperation getTransferOperation(String operationId) {
        return transferOperationsDataBase.get(operationId);
    }

    public BankCard putInBankCardsDataBase(String bankCardNumber, BankCard bankCard) {
        bankCardsDataBase.put(bankCardNumber, bankCard);
        return bankCard;
    }

    public boolean containsBankCardsDataBase(String bankCardNumber) {
        return bankCardsDataBase.containsKey(bankCardNumber);
    }

    public BankCard getBankCard(String bankCardNumber) {
        return bankCardsDataBase.get(bankCardNumber);
    }
}

