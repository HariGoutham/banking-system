package com.awesomegic.bankingsystem.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.awesomegic.bankingsystem.model.Transaction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TransactionUtilsTest {

  @Test
  @DisplayName("Generate transaction ID when there are no existing transactions")
  public void testGenerateTransactionIdNoTransactions() {
    LocalDate date = LocalDate.of(2023, 6, 1);
    Map<String, List<Transaction>> transactions = new HashMap<>();

    String transactionId = TransactionUtils.generateTransactionId(date, transactions);
    assertEquals("20230601-01", transactionId);
  }

  @Test
  @DisplayName("Generate transaction ID when there is one existing transaction")
  public void testGenerateTransactionIdOneTransaction() {
    LocalDate date = LocalDate.of(2023, 6, 1);
    Map<String, List<Transaction>> transactions = new HashMap<>();
    List<Transaction> transactionList = new ArrayList<>();
    transactionList.add(mock(Transaction.class));
    when(transactionList.get(0).id()).thenReturn("20230601-01");
    transactions.put("AC001", transactionList);

    String transactionId = TransactionUtils.generateTransactionId(date, transactions);
    assertEquals("20230601-02", transactionId);
  }

  @Test
  @DisplayName("Generate transaction ID when there are multiple existing transactions")
  public void testGenerateTransactionIdMultipleTransactions() {
    LocalDate date = LocalDate.of(2023, 6, 1);
    Map<String, List<Transaction>> transactions = new HashMap<>();
    List<Transaction> transactionList = new ArrayList<>();
    transactionList.add(mock(Transaction.class));
    when(transactionList.get(0).id()).thenReturn("20230601-01");
    transactionList.add(mock(Transaction.class));
    when(transactionList.get(1).id()).thenReturn("20230601-02");
    transactions.put("AC001", transactionList);

    String transactionId = TransactionUtils.generateTransactionId(date, transactions);
    assertEquals("20230601-03", transactionId);
  }

  @Test
  @DisplayName("Generate transaction ID when there are transactions on a different date")
  public void testGenerateTransactionIdDifferentDateTransactions() {
    LocalDate date = LocalDate.of(2023, 6, 1);
    Map<String, List<Transaction>> transactions = new HashMap<>();
    List<Transaction> transactionList = new ArrayList<>();
    transactionList.add(mock(Transaction.class));
    when(transactionList.get(0).id()).thenReturn("20230531-01"); // Different date
    transactions.put("AC001", transactionList);

    String transactionId = TransactionUtils.generateTransactionId(date, transactions);
    assertEquals("20230601-01", transactionId); // Should still be 01
  }
}
