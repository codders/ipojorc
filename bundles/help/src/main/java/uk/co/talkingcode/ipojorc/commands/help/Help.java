package uk.co.talkingcode.ipojorc.commands.help;

import uk.co.talkingcode.ipojorc.api.AbstractPrefixCommand;
import uk.co.talkingcode.ipojorc.api.IRCCommand;
import uk.co.talkingcode.ipojorc.api.IRCMessage;

public class Help extends AbstractPrefixCommand
{
  public Help() {
    super("help");
  }

  private IRCCommand[] commands;

  public String getDescription() {
    return "!help - Displays this help message";
  }

  @Override
  protected IRCMessage handleCommand(IRCMessage message, String data) {
    IRCMessage result = message.createReply("Help:");
    for (int i=0; i<commands.length; i++)
    {
      result.appendMessage(message.createReply(commands[i].getDescription()));
    }
    return result;
  }
  
}
