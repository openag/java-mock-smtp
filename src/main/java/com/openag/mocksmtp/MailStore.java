package com.openag.mocksmtp;

/**
 * Storage for incoming email objects
 *
 * @author Andrei Maus
 */
public interface MailStore {

  /**
   * Retrieves and removes a message from the store; in a stack-like store this is analogous to pop'ing entry out of
   * stack
   *
   * @return latest {@link MailMessage} instance or null if stack is empty
   */
  String pop();

  /**
   * Pushes new mail message into the store
   */
  void push(String message);
}
