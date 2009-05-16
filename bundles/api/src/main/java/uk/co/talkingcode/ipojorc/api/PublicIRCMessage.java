package uk.co.talkingcode.ipojorc.api;

public class PublicIRCMessage extends AbstractIncomingIRCMessage {

  public PublicIRCMessage(String sender, String login, String hostname,
      String message) {
    super(sender, login, hostname, message);
  }

  @Override
  public IRCMessage dispatchToCommand(IRCCommand command) {
    return command.handlePublicMessage(this);
  }

}
