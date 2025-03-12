package com.awesomegic.bankingsystem.services;

import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.StatementInput;
import com.awesomegic.bankingsystem.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class StatementGeneratorServiceTest {

  @InjectMocks private StatementGeneratorImpl statementGeneratorService;
  private Scanner scanner;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    scanner = Mockito.mock(Scanner.class);
  }

  @Test
  @DisplayName(
      "Scenario 1: When Account number, year and month are given, statement should be generated from account transactions and rules")
  public void testGenerateStatement() {

    List<Transaction> transactions =
        new ArrayList<>(
            List.of(
                new Transaction(
                    "202306-01",
                    LocalDate.of(2023, 6, 1),
                    "123456",
                    TransactionType.fromString("D"),
                    new BigDecimal("150.00"),
                    new BigDecimal("250.00")),
                new Transaction(
                    "202306-02",
                    LocalDate.of(2023, 6, 7),
                    "123456",
                    TransactionType.fromString("W"),
                    new BigDecimal("20.00"),
                    new BigDecimal("230.00")),
                new Transaction(
                    "202306-03",
                    LocalDate.of(2023, 6, 11),
                    "123456",
                    TransactionType.fromString("D"),
                    new BigDecimal("100.00"),
                    new BigDecimal("330.00")),
                new Transaction(
                    "202306-04",
                    LocalDate.of(2023, 6, 13),
                    "123456",
                    TransactionType.fromString("D"),
                    new BigDecimal("20.00"),
                    new BigDecimal("310.00")),
                new Transaction(
                    "202306-05",
                    LocalDate.of(2023, 6, 17),
                    "123456",
                    TransactionType.fromString("W"),
                    new BigDecimal("100.00"),
                    new BigDecimal("210.00")),
                new Transaction(
                    "202306-06",
                    LocalDate.of(2023, 6, 21),
                    "123456",
                    TransactionType.fromString("D"),
                    new BigDecimal("20.00"),
                    new BigDecimal("230.00")),
                new Transaction(
                    "202306-07",
                    LocalDate.of(2023, 6, 27),
                    "123456",
                    TransactionType.fromString("W"),
                    new BigDecimal("10.00"),
                    new BigDecimal("220.00")),
                new Transaction(
                    "202306-08",
                    LocalDate.of(2023, 6, 30),
                    "123456",
                    TransactionType.fromString("D"),
                    new BigDecimal("20.00"),
                    new BigDecimal("240.00"))));

    List<InterestRule> interestRules =
        new ArrayList<>(
            List.of(
                new InterestRule(LocalDate.of(2023, 1, 1), "RULE01", new BigDecimal("1.95")),
                new InterestRule(LocalDate.of(2023, 5, 20), "RULE02", new BigDecimal("1.90")),
                new InterestRule(LocalDate.of(2023, 6, 3), "RULE03", new BigDecimal("2.30")),
                new InterestRule(LocalDate.of(2023, 6, 8), "RULE04", new BigDecimal("1.75")),
                new InterestRule(LocalDate.of(2023, 6, 14), "RULE05", new BigDecimal("2.30")),
                new InterestRule(LocalDate.of(2023, 6, 20), "RULE06", new BigDecimal("1.30")),
                new InterestRule(LocalDate.of(2023, 6, 23), "RULE07", new BigDecimal("2.32")),
                new InterestRule(LocalDate.of(2023, 6, 28), "RULE08", new BigDecimal("3.10"))));

    var statementInput = new StatementInput("123456", YearMonth.of(2023, 06));

    statementGeneratorService.generateAndPrintStatement(
        statementInput, transactions, interestRules);
  }
}
