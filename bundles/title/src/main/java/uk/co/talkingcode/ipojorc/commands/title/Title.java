package uk.co.talkingcode.ipojorc.commands.title;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

import uk.co.talkingcode.ipojorc.api.AbstractURLWatchingPrefixCommand;
import uk.co.talkingcode.ipojorc.api.IRCMessage;

public class Title extends AbstractURLWatchingPrefixCommand {
  private static Pattern pattern = Pattern
      .compile(".*<title>(.*)</title>.*");

  public Title() {
    super("title");
  }

  public String getDescription() {
    return "!title - Retrieves the title for a URL";
  }

  private static String getTitle(String url)
  {
    try {
      return getTitle(new URL(url).openStream());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public static String getTitle(InputStream is)
  {
    try {
      int linecount = 0;
      String line = null;
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      while (linecount < 100 && (line = reader.readLine()) != null)
      {
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches())
        {
          return StringEscapeUtils.unescapeHtml(matcher.group(1));
        }
        linecount++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  protected IRCMessage handleCommand(IRCMessage message, String data) {
    String url = data;
    if (url == null || url.length() == 0) {
      url = lastUrl;
    }
    if (url != null && url.length() != 0) {
      return message.createReply(getTitle(url));
    }
    return null;
  }
}
