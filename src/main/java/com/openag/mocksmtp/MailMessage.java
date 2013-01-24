package com.openag.mocksmtp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static com.openag.mocksmtp.Utils.*;

/**
 * Simple email message bean
 *
 * @author Andrei Maus
 */
public class MailMessage {
  private final Map<String, String> headers = new HashMap<String, String>();

  private String text;

  /**
   * Copy or raw mail message for debug and printing options
   */
  private final String rawMessage;

  /**
   * Creates new mail message by parsing raw message payload
   *
   * @param s raw email message payload as it is sent via SMTP
   */
  MailMessage(final String s) {
    this.rawMessage = s;

    final BufferedReader in = new BufferedReader(new StringReader(s));

    String line;

    try {
      while (isNotEmpty(line = in.readLine())) {
        headers.put(substringBefore(line, ":"), substringAfter(line, ": "));

      }

      text = Utils.readAll(in);

    } catch (IOException e) {
      //ignore, not expected from StringReader...
    }
  }

  public String getText() {
    return text;
  }


  public String getFrom() {
    return headers.get("From");
  }

  public String[] getTo() {
    final String to = headers.get("To");
    if (Utils.isEmpty(to)) {
      return new String[0];
    }
    return to.split(",\\s*");
  }

  public String getHeader(final String name) {
    return headers.get(name);
  }

  @Override
  public String toString() {
    return rawMessage;
  }
}