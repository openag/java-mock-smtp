package com.openag.mocksmtp;

import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.Assert.assertEquals;

/**
 * Test sending mail using JavaMail (with spring)
 */
public class JavaMailTest {

  @Test
  public void testSimpleMailWithSpringJavaClient() throws Exception {
    final Server server = new Server();
    server.start();

    final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("localhost");
    mailSender.setPort(2525);
    mailSender.setDefaultEncoding("UTF-8");

    final SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("sender@test");
    message.setTo(new String[]{"receiver1@test", "receiver2@test"});
    message.setText("Hello!");
    mailSender.send(message);

    Thread.sleep(100);

    final MailMessage m = server.getMailStore().popMessage();
    assertEquals("Hello!", m.getText());

    server.stop();
  }

}
