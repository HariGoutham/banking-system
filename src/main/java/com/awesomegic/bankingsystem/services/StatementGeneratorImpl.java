package com.awesomegic.bankingsystem.services;

import static java.util.Objects.nonNull;

import com.awesomegic.bankingsystem.constants.BankMessages;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.StatementInput;
import com.awesomegic.bankingsystem.model.Transaction;
import com.awesomegic.bankingsystem.services.interfaces.StatementGenerator;
import com.awesomegic.bankingsystem.utils.DisplayUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class StatementGeneratorImpl implements StatementGenerator {
  @Override
  public void generateAndPrintStatement(
      StatementInput statementInput,
      List<Transaction> accountTransactions,
      List<InterestRule> interestRules) {

    YearMonth statementYearMonth = statementInput.yearMonth();

    LocalDate startOfMonth = statementYearMonth.atDay(1);
    LocalDate endOfMonth = statementYearMonth.atEndOfMonth();
    List<LocalDate> changePeriods =
        new ArrayList<>(
            Stream.concat(
                    accountTransactions.stream().map(Transaction::date),
                    interestRules.stream().map(InterestRule::date))
                .filter(date -> !date.isBefore(startOfMonth) && !date.isAfter(endOfMonth))
                .distinct()
                .toList());

    changePeriods.add(startOfMonth);
    changePeriods.add(endOfMonth);

    var sortedPeriods = changePeriods.stream().distinct().sorted().collect(Collectors.toList());
    var daysBetweenMap = calculateDaysBetween(sortedPeriods, endOfMonth);
    var dateInterestMap = calculateApplicableInterest(sortedPeriods, interestRules);
    var dateBalanceMap = calculateBalance(sortedPeriods, accountTransactions);

    BigDecimal interest = BigDecimal.ZERO;

    for (LocalDate ld : sortedPeriods) {
      if (nonNull(daysBetweenMap.get(ld))
          && nonNull(dateInterestMap.get(ld))
          && nonNull(dateBalanceMap.get(ld))) {
        interest =
            interest.add(
                BigDecimal.valueOf(daysBetweenMap.get(ld))
                    .multiply(dateInterestMap.get(ld))
                    .multiply(dateBalanceMap.get(ld))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
      }
    }

    BigDecimal interestAtEndOfMonth =
        interest
            .divide(BigDecimal.valueOf(365), 2, RoundingMode.HALF_UP)
            .setScale(2, RoundingMode.HALF_UP);

    var statement =
        new StringBuilder()
            .append("Account: ")
            .append(statementInput.account())
            .append("\n")
            .append(BankMessages.ACCOUNT_STATEMENT_HEADER);

    for (var transaction : accountTransactions) {
      statement.append(DisplayUtils.formatTransaction(transaction));
    }

    statement.append(
        DisplayUtils.formatInterestTransaction(
            endOfMonth,
            interestAtEndOfMonth,
            accountTransactions
                .get(accountTransactions.size() - 1)
                .balance()
                .add(interestAtEndOfMonth)));

    System.out.println(statement);
  }

  private static Map<LocalDate, Integer> calculateDaysBetween(
      List<LocalDate> dates, LocalDate endOfMonth) {
    Map<LocalDate, Integer> daysBetweenMap = new HashMap<>();
    LocalDate previousDate = null;
    for (LocalDate currentDate : dates) {
      if (previousDate != null) {
        int daysBetween =
            (int) java.time.temporal.ChronoUnit.DAYS.between(previousDate, currentDate);
        daysBetweenMap.put(previousDate, daysBetween);
      }
      previousDate = currentDate;
    }
    if (dates.get(dates.size() - 1).equals(endOfMonth)) {
      daysBetweenMap.put(endOfMonth, 1);
    }
    return daysBetweenMap;
  }

  private static Map<LocalDate, BigDecimal> calculateApplicableInterest(
      List<LocalDate> dates, List<InterestRule> interestRules) {
    Map<LocalDate, BigDecimal> applicableInterestMap = new HashMap<>();
    for (LocalDate date : dates) {
      BigDecimal applicableRate = null;
      for (InterestRule rule : interestRules) {
        if (!rule.date().isAfter(date)) {
          applicableRate = rule.rate();
        }
      }
      applicableInterestMap.put(date, applicableRate);
    }
    return applicableInterestMap;
  }

  private static Map<LocalDate, BigDecimal> calculateBalance(
      List<LocalDate> dates, List<Transaction> transactions) {
    Map<LocalDate, BigDecimal> applicableInterestMap = new HashMap<>();
    for (LocalDate date : dates) {
      BigDecimal balance = null;
      for (Transaction transaction : transactions) {
        if (!transaction.date().isAfter(date)) {
          balance = transaction.balance();
        }
      }
      applicableInterestMap.put(date, balance);
    }
    return applicableInterestMap;
  }
}
