package uk.co.talkingcode.ipojorc.api;

import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.PrivateIRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.PublicIRCMessage;

public interface IRCCommand {
  public IRCMessage handlePublicMessage(PublicIRCMessage message);
  public String getDescription();
  public IRCMessage handlePrivateMessage(PrivateIRCMessage ircMessage);
}
