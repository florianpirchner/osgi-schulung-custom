package org.example.server.impl;

import java.util.Enumeration;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public class Activator implements BundleActivator, BundleListener, BundleTrackerCustomizer<Bundle> {

	private BundleTracker<Bundle> tracker;

	public void start(BundleContext context) throws Exception {
		System.out.println("Started bundle " + context.getBundle().getBundleId());
		context.addBundleListener(this);

		// lets create a bundle tracker that holds all bundles in the states
		// active | starting | resolved
		// it is used to demo the extender pattern
		tracker = new BundleTracker<Bundle>(context, Bundle.ACTIVE | Bundle.STARTING | Bundle.RESOLVED, this);
		tracker.open();
	}

	public void stop(BundleContext context) throws Exception {
		tracker.close();
		tracker = null;
		context.removeBundleListener(this);
		System.out.println("Stopped bundle " + context.getBundle().getBundleId());
	}

	/**
	 * Impl of {@link BundleListener}. Be carefully with it. You won't get
	 * notifications about bundles that are already in there state before the
	 * listener was added. Better use {@link BundleTracker}.
	 */
	@Override
	public void bundleChanged(BundleEvent event) {
		String bsn = event.getBundle().getSymbolicName();
		System.out.println("Bundle " + bsn + " changed state!");
	}

	/**
	 * Is called every time the bundle is removed from the tracker.
	 */
	@Override
	public Bundle addingBundle(Bundle bundle, BundleEvent event) {
		System.out.println("Adding Bundle " + bundle.getSymbolicName() + " changed state!");

		/*
		 * Check if the given bundle contains the header "Server-Extendee". If
		 * it does so, try to load the class
		 * org.example.extendee.no1.common.MyAPI.
		 */
		Enumeration<String> headers = bundle.getHeaders().keys();
		while (headers.hasMoreElements()) {
			String key = headers.nextElement();
			if (key.equals("Server-Extendee")) {
				System.out.println("Found server attendee for bsn " + bundle.getSymbolicName());

				try {
					Class<?> myAPIClass = bundle.loadClass("org.example.extendee.no1.common.MyExtendee");
					System.out.println(myAPIClass);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		return bundle;
	}

	/**
	 * Is called if the state of the bundle changes, but the bundle is still in
	 * the tracker without removing or adding.
	 */
	@Override
	public void modifiedBundle(Bundle bundle, BundleEvent event, Bundle object) {
		System.out.println("Modified Bundle " + bundle.getSymbolicName() + " changed state!");
	}

	/**
	 * Is called every time the bundle is added to the tracker.
	 */
	@Override
	public void removedBundle(Bundle bundle, BundleEvent event, Bundle object) {
		System.out.println("Removing Bundle " + bundle.getSymbolicName() + " changed state!");
	}

}
