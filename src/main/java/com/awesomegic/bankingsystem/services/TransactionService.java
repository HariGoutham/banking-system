package com.awesomegic.bankingsystem.services;

import com.awesomegic.bankingsystem.constants.BankMessages;
import com.awesomegic.bankingsystem.model.Transaction;
import com.awesomegic.bankingsystem.utils.DisplayUtils;
import com.awesomegic.bankingsystem.utils.TransactionUtils;
import com.awesomegic.bankingsystem.validations.InputValidation;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  private final Map<String, List<Transaction>> transactions = new HashMap<>();

  public void handleTransactionInput(Scanner scanner) {
    DisplayUtils.printTransactionInstructionsToUser();
    String input = scanner.nextLine().trim();

    if (input.isEmpty()) {
      return;
    }

    Optional<Transaction> transaction = InputValidation.validateTransactionInputFromUser(input);
    transaction.ifPresentOrElse(
        this::processTransaction, DisplayUtils::printInvalidTransactionMessageToUser);
  }

  private void processTransaction(Transaction transaction) {
    try {
      addTransaction(transaction);
      DisplayUtils.printAccountTransactions(
          transaction.account(), getTransactions(transaction.account()));
    } catch (IllegalArgumentException e) {
      System.out.println(BankMessages.ERROR_PREFIX + e.getMessage());
    }
  }

  public void addTransaction(Transaction transaction) {
    List<Transaction> accountTransactions =
        transactions.computeIfAbsent(transaction.account(), k -> new ArrayList<>());

    InputValidation.checkIfValidTransaction(transaction, accountTransactions, transactions);

    String transactionId = TransactionUtils.generateTransactionId(transaction.date(), transactions);
    Transaction newTransaction =
        new Transaction(
            transactionId,
            transaction.date(),
            transaction.account(),
            transaction.type(),
            transaction.amount());

    accountTransactions.add(newTransaction);
  }

  public List<Transaction> getTransactions(String account) {
    return transactions.getOrDefault(account, Collections.emptyList());
  }
}
