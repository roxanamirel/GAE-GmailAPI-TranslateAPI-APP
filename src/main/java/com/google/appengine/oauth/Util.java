package com.google.appengine.oauth;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.utils.ConfigParameters;

public class Util {

	// Global instance of the DataStoreFactory
	private static final AppEngineDataStoreFactory DATA_STORE_FACTORY = AppEngineDataStoreFactory.getDefaultInstance();

	// Global instance of the HTTP transport 
	private static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport();

	// Global instance of the JSON factory 
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static GoogleClientSecrets clientSecrets = null;

	static GoogleClientSecrets getClientCredential() throws IOException {
		if (clientSecrets == null) {
			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
					new InputStreamReader(Util.class.getResourceAsStream("/client_secrets.json")));
			Preconditions.checkArgument(
					!clientSecrets.getDetails().getClientId().startsWith("Enter ")
							&& !clientSecrets.getDetails().getClientSecret().startsWith("Enter "),
					"Download client_secrets.json file from https://code.google.com/apis/console/"
							+ "?api=calendar into calendar-appengine-sample/src/main/resources/client_secrets.json");
		}
		return clientSecrets;
	}

	public static String getRedirectUri(HttpServletRequest req) {
		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
		url.setRawPath("/oauth2callback");
		return url.build();
	}

	public static GoogleAuthorizationCodeFlow newFlow() throws IOException {
		return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, getClientCredential(),
				Collections.singleton(GmailScopes.MAIL_GOOGLE_COM + "/")).setDataStoreFactory(DATA_STORE_FACTORY)
						.setAccessType("offline").build();
	}

	public static Gmail loadGmailClient() throws IOException {
		String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		Credential credential = newFlow().loadCredential(userId);
		return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(ConfigParameters.APPLICATION_NAME).build();
	}

	static IOException wrappedIOException(IOException e) {
		if (e.getClass() == IOException.class) {
			return e;
		}
		return new IOException(e.getMessage());
	}

	private Util() {
	}
}
