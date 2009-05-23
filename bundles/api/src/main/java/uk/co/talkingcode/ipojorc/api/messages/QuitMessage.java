package uk.co.talkingcode.ipojorc.api.messages;

import uk.co.talkingcode.ipojorc.api.IRCStatusWatcher;

public class QuitMessage extends AbstractStatusMessage {

  public QuitMessage(String nick, String login,
      String hostname, String reason) {
    super(nick, login, hostname);
    this.message = reason;
  }

  @Override
  public IRCMessage dispatchToStatusWatcher(IRCStatusWatcher watcher) {
    return watcher.handleQuit(this);
  }

}
