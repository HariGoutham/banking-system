package com.awesomegic.bankingsystem.services;

import static org.junit.jupiter.api.Assertions.*;

import com.awesomegic.bankingsystem.model.InterestRule;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InterestRuleManagerImplTest {
  private InterestRuleManagerImpl interestRuleManager;

  @BeforeEach
  void setUp() {
    interestRuleManager = new InterestRuleManagerImpl();
  }

  @DisplayName("Scenario-1 : Add a new interest rule and verify it is saved correctly")
  @Test
  void testSaveInterestRule() {
    InterestRule rule =
        new InterestRule(LocalDate.of(2023, 6, 15), "RULE03", new BigDecimal("2.20"));
    interestRuleManager.saveInterestRule(rule);
    List<InterestRule> rules = interestRuleManager.getInterestRules();
    assertEquals(1, rules.size());
    assertEquals(rule, rules.get(0));
  }

  @DisplayName("Scenario-2 : Add multiple interest rules and verify they are sorted by date")
  @Test
  void testInterestRulesAreSortedByDate() {
    InterestRule rule1 =
        new InterestRule(LocalDate.of(2023, 6, 15), "RULE03", new BigDecimal("2.20"));
    InterestRule rule2 =
        new InterestRule(LocalDate.of(2023, 5, 20), "RULE02", new BigDecimal("1.90"));
    InterestRule rule3 =
        new InterestRule(LocalDate.of(2023, 1, 1), "RULE01", new BigDecimal("1.95"));
    interestRuleManager.saveInterestRule(rule1);
    interestRuleManager.saveInterestRule(rule2);
    interestRuleManager.saveInterestRule(rule3);
    List<InterestRule> rules = interestRuleManager.getInterestRules();
    assertEquals(3, rules.size());
    assertEquals(rule3, rules.get(0)); // Earliest date
    assertEquals(rule2, rules.get(1)); // Middle date
    assertEquals(rule1, rules.get(2)); // Latest date
  }

  @DisplayName(
      "Scenario-3 : Add an interest rule with a duplicate date and verify it replaces the existing rule")
  @Test
  void testAddInterestRuleWithDuplicateDate() {
    InterestRule rule1 =
        new InterestRule(LocalDate.of(2023, 6, 15), "RULE03", new BigDecimal("2.20"));
    interestRuleManager.saveInterestRule(rule1);
    InterestRule rule2 =
        new InterestRule(LocalDate.of(2023, 6, 15), "RULE04", new BigDecimal("2.50"));
    interestRuleManager.saveInterestRule(rule2);
    List<InterestRule> rules = interestRuleManager.getInterestRules();
    assertEquals(1, rules.size());
    assertEquals(rule2, rules.get(0));
  }

  @DisplayName("Scenario-4 : Retrieve an empty list of interest rules when no rules are added")
  @Test
  void testGetInterestRulesWhenEmpty() {
    List<InterestRule> rules = interestRuleManager.getInterestRules();
    assertTrue(rules.isEmpty());
  }

  @DisplayName("Scenario-5 : Add multiple interest rules and verify the list size")
  @Test
  void testAddMultipleInterestRules() {
    InterestRule rule1 =
        new InterestRule(LocalDate.of(2023, 6, 15), "RULE03", new BigDecimal("2.20"));
    InterestRule rule2 =
        new InterestRule(LocalDate.of(2023, 5, 20), "RULE02", new BigDecimal("1.90"));
    InterestRule rule3 =
        new InterestRule(LocalDate.of(2023, 1, 1), "RULE01", new BigDecimal("1.95"));
    interestRuleManager.saveInterestRule(rule1);
    interestRuleManager.saveInterestRule(rule2);
    interestRuleManager.saveInterestRule(rule3);
    List<InterestRule> rules = interestRuleManager.getInterestRules();
    assertEquals(3, rules.size());
  }
}
