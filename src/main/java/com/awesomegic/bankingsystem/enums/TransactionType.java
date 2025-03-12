package com.awesomegic.bankingsystem.enums;

public enum TransactionType {
  D,
  W,
  I;

  public static TransactionType fromString(String type) {
    return switch (type.toUpperCase()) {
      case "D" -> D;
      case "W" -> W;
      case "I" -> I;
      default -> throw new IllegalArgumentException("Invalid transaction type: " + type);
    };
  }
}
