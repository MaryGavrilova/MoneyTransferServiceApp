package ru.netology.moneytransferservice.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.netology.moneytransferservice.exception.MoneyTransferConfirmationException;
import ru.netology.moneytransferservice.exception.MoneyTransferInitiationException;
import ru.netology.moneytransferservice.model.OperationConfirmation;
import ru.netology.moneytransferservice.model.OperationIdentifier;
import ru.netology.moneytransferservice.model.TransferRequest;
import ru.netology.moneytransferservice.service.TransferService;

import javax.validation.Valid;
import javax.validation.ValidationException;

@Validated
@RestController
public class TransferController {

    private static final Logger LOGGER = Logger.getLogger(TransferController.class);

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public OperationIdentifier initiateMoneyTransfer(@RequestBody @Valid TransferRequest transferRequest) {
        return transferService.initiateMoneyTransfer(transferRequest);
    }

    @PostMapping("/confirmOperation")
    public OperationIdentifier confirmMoneyTransfer(@RequestBody @Valid OperationConfirmation operationConfirmation) {
        return transferService.confirmMoneyTransfer(operationConfirmation);
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<String> handleValidationException(ValidationException e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MoneyTransferInitiationException.class)
    ResponseEntity<String> handleMoneyTransferInitiationException(MoneyTransferInitiationException e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(MoneyTransferConfirmationException.class)
    ResponseEntity<String> handleMoneyTransferConfirmationException(MoneyTransferConfirmationException e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
