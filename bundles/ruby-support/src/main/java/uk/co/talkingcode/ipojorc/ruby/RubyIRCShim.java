package uk.co.talkingcode.ipojorc.ruby;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;

import uk.co.talkingcode.ipojorc.api.IRCCommand;
import uk.co.talkingcode.ipojorc.api.IRCStatusWatcher;
import uk.co.talkingcode.ipojorc.api.messages.AbstractIncomingIRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.AbstractStatusMessage;
import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.JoinMessage;
import uk.co.talkingcode.ipojorc.api.messages.PartMessage;
import uk.co.talkingcode.ipojorc.api.messages.PrivateIRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.PublicIRCMessage;
import uk.co.talkingcode.ipojorc.api.messages.QuitMessage;

@Provides
@Component(name = "RubyCommandProvider", architecture = true)
public class RubyIRCShim implements IRCCommand, IRCStatusWatcher {

  @Override
  public String getDescription() {
    StringBuilder descriptions = new StringBuilder();
    IRCCommand[] commands = RubyBundleMonitor.getIRCCommands();
    if (commands.length > 0) {
      for (IRCCommand command : commands) {
        descriptions.append("\n" + command.getDescription());
      }
      return descriptions.toString().substring(1);
    } else {
      return "Placeholder for Ruby commands";
    }
  }

  @Override
  public IRCMessage handlePrivateMessage(PrivateIRCMessage ircMessage) {
    return handleIRCMessage(ircMessage);
  }

  @Override
  public IRCMessage handlePublicMessage(PublicIRCMessage message) {
    return handleIRCMessage(message);
  }

  @Override
  public IRCMessage handleJoin(JoinMessage message) {
    return handleWatcherMessage(message);
  }

  @Override
  public IRCMessage handlePart(PartMessage message) {
    return handleWatcherMessage(message);
  }

  @Override
  public IRCMessage handleQuit(QuitMessage message) {
    return handleWatcherMessage(message);
  }

  private IRCMessage handleWatcherMessage(AbstractStatusMessage message) {
    IRCStatusWatcher[] watchers = RubyBundleMonitor.getIRCStatusWatchers();
    for (IRCStatusWatcher watcher : watchers) {
      IRCMessage result = message.dispatchToStatusWatcher(watcher);
      if (result != null)
        return result;
    }
    return null;
  }

  private IRCMessage handleIRCMessage(AbstractIncomingIRCMessage ircMessage) {
    IRCCommand[] commands = RubyBundleMonitor.getIRCCommands();
    for (IRCCommand command : commands) {
      IRCMessage result = ircMessage.dispatchToCommand(command);
      if (result != null)
        return result;
    }
    return null;
  }

}
