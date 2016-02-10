package org.example.server.vaadin;

import java.util.HashMap;

import org.example.server.api.IAddonUiExtender;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
@Theme(Reindeer.THEME_NAME)
@Push
public class SimpleUI extends UI implements ServiceTrackerCustomizer<IAddonUiExtender, IAddonUiExtender> {

	private ServiceTracker<IAddonUiExtender, IAddonUiExtender> tracker;
	private BundleContext context = FrameworkUtil.getBundle(getClass()).getBundleContext();

	private HashMap<IAddonUiExtender, Component> cache = new HashMap<>();

	private CssLayout main;
	private TabSheet tabSheet;

	@Override
	protected void init(VaadinRequest request) {
		main = new CssLayout();
		main.setSizeFull();
		setContent(main);

		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		main.addComponent(tabSheet);

		// start a service tracker to find all content provider
		tracker = new ServiceTracker<IAddonUiExtender, IAddonUiExtender>(context, IAddonUiExtender.class, this);
		tracker.open();
	}

	/**
	 * Called by service tracker if a provider was found.
	 */
	@Override
	public IAddonUiExtender addingService(ServiceReference<IAddonUiExtender> reference) {
		final IAddonUiExtender provider = context.getService(reference);
		accessSynchronously(new Runnable() {
			@Override
			public void run() {
				Component c = provider.createComponent();
				cache.put(provider, c);
				c.setSizeFull();
				tabSheet.addTab(c, provider.getCaption());
			}
		});
		return provider;
	}

	@Override
	public void modifiedService(ServiceReference<IAddonUiExtender> reference, IAddonUiExtender service) {
	}

	/**
	 * Called by service tracker if a service was stopped.
	 */
	@Override
	public void removedService(ServiceReference<IAddonUiExtender> reference, IAddonUiExtender service) {
		final Component component = cache.remove(service);
		if (component != null) {
			accessSynchronously(new Runnable() {
				@Override
				public void run() {
					Tab tab = tabSheet.getTab(component);
					tabSheet.removeTab(tab);
				}
			});
		}
	}
}
