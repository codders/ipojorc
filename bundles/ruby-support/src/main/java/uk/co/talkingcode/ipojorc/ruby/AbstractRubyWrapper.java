package uk.co.talkingcode.ipojorc.ruby;

import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;

import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;

public abstract class AbstractRubyWrapper {

  protected Ruby runtime;
  protected IRubyObject rubyObject;

  public AbstractRubyWrapper(IRubyObject rubyObject, Ruby runtime) {
    this.rubyObject = rubyObject;
    this.runtime = runtime;
  }

  protected IRubyObject[] argsForMessage(IRCMessage ircMessage) {
    return new IRubyObject[] { rubyString(ircMessage.getChannel()), rubyString(ircMessage.getSender().getNick()),
        rubyString(ircMessage.getMessage()) };
  }

  protected IRubyObject rubyString(String javaString) {
    return runtime.newString(javaString);
  }

  protected IRCMessage makeRubyCall(String methodName, IRCMessage ircMessage) {
    IRubyObject[] ircMessageArgs = argsForMessage(ircMessage);
    IRubyObject result = rubyObject.callMethod(runtime.getCurrentContext(), methodName, ircMessageArgs);
    if (result != null && !result.isNil()) {
      return ircMessage.createReply(result.convertToString().asJavaString());
    }
    return null;
  }

}
