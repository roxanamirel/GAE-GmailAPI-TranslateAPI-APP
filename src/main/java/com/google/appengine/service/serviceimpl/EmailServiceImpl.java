package com.google.appengine.service.serviceimpl;

import org.springframework.stereotype.Service;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.appengine.dbconfig.Properties;
import com.google.appengine.models.Email;
import com.google.appengine.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private EntityManager em;
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT,
			Properties.getProperties());
	private static final String PERSISTENCE_UNIT = "p1";

	@Override
	public void insert(Email message) {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(message);
		em.getTransaction().commit();
		em.close();

	}

	@Override
	public List<Email> readAll() {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		List<Email> result = em.createQuery("FROM Email", Email.class).getResultList();
		em.getTransaction().commit();
		em.close();
		return result;
	}

	@Override
	public void deleteAll() {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		em.createQuery("Delete from Email").executeUpdate();
		em.flush();
		em.getTransaction().commit();
		em.close();
	}

}
