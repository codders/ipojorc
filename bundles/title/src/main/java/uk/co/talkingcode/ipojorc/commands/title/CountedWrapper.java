package uk.co.talkingcode.ipojorc.commands.title;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpMethod;

public class CountedWrapper extends InputStream {

  private int limit;
  private InputStream wrapped;
  private int read;
  private HttpMethod method;

  public CountedWrapper(int limit, HttpMethod get) throws IOException {
    this.limit = limit;
    this.wrapped = get.getResponseBodyAsStream();
    this.method = get;
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
    method.abort();
  }

}
