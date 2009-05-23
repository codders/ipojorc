package uk.co.talkingcode.ipojorc.api.messages;

import uk.co.talkingcode.ipojorc.api.IRCStatusWatcher;
import uk.co.talkingcode.ipojorc.api.IRCUser;

public abstract class AbstractStatusMessage extends IRCMessage {

  public AbstractStatusMessage(String sender, String login, String hostname) {
    super();
    this.sender = new IRCUser(sender, login, hostname);
  }

  public abstract IRCMessage dispatchToStatusWatcher(IRCStatusWatcher watcher);
  
}
