package uk.co.talkingcode.ipojorc.core;

import org.jibble.pircbot.PircBot;

import uk.co.talkingcode.ipojorc.api.AbstractIncomingIRCMessage;
import uk.co.talkingcode.ipojorc.api.IRCCommand;
import uk.co.talkingcode.ipojorc.api.IRCMessage;
import uk.co.talkingcode.ipojorc.api.PrivateIRCMessage;
import uk.co.talkingcode.ipojorc.api.PublicIRCMessage;

class Bot extends PircBot implements Runnable {
  
  private boolean stop = true;
  private IRCCommand[] commands;
  private String channel;
  private String server;
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
  
  public void starting() {
    System.out.println("Starting");
     Thread t = new Thread(this);     
     stop = false;     
     t.start(); 
  }

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
    System.out.println(commands.length + " handlers");
    for (int i=0; i<commands.length; i++)
    {
      IRCMessage reply = commands[i].handlePublicMessage(ircMessage);
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

}

