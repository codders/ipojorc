package uk.co.talkingcode.ipojorc.commands.title;

import java.io.IOException;
import java.io.InputStream;

public class CountedWrapper extends InputStream {

  private int limit;
  private InputStream wrapped;
  private int read;

  public CountedWrapper(int limit, InputStream wrapped) {
    this.limit = limit;
    this.wrapped = wrapped;
    this.read = 0;
  }

  @Override
  public int read() throws IOException {
    if (read > limit)
      return -1;
    int result = wrapped.read();
    read++;
    return result;
  }

  @Override
  public void close() throws IOException {
    wrapped.close();
  }

}
