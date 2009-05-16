package uk.co.talkingcode.ipojorc.api;

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
