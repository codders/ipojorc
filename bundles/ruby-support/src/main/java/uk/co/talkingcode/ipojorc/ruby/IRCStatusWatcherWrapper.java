package uk.co.talkingcode.ipojorc.ruby;

import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;

import uk.co.talkingcode.ipojorc.api.IRCStatusWatcher;
import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.JoinMessage;
import uk.co.talkingcode.ipojorc.api.messages.PartMessage;
import uk.co.talkingcode.ipojorc.api.messages.QuitMessage;

public class IRCStatusWatcherWrapper extends AbstractRubyWrapper implements IRCStatusWatcher {

  public IRCStatusWatcherWrapper(IRubyObject commandInstance, Ruby runtime) {
    super(commandInstance, runtime);
  }

  @Override
  public IRCMessage handleJoin(JoinMessage message) {
    return makeRubyCall("handleJoin", message);
  }

  @Override
  public IRCMessage handlePart(PartMessage message) {
    return makeRubyCall("handlePart", message);
  }

  @Override
  public IRCMessage handleQuit(QuitMessage message) {
    return makeRubyCall("handleQuit", message);
  }

}
