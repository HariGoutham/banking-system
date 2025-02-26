package com.awesomegic.bankingsystem.services;

import static org.junit.jupiter.api.Assertions.*;

import com.awesomegic.bankingsystem.model.InterestRule;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class InterestServiceTest {
  private InterestService interestService;
  private Scanner scanner;

  @BeforeEach
  public void setUp() {
    interestService = new InterestService();
    scanner = Mockito.mock(Scanner.class);
  }

  @Test
  @DisplayName("Valid input for a new interest rule")
  public void testValidInputNewRule() {
    Mockito.when(scanner.nextLine()).thenReturn("20230615 RULE03 2.20");
    interestService.handleInterestInput(scanner);
    assertEquals(1, interestService.getInterestRules().size());
    assertEquals("RULE03", interestService.getInterestRules().get(0).ruleId());
    assertEquals(
        BigDecimal.valueOf(2.20).setScale(2, RoundingMode.HALF_UP),
        interestService.getInterestRules().get(0).rate());
    assertEquals(LocalDate.of(2023, 6, 15), interestService.getInterestRules().get(0).date());
  }

  @Test
  @DisplayName("Valid input to update an existing interest rule")
  public void testValidInputUpdateExistingRule() {
    interestService.addInterestRule(
        new InterestRule(LocalDate.of(2023, 5, 20), "RULE02", BigDecimal.valueOf(1.90)));
    Mockito.when(scanner.nextLine()).thenReturn("20230520 RULE02 2.00");
    interestService.handleInterestInput(scanner);
    assertEquals(1, interestService.getInterestRules().size());
    assertEquals(
        BigDecimal.valueOf(2.00).setScale(2, RoundingMode.HALF_UP),
        interestService.getInterestRules().get(0).rate());
  }

  @Test
  @DisplayName("Valid input with incorrect date format")
  public void testInvalidInputIncorrectDateFormat() {
    Mockito.when(scanner.nextLine()).thenReturn("2023-06-15 RULE04 3.00");
    interestService.handleInterestInput(scanner);
    assertEquals(0, interestService.getInterestRules().size());
  }

  @Test
  @DisplayName("Invalid input with interest rate out of bounds")
  public void testInvalidInputInvalidInterestRate() {
    Mockito.when(scanner.nextLine()).thenReturn("20230615 RULE05 105.00");
    interestService.handleInterestInput(scanner);
    assertEquals(0, interestService.getInterestRules().size());
  }

  @Test
  @DisplayName("Handling of empty input")
  public void testInvalidInputEmptyInput() {
    Mockito.when(scanner.nextLine()).thenReturn("");
    interestService.handleInterestInput(scanner);
    assertEquals(0, interestService.getInterestRules().size());
  }

  @Test
  @DisplayName("Valid input with multiple rules on the same day")
  public void testValidInputMultipleRulesSameDay() {
    interestService.addInterestRule(
        new InterestRule(LocalDate.of(2023, 6, 15), "RULE03", BigDecimal.valueOf(2.20)));
    Mockito.when(scanner.nextLine()).thenReturn("20230615 RULE06 2.50");
    interestService.handleInterestInput(scanner);
    assertEquals(1, interestService.getInterestRules().size());
    assertEquals("RULE06", interestService.getInterestRules().get(0).ruleId());
    assertEquals(
        BigDecimal.valueOf(2.50).setScale(2, RoundingMode.HALF_UP),
        interestService.getInterestRules().get(0).rate());
  }

  @Test
  @DisplayName("Valid input with edge case for interest rate")
  public void testValidInputEdgeCaseForRate() {
    Mockito.when(scanner.nextLine()).thenReturn("20230616 RULE07 0.01");
    interestService.handleInterestInput(scanner);
    assertEquals(1, interestService.getInterestRules().size());
    assertEquals("RULE07", interestService.getInterestRules().get(0).ruleId());
    assertEquals(BigDecimal.valueOf(0.01), interestService.getInterestRules().get(0).rate());
  }

  @Test
  @DisplayName("Invalid input with negative interest rate")
  public void testInvalidInputNegativeInterestRate() {
    Mockito.when(scanner.nextLine()).thenReturn("20230617 RULE08 -1.00");
    interestService.handleInterestInput(scanner);
    assertEquals(0, interestService.getInterestRules().size());
  }
}
