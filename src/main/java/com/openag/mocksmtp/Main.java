package com.openag.mocksmtp;

import java.io.IOException;

/**
 * Main method to start SMTP server as standalone application
 *
 * @author Andrei Maus
 */
public class Main {

  public static void main(String[] args) throws IOException {
    printUsage();

    final Server server = new Server();
    server.setThreaded(true); //default mode for standalone app

    for (final String arg : args) {
      if ("--host".equals(key(arg))) {
        server.setHost(value(arg));
      }
      if ("--port".equals(key(arg))) {
        server.setPort(Utils.toInt(value(arg)));
      }
      if ("--threaded".equals(key(arg))) {
        server.setThreaded(Boolean.parseBoolean(value(arg)));
      }
    }

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          server.stop();
        } catch (IOException e) {
          //ignore
        }
      }
    });

    System.out.println("Starting Mock SMTP Server: " + server);

    server.start();
  }

  private static String key(String s) {
    return Utils.substringBefore(s, "=").trim();
  }

  private static String value(String s) {
    return Utils.substringAfter(s, "=").trim();
  }

  private static void printUsage() {
    System.out.println("Main [--host=HOST] [--port=PORT] [--threaded=TRUE/FALSE]");
  }
}
