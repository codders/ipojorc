package uk.co.talkingcode.ipojorc.ruby;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;

public class BundleWrappingClassloader extends ClassLoader {

  private Bundle bundle;

  public BundleWrappingClassloader(ClassLoader parent, Bundle bundle)
  {
    super(parent);
    this.bundle = bundle;
  }

  @SuppressWarnings("unchecked")
  public Class loadClass(String name) throws ClassNotFoundException {
    System.out.println("Bundle loading class: " + name);
    return bundle.loadClass(name);
  }

  @Override
  public URL getResource(String name) {
    System.out.println("Bundle Loading Resource: " + name);
    return bundle.getResource(name);
  }

  @Override
  public InputStream getResourceAsStream(String name) {
    System.out.println("Bundle Loading Resource as stream: " + name);
    try {
      return bundle.getResource(name).openStream();
    } catch (IOException e) {
      System.err.println("Unable to load resource " + name + " as stream: " + e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Enumeration<URL> getResources(String name) throws IOException {
    System.out.println("Bundle Loading Resources: " + name);
    return bundle.getResources(name);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    System.out.println("Bundle finding class: " + name);
    try
    {
      System.out.println("Attempting to load from bundle");
      return loadClass(name);
    }
    catch (Exception e)
    {
      System.err.println("Unable to load " + name + " from bundle. Searching system");
    }
    return super.findClass(name);
  }
  
  
}
