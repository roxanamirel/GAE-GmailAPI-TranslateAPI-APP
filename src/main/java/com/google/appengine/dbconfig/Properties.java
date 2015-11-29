package com.google.appengine.dbconfig;

import java.util.HashMap;
import java.util.Map;

import com.google.appengine.utils.ConfigParameters;

public class Properties {
	
	private static final String JDBC_DRIVER = "javax.persistence.jdbc.driver";
	private static final String GOOGLE_DRIVER= "com.mysql.jdbc.GoogleDriver";
    private static final String PERSISTENCE_JDBC_URL= "javax.persistence.jdbc.url";
   
    
    
	public static Map<String, String> getProperties() {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(JDBC_DRIVER, GOOGLE_DRIVER);
		properties.put(PERSISTENCE_JDBC_URL, ConfigParameters.CONNECTION_STRING);
		return properties;

	}

}
