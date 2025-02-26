package com.awesomegic.bankingsystem.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InterestRule(LocalDate date, String ruleId, BigDecimal rate) {}
