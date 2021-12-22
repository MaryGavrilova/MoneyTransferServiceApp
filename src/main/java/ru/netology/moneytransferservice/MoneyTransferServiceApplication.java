package ru.netology.moneytransferservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MoneyTransferServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyTransferServiceApplication.class, args);
    }

}
