package uk.co.talkingcode.ipojorc.commands.echo;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;

import uk.co.talkingcode.ipojorc.api.AbstractPrefixCommand;
import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;

@Provides
@Component(name="EchoCommandProvider", architecture=true)
public class Echo extends AbstractPrefixCommand {

  public Echo() {
    super("echo");
  }

  public String getDescription() {
    return "!echo - Echoes the entered text";
  }

  @Override
  protected IRCMessage handleCommand(IRCMessage message, String data) {
    return message.createReply(data);
  }

}
