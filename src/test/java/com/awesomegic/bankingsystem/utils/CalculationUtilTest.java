package com.awesomegic.bankingsystem.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.Transaction;
import java.math.BigDecimal;
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
  @DisplayName("Test getLastBalance with deposits and withdrawals")
  public void testCalculateBalance() {
    List<Transaction> accountTransactions = new ArrayList<>();
    accountTransactions.add(
        new Transaction(
            "1",
            LocalDate.now(),
            "AC001",
            TransactionType.D,
            BigDecimal.valueOf(100.00),
            BigDecimal.valueOf(200.00)));
    accountTransactions.add(
        new Transaction(
            "2",
            LocalDate.now(),
            "AC001",
            TransactionType.W,
            BigDecimal.valueOf(50.00),
            BigDecimal.valueOf(150.00)));
    transactions.put("AC001", accountTransactions);

    BigDecimal balance = CalculationUtil.getLastBalance("AC001", transactions);
    assertEquals(BigDecimal.valueOf(150.00), balance);
  }
}
