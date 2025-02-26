package com.awesomegic.bankingsystem.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.awesomegic.bankingsystem.constants.BankMessages;
import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.Transaction;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DisplayUtilsTest {

  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
  private PrintStream originalOut;

  @BeforeEach
  public void setUp() {
    originalOut = System.out;
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @Test
  @DisplayName("Test printTransactionInstructionsToUser ")
  public void testPrintTransactionInstructionsToUser() {
    DisplayUtils.printTransactionInstructionsToUser();
    assertEquals(
        (BankMessages.INPUT_TRANSACTION_PROMPT + "\n" + BankMessages.PROMPT)
            .trim()
            .replace("\r\n", "\n")
            .replace("\r", "\n"),
        outputStreamCaptor.toString().trim().replace("\r\n", "\n").replace("\r", "\n"));
  }

  @Test
  @DisplayName("Test printInvalidTransactionMessageToUser ")
  public void testPrintInvalidTransactionMessageToUser() {
    DisplayUtils.printInvalidTransactionMessageToUser();
    assertEquals(
        (BankMessages.INPUT_TRANSACTION_PROMPT + "\n" + BankMessages.PROMPT)
            .trim()
            .replace("\r\n", "\n")
            .replace("\r", "\n"),
        outputStreamCaptor.toString().trim().replace("\r\n", "\n").replace("\r", "\n"));
  }

  @Test
  @DisplayName("Test printAccountTransactions")
  public void testPrintAccountTransactions() {
    List<Transaction> transactions = new ArrayList<>();
    transactions.add(
        new Transaction(
            "1", LocalDate.of(2023, 6, 1), "AC001", TransactionType.D, BigDecimal.valueOf(150.00)));
    transactions.add(
        new Transaction(
            "2", LocalDate.of(2023, 6, 2), "AC001", TransactionType.W, BigDecimal.valueOf(50.00)));

    DisplayUtils.printAccountTransactions("AC001", transactions);
    String expectedOutput =
        "Account: AC001\n"
            + "| Date     | Txn Id      | Type | Amount |\n"
            + "| 20230601 | 1 | D    | 150.00 |\n"
            + "| 20230602 | 2 | W    |  50.00 |\n";
    String actualOutput = outputStreamCaptor.toString().replace("\r\n", "\n").replace("\r", "\n");
    expectedOutput = expectedOutput.replace("\r\n", "\n").replace("\r", "\n");
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  @DisplayName("Test printInterestRules")
  public void testPrintInterestRules() {
    List<InterestRule> interestRules = new ArrayList<>();
    interestRules.add(
        new InterestRule(LocalDate.of(2023, 1, 1), "RULE01", BigDecimal.valueOf(5.0)));
    interestRules.add(
        new InterestRule(LocalDate.of(2023, 2, 1), "RULE02", BigDecimal.valueOf(3.0)));

    DisplayUtils.printInterestRules(interestRules);
    String expectedOutput =
        "Interest rules:\n"
            + "| Date     | RuleId | Rate (%) |\n"
            + "| 20230101 | RULE01 |     5.00 |\n"
            + "| 20230201 | RULE02 |     3.00 |\n";
    String actualOutput = outputStreamCaptor.toString().replace("\r\n", "\n").replace("\r", "\n");
    expectedOutput = expectedOutput.replace("\r\n", "\n").replace("\r", "\n");
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  @DisplayName("Test formatTransaction")
  public void testFormatTransaction() {
    Transaction transaction =
        new Transaction(
            "1", LocalDate.of(2023, 6, 1), "AC001", TransactionType.D, BigDecimal.valueOf(150.00));
    BigDecimal balance = BigDecimal.valueOf(1000.00);
    String formattedTransaction = DisplayUtils.formatTransaction(transaction, balance);
    String expectedOutput = "| 20230601 | 1 | D    |  150.00 | 1000.00 |\n";
    assertEquals(expectedOutput, formattedTransaction);
  }

  @Test
  @DisplayName("Test formatInterestTransaction")
  public void testFormatInterestTransaction() {
    LocalDate date = LocalDate.of(2023, 6, 1);
    BigDecimal interest = BigDecimal.valueOf(5.00);
    BigDecimal balance = BigDecimal.valueOf(1000.00);
    String formattedInterestTransaction =
        DisplayUtils.formatInterestTransaction(date, interest, balance);
    String expectedOutput = "| 20230601 |             | I    |    5.00 | 1000.00 |\n";
    assertEquals(expectedOutput, formattedInterestTransaction);
  }

  @Test
  @DisplayName("Test printInterestAndRulesInstructionsToUser ")
  public void testPrintInterestAndRulesInstructionsToUser() {
    DisplayUtils.printInterestAndRulesInstructionsToUser();
    String expectedOutput = BankMessages.INTEREST_RULES_PROMPT + "\n" + BankMessages.PROMPT;
    String actualOutput = outputStreamCaptor.toString().replace("\r\n", "\n").replace("\r", "\n");
    expectedOutput = expectedOutput.replace("\r\n", "\n").replace("\r", "\n");
    assertEquals(expectedOutput.trim(), actualOutput.trim());
  }

  @Test
  @DisplayName("Test printBankStatementInstructionsToUser ")
  public void testPrintBankStatementInstructionsToUser() {
    DisplayUtils.printBankStatementInstructionsToUser();
    String expectedOutput = BankMessages.PRINT_STATEMENT_PROMPT + "\n" + BankMessages.PROMPT;
    String actualOutput = outputStreamCaptor.toString().replace("\r\n", "\n").replace("\r", "\n");
    expectedOutput = expectedOutput.replace("\r\n", "\n").replace("\r", "\n");
    assertEquals(expectedOutput.trim(), actualOutput.trim());
  }

  @Test
  @DisplayName("Test displayMenu when isFirstTime is true")
  public void testDisplayMenuFirstTime() {
    DisplayUtils.displayMenu(true);

    String expectedOutput = String.join("\n", BankMessages.MENU_OPTIONS);
    String actualOutput = outputStreamCaptor.toString().replace("\r\n", "\n").replace("\r", "\n");
    expectedOutput = expectedOutput.replace("\r\n", "\n").replace("\r", "\n");

    assertEquals(expectedOutput.trim(), actualOutput.trim());
  }

  @Test
  @DisplayName("Test displayMenu when isFirstTime is false")
  public void testDisplayMenuNotFirstTime() {
    DisplayUtils.displayMenu(false);

    StringBuilder expectedOutput = new StringBuilder();
    for (String option : BankMessages.MENU_OPTIONS) {
      if (option.equals(BankMessages.PROMPT)) {
        expectedOutput.append(option).append("\n");
        break;
      }
      expectedOutput
          .append(option.equals(BankMessages.WELCOME_MESSAGE) ? BankMessages.ANYTHING_ELSE : option)
          .append("\n");
    }

    String actualOutput = outputStreamCaptor.toString().replace("\r\n", "\n").replace("\r", "\n");
    expectedOutput =
        new StringBuilder(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n"));

    assertEquals(expectedOutput.toString().trim(), actualOutput.trim());
  }
}
