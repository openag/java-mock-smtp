package com.openag.mocksmtp;

import java.io.IOException;

/**
 * Set of {@link CommandHandler} implementations; use {@link #create(Command)} to obtain corresponding handler instance
 *
 * @author Andrei Maus
 */
class Handlers {
  static final CommandHandler GREETINGS = new Greetings();

  private Handlers() {
  }

  /**
   * Obtain command handler for provided command descriptor
   *
   * @param command {@link Command} instance
   * @return {@link CommandHandler} instance
   */
  public static CommandHandler create(final Command command) {
    final String parameter = command.getParameter();
    final Command.Type type = command.getType();

    switch (type) {
      case HELO:
      case EHLO:
        return new Helo(parameter);
      case MAIL:
        return new Mail(parameter);
      case RCPT:
        return new Rcpt(parameter);
      case DATA:
        return new Data();
      case QUIT:
        return new Quit();
    }

    throw new RuntimeException("No handler implementation found for command " + type);
  }

  /**
   * Abstract base handler with optional parameter field
   */
  private abstract static class GenericRequestHandler implements CommandHandler {
    final String parameter;

    GenericRequestHandler(String parameter) {
      this.parameter = parameter;
    }

    private GenericRequestHandler() {
      this(null);
    }
  }

  /**
   * Sends greeting message on connection; very first handler to execute just after the connection is established
   */
  private static class Greetings extends GenericRequestHandler {
    public void handle(Conversation conversation) throws IOException {
      conversation.sendLine("220 localhost java-mock-smtp service ready");
    }
  }

  /**
   * Handler for HELO SMTP command
   */
  static class Helo extends GenericRequestHandler {
    Helo(String parameter) {
      super(parameter);
    }

    public void handle(Conversation conversation) throws IOException {
      conversation.sendLine("250 Hello " + parameter + ", I am glad to meet you");
    }
  }

  /**
   * Handler for MAIL SMTP command
   */
  static class Mail extends GenericRequestHandler {
    Mail(String parameter) {
      super(parameter);
    }

    public void handle(Conversation conversation) throws IOException {
      conversation.sendLine("250 Ok");
    }
  }

  /**
   * Handler for MAIL RCPT command
   */
  static class Rcpt extends GenericRequestHandler {
    Rcpt(String parameter) {
      super(parameter);
    }

    public void handle(Conversation conversation) throws IOException {
      conversation.sendLine("250 Ok");
    }
  }

  /**
   * Handler for DATA SMTP command
   */
  static class Data extends GenericRequestHandler {
    public void handle(Conversation conversation) throws IOException {
      conversation.sendLine("354 End data with <CR><LF>.<CR><LF>");

      final String body = conversation.receiveBody();
      conversation.setMessage(body);

      conversation.sendLine("250 Ok: queued as 12345");
    }
  }

  /**
   * Handler for QUIT SMTP command
   */
  static class Quit extends GenericRequestHandler {
    public void handle(Conversation conversation) throws IOException {
      conversation.sendLine("221 Bye");
    }
  }

}
