package org.monet;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}
	
	public static String getBundlePath() throws IOException {
	  File pluginBundleFile = FileLocator.getBundleFile(getContext().getBundle());
	  if(pluginBundleFile.isDirectory())
	    return new File(pluginBundleFile, "bin").getAbsolutePath();
	    
	  return pluginBundleFile.getAbsolutePath();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
