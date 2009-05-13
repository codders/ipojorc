package uk.co.talkingcode.ipojorc.commands.help;

import uk.co.talkingcode.ipojorc.api.IRCCommand;
import uk.co.talkingcode.ipojorc.api.IRCMessage;

public class Help implements IRCCommand
{
  private IRCCommand[] commands;

  public IRCMessage handleCommand(IRCMessage message) {
    if (message.getMessage().equals("!help"))
    {
      StringBuilder builder = new StringBuilder("Help:");
      for (int i=0; i<commands.length; i++)
      {
        builder.append("\n  " + commands[i].toString());
      }
      IRCMessage result = new IRCMessage();
      result.setChannel(message.getChannel());
      result.setMessage(builder.toString());
      return result;
    }
    return null;
  }
}
