package org.example.server.api;

import java.util.Map;

import com.vaadin.ui.Component;

/**
 * Implementations will be provided by clients. They need to create component
 * for specific parts of the UI.
 */
public interface IAddonUiExtender {

	/**
	 * Creates a component that should be added to the UI.
	 * 
	 * @param properties
	 *            information to create components
	 * @return
	 */
	Component createComponent();

	/**
	 * Returns the caption for the component.
	 * 
	 * @return
	 */
	String getCaption();

}
