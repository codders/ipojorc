package uk.co.talkingcode.ipojorc.commands.define;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;

import uk.co.talkingcode.ipojorc.api.AbstractPrefixCommand;
import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;

@Provides
@Component(name="ExampleCommandProvider", architecture=true)
public class Example extends AbstractPrefixCommand {

  public Example() {
    super("example");
  }

  public String getDescription() {
    return "!example - Retrieves an Urban Dictionary example";
  }

  @Override
  protected IRCMessage handleCommand(IRCMessage message, String query) {
    UrbanLookup ul = new UrbanLookup(query);
    return message.createReply("*" + query + "* " + ul.getExample());
  }
  
}
