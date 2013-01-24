package com.openag.mocksmtp;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class HandlersTest {

  @Test
  public void testGreetingsHandler() throws Exception {
    final String result = runHandler(Handlers.GREETINGS);
    assertEquals("220 localhost java-mock-smtp service ready\r\n", result);
  }

  @Test
  public void testHeloHandler() throws Exception {
    final String result = runHandler(new Handlers.Helo("test.server"));
    assertEquals("250 Hello test.server, I am glad to meet you\r\n", result);
  }

  @Test
  public void testMailHandler() throws Exception {
    final String result = runHandler(new Handlers.Mail("FROM:<bob@example.org>"));
    assertEquals("250 Ok\r\n", result);
  }

  @Test
  public void testRcptHandler() throws Exception {
    final String result = runHandler(new Handlers.Rcpt("TO:<alice@example.com>"));
    assertEquals("250 Ok\r\n", result);
  }

  @Test
  public void testDataHandler() throws Exception {
    final String sampleMailBody = "From: \"Bob Example\" <bob@example.org>\n" +
        "To: \"Alice Example\" <alice@example.com>\n" +
        "Cc: theboss@example.com\n" +
        "Date: Tue, 15 January 2008 16:02:43 -0500\n" +
        "Subject: Test message\n" +
        "\n" +
        "Hello Alice.\n" +
        "This is a test message with 5 header fields and 4 lines in the message body.\n" +
        "Your friend,\n" +
        "Bob\n";

    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    final ByteArrayInputStream in = new ByteArrayInputStream((sampleMailBody + "\r\n.\r\n").getBytes());

    final Handlers.Data handler = new Handlers.Data();
    final Conversation conversation = new Conversation(in, out);

    handler.handle(conversation);

    final BufferedReader resultReader = new BufferedReader(new StringReader(out.toString()));

    assertEquals("354 End data with <CR><LF>.<CR><LF>", resultReader.readLine());
    assertEquals(sampleMailBody, conversation.getMessage());
    assertEquals("250 Ok: queued as 12345", resultReader.readLine());
  }

  private String runHandler(CommandHandler handler) throws IOException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);

    final Conversation conversation = new Conversation(in, out);

    handler.handle(conversation);

    return new String(out.toByteArray());
  }

}
