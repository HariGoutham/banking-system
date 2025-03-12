package com.awesomegic.bankingsystem.exceptions;

public class InsufficientBalanceException extends Exception {
  public InsufficientBalanceException(String message) {
    super(message);
  }
}
