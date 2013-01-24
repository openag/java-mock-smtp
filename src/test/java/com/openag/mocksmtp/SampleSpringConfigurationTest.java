package com.openag.mocksmtp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SampleSpringConfigurationTest {

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private MailStore mailStore;

  private ClassPathXmlApplicationContext context;

  @Before
  public void setUp() throws Exception {
    context = new ClassPathXmlApplicationContext("classpath:sample-spring-config.xml");
    context.getAutowireCapableBeanFactory().autowireBeanProperties(this,
        AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
  }

  @Test
  public void testSpringConfiguration() throws Exception {
    assertNull(mailStore.popMessage());

    final SimpleMailMessage message = new SimpleMailMessage();
    message.setTo("test@test");
    message.setText("spring-config-test");
    mailSender.send(message);

    Thread.sleep(10);

    final MailMessage result = mailStore.popMessage();
    assertEquals("spring-config-test", result.getText());
  }

  @After
  public void tearDown() throws Exception {
    context.destroy();
  }
}
