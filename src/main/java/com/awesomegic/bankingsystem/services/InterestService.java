package com.awesomegic.bankingsystem.services;

import com.awesomegic.bankingsystem.constants.BankMessages;
import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.utils.DisplayUtils;
import com.awesomegic.bankingsystem.validations.InputValidation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import org.springframework.stereotype.Service;

@Service
public class InterestService {
  private final List<InterestRule> interestRules = new ArrayList<>();

  public void handleInterestInput(Scanner scanner) {
    DisplayUtils.printInterestAndRulesInstructionsToUser();
    String input = scanner.nextLine().trim();
    if (input.isEmpty()) return;
    Optional<InterestRule> interestRule = InputValidation.validateInterestRuleInput(input);
    interestRule.ifPresentOrElse(
        this::processInterestRule, () -> System.out.println(BankMessages.INVALID_INPUT_FORMAT));
  }

  public void processInterestRule(InterestRule rule) {
    addInterestRule(rule);
    DisplayUtils.printInterestRules(interestRules);
  }

  public void addInterestRule(InterestRule rule) {
    interestRules.removeIf(r -> r.date().equals(rule.date()));
    interestRules.add(rule);
    interestRules.sort(Comparator.comparing(InterestRule::date));
  }

  public List<InterestRule> getInterestRules() {
    return interestRules;
  }
}
