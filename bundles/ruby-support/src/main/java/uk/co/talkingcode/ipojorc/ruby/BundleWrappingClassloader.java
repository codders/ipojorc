package uk.co.talkingcode.ipojorc.ruby;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.Bundle;

public class BundleWrappingClassloader extends ClassLoader {

  private Set<Bundle> bundles = new HashSet<Bundle>();

  public BundleWrappingClassloader(ClassLoader parent) {
    super(parent);
  }

  @Override
  public URL getResource(String name) {
    for (Bundle bundle : bundles) {
      try {
        System.out.println("Searching for " + name + " in " + bundle);
        URL result = bundle.getResource(name);
        if (result != null)
          return result;
      } catch (Exception e) {
      }
    }
    return super.getResource(name);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    for (Bundle bundle : bundles) {
      try {
        System.out.println("Searching for " + name + " in " + bundle);
        return bundle.loadClass(name);
      } catch (Exception e) {
      }
    }
    return super.findClass(name);
  }

  public void addBundle(Bundle bundle) {
    if (bundles.add(bundle)) {
      System.out.println("Added bundle: " + bundle);
    }
  }

  public void removeBundle(Bundle bundle) {
    if (bundles.remove(bundle)) {
      System.out.println("Removed bundle: " + bundle);
    }
  }

}
