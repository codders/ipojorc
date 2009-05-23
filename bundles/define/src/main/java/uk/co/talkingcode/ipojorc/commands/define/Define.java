package uk.co.talkingcode.ipojorc.commands.define;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;

import uk.co.talkingcode.ipojorc.api.AbstractPrefixCommand;
import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;

@Provides
@Component(name="DefineCommandProvider", architecture=true)
public class Define extends AbstractPrefixCommand {

  public Define() {
    super("define");
  }

  public String getDescription() {
    return "!define - Retrieves an Urban Dictionary definition";
  }

  @Override
  protected IRCMessage handleCommand(IRCMessage message, String data) {
    UrbanLookup ul = new UrbanLookup(data);
    return message.createReply("*" + data + "* " + ul.getDefinition());
  }
}
