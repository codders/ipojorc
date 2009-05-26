package uk.co.talkingcode.ipojorc.core;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.jibble.pircbot.PircBot;

import uk.co.talkingcode.ipojorc.api.IRCCommand;
import uk.co.talkingcode.ipojorc.api.IRCStatusWatcher;
import uk.co.talkingcode.ipojorc.api.messages.AbstractIncomingIRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.AbstractStatusMessage;
import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.JoinMessage;
import uk.co.talkingcode.ipojorc.api.messages.PartMessage;
import uk.co.talkingcode.ipojorc.api.messages.PrivateIRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.PublicIRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.QuitMessage;

@Component(name="IrcBot", immediate=true, architecture=true)
@Provides
class Bot extends PircBot implements Runnable {
  
  private boolean stop = true;
  
  @Requires(optional=true, nullable=true)
  private IRCCommand[] commands;
  @Requires(optional=true, nullable=true)
  private IRCStatusWatcher[] watchers;
  
  @Property(mandatory=true)
  private String channel;
  @Property(mandatory=true)
  private String server;
  @Property(mandatory=true)
  private String nick;
  
  public Bot() {
    super();
  }
  
  public void run() {
    System.out.println("Starting");
    System.out.println("Server: " + server);
    System.out.println("Channel: " + channel);
    setName(nick);
    setVerbose(true);
    try {
      connect(server);
    } catch (Exception e) {
      System.err.println("Unable to connect");
      e.printStackTrace();
    }
    joinChannel(channel); 
    while (!stop) {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        //
      }
    }
    disconnect();
  }

  @Validate
  public void starting() {
    System.out.println("Starting");
     Thread t = new Thread(this);     
     stop = false;     
     t.start(); 
  }

  @Invalidate
  public void stopping() {    
    System.out.println("Stoppingh");
      stop = true; 
  }

  @Override
  protected void onMessage(String channel, String sender, String login,
      String hostname, String message) {
    System.out.println("Processing: " + message);
    AbstractIncomingIRCMessage ircMessage = new PublicIRCMessage(sender, login, hostname, message);
    ircMessage.setChannel(channel);
    dispatchToCommands(ircMessage);
  }

  private void dispatchToCommands(AbstractIncomingIRCMessage ircMessage) {
    if (commands == null)
      return;
    System.out.println(commands.length + " handlers");
    for (int i=0; i<commands.length; i++)
    {
      if (commands[i] == null)
        continue;
      IRCMessage reply = ircMessage.dispatchToCommand(commands[i]);
      while (reply != null)
      {
        reply = processLines(reply);
      }
    }
  }

  private void dispatchToStatusWatchers(AbstractStatusMessage ircMessage) {
    if (watchers == null)
      return;
    System.out.println(watchers.length + " handlers");
    for (int i=0; i<watchers.length; i++)
    {
      IRCMessage reply = ircMessage.dispatchToStatusWatcher(watchers[i]);
      while (reply != null)
      {
        reply = processLines(reply);
      }
    }
  }

  private IRCMessage processLines(IRCMessage reply) {
    String replyString = reply.getMessage();
    String[] parts = replyString.split("\n\r|\r|\n");
    for (String part : parts)
    {
      sendMessage(reply.getChannel(), part);
    }
    return reply.getNextMessage();
  }

  @Override
  protected void onPrivateMessage(String sender, String login, String hostname,
      String message) {
    AbstractIncomingIRCMessage ircMessage = new PrivateIRCMessage(sender, login, hostname, message);
    dispatchToCommands(ircMessage);
  }

  @Override
  protected void onJoin(String channel, String sender, String login,
      String hostname) {
    AbstractStatusMessage ircMessage = new JoinMessage(sender, login, hostname);
    dispatchToStatusWatchers(ircMessage);
  }

  @Override
  protected void onPart(String channel, String sender, String login,
      String hostname) {
    AbstractStatusMessage ircMessage = new PartMessage(sender, login, hostname);
    dispatchToStatusWatchers(ircMessage);
  }

  @Override
  protected void onQuit(String sourceNick, String sourceLogin,
      String sourceHostname, String reason) {
    AbstractStatusMessage ircMessage = new QuitMessage(sourceNick, sourceLogin, sourceHostname, reason);
    dispatchToStatusWatchers(ircMessage);
  }

}

