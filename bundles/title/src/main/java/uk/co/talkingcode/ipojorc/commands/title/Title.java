package uk.co.talkingcode.ipojorc.commands.title;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;

import uk.co.talkingcode.ipojorc.api.AbstractURLWatchingPrefixCommand;
import uk.co.talkingcode.ipojorc.api.messages.IRCMessage;

@Provides
@Component(name = "TitleCommandProvider", architecture = true)
public class Title extends AbstractURLWatchingPrefixCommand {

  private static Pattern pattern = Pattern.compile(".*<title>(.*)</title>.*");
  private HttpClient client = new HttpClient();
  private boolean auto = true;

  public Title() {
    super("title");
  }

  public String getDescription() {
    return "!title - Retrieves the title for a URL";
  }

  private String getTitle(String url) {
    HttpMethod get = new GetMethod(url);
    try {
      client.executeMethod(get);
      CountedWrapper wrapper = new CountedWrapper(4096, get);
      return getTitle(wrapper);
    } catch (HttpException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      get.releaseConnection();
    }
    return null;
  }

  public String getTitle(InputStream is) throws IOException {
    try {
      int linecount = 0;
      String line = null;
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          is));
      while (linecount < 100 && (line = reader.readLine()) != null) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
          return StringEscapeUtils.unescapeHtml(matcher.group(1));
        }
        linecount++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      is.close();
    }
    return null;
  }

  @Override
  protected IRCMessage handleURL(IRCMessage message, String url) {
    if (auto)
    {
      String result = getTitle(url);
      if (result != null)
        return message.createReply(getTitle(url));
    }
    return null;
  }

  @Override
  protected IRCMessage handleCommand(IRCMessage message, String data) {
    if ("noauto".equals(data)) {
      auto = false;
      return message.createReply("Auto titling disabled");
    }
    if ("auto".equals(data)) {
      auto = true;
      return message.createReply("Auto titling enabled");
    }
    if ("help".equals(data)) {
      return message.createReply("'auto' to enabled auto titling, 'noauto' to disable it");
    }
    String url = data;
    if (url == null || url.length() == 0) {
      url = lastUrl;
    }
    if (url != null && url.length() != 0) {
      String result = getTitle(url);
      if (result != null)
        return message.createReply(getTitle(url));
    }
    return null;
  }
}
