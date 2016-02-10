package org.example.server.impl;

import java.util.HashSet;
import java.util.Set;

import org.example.server.api.IAddonInfo;
import org.osgi.service.component.ComponentContext;

/**
 * This server is just implementation detail. Nobody in the (bundle)-outside
 * world knows about it.
 * <p>
 * Using the whiteboard pattern, {@link IAddonInfo addonInfos} are injected
 * automatically.
 */
public class MyServer {

	private Set<IAddonInfo> addonInfos = new HashSet<>();

	/**
	 * Is called by OSGi-DS when the service component is activated.
	 * 
	 * @param context
	 */
	protected void activate(ComponentContext context) {

	}

	/**
	 * Is called by OSGi-DS when the service component is deactivated.
	 */
	protected void deactivate() {

	}

	/**
	 * Is called by OSGi-DS. See OSGI-INF/component.xml
	 * 
	 * @param info
	 */
	protected void addAddonInfo(IAddonInfo info) {
		addonInfos.add(info);

		System.out.println("Added addon info for " + info.getId());
	}

	/**
	 * Is called by OSGi-DS. See OSGI-INF/component.xml
	 * 
	 * @param info
	 */
	protected void removeAddonInfo(IAddonInfo info) {
		addonInfos.remove(info);
	}
}
