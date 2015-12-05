package com.google.appengine.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.GoogleAPI;
import com.google.api.GoogleAPIException;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.google.appengine.models.Email;
import com.google.appengine.service.EmailService;

public class HomeControllerUtil {
	private static final Logger logger = LoggerFactory.getLogger(HomeControllerUtil.class);

	public static List<Email> convertToEmailType(List<Message> unreadEmails, Gmail service) throws IOException {

		List<Email> toStoreEmails = new ArrayList<Email>();
		// Get Email by id and format FULL to retrieve the snippet and create
		// Email objects for storing in CloudSQL
		for (Message message : unreadEmails) {
			Message fullMessage = EmailUtil.getMessage(service, ConfigParameters.AUTHENTICATED_USER, message.getId(),
					ConfigParameters.FORMAT);
			Email tm = new Email();
			tm.setSnippet(fullMessage.getSnippet());
			tm.setDate(new Date());
			toStoreEmails.add(tm);
		}
		return toStoreEmails;
	}

	public static void insertEmailsInDB(EmailService emailService, List<Email> emails) {
		for (Email email : emails) {
			emailService.insert(email);
		}
	}

	public static List<Message> getUnreadEmails(Gmail service) throws IOException {
		List<Message> unreadEmails = new ArrayList<Message>();
		ListMessagesResponse emails = service.users().messages().list(ConfigParameters.AUTHENTICATED_USER)
				.setQ(ConfigParameters.QUERY_PARAM).setMaxResults(new Long(3)).execute();
		unreadEmails.addAll(emails.getMessages());
		if (emails.getNextPageToken() != null) {
			String pageToken = emails.getNextPageToken();
			emails = service.users().messages().list(ConfigParameters.AUTHENTICATED_USER)
					.setQ(ConfigParameters.QUERY_PARAM).setPageToken(pageToken).setMaxResults(new Long(3)).execute();
			
		
		}
		
//		while (emails.getMessages() != null) {
//			unreadEmails.addAll(emails.getMessages());
//			if (emails.getNextPageToken() != null) {
//				String pageToken = emails.getNextPageToken();
//				emails = service.users().messages().list(ConfigParameters.AUTHENTICATED_USER)
//						.setQ(ConfigParameters.QUERY_PARAM).setPageToken(pageToken).setMaxResults(new Long(1)).execute();
//				
//			
//			} else {
//				break;
//			}
//		}
		return unreadEmails;
	}

	public static List<Email> translateSnippetsToFinish(List<Email> emails) {

		// Set the HTTP referrer to your website address.
		GoogleAPI.setHttpReferrer(ConfigParameters.HTTP_REF);

		// Set the Google Translate API key
		GoogleAPI.setKey(ConfigParameters.MY_KEY);
		try {
			for (Email email : emails) {
				email.setTranslatedSnippet(
						Translate.DEFAULT.execute(email.getSnippet(), Language.ENGLISH, Language.FINNISH));
			}
		} catch (GoogleAPIException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return emails;
	}

}
