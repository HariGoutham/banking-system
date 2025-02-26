package com.awesomegic.bankingsystem.utils;

import static com.awesomegic.bankingsystem.constants.BankMessages.*;

import com.awesomegic.bankingsystem.constants.BankMessages;
import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DisplayUtils {
  public static void displayMenu(boolean isFirstTime) {
    for (String option : BankMessages.MENU_OPTIONS) {
      if (option.equals(PROMPT)) {
        System.out.print(option);
        break;
      }
      System.out.println(!isFirstTime && option.equals(WELCOME_MESSAGE) ? ANYTHING_ELSE : option);
    }
  }

  public static void printTransactionInstructionsToUser() {
    System.out.println(BankMessages.INPUT_TRANSACTION_PROMPT);
    System.out.print(BankMessages.PROMPT);
  }

  public static void printInvalidTransactionMessageToUser() {
    System.out.println(BankMessages.INPUT_TRANSACTION_PROMPT);
    System.out.print(BankMessages.PROMPT);
  }

  public static void printAccountTransactions(
      String account, List<Transaction> accountTransactions) {
    System.out.println("Account: " + account);
    System.out.println("| Date     | Txn Id      | Type | Amount |");
    for (Transaction txn : accountTransactions) {
      System.out.printf(
          "| %s | %s | %-4s | %6.2f |\n",
          txn.date().format(DateTimeFormatter.BASIC_ISO_DATE), txn.id(), txn.type(), txn.amount());
    }
  }

  public static void quitBankingSystem() {
    System.out.println(BankMessages.THANK_YOU_MESSAGE);
    System.out.println(BankMessages.HAVE_A_NICE_DAY);
    System.exit(0);
  }

  public static void printInterestAndRulesInstructionsToUser() {
    System.out.println(BankMessages.INTEREST_RULES_PROMPT);
    System.out.print(BankMessages.PROMPT);
  }

  public static void printBankStatementInstructionsToUser() {
    System.out.println(BankMessages.PRINT_STATEMENT_PROMPT);
    System.out.print(BankMessages.PROMPT);
  }

  public static String formatTransaction(Transaction transaction, BigDecimal balance) {
    String type = transaction.type() == TransactionType.D ? "D " : "W ";
    return String.format(
        "| %s | %s | %-4s | %7.2f | %7.2f |\n",
        transaction.date().format(DateTimeFormatter.BASIC_ISO_DATE),
        transaction.id(),
        type,
        transaction.amount(),
        balance);
  }

  public static String formatInterestTransaction(
      LocalDate date, BigDecimal interest, BigDecimal balance) {
    return String.format(
        "| %s |             | I    | %7.2f | %7.2f |\n",
        date.format(DateTimeFormatter.BASIC_ISO_DATE), interest, balance);
  }

  public static void printInterestRules(List<InterestRule> interestRules) {
    System.out.println("Interest rules:");
    System.out.println("| Date     | RuleId | Rate (%) |");
    for (InterestRule rule : interestRules) {
      System.out.printf(
          "| %s | %s | %8.2f |\n",
          rule.date().format(DateTimeFormatter.BASIC_ISO_DATE), rule.ruleId(), rule.rate());
    }
  }
}
