package com.awesomegic.bankingsystem.services;

import static org.junit.jupiter.api.Assertions.*;

import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionManagerImplTest {
  private TransactionManagerImpl transactionManager;

  @BeforeEach
  void setUp() {
    transactionManager = new TransactionManagerImpl();
  }

  @DisplayName("Scenario-1 : Save a transaction and verify it is added to the correct account")
  @Test
  void testSaveTransaction() {
    Transaction transaction =
        new Transaction(
            "20230601-01",
            LocalDate.of(2023, 6, 1),
            "123456",
            TransactionType.D,
            new BigDecimal("150.00"),
            new BigDecimal("250.00"));
    transactionManager.getTransactions().put("123456", new ArrayList<>());
    transactionManager.saveTransaction(transaction);
    List<Transaction> transactions = transactionManager.getTransactionsByAccount("123456");
    assertEquals(1, transactions.size());
    assertEquals(transaction, transactions.get(0));
  }

  @DisplayName("Scenario-2 : Retrieve transactions for an account with no transactions")
  @Test
  void testGetTransactionsByAccount_NoTransactions() {
    List<Transaction> transactions = transactionManager.getTransactionsByAccount("123456");
    assertTrue(transactions.isEmpty());
  }

  @DisplayName("Scenario-3 : Retrieve all transactions when no transactions are saved")
  @Test
  void testGetTransactions_NoTransactions() {
    Map<String, List<Transaction>> transactions = transactionManager.getTransactions();
    assertTrue(transactions.isEmpty());
  }

  @DisplayName(
      "Scenario-4 : Save multiple transactions for different accounts and verify retrieval")
  @Test
  void testSaveAndRetrieveMultipleTransactions() {
    Transaction transaction1 =
        new Transaction(
            "20230601-01",
            LocalDate.of(2023, 6, 1),
            "123456",
            TransactionType.D,
            new BigDecimal("150.00"),
            new BigDecimal("250.00"));
    Transaction transaction2 =
        new Transaction(
            "20230602-01",
            LocalDate.of(2023, 6, 2),
            "654321",
            TransactionType.W,
            new BigDecimal("50.00"),
            new BigDecimal("200.00"));
    transactionManager.getTransactions().put("123456", new ArrayList<>());
    transactionManager.getTransactions().put("654321", new ArrayList<>());
    transactionManager.saveTransaction(transaction1);
    transactionManager.saveTransaction(transaction2);
    List<Transaction> account1Transactions = transactionManager.getTransactionsByAccount("123456");
    List<Transaction> account2Transactions = transactionManager.getTransactionsByAccount("654321");
    assertEquals(1, account1Transactions.size());
    assertEquals(transaction1, account1Transactions.get(0));
    assertEquals(1, account2Transactions.size());
    assertEquals(transaction2, account2Transactions.get(0));
    Map<String, List<Transaction>> allTransactions = transactionManager.getTransactions();
    assertEquals(2, allTransactions.size());
    assertTrue(allTransactions.containsKey("123456"));
    assertTrue(allTransactions.containsKey("654321"));
  }
}
