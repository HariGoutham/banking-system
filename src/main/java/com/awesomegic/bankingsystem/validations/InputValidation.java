package com.awesomegic.bankingsystem.validations;

import com.awesomegic.bankingsystem.enums.TransactionType;
import com.awesomegic.bankingsystem.exceptions.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputValidation {

  public static void validateBusinessRule(boolean businessCondition, String businessMessage)
      throws BusinessRuleException {
    if (businessCondition) {
      throw new BusinessRuleException(businessMessage);
    }
  }

  public static LocalDate validateDate(String dateString) throws InvalidDateFormatException {
    try {
      return LocalDate.parse(dateString, DateTimeFormatter.BASIC_ISO_DATE);
    } catch (DateTimeParseException e) {
      throw new InvalidDateFormatException(
          "Invalid date. Please provide a valid date in YYYYMMdd format.");
    }
  }

  public static YearMonth validateYearMonth(String yearMonthString)
      throws InvalidDateFormatException {
    try {
      return YearMonth.parse(yearMonthString, DateTimeFormatter.ofPattern("yyyyMM"));
    } catch (DateTimeParseException e) {
      throw new InvalidDateFormatException(
          "Invalid date format. Please provide a valid date in YYYYMM format.");
    }
  }

  public static TransactionType validateTransactionType(String transactionTypeString)
      throws InvalidTransactionTypeException {
    try {
      return TransactionType.fromString(transactionTypeString.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new InvalidTransactionTypeException(
          "Invalid transaction type. Please provide a valid transaction type.");
    }
  }

  public static BigDecimal validateAmount(String amountString) throws InvalidAmountException {
    try {
      BigDecimal amount = new BigDecimal(amountString);
      if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidAmountException("Amount must be greater than zero.");
      }
      if (amount.scale() > 2) {
        throw new InvalidAmountException("Amount can have up to 2 decimal places only.");
      }
      return amount;
    } catch (NumberFormatException e) {
      throw new InvalidAmountException(
          "Invalid amount format. Please provide a valid numeric value.");
    }
  }

  public static BigDecimal validateRate(String amountString) throws InvalidRateException {
    try {
      return new BigDecimal(amountString);
    } catch (NumberFormatException e) {
      throw new InvalidRateException(
          "Invalid interest rate format. Please provide a valid numeric value.");
    }
  }
}
