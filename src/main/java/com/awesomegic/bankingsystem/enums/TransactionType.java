package com.awesomegic.bankingsystem.enums;

public enum TransactionType {
  D,
  W;

  public static TransactionType fromString(String type) {
    return switch (type.toUpperCase()) {
      case "D" -> D;
      case "W" -> W;
      default -> throw new IllegalArgumentException("Invalid transaction type: " + type);
    };
  }
}
