package com.awesomegic.bankingsystem.services;

import static org.junit.jupiter.api.Assertions.*;

import com.awesomegic.bankingsystem.model.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TransactionServiceTest {

  @InjectMocks private TransactionService transactionService;

  @Mock private Scanner scanner;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Valid input to add a transaction")
  public void testValidInputAddTransaction() {
    Mockito.when(scanner.nextLine()).thenReturn("20230615 AC001 D 150.00");
    transactionService.handleTransactionInput(scanner);
    List<Transaction> transactions = transactionService.getTransactions("AC001");
    assertEquals(1, transactions.size());
    assertEquals("D", transactions.get(0).type().toString());
    assertEquals(
        BigDecimal.valueOf(150.00).setScale(2, RoundingMode.HALF_UP), transactions.get(0).amount());
  }

  @Test
  @DisplayName("Invalid input with incorrect format")
  public void testInvalidInputIncorrectFormat() {
    Mockito.when(scanner.nextLine()).thenReturn("2023-06-15 AC001 D 150.00");
    transactionService.handleTransactionInput(scanner);
  }

  @Test
  @DisplayName("Handling of empty input")
  public void testInvalidInputEmptyInput() {
    Mockito.when(scanner.nextLine()).thenReturn("");
    transactionService.handleTransactionInput(scanner);
    List<Transaction> transactions = transactionService.getTransactions("AC001");
    assertTrue(transactions.isEmpty());
  }

  @Test
  @DisplayName("Valid input to add multiple transactions")
  public void testValidInputAddMultipleTransactions() {
    Mockito.when(scanner.nextLine()).thenReturn("20230615 AC001 D 150.00");
    transactionService.handleTransactionInput(scanner);
    Mockito.when(scanner.nextLine()).thenReturn("20230616 AC001 W 50.00");
    transactionService.handleTransactionInput(scanner);
    List<Transaction> transactions = transactionService.getTransactions("AC001");
    assertEquals(2, transactions.size());
  }

  @Test
  @DisplayName("Invalid input with duplicate transaction")
  public void testInvalidInputDuplicateTransaction() {
    Mockito.when(scanner.nextLine()).thenReturn("20230615 AC001 D 150.00");
    transactionService.handleTransactionInput(scanner);
    Mockito.when(scanner.nextLine()).thenReturn("20230615 AC001 D 150.00");
    transactionService.handleTransactionInput(scanner);
  }

  @Test
  @DisplayName("Valid input to check transactions for non-existent account")
  public void testValidInputCheckTransactionsNonExistentAccount() {
    Mockito.when(scanner.nextLine()).thenReturn("AC002");
    transactionService.handleTransactionInput(scanner);
    List<Transaction> transactions = transactionService.getTransactions("AC002");
    assertTrue(transactions.isEmpty());
  }
}
