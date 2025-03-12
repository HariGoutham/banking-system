package com.awesomegic.bankingsystem.services;

import com.awesomegic.bankingsystem.model.InterestRule;
import com.awesomegic.bankingsystem.services.interfaces.InterestRuleManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InterestRuleManagerImpl implements InterestRuleManager {
  private final List<InterestRule> interestRules = new ArrayList<>();

  @Override
  public void saveInterestRule(InterestRule rule) {
    addInterestRule(rule);
  }

  @Override
  public List<InterestRule> getInterestRules() {
    return interestRules;
  }

  public void addInterestRule(InterestRule rule) {
    interestRules.removeIf(r -> r.date().equals(rule.date()));
    interestRules.add(rule);
    interestRules.sort(Comparator.comparing(InterestRule::date));
  }
}
