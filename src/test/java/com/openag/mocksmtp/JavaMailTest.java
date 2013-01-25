package com.openag.mocksmtp;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.Assert.assertEquals;

/**
 * Test sending mail using JavaMail (with spring)
 */
public class JavaMailTest {

  private final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

  private final SimpleMailMessage message = new SimpleMailMessage();

  @Before
  public void setUp() throws Exception {
    mailSender.setHost("localhost");
    mailSender.setPort(2525);
    mailSender.setDefaultEncoding("UTF-8");

    message.setFrom("sender@test");
    message.setTo(new String[]{"receiver1@test", "receiver2@test"});
    message.setText("Hello!");
  }

  @Test
  public void testSimpleMailWithSpringJavaClient() throws Exception {
    final Server server = new Server();
    server.start();

    mailSender.send(message);

    Thread.sleep(10);

    final MailMessage m = server.getMailStore().popMessage();
    assertEquals("Hello!", m.getText());

    server.stop();
  }

  @Test
  @Ignore
  public void testStandaloneClient() throws Exception {
    mailSender.send(message);
  }
}
