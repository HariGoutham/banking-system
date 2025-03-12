package com.awesomegic.bankingsystem.services.interfaces;

import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.model.StatementInput;
import com.awesomegic.bankingsystem.model.Transaction;
import java.util.List;

public interface StatementGenerator {
  void generateAndPrintStatement(
      StatementInput statementInput,
      List<Transaction> accountTransactions,
      List<InterestRule> interestRules);
}
