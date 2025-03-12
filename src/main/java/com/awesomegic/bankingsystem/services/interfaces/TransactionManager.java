package com.awesomegic.bankingsystem.services.interfaces;

import com.awesomegic.bankingsystem.model.Transaction;
import java.util.List;
import java.util.Map;

public interface TransactionManager {
  void saveTransaction(Transaction transaction);

  List<Transaction> getTransactionsByAccount(String account);

  Map<String, List<Transaction>> getTransactions();
}
