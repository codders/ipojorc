package uk.co.talkingcode.ipojorc.commands.define;

import uk.co.talkingcode.ipojorc.api.IRCCommand;
import uk.co.talkingcode.ipojorc.api.IRCMessage;

public class Define implements IRCCommand {

  public IRCMessage handleCommand(IRCMessage message) {
    if (!message.isProcessed() && 
        message != null &&
        message.getMessage().startsWith("!define "))
    {
      message.setProcessed(true);

      IRCMessage define = new IRCMessage();
      UrbanLookup ul = new UrbanLookup(message.getMessage().substring(8));
      define.setChannel(message.getChannel());
      define.setMessage(ul.getDefinition());
      return define;
    }
    return null;
  }

}
