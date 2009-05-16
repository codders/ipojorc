package uk.co.talkingcode.ipojorc.api;

public interface IRCCommand {
  public IRCMessage handlePublicMessage(IRCMessage message);
  public String getDescription();
  public IRCMessage handlePrivateMessage(IRCMessage ircMessage);
}
