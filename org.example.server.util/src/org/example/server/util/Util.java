package org.example.server.util;

import org.example.server.api.IAddonInfo;

/**
 * Is used to demo require bundle and import package.
 */
public class Util {

	public static String toClientInfoString(IAddonInfo info) {
		return info.getAddon();
	}

}
