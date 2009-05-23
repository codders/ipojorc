package uk.co.talkingcode.ipojorc.api.messages;

import java.util.Date;

import uk.co.talkingcode.ipojorc.api.IRCUser;

public class IRCMessage {
  String channel;
  IRCUser sender;
  String message;
  Date created;
  boolean processed;
  private IRCMessage nextMessage;

  public IRCMessage() {
    created = new Date();
  }
  
  public IRCMessage(String sender, String login, String hostname,
      String message) {
    this();
    this.sender = new IRCUser(sender, login, hostname);
    this.message = message;
  }

  public void appendMessage(IRCMessage message)
  {
    if (nextMessage != null)
    {
      IRCMessage last = nextMessage;
      while (last.getNextMessage() != null)
      {
        last = last.getNextMessage();
      }
      last.appendMessage(message);
    }
    else
    {
      nextMessage = message;
    }
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public IRCUser getSender() {
    return sender;
  }

  public void setSender(IRCUser sender) {
    this.sender = sender;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isProcessed() {
    return processed;
  }

  public void setProcessed(boolean processed) {
    this.processed = processed;
  }

  public Date getCreated() {
    return created;
  }

  public IRCMessage getNextMessage() {
    return nextMessage;
  }

  public IRCMessage createReply(String message) {
    IRCMessage reply = new IRCMessage();
    if (channel != null)
    {
      reply.setChannel(channel);
    }
    else
    {
      reply.setChannel(sender.getNick());
    }
    reply.setMessage(message);
    return reply;
  }

}
