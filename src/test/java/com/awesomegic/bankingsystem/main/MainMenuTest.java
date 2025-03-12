package com.awesomegic.bankingsystem.main;

import static org.mockito.Mockito.*;

import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.exceptions.*;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.StatementInput;
import com.awesomegic.bankingsystem.model.Transaction;
import com.awesomegic.bankingsystem.services.interfaces.InterestRuleManager;
import com.awesomegic.bankingsystem.services.interfaces.StatementGenerator;
import com.awesomegic.bankingsystem.services.interfaces.TransactionManager;
import com.awesomegic.bankingsystem.services.interfaces.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MainMenuTest {
  private MainMenu mainMenu;
  private TransactionManager transactionManagerMock;
  private InterestRuleManager interestRuleManagerMock;
  private StatementGenerator statementGeneratorMock;
  private Validator inputValidatorMock;
  private Scanner scannerMock;

  @BeforeEach
  void setUp() {
    transactionManagerMock = mock(TransactionManager.class);
    interestRuleManagerMock = mock(InterestRuleManager.class);
    statementGeneratorMock = mock(StatementGenerator.class);
    inputValidatorMock = mock(Validator.class);
    scannerMock = mock(Scanner.class);
    mainMenu =
        new MainMenu(
            transactionManagerMock,
            interestRuleManagerMock,
            statementGeneratorMock,
            inputValidatorMock);
  }

  @DisplayName(
      "Scenario-1 : When user selects the transaction type, the main menu should be able to handle it properly")
  @Test
  void testHandleTransaction()
      throws InvalidDateFormatException,
          InvalidAmountException,
          InvalidTransactionTypeException,
          BusinessRuleException {
    when(scannerMock.nextLine()).thenReturn("1000 deposit 2024-03-11");
    List<Transaction> transactionList = new ArrayList<>();
    transactionList.add(
        new Transaction(
            "20240203-1",
            LocalDate.of(2024, 02, 03),
            "AC001",
            TransactionType.D,
            BigDecimal.valueOf(100),
            BigDecimal.valueOf(100)));
    transactionList.add(
        new Transaction(
            "20240203-2",
            LocalDate.of(2024, 02, 03),
            "AC001",
            TransactionType.W,
            BigDecimal.valueOf(80),
            BigDecimal.valueOf(20)));
    Map<String, List<Transaction>> transactions = new HashMap<>();
    transactions.put("123456", transactionList);
    when(transactionManagerMock.getTransactions()).thenReturn(transactions);
    Transaction mockTransaction = mock(Transaction.class);
    when(inputValidatorMock.getValidatedTransaction(anyString(), anyMap()))
        .thenReturn(mockTransaction);
    when(mockTransaction.account()).thenReturn("123456");
    mainMenu.handleTransaction(scannerMock);
    verify(scannerMock, times(1)).nextLine();
    verify(transactionManagerMock, times(1)).getTransactions();
    verify(inputValidatorMock, times(1)).getValidatedTransaction(anyString(), anyMap());
    verify(transactionManagerMock, times(1)).saveTransaction(mockTransaction);
  }

  @DisplayName(
      "Scenario-2 : When user selects the interest rule, the main menu should be able to handle it properly")
  @Test
  void testHandleInterestRule()
      throws InvalidDateFormatException, InvalidRateException, BusinessRuleException {
    when(scannerMock.nextLine()).thenReturn("20230615 RULE03 2.20");
    InterestRule mockInterestRule = mock(InterestRule.class);
    when(inputValidatorMock.validateInterestRuleInput(anyString())).thenReturn(mockInterestRule);
    List<InterestRule> interestRules = new ArrayList<>();
    when(interestRuleManagerMock.getInterestRules()).thenReturn(interestRules);
    mainMenu.handleInterestInput(scannerMock);
    verify(scannerMock, times(1)).nextLine();
    verify(inputValidatorMock, times(1)).validateInterestRuleInput(anyString());
    verify(interestRuleManagerMock, times(1)).saveInterestRule(mockInterestRule);
    verify(interestRuleManagerMock, times(1)).getInterestRules();
  }

  @DisplayName(
      "Scenario-3 : When user selects the generate statement, the main menu should be able to handle it properly")
  @Test
  void testHandleGenerateStatement() throws InvalidDateFormatException, BusinessRuleException {
    when(scannerMock.nextLine()).thenReturn("123456 202306");
    StatementInput mockStatementInput = mock(StatementInput.class);
    when(inputValidatorMock.validatePrintStatementInput(anyString()))
        .thenReturn(mockStatementInput);
    List<Transaction> transactions = new ArrayList<>();
    when(transactionManagerMock.getTransactionsByAccount(anyString())).thenReturn(transactions);
    List<InterestRule> interestRules = new ArrayList<>();
    when(interestRuleManagerMock.getInterestRules()).thenReturn(interestRules);
    mainMenu.handlePrintStatementInput(scannerMock);
    verify(scannerMock, times(1)).nextLine();
    verify(inputValidatorMock, times(1)).validatePrintStatementInput(anyString());
    verify(interestRuleManagerMock, times(1)).getInterestRules();
    verify(statementGeneratorMock, times(1))
        .generateAndPrintStatement(mockStatementInput, transactions, interestRules);
  }
}
