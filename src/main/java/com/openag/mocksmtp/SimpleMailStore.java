package com.openag.mocksmtp;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * In-memory stack-like storage for incoming mails
 *
 * @author Andrei Maus
 */
public class SimpleMailStore implements MailStore {

  private final ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<String>();

  public void push(final String message) {
    messages.offer(message);
  }

  public String pop() {
    return messages.poll();
  }
}
