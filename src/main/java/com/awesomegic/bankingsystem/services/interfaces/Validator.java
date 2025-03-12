package com.awesomegic.bankingsystem.services.interfaces;

import com.awesomegic.bankingsystem.exceptions.*;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.StatementInput;
import com.awesomegic.bankingsystem.model.Transaction;
import java.util.List;
import java.util.Map;

public interface Validator {
  Transaction getValidatedTransaction(String input, Map<String, List<Transaction>> transactions)
      throws InvalidDateFormatException,
          InvalidTransactionTypeException,
          InvalidAmountException,
          BusinessRuleException;

  InterestRule validateInterestRuleInput(String input)
      throws BusinessRuleException, InvalidDateFormatException, InvalidRateException;

  StatementInput validatePrintStatementInput(String input)
      throws BusinessRuleException, InvalidDateFormatException;
}
