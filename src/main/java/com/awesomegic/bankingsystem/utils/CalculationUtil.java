package com.awesomegic.bankingsystem.utils;

import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CalculationUtil {

  public static BigDecimal calculateBalance(
      String account, Map<String, List<Transaction>> transactions) {
    return transactions.getOrDefault(account, Collections.emptyList()).stream()
        .map(t -> t.type() == TransactionType.D ? t.amount() : t.amount().negate())
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public static BigDecimal calculateDailyInterest(
      LocalDate date, BigDecimal balance, List<InterestRule> interestRules) {
    BigDecimal rate = getApplicableInterestRate(date, interestRules);
    return balance
        .multiply(rate)
        .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
        .divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP);
  }

  private static BigDecimal getApplicableInterestRate(
      LocalDate date, List<InterestRule> interestRules) {
    InterestRule rule =
        interestRules.stream()
            .filter(r -> !r.date().isAfter(date))
            .reduce((first, second) -> second)
            .orElseThrow(
                () -> new IllegalArgumentException("No interest rule found for date: " + date));

    return rule.rate();
  }

  public static BigDecimal calculateOpeningBalance(
      List<Transaction> transactions, int year, int month) {
    return transactions.stream()
        .filter(transaction -> transaction.date().isBefore(LocalDate.of(year, month, 1)))
        .map(
            transaction ->
                transaction.type() == TransactionType.D
                    ? transaction.amount()
                    : transaction.amount().negate())
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public static BigDecimal calculateMonthlyInterest(
      int year,
      int month,
      List<Transaction> monthlyTransactions,
      BigDecimal openingBalance,
      List<InterestRule> interestRules) {

    var startDate = LocalDate.of(year, month, 1);

    var endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

    BigDecimal[] balanceHolder = new BigDecimal[] {openingBalance};

    BigDecimal totalInterest = BigDecimal.ZERO;

    var sortedTransactions =
        monthlyTransactions.stream().sorted(Comparator.comparing(Transaction::date)).toList();

    LocalDate currentDate = startDate;

    while (!currentDate.isAfter(endDate)) {
      final LocalDate finalCurrentDate = currentDate;
      List<Transaction> transactionsOnDate =
          sortedTransactions.stream().filter(t -> t.date().isEqual(finalCurrentDate)).toList();

      for (var transaction : transactionsOnDate) {
        balanceHolder[0] = updateBalance(balanceHolder[0], transaction);
      }
      BigDecimal dailyInterest =
          CalculationUtil.calculateDailyInterest(finalCurrentDate, balanceHolder[0], interestRules);
      totalInterest = totalInterest.add(dailyInterest);
      currentDate = currentDate.plusDays(1);
    }

    return totalInterest.setScale(2, RoundingMode.HALF_UP);
  }

  public static BigDecimal updateBalance(BigDecimal balance, Transaction transaction) {
    return transaction.type() == TransactionType.D
        ? balance.add(transaction.amount())
        : balance.subtract(transaction.amount());
  }
}
