package com.openag.mocksmtp;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class LoadTestWithJavaMail {

  @Test
  public void testSendMailsConcurrently() throws Exception {
    final Server server = new Server();
    try {
      server.start();

      final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      mailSender.setHost("localhost");
      mailSender.setPort(2525);
      mailSender.setDefaultEncoding("UTF-8");


      final ExecutorService executor = Executors.newFixedThreadPool(20);

      final int total = 10000;

      final List<Future> futures = new ArrayList<Future>(total);

      final long start = System.currentTimeMillis();

      for (int i = 0; i < total; i++) {
        final int finalI = i;

        final Future<?> future = executor.submit(new Runnable() {
          public void run() {
            final SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(String.valueOf(finalI));
            message.setTo("receiver@test");
            message.setText(String.valueOf(finalI));
            mailSender.send(message);
          }
        });

        futures.add(future);
      }

      for (Future future : futures) {
        future.get(); //wait for completion
      }

      System.out.println("Time: " + (System.currentTimeMillis() - start));

      final MailStore store = server.getMailStore();

      int count = 0;
      MailMessage message;
      while ((message = store.popMessage()) != null) {
        assertTrue(message.getFrom().equals(message.getText()));
        ++count;
      }

      assertEquals(total, count);
    } finally {
      server.stop();
    }
  }


}
