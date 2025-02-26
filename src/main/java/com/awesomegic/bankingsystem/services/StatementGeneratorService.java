package com.awesomegic.bankingsystem.services;

import static com.awesomegic.bankingsystem.utils.CalculationUtil.*;

import com.awesomegic.bankingsystem.constants.BankMessages;
import com.awesomegic.bankingsystem.model.Transaction;
import com.awesomegic.bankingsystem.utils.DisplayUtils;
import com.awesomegic.bankingsystem.validations.InputValidation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatementGeneratorService {

  @Autowired private TransactionService transactionService;
  @Autowired private InterestService interestService;

  public void handlePrintStatementInput(Scanner scanner) {
    DisplayUtils.printBankStatementInstructionsToUser();
    String input = scanner.nextLine().trim();
    if (input.isEmpty()) return;
    InputValidation.parseStatementInput(input)
        .ifPresentOrElse(
            this::generateAndPrintStatement,
            () -> System.out.println(BankMessages.INVALID_INPUT_FORMAT));
  }

  public void generateAndPrintStatement(String[] parts) {
    var account = parts[0];
    var yearMonth = parts[1];

    try {
      System.out.println(generateAccountStatement(account, yearMonth));
    } catch (Exception e) {
      System.out.println(BankMessages.ERROR_PREFIX + e.getMessage());
    }
  }

  public String generateAccountStatement(String account, String yearMonth) {
    var accountTransactions = transactionService.getTransactions(account);
    return generateMonthlyAccountStatement(account, accountTransactions, yearMonth);
  }

  private String generateMonthlyAccountStatement(
      String account, List<Transaction> transactions, String yearMonth) {

    var statement =
        new StringBuilder()
            .append("Account: ")
            .append(account)
            .append("\n")
            .append(BankMessages.ACCOUNT_STATEMENT_HEADER);

    var year = Integer.parseInt(yearMonth.substring(0, 4));

    var month = Integer.parseInt(yearMonth.substring(4, 6));

    var openingBalance = calculateOpeningBalance(transactions, year, month);

    var monthlyTransactions =
        transactions.stream()
            .filter(
                transaction ->
                    transaction.date().getYear() == year
                        && transaction.date().getMonthValue() == month)
            .sorted(Comparator.comparing(Transaction::date))
            .toList();

    var balance = openingBalance;

    for (var transaction : monthlyTransactions) {
      balance = updateBalance(balance, transaction);
      statement.append(DisplayUtils.formatTransaction(transaction, balance));
    }
    var interest =
        calculateMonthlyInterest(
            year, month, monthlyTransactions, openingBalance, interestService.getInterestRules());
    if (interest.compareTo(BigDecimal.ZERO) > 0) {
      balance = balance.add(interest);
      var interestDate =
          LocalDate.of(year, month, 1).withDayOfMonth(LocalDate.of(year, month, 1).lengthOfMonth());
      statement.append(DisplayUtils.formatInterestTransaction(interestDate, interest, balance));
    }
    return statement.toString();
  }
}
