package com.google.appengine.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.models.Email;
import com.google.appengine.oauth.Util;
import com.google.appengine.service.EmailService;
import com.google.appengine.service.serviceimpl.EmailServiceImpl;
import com.google.appengine.utils.HomeControllerUtil;
import com.google.appengine.utils.RequestAttributesNames;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/")

public class HomeController extends AbstractAppEngineAuthorizationCodeServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	//@Inject
	private EmailService emailService = new EmailServiceImpl();

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		return Util.getRedirectUri(req);
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {
		return Util.newFlow();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Message> unreadEmails = new ArrayList<Message>();
		List<Email> toStoreEmails = new ArrayList<Email>();
		List<Email> translatedEmails = new ArrayList<Email>();
		UserService userService = UserServiceFactory.getUserService();
		Principal user = request.getUserPrincipal();
		String logoutURL = userService.createLogoutURL(request.getRequestURL().toString());

		// flush DB before
		emailService.deleteAll();

		try {
			// Build a new authorized API client service.
			Gmail service = Util.loadGmailClient();
			unreadEmails = HomeControllerUtil.getUnreadEmails(service);
			toStoreEmails = HomeControllerUtil.convertToEmailType(unreadEmails, service);
			translatedEmails = HomeControllerUtil.translateSnippetsToFinish(toStoreEmails);
			// insert in CloudSQL DB
			HomeControllerUtil.insertEmailsInDB(emailService, translatedEmails);

		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		List<Email> emailsFromDB = emailService.readAll();

		// Set the request attributes used in the views
		request.setAttribute(RequestAttributesNames.logoutURL.name(), logoutURL);
		request.setAttribute(RequestAttributesNames.userName.name(), user != null ? user.getName() : "");
		request.setAttribute(RequestAttributesNames.toStoreEmails.name(), toStoreEmails);
		request.setAttribute(RequestAttributesNames.translatedEmails.name(), translatedEmails);
		request.setAttribute(RequestAttributesNames.emailsFromDB.name(), emailsFromDB);

		try {
			request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
		} catch (ServletException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

}
