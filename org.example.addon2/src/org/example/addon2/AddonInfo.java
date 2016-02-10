package org.example.addon2;

import org.example.server.api.IAddonInfo;
import org.osgi.service.component.annotations.Component;

@Component(service = IAddonInfo.class, property = { "type=purchasing" })
public class AddonInfo implements IAddonInfo {

	@Override
	public String getId() {
		return "addon2";
	}

	@Override
	public String getAddon() {
		return "My addon no 2";
	}

}
