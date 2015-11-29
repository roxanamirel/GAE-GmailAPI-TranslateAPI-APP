package com.google.appengine.utils;

import java.io.IOException;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

public class EmailUtil {

	/**
	   * Get Message with given ID.
	   *
	   * @param service Authorized Gmail API instance.
	   * @param userId User's email address. The special value "me"
	   * can be used to indicate the authenticated user.
	   * @param messageId ID of Message to retrieve.
	   * @return Message Retrieved Message.
	   * @throws IOException
	   */
	  public static Message getMessage(Gmail service, String userId, String messageId, String format)
	      throws IOException {
	    Message message = service.users().messages().get(userId, messageId).setFormat(format).execute();
	    return message;
	  }

}
