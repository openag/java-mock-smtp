package com.openag.mocksmtp;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandTest {

  @Test
  public void testParse() throws Exception {
    Command command = Command.parse("HELO relay.example.org");
    assertSame(Command.Type.HELO, command.getType());
    assertEquals("relay.example.org", command.getParameter());

    command = Command.parse("MAIL FROM:<bob@example.org>");
    assertSame(Command.Type.MAIL, command.getType());
    assertEquals("FROM:<bob@example.org>", command.getParameter());

    command = Command.parse("RCPT TO:<alice@example.com>");
    assertSame(Command.Type.RCPT, command.getType());
    assertEquals("TO:<alice@example.com>", command.getParameter());

    command = Command.parse("DATA");
    assertSame(Command.Type.DATA, command.getType());
    assertNull(command.getParameter());

    command = Command.parse("QUIT");
    assertSame(Command.Type.QUIT, command.getType());
    assertNull(command.getParameter());

    command = Command.parse(null);
    assertSame(Command.Type.UNKNOWN, command.getType());
    assertNull(command.getParameter());

    command = Command.parse("");
    assertSame(Command.Type.UNKNOWN, command.getType());
    assertNull(command.getParameter());

    command = Command.parse("DUMMY");
    assertSame(Command.Type.UNKNOWN, command.getType());
    assertNull(command.getParameter());
  }

}
