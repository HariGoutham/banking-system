package com.awesomegic.bankingsystem.utils;

import com.awesomegic.bankingsystem.model.Transaction;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class TransactionUtils {
  public static String generateTransactionId(
      LocalDate date, Map<String, List<Transaction>> transactions) {
    String datePrefix = date.format(DateTimeFormatter.BASIC_ISO_DATE);
    long count =
        transactions.values().stream()
            .flatMap(List::stream)
            .filter(t -> t.id() != null && t.id().startsWith(datePrefix))
            .count();
    return datePrefix + "-" + String.format("%02d", count + 1);
  }
}
