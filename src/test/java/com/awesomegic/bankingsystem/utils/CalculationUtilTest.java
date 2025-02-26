package com.awesomegic.bankingsystem.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculationUtilTest {

  private Map<String, List<Transaction>> transactions;
  private List<InterestRule> interestRules;

  @BeforeEach
  public void setUp() {
    transactions = new HashMap<>();
    interestRules = new ArrayList<>();
  }

  @Test
  @DisplayName("Test calculateBalance with deposits and withdrawals")
  public void testCalculateBalance() {
    List<Transaction> accountTransactions = new ArrayList<>();
    accountTransactions.add(
        new Transaction(
            "1", LocalDate.now(), "AC001", TransactionType.D, BigDecimal.valueOf(100.00)));
    accountTransactions.add(
        new Transaction(
            "2", LocalDate.now(), "AC001", TransactionType.W, BigDecimal.valueOf(50.00)));
    transactions.put("AC001", accountTransactions);

    BigDecimal balance = CalculationUtil.calculateBalance("AC001", transactions);
    assertEquals(BigDecimal.valueOf(50.00), balance);
  }

  @Test
  @DisplayName("Test calculateBalance with no transactions")
  public void testCalculateBalanceNoTransactions() {
    BigDecimal balance = CalculationUtil.calculateBalance("AC002", transactions);
    assertEquals(BigDecimal.ZERO, balance);
  }

  @Test
  @DisplayName("Test calculateDailyInterest with valid inputs")
  public void testCalculateDailyInterest() {
    interestRules.add(
        new InterestRule(LocalDate.of(2023, 1, 1), "RULE01", BigDecimal.valueOf(5.0)));
    BigDecimal dailyInterest =
        CalculationUtil.calculateDailyInterest(
            LocalDate.of(2023, 1, 1), BigDecimal.valueOf(1000.00), interestRules);
    assertEquals(
        BigDecimal.valueOf(0.13699).setScale(2, RoundingMode.HALF_UP),
        dailyInterest.setScale(2, RoundingMode.HALF_UP));
  }

  @Test
  @DisplayName("Test calculateDailyInterest with no applicable interest rule")
  public void testCalculateDailyInterestNoRule() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          CalculationUtil.calculateDailyInterest(
              LocalDate.of(2023, 1, 1), BigDecimal.valueOf(1000.00), interestRules);
        });
  }

  @Test
  @DisplayName("Test calculateOpeningBalance with transactions before the specified date")
  public void testCalculateOpeningBalance() {
    List<Transaction> accountTransactions = new ArrayList<>();
    accountTransactions.add(
        new Transaction(
            "1",
            LocalDate.of(2023, 5, 15),
            "AC001",
            TransactionType.D,
            BigDecimal.valueOf(200.00)));
    accountTransactions.add(
        new Transaction(
            "2", LocalDate.of(2023, 6, 1), "AC001", TransactionType.W, BigDecimal.valueOf(50.00)));
    BigDecimal openingBalance =
        CalculationUtil.calculateOpeningBalance(accountTransactions, 2023, 6);
    assertEquals(BigDecimal.valueOf(200.00), openingBalance);
  }

  @Test
  @DisplayName("Test calculateOpeningBalance with no transactions before the specified date")
  public void testCalculateOpeningBalanceNoTransactions() {
    List<Transaction> accountTransactions = new ArrayList<>();
    BigDecimal openingBalance =
        CalculationUtil.calculateOpeningBalance(accountTransactions, 2023, 6);
    assertEquals(BigDecimal.ZERO, openingBalance);
  }

  @Test
  @DisplayName("Test calculateMonthlyInterest with transactions")
  public void testCalculateMonthlyInterest() {
    List<Transaction> monthlyTransactions = new ArrayList<>();
    monthlyTransactions.add(
        new Transaction(
            "1", LocalDate.of(2023, 6, 1), "AC001", TransactionType.D, BigDecimal.valueOf(100.00)));
    monthlyTransactions.add(
        new Transaction(
            "2", LocalDate.of(2023, 6, 15), "AC001", TransactionType.W, BigDecimal.valueOf(50.00)));
    interestRules.add(
        new InterestRule(LocalDate.of(2023, 1, 1), "RULE01", BigDecimal.valueOf(5.0)));
    BigDecimal openingBalance = BigDecimal.valueOf(1000.00);
    BigDecimal totalInterest =
        CalculationUtil.calculateMonthlyInterest(
            2023, 6, monthlyTransactions, openingBalance, interestRules);
    assertEquals(BigDecimal.valueOf(4.41).setScale(2, RoundingMode.HALF_UP), totalInterest);
  }

  @Test
  @DisplayName("Test calculateMonthlyInterest with no transactions")
  public void testCalculateMonthlyInterestNoTransactions() {
    List<Transaction> monthlyTransactions = new ArrayList<>();
    interestRules.add(
        new InterestRule(LocalDate.of(2023, 1, 1), "RULE01", BigDecimal.valueOf(5.0)));
    BigDecimal openingBalance = BigDecimal.valueOf(1000.00);
    BigDecimal totalInterest =
        CalculationUtil.calculateMonthlyInterest(
            2023, 6, monthlyTransactions, openingBalance, interestRules);
    assertEquals(BigDecimal.valueOf(4.11).setScale(2, RoundingMode.HALF_UP), totalInterest);
  }

  @Test
  @DisplayName("Test updateBalance with deposit")
  public void testUpdateBalanceDeposit() {
    Transaction transaction =
        new Transaction(
            "1", LocalDate.now(), "AC001", TransactionType.D, BigDecimal.valueOf(100.00));
    BigDecimal newBalance = CalculationUtil.updateBalance(BigDecimal.ZERO, transaction);
    assertEquals(BigDecimal.valueOf(100.00), newBalance);
  }

  @Test
  @DisplayName("Test updateBalance with withdrawal")
  public void testUpdateBalanceWithdrawal() {
    Transaction transaction =
        new Transaction(
            "1", LocalDate.now(), "AC001", TransactionType.W, BigDecimal.valueOf(50.00));
    BigDecimal newBalance = CalculationUtil.updateBalance(BigDecimal.valueOf(100.00), transaction);
    assertEquals(BigDecimal.valueOf(50.00), newBalance);
  }
}
