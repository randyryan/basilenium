/**
 * Copyright (c) 2013-2018 Ryan Li Wan. All rights reserved.
 */

package org.basil.selenium.remote;

import org.openqa.selenium.Capabilities;

/**
 * SessionSerialization
 *
 * @author ryan131
 * @since Nov 2, 2015, 10:37:54 PM
 */
@Deprecated
public class SessionSerialization {

	protected String getSessionId(Capabilities capabilities) {
		String key = "webdriver.remote.sessionid";
		return (String) capabilities.getCapability(key);
	}

	public SessionSerialization(Capabilities capabilities) {
	}

}
