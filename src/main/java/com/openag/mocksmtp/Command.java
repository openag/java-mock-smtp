package com.openag.mocksmtp;

/**
 * SMTP command issued by the client; the command can have optional parameter according to SMTP specification
 *
 * @author Andrei Maus
 */
class Command {
  private final Type type;
  private final String parameter;

  private Command(Type type, String parameter) {
    this.type = type;
    this.parameter = parameter;
  }

  Command(Type type) {
    this(type, null);
  }

  /**
   * Creates {@link Command} object from provided SMTP command line
   *
   * @param s SMTP command line
   * @return {@link Command} instance, encapsulated command type and optional parameter value
   */
  static Command parse(final String s) {
    if (s != null) {
      final String line = s.trim();

      if (line.length() > 0) {
        final int separatorPosition = line.indexOf(' ');

        final String type;
        final String parameter;

        if (separatorPosition > 0) {
          type = line.substring(0, separatorPosition);
          parameter = line.substring(separatorPosition + 1); //don't include leading space
        } else {
          type = line;
          parameter = null;
        }

        return new Command(Type.parse(type), parameter);
      }
    }

    return new Command(Type.UNKNOWN);
  }

  public Type getType() {
    return type;
  }

  public String getParameter() {
    return parameter;
  }

  /**
   * Supported SMTP commands
   */
  enum Type {
    HELO,

    EHLO,

    MAIL,

    RCPT,

    DATA,

    QUIT,

    UNKNOWN;

    private static Type parse(final String s) {
      if (s != null) {
        try {
          return Type.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
          //ignore, will default to UNKNOWN
        }
      }
      return UNKNOWN;
    }
  }
}
