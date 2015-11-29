package com.google.appengine.service;

import java.util.List;

import com.google.appengine.models.Email;


public interface EmailService {

	void insert(Email message);
	List<Email> readAll ();
	void deleteAll();
}
