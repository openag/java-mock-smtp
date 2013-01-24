package com.openag.mocksmtp;

/**
 * Public interface for the mail store, read-only for now
 *
 * @author Andrei Maus
 */
public interface MailStore {

  /**
   * Retrieves and removes a message from the top of the messages stack
   *
   * @return latest {@link MailMessage} instance or null if stack is empty
   */
  MailMessage popMessage();

}
