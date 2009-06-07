package uk.co.talkingcode.ipojorc.ruby;

import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;

public abstract class AbstractRubyWrapper {

  protected Ruby runtime;
  protected IRubyObject rubyObject;

  public AbstractRubyWrapper(IRubyObject rubyObject, Ruby runtime) {
    this.rubyObject = rubyObject;
    this.runtime = runtime;
  }

  protected IRubyObject rubyString(String javaString) {
    return runtime.newString(javaString);
  }

  protected IRCMessage makeRubyCall(String methodName, IRCMessage ircMessage) {
    IRubyObject[] ircMessageArgs = new IRubyObject[] { JavaEmbedUtils.javaToRuby(runtime, ircMessage) };
    IRubyObject result = rubyObject.callMethod(runtime.getCurrentContext(), methodName, ircMessageArgs);
    if (result != null && !result.isNil()) {
      return (IRCMessage) JavaEmbedUtils.rubyToJava(result);
    }
    return null;
  }

}
