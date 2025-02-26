package com.awesomegic.bankingsystem.constants;

public class BankMessages {
  public static final String WELCOME_MESSAGE =
      "Welcome to AwesomeGIC Bank! What would you like to do?";
  public static final String ANYTHING_ELSE = "Is there anything else you'd like to do?";
  public static final String INPUT_TRANSACTIONS = "[T] Input transactions";
  public static final String DEFINE_INTEREST_RULES = "[I] Define interest rules";
  public static final String PRINT_STATEMENT = "[P] Print statement";
  public static final String QUIT = "[Q] Quit";
  public static final String PROMPT = "> ";

  public static final String INPUT_TRANSACTION_PROMPT =
      "Please enter transaction details in <Date> <Account> <Type> <Amount> format (or enter blank to go back to main menu):";
  public static final String INVALID_INPUT_FORMAT = "Invalid input format. Please try again.";
  public static final String THANK_YOU_MESSAGE = "Thank you for banking with AwesomeGIC Bank.";
  public static final String HAVE_A_NICE_DAY = "Have a nice day!";
  public static final String INTEREST_RULES_PROMPT =
      "Please enter interest rules details in <Date> <RuleId> <Rate in %> format (or enter blank to go back to main menu):";
  public static final String PRINT_STATEMENT_PROMPT =
      "Please enter account and month to generate the statement <Account> <Year><Month> (or enter blank to go back to main menu):";
  public static final String INVALID_CHOICE = "Invalid choice. Please try again.";
  public static final String ERROR_PREFIX = "Error: ";
  public static final String ACCOUNT_STATEMENT_HEADER =
      "| Date     | Txn Id      | Type | Amount  | Balance |\n";
  public static final String[] MENU_OPTIONS = {
    WELCOME_MESSAGE, INPUT_TRANSACTIONS, DEFINE_INTEREST_RULES, PRINT_STATEMENT, QUIT, PROMPT
  };
}
