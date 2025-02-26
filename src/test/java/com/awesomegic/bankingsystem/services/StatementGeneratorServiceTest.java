package com.awesomegic.bankingsystem.services;

import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class StatementGeneratorServiceTest {

  @InjectMocks private StatementGeneratorService statementGeneratorService;
  @Mock private TransactionService transactionService;
  private Scanner scanner;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    scanner = Mockito.mock(Scanner.class);
  }

  @Test
  @DisplayName("Valid input to generate account statement")
  public void testValidInputGenerateStatement() {
    Mockito.when(scanner.nextLine()).thenReturn("AC001 202306");
    List<Transaction> transactions = new ArrayList<>();
    transactions.add(
        new Transaction(
            "Test1",
            LocalDate.of(2023, 6, 1),
            "20230601-01",
            TransactionType.D,
            BigDecimal.valueOf(150.00)));
    transactions.add(
        new Transaction(
            "Test2",
            LocalDate.of(2023, 6, 26),
            "20230626-01",
            TransactionType.W,
            BigDecimal.valueOf(100.00)));
    Mockito.when(transactionService.getTransactions("AC001")).thenReturn(transactions);
    statementGeneratorService.handlePrintStatementInput(scanner);
  }

  @Test
  @DisplayName("Invalid input with incorrect format")
  public void testInvalidInputIncorrectFormat() {
    Mockito.when(scanner.nextLine()).thenReturn("AC001 2023-06");
    statementGeneratorService.handlePrintStatementInput(scanner);
  }

  @Test
  @DisplayName("Handling of empty input")
  public void testInvalidInputEmptyInput() {
    Mockito.when(scanner.nextLine()).thenReturn("");
    statementGeneratorService.handlePrintStatementInput(scanner);
  }

  @Test
  @DisplayName("Valid input with no transactions")
  public void testValidInputNoTransactions() {
    Mockito.when(scanner.nextLine()).thenReturn("AC002 202306");
    Mockito.when(transactionService.getTransactions("AC002")).thenReturn(new ArrayList<>());
    statementGeneratorService.handlePrintStatementInput(scanner);
  }

  @Test
  @DisplayName("Valid input with transactions")
  public void testValidInputWithTransactions() {
    Mockito.when(scanner.nextLine()).thenReturn("AC001 202305");
    List<Transaction> transactions = new ArrayList<>();
    transactions.add(
        new Transaction(
            "Test1",
            LocalDate.of(2023, 5, 15),
            "20230515-01",
            TransactionType.D,
            BigDecimal.valueOf(200.00)));
    Mockito.when(transactionService.getTransactions("AC001")).thenReturn(transactions);
    statementGeneratorService.handlePrintStatementInput(scanner);
  }
}
