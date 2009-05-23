package uk.co.talkingcode.ipojorc.api;

import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;

public abstract class AbstractPrefixCommand implements IRCCommand {

  private String prefix;

  protected IRCMessage detectAndProcessPrefixCommand(IRCMessage message)
  {
    if (!message.isProcessed())
    {
      String messageText = message.getMessage();
      String[] parts = messageText.split(" ", 2);
      if (parts[0].equals("!" + prefix))
      {
        message.setProcessed(true);
        return handleCommand(message, (parts.length == 2 ? parts[1] : null));
      }
    }
    return null;    
  }
  
  public IRCMessage handlePublicMessage(IRCMessage message) {
    return detectAndProcessPrefixCommand(message);
  }

  public IRCMessage handlePrivateMessage(IRCMessage message) {
    return detectAndProcessPrefixCommand(message);
  }

  protected abstract IRCMessage handleCommand(IRCMessage message, String data);
  
  protected AbstractPrefixCommand(String prefix)
  {
    this.prefix = prefix;
  }
}
