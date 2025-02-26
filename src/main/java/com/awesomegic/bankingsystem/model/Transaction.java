package com.awesomegic.bankingsystem.model;

import com.awesomegic.bankingsystem.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(
    String id, LocalDate date, String account, TransactionType type, BigDecimal amount) {}
