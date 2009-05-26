package uk.co.talkingcode.ipojorc.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;

public abstract class AbstractURLWatchingPrefixCommand extends
    AbstractPrefixCommand {

  private Pattern pattern = Pattern
      .compile(".*(http://[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]+).*");
  protected String lastUrl;

  public IRCMessage handlePublicMessage(IRCMessage message) {
    Matcher matcher = pattern.matcher(message.getMessage());
    IRCMessage commandResult = super.handlePublicMessage(message);
    if (matcher.matches()) {
      lastUrl = matcher.group(1);
      if (commandResult == null) {
        return handleURL(message, lastUrl);
      }
    }
    return commandResult;
  }

  public IRCMessage handlePrivateMessage(IRCMessage message) {
    return handlePublicMessage(message);
  }

  protected abstract IRCMessage handleURL(IRCMessage message, String url);
  protected abstract IRCMessage handleCommand(IRCMessage message, String data);

  protected AbstractURLWatchingPrefixCommand(String prefix) {
    super(prefix);
  }

}
