package com.awesomegic.bankingsystem.services;

import com.awesomegic.bankingsystem.model.Transaction;
import com.awesomegic.bankingsystem.services.interfaces.TransactionManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TransactionManagerImpl implements TransactionManager {

  private final Map<String, List<Transaction>> transactions = new HashMap<>();

  @Override
  public void saveTransaction(Transaction transaction) {
    transactions.get(transaction.account()).add(transaction);
  }

  @Override
  public List<Transaction> getTransactionsByAccount(String account) {
    return transactions.getOrDefault(account, Collections.emptyList());
  }

  @Override
  public Map<String, List<Transaction>> getTransactions() {
    return transactions;
  }
}
