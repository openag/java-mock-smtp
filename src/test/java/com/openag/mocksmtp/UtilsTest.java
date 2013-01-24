package com.openag.mocksmtp;

import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

  @Test
  public void testReadWithLength() throws Exception {
    StringReader in = new StringReader("abcdef");
    assertEquals("", Utils.read(in, 0));
    assertEquals("a", Utils.read(in, 1));
    assertEquals("bcd", Utils.read(in, 3));
    assertEquals("ef", Utils.read(in, 3));
    assertEquals("", Utils.read(in, 3));
  }

  @Test
  public void testReadWithEndToken() throws Exception {
    StringReader in = new StringReader("abc\n");
    assertEquals("abc", Utils.read(in, "\n"));

    in = new StringReader("abc\ndef\n");
    assertEquals("abc", Utils.read(in, "\n"));
    assertEquals("def", Utils.read(in, "\n"));

    in = new StringReader(
        "HELO relay.example.org\r\n" +
            "MAIL FROM:<bob@example.org>\r\n" +
            "DATA\r\n" +
            "datadatadata\r\n.\r\n" +
            "QUIT\r\n");
    assertEquals("HELO relay.example.org", Utils.read(in, "\r\n"));
    assertEquals("MAIL FROM:<bob@example.org>", Utils.read(in, "\r\n"));
    assertEquals("DATA", Utils.read(in, "\r\n"));
    assertEquals("datadatadata", Utils.read(in, "\r\n.\r\n"));
    assertEquals("QUIT", Utils.read(in, "\r\n"));

    in = new StringReader("data\r\ndata\r\ndata\r\n.\r\n");
    assertEquals("data\r\ndata\r\ndata", Utils.read(in, "\r\n.\r\n"));
  }

  @Test
  public void testSubstring() throws Exception {
    assertEquals("From", Utils.substringBefore("From: sender@test", ":"));
    assertEquals("From", Utils.substringBefore("From: sender@test", ": "));
    assertEquals("From: sender@test", Utils.substringBefore("From: sender@test", "1"));

    assertEquals(" sender@test", Utils.substringAfter("From: sender@test", ":"));
    assertEquals("sender@test", Utils.substringAfter("From: sender@test", ": "));

  }
}
