package uk.co.talkingcode.ipojorc.api;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class TestURLParsing {

  @Test
  public void testUrlParsing()
  {
    AbstractURLWatchingPrefixCommand command = new AbstractURLWatchingPrefixCommand("some prefix") {

      @Override
      protected IRCMessage handleCommand(IRCMessage message, String data) {
        return null;
      }

      public String getDescription() {
        return null;
      }
    };
    
    assertNull("Expected no url", command.lastUrl);
    command.handlePublicMessage(stringMessage("Fish"));
    assertNull("Still expected no url", command.lastUrl);
    command.handlePublicMessage(stringMessage("http://www.fish.com/fish.jpg"));
    assertEquals("Expected url capture", "http://www.fish.com/fish.jpg", command.lastUrl);
    command.handlePublicMessage(stringMessage("Cat"));
    assertEquals("Expected no url capture", "http://www.fish.com/fish.jpg", command.lastUrl);
    command.handlePublicMessage(stringMessage("Look at this!!! : http://www.fish.com/ <<--- a url"));
    assertEquals("Expected url capture", "http://www.fish.com/", command.lastUrl);
  }
  
  private IRCMessage stringMessage(String message)
  {
    IRCMessage result = new IRCMessage();
    result.setMessage(message);
    return result;
  }
}
