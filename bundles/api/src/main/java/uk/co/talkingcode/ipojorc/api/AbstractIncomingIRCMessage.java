package uk.co.talkingcode.ipojorc.api;

public abstract class AbstractIncomingIRCMessage extends IRCMessage {

  public AbstractIncomingIRCMessage(String sender, String login,
      String hostname, String message) {
    super(sender, login, hostname, message);
  }

  public abstract IRCMessage dispatchToCommand(IRCCommand command);
  
}
