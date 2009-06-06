package uk.co.talkingcode.ipojorc.ruby;

import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;

import uk.co.talkingcode.ipojorc.api.IRCCommand;
import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.PrivateIRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.PublicIRCMessage;

public class IRCCommandWrapper extends AbstractRubyWrapper implements IRCCommand {

  public IRCCommandWrapper(IRubyObject rubyObject, Ruby runtime) {
    super(rubyObject, runtime);
  }

  @Override
  public String getDescription() {
    IRubyObject result = rubyObject.callMethod(runtime.getCurrentContext(), "getDescription");
    return result.convertToString().asJavaString();
  }

  @Override
  public IRCMessage handlePrivateMessage(PrivateIRCMessage ircMessage) {
    return makeRubyCall("handlePrivateMessage", ircMessage);
  }

  @Override
  public IRCMessage handlePublicMessage(PublicIRCMessage message) {
    return makeRubyCall("handlePublicMessage", message);
  }

}
