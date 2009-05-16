package uk.co.talkingcode.ipojorc.api;

public abstract class AbstractPrefixCommand implements IRCCommand {

  private String prefix;

  public IRCMessage handlePublicMessage(IRCMessage message) {
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

  public IRCMessage handlePrivateMessage(IRCMessage message) {
    return handlePublicMessage(message);
  }

  protected abstract IRCMessage handleCommand(IRCMessage message, String data);
  
  protected AbstractPrefixCommand(String prefix)
  {
    this.prefix = prefix;
  }
}
