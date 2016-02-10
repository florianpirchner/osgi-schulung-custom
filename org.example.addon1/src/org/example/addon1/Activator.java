package org.example.addon1;

import java.util.Hashtable;

import org.example.server.api.IAddonInfo;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		// registering the addon info as an OSGi service
		Hashtable<String, Object> props = new Hashtable<>();
		props.put("addonId", "addon1");
		props.put("type", "sales");
		bundleContext.registerService(IAddonInfo.class, new AddonInfo(), props);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
}
