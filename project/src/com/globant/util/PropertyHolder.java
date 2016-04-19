package com.globant.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyHolder {
	private static final Logger LOGGER = Logger.getLogger(PropertyHolder.class);
	private Properties prop;

	public PropertyHolder() {
		loadProperties();
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	private void loadProperties() {
		prop = new Properties();
		InputStream input = null;

		try {
			input = getClass().getClassLoader().getResourceAsStream("config.properties");
			if (input == null) {
				LOGGER.error("Problems load config properties, the file is not found.");
			}
			prop.load(input);

		} catch (IOException e) {
			LOGGER.error("Problems in load Properties.", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error("Problems when load the file Properties.", e);
				}
			}
		}
	}
}
