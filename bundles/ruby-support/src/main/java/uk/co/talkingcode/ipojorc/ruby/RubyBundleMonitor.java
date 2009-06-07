package uk.co.talkingcode.ipojorc.ruby;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
public class RubyBundleMonitor implements BundleActivator, BundleListener {
  private Ruby runtime;
  private BundleWrappingClassloader wrappingClassloader;
  private static Set<IRCCommand> ircCommands = new HashSet<IRCCommand>();
  private static Set<IRCStatusWatcher> ircStatusWatchers = new HashSet<IRCStatusWatcher>();
  private static Map<Bundle, Set<Object>> bundleCommands = new HashMap<Bundle, Set<Object>>();

  /**
   * Called whenever the OSGi framework starts our bundle
   */
  public void start(BundleContext bc) throws Exception {
    bc.addBundleListener(this);
    RubyInstanceConfig config = new RubyInstanceConfig();
    wrappingClassloader = new BundleWrappingClassloader(Thread.currentThread().getContextClassLoader());
    wrappingClassloader.addBundle(bc.getBundle());
    config.setLoader(wrappingClassloader);
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
        wrappingClassloader.addBundle(e.getBundle());
        evalBundleScripts(scriptNames, e.getBundle());
        String commandClasses = stringBundleHeader(e.getBundle(), "RubyIRCCommands");
        loadIRCCommands(commandClasses, e.getBundle());
      }
    } else if (e.getType() == BundleEvent.STOPPING) {
      System.out.println("Foreign Bundle stopped");
      wrappingClassloader.removeBundle(e.getBundle());
      Set<Object> commands = bundleCommands.get(e.getBundle());
      if (commands != null) {
        for (Object command : commands) {
          ircCommands.remove(command);
          ircStatusWatchers.remove(command);
        }
      }
    }
  }

  private void loadIRCCommands(String commandClassNames, Bundle bundle) {
    if (bundleCommands.get(bundle) == null)
      bundleCommands.put(bundle, new HashSet<Object>());
    Set<Object> thisBundlesCommands = bundleCommands.get(bundle);
    String[] commandClasses = commandClassNames.split(",");
    for (String commandClassName : commandClasses) {
      System.out.println("Evaluating class: " + commandClassName);
      RubyClass commandClass = runtime.getClass(commandClassName);
      IRubyObject commandInstance = commandClass.newInstance(runtime.getCurrentContext(), new IRubyObject[] {}, null);
      if (rubyObjectImplementsInterface(commandInstance, IRCCommand.class)) {
        IRCCommand command = new IRCCommandWrapper(commandInstance, runtime);
        ircCommands.add(command);
        thisBundlesCommands.add(command);
      }
      if (rubyObjectImplementsInterface(commandInstance, IRCStatusWatcher.class)) {
        IRCStatusWatcher watcher = new IRCStatusWatcherWrapper(commandInstance, runtime);
        ircStatusWatchers.add(watcher);
        thisBundlesCommands.add(watcher);
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

  public static IRCCommand[] getIRCCommands() {
    return ircCommands.toArray(new IRCCommand[] {});
  }

  public static IRCStatusWatcher[] getIRCStatusWatchers() {
    return ircStatusWatchers.toArray(new IRCStatusWatcher[] {});
  }

}
