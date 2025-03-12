package com.awesomegic.bankingsystem.exceptions;

public class InvalidTransactionTypeException extends Exception {
  public InvalidTransactionTypeException(String message) {
    super(message);
  }
}
