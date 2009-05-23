package uk.co.talkingcode.ipojorc.api.messages;

import uk.co.talkingcode.ipojorc.api.IRCCommand;

public abstract class AbstractIncomingIRCMessage extends IRCMessage {

  public AbstractIncomingIRCMessage(String sender, String login,
      String hostname, String message) {
    super(sender, login, hostname, message);
  }

  public abstract IRCMessage dispatchToCommand(IRCCommand command);
  
}
