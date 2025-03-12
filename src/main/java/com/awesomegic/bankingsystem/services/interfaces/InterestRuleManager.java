package com.awesomegic.bankingsystem.services.interfaces;

import com.awesomegic.bankingsystem.model.InterestRule;
import java.util.List;

public interface InterestRuleManager {

  void saveInterestRule(InterestRule rule);

  List<InterestRule> getInterestRules();
}
