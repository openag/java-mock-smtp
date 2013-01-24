package com.openag.mocksmtp;

import org.junit.Test;

import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class SmtpServerTest {

  @Test
  public void testSimpleConversation() throws Exception {
    final Server server = new Server();

    Socket socket = null;

    try {
      server.start();

      socket = new Socket("localhost", 2525); //connect to local server

      final Conversation conversation = new Conversation(socket.getInputStream(), socket.getOutputStream());

      assertEquals("220 localhost java-mock-smtp service ready", conversation.receiveLine());

      conversation.sendLine("HELO relay.example.org");
      assertEquals("250 Hello relay.example.org, I am glad to meet you", conversation.receiveLine());

      conversation.sendLine("MAIL FROM:<bob@example.org>");
      assertEquals("250 Ok", conversation.receiveLine());

      conversation.sendLine("RCPT TO:<alice@example.com>");
      assertEquals("250 Ok", conversation.receiveLine());

      conversation.sendLine("RCPT TO:<theboss@example.com>");
      assertEquals("250 Ok", conversation.receiveLine());

      conversation.sendLine("DATA");
      assertEquals("354 End data with <CR><LF>.<CR><LF>", conversation.receiveLine());

      conversation.sendBody("This is mail body");
      assertEquals("250 Ok: queued as 12345", conversation.receiveLine());

      conversation.sendLine("QUIT");
      assertEquals("221 Bye", conversation.receiveLine());

    } finally {
      if (socket != null) {
        socket.close();
      }

      server.stop();
    }
  }

}
