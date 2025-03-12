package com.awesomegic.bankingsystem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.StatementInput;
import com.awesomegic.bankingsystem.model.Transaction;
import com.awesomegic.bankingsystem.services.ValidatorImpl;
import com.awesomegic.bankingsystem.utils.TransactionUtils;
import com.awesomegic.bankingsystem.validations.InputValidation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class ValidatorImplTest {

  @InjectMocks private ValidatorImpl validator;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetValidatedTransactionValidDeposit() throws Exception {
    String input = "20231001 ACC123 D 100.00";
    Map<String, List<Transaction>> transactions = new HashMap<>();

    try (MockedStatic<InputValidation> mockedInputValidation =
            Mockito.mockStatic(InputValidation.class);
        MockedStatic<TransactionUtils> mockedTransactionUtils =
            Mockito.mockStatic(TransactionUtils.class)) {

      mockedInputValidation
          .when(() -> InputValidation.validateDate("20231001"))
          .thenReturn(LocalDate.of(2023, 10, 1));
      mockedInputValidation
          .when(() -> InputValidation.validateTransactionType("D"))
          .thenReturn(TransactionType.D);
      mockedInputValidation
          .when(() -> InputValidation.validateAmount("100.00"))
          .thenReturn(new BigDecimal("100.00"));
      mockedTransactionUtils
          .when(() -> TransactionUtils.generateTransactionId(any(LocalDate.class), any(Map.class)))
          .thenReturn("T1");

      Transaction result = validator.getValidatedTransaction(input, transactions);

      assertNotNull(result);
      assertEquals("T1", result.id());
      assertEquals(LocalDate.of(2023, 10, 1), result.date());
      assertEquals("ACC123", result.account());
      assertEquals(TransactionType.D, result.type());
      assertEquals(new BigDecimal("100.00"), result.amount());
      assertEquals(new BigDecimal("100.00"), result.balance());

      mockedInputValidation.verify(() -> InputValidation.validateDate("20231001"));
      mockedInputValidation.verify(() -> InputValidation.validateTransactionType("D"));
      mockedInputValidation.verify(() -> InputValidation.validateAmount("100.00"));
      mockedTransactionUtils.verify(
          () -> TransactionUtils.generateTransactionId(any(LocalDate.class), any(Map.class)));
    }
  }

  @Test
  void testValidateInterestRuleInputValid() throws Exception {
    String input = "20231001 RULE123 5.0";
    try (MockedStatic<InputValidation> mockedInputValidation =
        Mockito.mockStatic(InputValidation.class)) {
      mockedInputValidation
          .when(() -> InputValidation.validateDate("20231001"))
          .thenReturn(LocalDate.of(2023, 10, 1));
      mockedInputValidation
          .when(() -> InputValidation.validateRate("5.0"))
          .thenReturn(new BigDecimal("5.0"));

      InterestRule result = validator.validateInterestRuleInput(input);

      assertNotNull(result);
      assertEquals(LocalDate.of(2023, 10, 1), result.date());
      assertEquals("RULE123", result.ruleId());
      assertEquals(new BigDecimal("5.0"), result.rate());

      mockedInputValidation.verify(
          () -> InputValidation.validateBusinessRule(eq(false), anyString()),
          times(2)); // Use eq(false) instead of false
      mockedInputValidation.verify(() -> InputValidation.validateDate("20231001"));
      mockedInputValidation.verify(() -> InputValidation.validateRate("5.0"));
    }
  }

  @Test
  void testValidatePrintStatementInput_Valid() throws Exception {
    String input = "ACC123 202310";

    try (MockedStatic<InputValidation> mockedInputValidation =
        Mockito.mockStatic(InputValidation.class)) {

      mockedInputValidation
          .when(() -> InputValidation.validateYearMonth("202310"))
          .thenReturn(YearMonth.of(2023, 10));

      StatementInput result = validator.validatePrintStatementInput(input);

      assertNotNull(result);
      assertEquals("ACC123", result.account());
      assertEquals(YearMonth.of(2023, 10), result.yearMonth());

      mockedInputValidation.verify(
          () -> InputValidation.validateBusinessRule(eq(false), anyString()));
      mockedInputValidation.verify(() -> InputValidation.validateYearMonth("202310"));
    }
  }
}
