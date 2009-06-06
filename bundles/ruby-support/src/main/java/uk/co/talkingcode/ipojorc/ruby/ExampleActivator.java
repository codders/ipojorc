package uk.co.talkingcode.ipojorc.ruby;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.javasupport.JavaEmbedUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

/**
 * Extension of the default OSGi bundle activator
 */
public class ExampleActivator implements BundleActivator, BundleListener {
  private Ruby runtime;
  private BundleContext context;

  /**
   * Called whenever the OSGi framework starts our bundle
   */
  public void start(BundleContext bc) throws Exception {
    bc.addBundleListener(this);
    context = bc;
    RubyInstanceConfig config = new RubyInstanceConfig();
    config.setLoader(new BundleWrappingClassloader(Thread.currentThread()
        .getContextClassLoader(), bc.getBundle()));
    runtime = Ruby.newInstance(config);
    runtime.getLoadService().init(new ArrayList<String>());
  }

  private void evalLoadedScript(String script, Bundle bundle)
      throws IOException {
    runtime.evalScriptlet(IOUtils.toString(bundle.getResource(script)
        .openStream()));
  }

  /**
   * Called whenever the OSGi framework stops our bundle
   */
  public void stop(BundleContext bc) throws Exception {
    System.out.println("STOPPING org.test.jruby");
    JavaEmbedUtils.terminate(runtime);
    context = null;
  }

  @Override
  public void bundleChanged(BundleEvent e) {
    System.out.println("Bundle changed");
    if (e.getType() == BundleEvent.STARTED) {
      System.out.println("Foreign Bundle started");
      String scriptNames = (String) e.getBundle().getHeaders()
          .get("RubyScript");
      if (scriptNames != null) {
        System.out.println("Bundle has scripts: " + scriptNames);
        String scripts[] = scriptNames.split(",");
        for (String script : scripts) {
          try {
            evalLoadedScript(script, e.getBundle());
          } catch (IOException e1) {
            System.err.println("Error executing script: " + e1.getMessage());
            e1.printStackTrace();
          }
        }
      }
    }
  }
}
