package com.globant.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyHolder {

	private Properties prop ;
	
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


		    // load a properties file
		    prop.load(input);

		    
		    
		    

		} catch (IOException ex) {
		    ex.printStackTrace();
		} finally {
		    if (input != null) {
		        try {
		            input.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}
		
		


	}
	
	
}
