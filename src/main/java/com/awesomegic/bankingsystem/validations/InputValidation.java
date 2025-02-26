package com.awesomegic.bankingsystem.validations;

import static com.awesomegic.bankingsystem.utils.CalculationUtil.calculateBalance;

import com.awesomegic.bankingsystem.constants.BankMessages;
import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InputValidation {

  public static Optional<Transaction> validateTransactionInputFromUser(String input) {
    String[] parts = input.split(" ");
    if (parts.length != 4) return Optional.empty();
    try {
      LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.BASIC_ISO_DATE);
      String account = parts[1];
      TransactionType transactionType = TransactionType.fromString(parts[2].toUpperCase());
      BigDecimal amount = new BigDecimal(parts[3]);
      if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Amount must be greater than zero.");
      }
      return Optional.of(new Transaction(null, date, account, transactionType, amount));
    } catch (Exception e) {
      System.out.println(BankMessages.ERROR_PREFIX + e.getMessage());
      return Optional.empty();
    }
  }

  public static void checkIfValidTransaction(
      Transaction transaction,
      List<Transaction> accountTransactions,
      Map<String, List<Transaction>> transactions) {
    if (accountTransactions.isEmpty() && transaction.type() == TransactionType.W) {
      throw new IllegalArgumentException("First transaction cannot be a withdrawal.");
    }
    if (transaction.amount().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Amount must be greater than zero.");
    }
    BigDecimal balance = calculateBalance(transaction.account(), transactions);
    if (transaction.type() == TransactionType.W
        && balance.subtract(transaction.amount()).compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Insufficient balance.");
    }
  }

  public static Optional<InterestRule> validateInterestRuleInput(String input) {
    String[] parts = input.split(" ");
    if (parts.length != 3) return Optional.empty();
    try {
      LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.BASIC_ISO_DATE);
      String ruleId = parts[1];
      BigDecimal rate = new BigDecimal(parts[2]);
      if (rate.compareTo(BigDecimal.ZERO) <= 0 || rate.compareTo(new BigDecimal(100)) >= 0) {
        throw new IllegalArgumentException(
            "Interest rate must be greater than 0 and less than 100.");
      }
      return Optional.of(new InterestRule(date, ruleId, rate));
    } catch (Exception e) {
      System.out.println(BankMessages.ERROR_PREFIX + e.getMessage());
      return Optional.empty();
    }
  }

  public static Optional<String[]> parseStatementInput(String input) {
    var parts = input.split(" ");
    if (parts.length != 2) return Optional.empty();
    if (parts[1].length() != 6 || !parts[1].matches("\\d{6}")) {
      return Optional.empty();
    }
    return Optional.of(parts);
  }
}
