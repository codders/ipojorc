package uk.co.talkingcode.ipojorc.ruby;

import java.net.URL;

import org.osgi.framework.Bundle;

public class BundleWrappingClassloader extends ClassLoader {

  private Bundle bundle;

  public BundleWrappingClassloader(ClassLoader parent, Bundle bundle)
  {
    super(parent);
    this.bundle = bundle;
  }

  @Override
  public URL getResource(String name) {
    return bundle.getResource(name);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    try
    {
      return bundle.loadClass(name);
    }
    catch (Exception e)
    {
      System.err.println("Unable to load " + name + " from bundle. Searching default classpath");
    }
    return super.findClass(name);
  }
  
  
}
