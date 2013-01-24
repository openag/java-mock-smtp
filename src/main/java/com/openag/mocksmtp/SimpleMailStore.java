package com.openag.mocksmtp;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * In-memory stack-like storage for incoming mails
 *
 * @author Andrei Maus
 */
class SimpleMailStore implements MailStore {

  private final ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<String>();

  void push(final String message) {
    messages.offer(message);
  }

  String pop() {
    return messages.poll();
  }

  void clear() {
    messages.clear();
  }

  public MailMessage popMessage() {
    final String s = pop();
    return (s == null) ? null : new MailMessage(s);
  }
}
