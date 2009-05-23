package uk.co.talkingcode.ipojorc.commands.scrollback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;

import uk.co.talkingcode.ipojorc.api.AbstractChatWatchingPrefixCommand;
import uk.co.talkingcode.ipojorc.api.IRCUser;
import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.JoinMessage;
import uk.co.talkingcode.ipojorc.api.messages.PartMessage;
import uk.co.talkingcode.ipojorc.api.messages.QuitMessage;

@Provides
@Component(name="ScrollbackCommandProvider", architecture=true)
public class Scrollback extends AbstractChatWatchingPrefixCommand {

  private Pattern register = Pattern.compile("register");
  private Pattern unregister = Pattern.compile("unregister");
  private Pattern help = Pattern.compile("help");
  private CircularBuffer<IRCMessage> lineBuffer;
  private Set<IRCUser> watched = new HashSet<IRCUser>();
  private static DateFormat dateFormat = new SimpleDateFormat("HH:MM:ss");
  private static Map<IRCUser,Date> lastSeen = new HashMap<IRCUser,Date>();
  private static final int BUFFER_SIZE = 10000;
  
  public Scrollback() {
    super("scrollback");
    lineBuffer = new CircularBuffer<IRCMessage>(BUFFER_SIZE);
  }

  public String getDescription() {
    return "!scrollback - type \"!scrollback help\" for more details";
  }

  @Override
  protected IRCMessage handleCommand(IRCMessage message, String data) {
    Matcher matcher = register.matcher(data);
    if (matcher.matches())
    {
      return processRegistration(message);
    }
    matcher = unregister.matcher(data);
    if (matcher.matches()){
      return processUnregistration(message);
    }
    matcher = help.matcher(data);
    if (matcher.matches())
    {
      return message.createReply("Send '!scrollback register' to register for scrollback, '!scrollback unregister' to unregister");
    }
    return message.createReply("Unknown command: " + data);
  }

  private IRCMessage processRegistration(IRCMessage message) {
    String response = "Registered " + message.getSender().getNick() + " for scrollback";
    if (!watched.add(message.getSender()))
    {
      response = message.getSender().getNick() + " already registered for scrollback";
    }
    return message.createReply(response);
  }

  private IRCMessage processUnregistration(IRCMessage message) {
    String response = "Unregistered " + message.getSender().getNick() + " for scrollback";
    if (!watched.remove(message.getSender()))
    {
      response = message.getSender().getNick() + " was not registered for scrollback";
    }
    return message.createReply(response);
  }

  private IRCMessage displayScrollback(IRCMessage message) {
    Date minDate = lastSeen.get(message.getSender());
    Iterator<IRCMessage> scrollback = lineBuffer.borrowIterator();
    StringBuffer response = new StringBuffer("Scrollback:\n");
    boolean addedLine = false;
    while (scrollback.hasNext())
    {
      IRCMessage next = scrollback.next();
      if (minDate == null ||
          minDate.before(next.getCreated()))
      {
        addedLine = true;
        response.append(generateScrollbackLine(next) + "\n");
      }
    }
    lineBuffer.returnIterator();
    if (!addedLine)
      return null;
    return message.createReply(response.toString());
  }

  private String generateScrollbackLine(IRCMessage next) {
    return "[" + dateFormat.format(next.getCreated()) + "] <" + next.getSender().getNick() + "> " + next.getMessage();
  }

  @Override
  protected IRCMessage handlePrivateChat(IRCMessage message) {
    return null;
  }

  @Override
  protected IRCMessage handlePublicChat(IRCMessage message) {
    lastSeen.put(message.getSender(), message.getCreated());
    lineBuffer.put(message);
    return null;
  }

  public IRCMessage handleJoin(JoinMessage message) {
    if (watched.contains(message.getSender()))
    {
      return displayScrollback(message);
    }
    lastSeen.put(message.getSender(), message.getCreated());
    return null;
  }

  public IRCMessage handlePart(PartMessage message) {
    lastSeen.put(message.getSender(), message.getCreated());
    return null;
  }

  public IRCMessage handleQuit(QuitMessage message) {
    lastSeen.put(message.getSender(), message.getCreated());
    return null;
  }

}
