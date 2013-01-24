package com.openag.mocksmtp;

import java.io.*;

/**
 * Aggregator for objects related to particular conversation with a simple request/reply functionality. The conversation
 * follows SMTP structure and uses standard SMTP delimiters.
 *
 * @author Andrei Maus
 */
class Conversation {

  private final BufferedWriter out;
  private final BufferedReader in;

  /**
   * String for complete mail message as it is sent through SMTP
   */
  private String message;

  Conversation(final InputStream in, final OutputStream out) {
    this.in = new BufferedReader(new InputStreamReader(in)); //support ascii only, ignore encoding for now
    this.out = new BufferedWriter(new OutputStreamWriter(out));
  }

  /**
   * Sends a SMTP line to receiver (appending <CR><LF> to the end)
   *
   * @param s string to be written
   * @throws IOException
   */
  final void sendLine(final String s) throws IOException {
    out.write(s);
    out.write("\r\n");
    out.flush();
  }

  /**
   * Reads and returns SMTP line from receiver (using <CR><LF> as line separator)
   *
   * @return complete SMTP line
   * @throws IOException
   */
  final String receiveLine() throws IOException {
    return Utils.read(in, Utils.COMMAND_DELIMITER);
  }

  /**
   * Reads and returns SMTP message body (using <CR><LF>.<CR><LF> as end-of-data symbol)
   *
   * @return complete SMTP message body
   * @throws IOException
   */
  final String receiveBody() throws IOException {
    return Utils.read(in, Utils.DATA_DELIMITER);
  }

  /**
   * Sends SMTP mail body to the receiver (using <CR><LF>.<CR><LF> as end-of-data symbol)
   *
   * @param s message body
   */
  final void sendBody(final String s) throws IOException {
    out.write(s);
    out.write("\r\n.\r\n");
    out.flush();
  }

  void setMessage(String message) {
    this.message = message;
  }

  String getMessage() {
    return message;
  }

}
