package com.openag.mocksmtp;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MailMessageTest {

  @Test
  public void testBuildNewMessage() throws Exception {
    final String sample = "Date: Tue, 22 Jan 2013 16:40:10 +0100 (CET)\n" +
        "From: sender@test\n" +
        "To: receiver1@test, receiver2@test\n" +
        "Message-ID: <26354209.0.1358869210980.JavaMail.localhost>\n" +
        "MIME-Version: 1.0\n" +
        "Content-Type: text/plain; charset=UTF-8\n" +
        "Content-Transfer-Encoding: 7bit\n" +
        "\n" +
        "Hello!";


    final MailMessage message = MailMessage.toMessage(sample);
    assertEquals("Hello!", message.getText());
    assertEquals("sender@test", message.getFrom());
    assertArrayEquals(new String[]{"receiver1@test", "receiver2@test"}, message.getTo());
    assertEquals("<26354209.0.1358869210980.JavaMail.localhost>", message.getHeader("Message-ID"));
  }
}
