package uk.co.talkingcode.ipojorc.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractURLWatchingPrefixCommand extends AbstractPrefixCommand {

  private Pattern pattern = Pattern
      .compile(".*(http://[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]+).*");
  protected String lastUrl;

  public IRCMessage handlePublicMessage(IRCMessage message) {
    Matcher matcher = pattern.matcher(message.getMessage());
    if (matcher.matches()) {
      lastUrl = matcher.group(1);
    }
    return super.handlePublicMessage(message);
  }

  public IRCMessage handlePrivateMessage(IRCMessage message) {
    return handlePublicMessage(message);
  }

  protected abstract IRCMessage handleCommand(IRCMessage message, String data);

  protected AbstractURLWatchingPrefixCommand(String prefix) {
    super(prefix);
  }

}
