package uk.co.talkingcode.ipojorc.commands.echo;

import uk.co.talkingcode.ipojorc.api.IRCCommand;
import uk.co.talkingcode.ipojorc.api.IRCMessage;

public class Echo implements IRCCommand {

  public IRCMessage handleCommand(IRCMessage message) {
    if (!message.isProcessed() && 
        message.getMessage().startsWith("!echo"))
    {
      message.setProcessed(true);

      IRCMessage echo = new IRCMessage();
      echo.setChannel(message.getChannel());
      echo.setMessage(message.getMessage().substring(6));
      return echo;
    }
    return null;
  }

}
