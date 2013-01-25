package com.openag.mocksmtp;

import java.io.IOException;
import java.io.Reader;

/**
 * Collection of static utility methods used within the package
 * <p/>
 *
 * @author Andrei Maus
 */
class Utils {
  static final String COMMAND_DELIMITER = "\r\n";
  static final String DATA_DELIMITER = "\r\n.\r\n";

  private Utils() {
  }

  /**
   * Reads predefined amount of characters from the provided reader
   *
   * @param in     {@link Reader} instance
   * @param length the number of characters to read, must be greater than 0
   * @return string that contains requested amount of characters extracted from provided reader instance. The result can
   *         contain less characters than requested (down to 0), if end of stream was reached.
   * @throws IOException
   */
  static String read(final Reader in, final int length) throws IOException {
    if (length <= 0 || in == null) {
      return "";
    }

    final char[] buf = new char[length];

    int count = 0;

    int i;
    while (count < length && (i = in.read()) != -1) {
      buf[count++] = (char) i;
    }

    return new String(buf, 0, count);
  }

  /**
   * Reading characters from provided reader until end token is reached. The result returned as singe string, end token
   * excluded.
   *
   * @param in  {@link Reader} instance; must support mark/reset operations
   * @param end string token that indicates end of read operation
   * @return all extracted characters as single string, end delimiter excluded; can return less characters if end of
   *         stream was reached
   * @throws IOException
   */
  static String read(final Reader in, final String end) throws IOException {
    if (!in.markSupported()) {
      throw new IllegalArgumentException("mark/reset must be supported by provided reader");
    }

    final StringBuilder sb = new StringBuilder();

    int i;
    while ((i = in.read()) != -1) {

      final char c = (char) i;

      if (c == end.charAt(0)) { //first position match
        in.mark(end.length());

        final String buf = (c + read(in, end.length() - 1)); //read the rest of the buffer

        if (end.equals(buf)) {
          break; //exact match to end token, exiting the main loop...
        }

        in.reset(); //false alarm, end token is not matched fully
      }

      sb.append(c);
    }

    return sb.toString();
  }

  static boolean isEmpty(final String s) {
    return s == null || "".equals(s);
  }

  static boolean isNotEmpty(final String s) {
    return !isEmpty(s);
  }

  static String substringBefore(final String s, final String separator) {
    if (isEmpty(s)) {
      return "";
    }

    final int separatorPosition = s.indexOf(separator);
    if (separatorPosition > 0) {
      return s.substring(0, separatorPosition);
    }

    return s;
  }

  static String substringAfter(final String s, final String separator) {
    if (isEmpty(s)) {
      return "";
    }

    final int separatorPosition = s.indexOf(separator);
    if (separatorPosition > 0) {
      return s.substring(separatorPosition + separator.length());
    }

    return s;
  }

  static String readAll(final Reader reader) throws IOException {
    final StringBuilder sb = new StringBuilder();

    int c;
    while ((c = reader.read()) != -1) {
      sb.append((char) c);
    }

    return sb.toString();
  }

  static int toInt(final String s) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

}
