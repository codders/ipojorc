package uk.co.talkingcode.ipojorc.api;

import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;


public abstract class AbstractChatWatchingPrefixCommand extends AbstractPrefixCommand implements IRCStatusWatcher {

  public IRCMessage handlePublicMessage(IRCMessage message) {
    IRCMessage result = handlePublicChat(message);
    if (result != null)
    {
      return result;
    }
    return super.handlePublicMessage(message);
  }

  public IRCMessage handlePrivateMessage(IRCMessage message) {
    IRCMessage result = handlePrivateChat(message);
    if (result != null)
    {
      return result;
    }
    return super.handlePrivateMessage(message);
  }

  protected abstract IRCMessage handlePrivateChat(IRCMessage message);
  protected abstract IRCMessage handlePublicChat(IRCMessage message);

  protected AbstractChatWatchingPrefixCommand(String prefix) {
    super(prefix);
  }

}
