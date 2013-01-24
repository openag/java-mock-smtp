package com.openag.mocksmtp;

import java.io.IOException;

/**
 * Interface for handling particular command issued by the client in the frame of current conversation.
 *
 * @author Andrei Maus
 */
interface CommandHandler {

  /**
   * Handler method for specific SMTP command; implementation should read and/or write to/from conversation streams data
   * according to SMTP specification
   *
   * @param conversation {@link Conversation} instance
   */
  void handle(Conversation conversation) throws IOException;

}
