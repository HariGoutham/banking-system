package com.awesomegic.bankingsystem.main;

import com.awesomegic.bankingsystem.constants.BankMessages;
import com.awesomegic.bankingsystem.exceptions.*;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.StatementInput;
import com.awesomegic.bankingsystem.model.Transaction;
import com.awesomegic.bankingsystem.services.interfaces.*;
import com.awesomegic.bankingsystem.utils.DisplayUtils;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MainMenu implements CommandLineRunner {
  private final TransactionManager transactionManager;
  private final InterestRuleManager interestRuleManager;
  private final StatementGenerator statementGenerator;
  private final Validator inputValidator;

  @Autowired
  public MainMenu(
      TransactionManager transactionManagerImpl,
      InterestRuleManager interestRuleManagerImpl,
      StatementGenerator statementGeneratorImpl,
      Validator inputValidatorImpl) {
    this.transactionManager = transactionManagerImpl;
    this.interestRuleManager = interestRuleManagerImpl;
    this.statementGenerator = statementGeneratorImpl;
    this.inputValidator = inputValidatorImpl;
  }

  @Override
  public void run(String... args) {
    Scanner scanner = new Scanner(System.in);
    boolean isFirstRun = true;
    while (true) {
      try {
        DisplayUtils.displayMenu(isFirstRun);
        String choice = scanner.nextLine().trim().toUpperCase();
        isFirstRun = false;
        switch (choice) {
          case "T" -> handleTransaction(scanner);
          case "I" -> handleInterestInput(scanner);
          case "P" -> handlePrintStatementInput(scanner);
          case "Q" -> DisplayUtils.quitBankingSystem();
          default -> System.out.println(BankMessages.INVALID_CHOICE);
        }
      } catch (Exception e) {
        System.out.println("An error occurred: " + e.getMessage());
      }
    }
  }

  public void handleTransaction(Scanner scanner)
      throws InvalidDateFormatException,
          InvalidAmountException,
          InvalidTransactionTypeException,
          BusinessRuleException {
    DisplayUtils.printTransactionInstructionsToUser();
    String input = scanner.nextLine().trim();
    if (input.isEmpty()) return;
    Map<String, List<Transaction>> transactions = transactionManager.getTransactions();
    Transaction transaction = inputValidator.getValidatedTransaction(input, transactions);
    transactionManager.saveTransaction(transaction);
    DisplayUtils.printAccountTransactions(
        transaction.account(), transactions.get(transaction.account()));
  }

  public void handleInterestInput(Scanner scanner)
      throws BusinessRuleException, InvalidDateFormatException, InvalidRateException {
    DisplayUtils.printInterestAndRulesInstructionsToUser();
    String input = scanner.nextLine().trim();
    if (input.isEmpty()) return;
    InterestRule interestRule = inputValidator.validateInterestRuleInput(input);
    interestRuleManager.saveInterestRule(interestRule);
    DisplayUtils.printInterestRules(interestRuleManager.getInterestRules());
  }

  public void handlePrintStatementInput(Scanner scanner)
      throws BusinessRuleException, InvalidDateFormatException {
    DisplayUtils.printBankStatementInstructionsToUser();
    String input = scanner.nextLine().trim();
    if (input.isEmpty()) return;
    StatementInput statementInput = inputValidator.validatePrintStatementInput(input);
    List<Transaction> accountTransactions =
        transactionManager.getTransactionsByAccount(statementInput.account());
    List<InterestRule> interestRules = interestRuleManager.getInterestRules();
    statementGenerator.generateAndPrintStatement(
        statementInput, accountTransactions, interestRules);
  }
}
