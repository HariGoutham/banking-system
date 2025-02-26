package com.awesomegic.bankingsystem.controllers;

import com.awesomegic.bankingsystem.constants.BankMessages;
import com.awesomegic.bankingsystem.services.InterestService;
import com.awesomegic.bankingsystem.services.StatementGeneratorService;
import com.awesomegic.bankingsystem.services.TransactionService;
import com.awesomegic.bankingsystem.utils.DisplayUtils;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BankingSystemLauncher implements CommandLineRunner {
  @Autowired private TransactionService transactionService;
  @Autowired private InterestService interestService;
  @Autowired private StatementGeneratorService statementGeneratorService;

  @Override
  public void run(String... args) {
    try (Scanner scanner = new Scanner(System.in)) {
      int i = 0;
      while (true) {
        DisplayUtils.displayMenu(i == 0);
        String choice = scanner.nextLine().trim().toUpperCase();
        i++;
        switch (choice) {
          case "T" -> transactionService.handleTransactionInput(scanner);
          case "I" -> interestService.handleInterestInput(scanner);
          case "P" -> statementGeneratorService.handlePrintStatementInput(scanner);
          case "Q" -> DisplayUtils.quitBankingSystem();
          default -> System.out.println(BankMessages.INVALID_CHOICE);
        }
      }
    }
  }
}
