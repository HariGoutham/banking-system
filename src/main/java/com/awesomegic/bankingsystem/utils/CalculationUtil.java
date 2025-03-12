package com.awesomegic.bankingsystem.utils;

import com.awesomegic.bankingsystem.model.Transaction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalculationUtil {

  public static BigDecimal getLastBalance(
      String account, Map<String, List<Transaction>> transactions) {
    List<Transaction> accountTransactions = transactions.getOrDefault(account, new ArrayList<>());
    if (accountTransactions.isEmpty()) {
      return BigDecimal.ZERO;
    }
    return accountTransactions.get(accountTransactions.size() - 1).balance();
  }
}
