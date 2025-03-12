package com.awesomegic.bankingsystem.services;

import static com.awesomegic.bankingsystem.utils.CalculationUtil.getLastBalance;

import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.exceptions.*;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.StatementInput;
import com.awesomegic.bankingsystem.model.Transaction;
import com.awesomegic.bankingsystem.services.interfaces.Validator;
import com.awesomegic.bankingsystem.utils.TransactionUtils;
import com.awesomegic.bankingsystem.validations.InputValidation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ValidatorImpl implements Validator {
  @Override
  public Transaction getValidatedTransaction(
      String input, Map<String, List<Transaction>> transactions)
      throws InvalidDateFormatException,
          InvalidTransactionTypeException,
          InvalidAmountException,
          BusinessRuleException {
    String[] parts = input.split(" ");

    // Number of parameters validation
    InputValidation.validateBusinessRule(
        parts.length != 4, "All details are mandatory, Please check the format");

    // Format validations for each parameter
    LocalDate date = InputValidation.validateDate(parts[0]);
    String account = parts[1];
    TransactionType transactionType = InputValidation.validateTransactionType(parts[2]);
    BigDecimal amount = InputValidation.validateAmount(parts[3]);

    // Business Validations
    List<Transaction> accountTransactions =
        transactions.computeIfAbsent(account, k -> new ArrayList<>());
    InputValidation.validateBusinessRule(
        accountTransactions.isEmpty() && transactionType == TransactionType.W,
        "First transaction cannot be a withdrawal.");
    BigDecimal currentBalance = getLastBalance(account, transactions);
    InputValidation.validateBusinessRule(
        transactionType == TransactionType.W
            && currentBalance.subtract(amount).compareTo(BigDecimal.ZERO) < 0,
        "Insufficient balance.");

    // Update balance after transaction
    BigDecimal newBalance =
        (transactionType == TransactionType.D)
            ? currentBalance.add(amount)
            : currentBalance.subtract(amount);

    // Generate the ID for transaction
    String transactionId = TransactionUtils.generateTransactionId(date, transactions);

    return new Transaction(transactionId, date, account, transactionType, amount, newBalance);
  }

  @Override
  public InterestRule validateInterestRuleInput(String input)
      throws BusinessRuleException, InvalidDateFormatException, InvalidRateException {
    String[] parts = input.split(" ");
    InputValidation.validateBusinessRule(
        parts.length != 3, "All details are mandatory, Please check the format");
    LocalDate date = InputValidation.validateDate(parts[0]);
    String ruleId = parts[1];
    BigDecimal rate = InputValidation.validateRate(parts[2]);
    InputValidation.validateBusinessRule(
        rate.compareTo(BigDecimal.ZERO) <= 0 || rate.compareTo(new BigDecimal(100)) >= 0,
        "Interest rate must be greater than 0 and less than 100.");
    return new InterestRule(date, ruleId, rate);
  }

  @Override
  public StatementInput validatePrintStatementInput(String input)
      throws BusinessRuleException, InvalidDateFormatException {
    String[] parts = input.split(" ");
    InputValidation.validateBusinessRule(
        parts.length != 2, "All details are mandatory, Please check the format");
    String account = parts[0];
    YearMonth yearMonth = InputValidation.validateYearMonth(parts[1]);
    return new StatementInput(account, yearMonth);
  }
}
