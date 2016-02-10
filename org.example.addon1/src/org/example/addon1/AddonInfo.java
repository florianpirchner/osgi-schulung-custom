package org.example.addon1;

import org.example.server.api.IAddonInfo;

public class AddonInfo implements IAddonInfo {

	@Override
	public String getId() {
		return "addon1";
	}

	@Override
	public String getAddon() {
		return "My addon no 1";
	}

}
