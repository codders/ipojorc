package uk.co.talkingcode.ipojorc.commands.title;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class TitleTest {

  private String htmlDoc = "<html>\n"
      + "<head>  <title>Some &amp; title</title>\n" + "</head>" + "\n</html>";

  @Test
  public void testTitleParsing() throws IOException {
    String title = new Title().getTitle(IOUtils.toInputStream(htmlDoc));
    assertEquals("Expected a title", "Some & title", title);
  }
}
