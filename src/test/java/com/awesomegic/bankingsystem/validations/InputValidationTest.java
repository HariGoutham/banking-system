package com.awesomegic.bankingsystem.validations;

import static org.junit.jupiter.api.Assertions.*;

import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.exceptions.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InputValidationTest {

  @Test
  @DisplayName("validateBusinessRule - Should not throw exception when business condition is false")
  void testValidateBusinessRuleValid() {
    assertDoesNotThrow(() -> InputValidation.validateBusinessRule(false, "No error"));
  }

  @Test
  @DisplayName(
      "validateBusinessRule - Should throw BusinessRuleException when business condition is true")
  void testValidateBusinessRuleInvalid() {
    Exception exception =
        assertThrows(
            BusinessRuleException.class,
            () -> InputValidation.validateBusinessRule(true, "Business rule violated"));
    assertEquals("Business rule violated", exception.getMessage());
  }

  @Test
  @DisplayName("validateDate - Should return LocalDate for a valid date in YYYYMMdd format")
  void testValidateDateValid() throws InvalidDateFormatException {
    LocalDate date = InputValidation.validateDate("20231001");
    assertEquals(LocalDate.of(2023, 10, 1), date);
  }

  @Test
  @DisplayName("validateDate - Should throw InvalidDateFormatException for an invalid date format")
  void testValidateDateInvalidFormat() {
    Exception exception =
        assertThrows(
            InvalidDateFormatException.class, () -> InputValidation.validateDate("2023-10-01"));
    assertEquals(
        "Invalid date. Please provide a valid date in YYYYMMdd format.", exception.getMessage());
  }

  @Test
  @DisplayName("validateYearMonth - Should return YearMonth for a valid date in YYYYMM format")
  void testValidateYearMonthValid() throws InvalidDateFormatException {
    YearMonth yearMonth = InputValidation.validateYearMonth("202310");
    assertEquals(YearMonth.of(2023, 10), yearMonth);
  }

  @Test
  @DisplayName(
      "validateYearMonth - Should throw InvalidDateFormatException for an invalid date format")
  void testValidateYearMonthInvalidFormat() {
    Exception exception =
        assertThrows(
            InvalidDateFormatException.class, () -> InputValidation.validateYearMonth("2023/10"));
    assertEquals(
        "Invalid date format. Please provide a valid date in YYYYMM format.",
        exception.getMessage());
  }

  @Test
  @DisplayName(
      "validateTransactionType - Should return TransactionType for a valid transaction type")
  void testValidateTransactionTypeValid() throws InvalidTransactionTypeException {
    TransactionType type = InputValidation.validateTransactionType("D");
    assertEquals(TransactionType.D, type);
  }

  @Test
  @DisplayName(
      "validateTransactionType - Should throw InvalidTransactionTypeException for an invalid transaction type")
  void testValidateTransactionTypeInvalid() {
    Exception exception =
        assertThrows(
            InvalidTransactionTypeException.class,
            () -> InputValidation.validateTransactionType("X"));
    assertEquals(
        "Invalid transaction type. Please provide a valid transaction type.",
        exception.getMessage());
  }

  @Test
  @DisplayName("validateAmount - Should return BigDecimal for a valid amount")
  void testValidateAmountValid() throws InvalidAmountException {
    BigDecimal amount = InputValidation.validateAmount("100.00");
    assertEquals(new BigDecimal("100.00"), amount);
  }

  @Test
  @DisplayName("validateAmount - Should throw InvalidAmountException for an invalid amount format")
  void testValidateAmountInvalidFormat() {
    Exception exception =
        assertThrows(InvalidAmountException.class, () -> InputValidation.validateAmount("invalid"));
    assertEquals(
        "Invalid amount format. Please provide a valid numeric value.", exception.getMessage());
  }

  @Test
  @DisplayName("validateAmount - Should throw InvalidAmountException for a zero or negative amount")
  void testValidateAmountZeroOrNegative() {
    Exception exception =
        assertThrows(InvalidAmountException.class, () -> InputValidation.validateAmount("0"));
    assertEquals("Amount must be greater than zero.", exception.getMessage());
  }

  @Test
  @DisplayName(
      "validateAmount - Should throw InvalidAmountException for an amount with more than 2 decimal places")
  void testValidateAmountMoreThanTwoDecimalPlaces() {
    Exception exception =
        assertThrows(InvalidAmountException.class, () -> InputValidation.validateAmount("100.123"));
    assertEquals("Amount can have up to 2 decimal places only.", exception.getMessage());
  }

  @Test
  @DisplayName("validateRate - Should return BigDecimal for a valid rate")
  void testValidateRateValid() throws InvalidRateException {
    BigDecimal rate = InputValidation.validateRate("5.0");
    assertEquals(new BigDecimal("5.0"), rate);
  }

  @Test
  @DisplayName("validateRate - Should throw InvalidRateException for an invalid rate format")
  void testValidateRateInvalidFormat() {
    Exception exception =
        assertThrows(InvalidRateException.class, () -> InputValidation.validateRate("invalid"));
    assertEquals(
        "Invalid interest rate format. Please provide a valid numeric value.",
        exception.getMessage());
  }
}
