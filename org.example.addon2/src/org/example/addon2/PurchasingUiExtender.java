package org.example.addon2;

import org.example.server.api.IAddonUiExtender;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;

@org.osgi.service.component.annotations.Component(property = { "type=purchasing" })
public class PurchasingUiExtender implements IAddonUiExtender {

	@Override
	public String getCaption() {
		return "Purchasing";
	}

	@Override
	public Component createComponent() {
		BrowserFrame frame = new BrowserFrame();
		frame.setSource(new ExternalResource("http://www.lunifera.com"));
		return frame;
	}
}
