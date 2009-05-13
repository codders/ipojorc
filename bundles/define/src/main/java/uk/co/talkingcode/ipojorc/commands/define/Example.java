package uk.co.talkingcode.ipojorc.commands.define;

import uk.co.talkingcode.ipojorc.api.AbstractPrefixCommand;
import uk.co.talkingcode.ipojorc.api.IRCMessage;

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
