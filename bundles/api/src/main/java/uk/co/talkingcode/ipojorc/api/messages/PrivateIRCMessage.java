package uk.co.talkingcode.ipojorc.api.messages;

import uk.co.talkingcode.ipojorc.api.IRCCommand;

public class PrivateIRCMessage extends AbstractIncomingIRCMessage {

  public PrivateIRCMessage(String sender, String login, String hostname,
      String message) {
    super(sender, login, hostname, message);
  }

  @Override
  public IRCMessage dispatchToCommand(IRCCommand command) {
    return command.handlePrivateMessage(this);
  }

}
