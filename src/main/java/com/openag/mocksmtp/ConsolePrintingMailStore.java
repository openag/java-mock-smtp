package com.openag.mocksmtp;

/**
 * {@link com.openag.mocksmtp.MailStore} implementation that immediately prints all incomming mail messages to standard
 * output
 *
 * @author Andrei Maus
 */
public class ConsolePrintingMailStore implements MailStore {

  @Override
  public String pop() {
    return null; //no messages are stored in this class
  }

  @Override
  public void push(String message) {
    System.out.println(message);
  }
}
