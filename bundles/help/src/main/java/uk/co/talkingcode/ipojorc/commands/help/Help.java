package uk.co.talkingcode.ipojorc.commands.help;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

import uk.co.talkingcode.ipojorc.api.AbstractPrefixCommand;
import uk.co.talkingcode.ipojorc.api.IRCCommand;
import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;

@Provides
@Component(name="HelpCommandProvider", architecture=true)
public class Help extends AbstractPrefixCommand
{
  public Help() {
    super("help");
  }

  @Requires
  private IRCCommand[] commands;

  public String getDescription() {
    return "!help - Displays this help message";
  }

  @Override
  protected IRCMessage handleCommand(IRCMessage message, String data) {
    IRCMessage result = message.createReply("Help:");
    for (int i=0; i<commands.length; i++)
    {
      if (commands[i] == null)
        continue;
      result.appendMessage(message.createReply(commands[i].getDescription()));
    }
    return result;
  }
  
}
