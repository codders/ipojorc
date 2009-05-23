package uk.co.talkingcode.ipojorc.api;

import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.JoinMessage;
import uk.co.talkingcode.ipojorc.api.messages.PartMessage;
import uk.co.talkingcode.ipojorc.api.messages.QuitMessage;

public interface IRCStatusWatcher {

  public IRCMessage handleJoin(JoinMessage message);
  public IRCMessage handlePart(PartMessage message);
  public IRCMessage handleQuit(QuitMessage message);
  
}
