package uk.co.talkingcode.ipojorc.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.talkingcode.ipojorc.api.messages.AbstractIncomingIRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.PrivateIRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.PublicIRCMessage;

public abstract class AbstractURLWatchingPrefixCommand extends
    AbstractPrefixCommand {

  private Pattern pattern = Pattern
      .compile(".*(http://[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]+).*");
  protected String lastUrl;

  protected IRCMessage handleMessage(AbstractIncomingIRCMessage message)
  {
    Matcher matcher = pattern.matcher(message.getMessage());
    IRCMessage commandResult = super.detectAndProcessPrefixCommand(message);
    if (matcher.matches()) {
      lastUrl = matcher.group(1);
      if (commandResult == null) {
        return handleURL(message, lastUrl);
      }
    }
    return commandResult;    
  }
  
  public IRCMessage handlePublicMessage(PublicIRCMessage message) {
    return handleMessage(message);
  }

  public IRCMessage handlePrivateMessage(PrivateIRCMessage message) {
    return handleMessage(message);
  }

  protected abstract IRCMessage handleURL(IRCMessage message, String url);
  protected abstract IRCMessage handleCommand(IRCMessage message, String data);

  protected AbstractURLWatchingPrefixCommand(String prefix) {
    super(prefix);
  }

}
