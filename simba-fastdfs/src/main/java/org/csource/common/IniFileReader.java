/**
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
**/

package org.csource.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * ini file reader / parser
 * 
 * @author Happy Fish / YuQing
 * @version Version 1.0
 */
public class IniFileReader {
	private HashMap<String, Object> paramMap;
	private String confFilename;

	/**
	 * @param confFilename
	 *            config filename
	 */
	public IniFileReader(String confFilename) throws FileNotFoundException, IOException {
		this.confFilename = confFilename;
		loadFromFile(confFilename);
	}

	/**
	 * get the config filename
	 * 
	 * @return config filename
	 */
	public String getConfFilename() {
		return this.confFilename;
	}

	/**
	 * get string value from config file
	 * 
	 * @param name
	 *            item name in config file
	 * @return string value
	 */
	public String getStrValue(String name) {
		return (String) this.paramMap.get(name);
	}

	/**
	 * get int value from config file
	 * 
	 * @param name
	 *            item name in config file
	 * @param default_value
	 *            the default value
	 * @return int value
	 */
	public int getIntValue(String name, int default_value) {
		String szValue = this.getStrValue(name);
		if (szValue == null) {
			return default_value;
		}

		return Integer.parseInt(szValue);
	}

	/**
	 * get boolean value from config file
	 * 
	 * @param name
	 *            item name in config file
	 * @param default_value
	 *            the default value
	 * @return boolean value
	 */
	public boolean getBoolValue(String name, boolean default_value) {
		String szValue = this.getStrValue(name);
		if (szValue == null) {
			return default_value;
		}

		return szValue.equalsIgnoreCase("yes") || szValue.equalsIgnoreCase("on") || szValue.equalsIgnoreCase("true") || szValue.equals("1");
	}

	/**
	 * get all values from config file
	 * 
	 * @param name
	 *            item name in config file
	 * @return string values (array)
	 */
	@SuppressWarnings("unchecked")
	public String[] getValues(String name) {
		Object obj;
		String[] values;
		obj = this.paramMap.get(name);
		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			values = new String[1];
			values[0] = (String) obj;
			return values;
		}
		Object[] objs = ((ArrayList<String>) obj).toArray();
		values = new String[objs.length];
		System.arraycopy(objs, 0, values, 0, objs.length);
		return values;
	}

	private void loadFromFile(String conf_filename) throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.load(this.getClass().getResourceAsStream(conf_filename));
		paramMap = new HashMap<>();
		for (Object key : p.keySet()) {
			paramMap.put(key.toString().trim(), p.getProperty(key.toString()).trim());
		}
	}
}
