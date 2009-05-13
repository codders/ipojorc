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
      String query = message.getMessage().substring(8));
      IRCMessage define = new IRCMessage();
      UrbanLookup ul = new UrbanLookup(query);
      define.setChannel(message.getChannel());
      define.setMessage("*" + query + "* " + ul.getDefinition());
      return define;
    }
    return null;
  }

}
