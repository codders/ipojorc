package uk.co.talkingcode.ipojorc.api.messages;

import uk.co.talkingcode.ipojorc.api.IRCStatusWatcher;

public class JoinMessage extends AbstractStatusMessage {

  public JoinMessage(String sender, String login, String hostname) {
    super(sender, login, hostname);
  }

  @Override
  public IRCMessage dispatchToStatusWatcher(IRCStatusWatcher watcher) {
    return watcher.handleJoin(this);
  }

}
