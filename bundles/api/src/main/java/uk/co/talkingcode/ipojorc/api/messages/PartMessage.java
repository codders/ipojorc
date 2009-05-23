package uk.co.talkingcode.ipojorc.api.messages;

import uk.co.talkingcode.ipojorc.api.IRCStatusWatcher;

public class PartMessage extends AbstractStatusMessage {

  public PartMessage(String sender, String login, String hostname) {
    super(sender, login, hostname);
  }

  @Override
  public IRCMessage dispatchToStatusWatcher(IRCStatusWatcher watcher) {
    return watcher.handlePart(this);
  }

}
