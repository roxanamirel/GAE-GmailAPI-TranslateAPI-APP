package com.google.appengine.utils;

public class ConfigParameters {

	// Application name
	public static final String APPLICATION_NAME = "your_app";

	public static final String HTTP_REF = "https://your_app.appspot.com/";
	// The special value "me" can be used to indicate the authenticated user
	public static final String AUTHENTICATED_USER = "me";

	// Get unread emails only
	public static final String QUERY_PARAM = "label:UNREAD";

	// optional parameter for retriving emails
	public static final String FORMAT = "FULL";

	// api key
	public static final String MY_KEY = "your_key";
	// connection string to db
	
	public static final String CONNECTION_STRING = "jdbc:google:mysql://yourapp:dbInstance/dbName?user=username?password=pass";

}
