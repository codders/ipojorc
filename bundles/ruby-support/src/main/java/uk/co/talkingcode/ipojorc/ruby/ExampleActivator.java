package uk.co.talkingcode.ipojorc.ruby;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyInstanceConfig;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import uk.co.talkingcode.ipojorc.api.IRCCommand;
import uk.co.talkingcode.ipojorc.api.IRCStatusWatcher;

/**
 * Extension of the default OSGi bundle activator
 */
public class ExampleActivator implements BundleActivator, BundleListener {
  private Ruby runtime;
  private static List<IRCCommand> ircCommands = new ArrayList<IRCCommand>();
  private static List<IRCStatusWatcher> ircStatusWatchers = new ArrayList<IRCStatusWatcher>();

  /**
   * Called whenever the OSGi framework starts our bundle
   */
  public void start(BundleContext bc) throws Exception {
    bc.addBundleListener(this);
    RubyInstanceConfig config = new RubyInstanceConfig();
    config.setLoader(new BundleWrappingClassloader(Thread.currentThread().getContextClassLoader(), bc.getBundle()));
    runtime = Ruby.newInstance(config);
    runtime.getLoadService().init(new ArrayList<String>());
  }

  private void evalLoadedScript(String script, Bundle bundle) throws IOException {
    runtime.evalScriptlet(IOUtils.toString(bundle.getResource(script).openStream()));
  }

  /**
   * Called whenever the OSGi framework stops our bundle
   */
  public void stop(BundleContext bc) throws Exception {
    System.out.println("STOPPING org.test.jruby");
    JavaEmbedUtils.terminate(runtime);
  }

  @Override
  public void bundleChanged(BundleEvent e) {
    System.out.println("Bundle changed");
    if (e.getType() == BundleEvent.STARTED) {
      System.out.println("Foreign Bundle started");
      String scriptNames = stringBundleHeader(e.getBundle(), "RubyScript");
      if (scriptNames != null) {
        evalBundleScripts(scriptNames, e.getBundle());
        String commandClasses = stringBundleHeader(e.getBundle(), "RubyIRCCommands");
        loadIRCCommands(commandClasses);
      }
    }
  }

  private void loadIRCCommands(String commandClassNames) {
    String[] commandClasses = commandClassNames.split(",");
    for (String commandClassName : commandClasses) {
      System.out.println("Evaluating class: " + commandClassName);
      RubyClass commandClass = runtime.getClass(commandClassName);
      IRubyObject commandInstance = commandClass.newInstance(runtime.getCurrentContext(), new IRubyObject[] {}, null);
      if (rubyObjectImplementsInterface(commandInstance, IRCCommand.class)) {
        ircCommands.add(new IRCCommandWrapper(commandInstance, runtime));
      }
      if (rubyObjectImplementsInterface(commandInstance, IRCStatusWatcher.class))
      {
        ircStatusWatchers.add(new IRCStatusWatcherWrapper(commandInstance, runtime));
      }
    }
  }

  private boolean rubyObjectImplementsInterface(IRubyObject commandInstance, Class<?> interfaceClass) {
    Method[] methods = interfaceClass.getMethods();
    for (Method method : methods) {
      System.err.println("Checking for " + method.getName() + " method");
      if (!commandInstance.respondsTo(method.getName())) {
        System.err.println("Didn't respond to " + method.getName() + ", is no a " + interfaceClass.getName());
        return false;
      }
    }
    return true;
  }

  private String stringBundleHeader(Bundle bundle, String headerName) {
    return (String) bundle.getHeaders().get(headerName);
  }

  private void evalBundleScripts(String scriptNames, Bundle bundle) {
    System.out.println("Bundle has scripts: " + scriptNames);
    String scripts[] = scriptNames.split(",");
    for (String script : scripts) {
      try {
        evalLoadedScript(script, bundle);
      } catch (IOException e1) {
        System.err.println("Error executing script: " + e1.getMessage());
        e1.printStackTrace();
      }
    }
  }
  
  public static IRCCommand[] getIRCCommands()
  {
    return ircCommands.toArray(new IRCCommand[] {});
  }
  
  public static IRCStatusWatcher[] getIRCStatusWatchers()
  {
    return ircStatusWatchers.toArray(new IRCStatusWatcher[] {});
  }
  
}
