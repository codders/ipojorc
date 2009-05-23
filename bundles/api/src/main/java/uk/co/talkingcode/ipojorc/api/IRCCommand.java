package uk.co.talkingcode.ipojorc.api;

import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;

public interface IRCCommand {
  public IRCMessage handlePublicMessage(IRCMessage message);
  public String getDescription();
  public IRCMessage handlePrivateMessage(IRCMessage ircMessage);
}
