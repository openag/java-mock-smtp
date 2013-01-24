package com.openag.mocksmtp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * Single client connection loop; performs full cycle of SMTP client-server interaction
 *
 * @author Andrei Maus
 */
class Connection implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(Connection.class);

  private final Socket socket;
  private final Conversation conversation;
  private final SimpleMailStore mailStore;

  Connection(final Socket socket, SimpleMailStore mailStore) throws IOException {
    this.socket = socket;
    this.conversation = new Conversation(socket.getInputStream(), socket.getOutputStream());
    this.mailStore = mailStore;
  }

  public void run() {
    LOG.debug("Started client connection loop for {}", socket.getRemoteSocketAddress());

    try {
      LOG.debug("Sending greeting message to {}", socket.getRemoteSocketAddress());
      Handlers.GREETINGS.handle(conversation); // greeting must always come first

      CommandHandler handler;

      try {
        do {
          final String line = conversation.receiveLine();

          final Command command = Command.parse(line);
          LOG.debug("Received command {} (line: {})", command.getType(), line);

          if (command.getType() == Command.Type.UNKNOWN) {
            throw new RuntimeException("SMTP command line '" + line + "' is not recognised");
          }

          handler = Handlers.create(command);
          LOG.debug("Created handler {} for command {}", handler.getClass(), command.getType());

          handler.handle(conversation);
        } while (!(handler instanceof Handlers.Quit));

        LOG.debug("Exiting client connection loop for {}", socket.getRemoteSocketAddress());

        mailStore.push(conversation.getMessage());

      } finally {
        socket.close();
      }
    } catch (IOException e) {
      throw new RuntimeException("Exception in connection loop", e);
    }
  }
}
