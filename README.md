Java Mock SMTP Server
=====================

Simple implementation of SMTP server, mainly for testing purposes. 

How To Use
==========
First, create an SMTP server instance `(com.openag.mocksmtp.Server)` using no-args constructor:

<code>
   Server server = new Server();
</code>

Then server must be started:

<code>
   server.start();
</code>

The start method call will actually open the server socket and prepare necessary resources

In order to access messages, obtain MailStore instance from Server:

<code>
   MailStore store = server.getMailStore()
</code>
