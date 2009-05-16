package uk.co.talkingcode.ipojorc.commands.exif;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;

import uk.co.talkingcode.ipojorc.api.AbstractURLWatchingPrefixCommand;
import uk.co.talkingcode.ipojorc.api.IRCMessage;

public class Exif extends AbstractURLWatchingPrefixCommand {

  public Exif() {
    super("exif");
  }

  public String getDescription() {
    return "!exif - Reads the exif info for the most recently posted URL";
  }

  @Override
  protected IRCMessage handleCommand(IRCMessage message, String data) {
    String url = data;
    if (url == null || url.length() == 0) {
      url = lastUrl;
    }
    if (url != null && url.length() != 0) {
      try {
        return message.createReply(getExif(url));
      } catch (Exception e) {
        System.err.println("Unable to process data for " + lastUrl);
        e.printStackTrace();
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private String getExif(String url) throws ImageReadException,
      MalformedURLException, IOException {
    IImageMetadata metadata = Sanselan.getMetadata(new URL(url).openStream(),
        null);

    List items = metadata.getItems();
    if (items.size() == 0) {
      throw new ImageReadException("No metadata items found");
    }
    StringBuilder result = new StringBuilder("");
    for (Object item : items) {
      result.append(", " + item.toString());
    }
    return result.toString().substring(2);
  }
}
