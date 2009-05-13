package uk.co.talkingcode.ipojorc.commands.define;

import uk.co.talkingcode.ipojorc.api.AbstractPrefixCommand;
import uk.co.talkingcode.ipojorc.api.IRCMessage;

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
