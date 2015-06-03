package iop.poster.rest.db;

import io.dropwizard.hibernate.AbstractDAO;
import iop.poster.rest.core.Poster;
import iop.poster.rest.resources.SetupResource;

import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PosterDAO extends AbstractDAO<Poster> {

	private static Logger log = LoggerFactory.getLogger(PosterDAO.class);

	public PosterDAO(SessionFactory factory) {
		super(factory);
	}

	public Poster findById(long id) {
		log.trace("findById==>");
		return get(id);
	}

	public Poster findByEmail(String email) {
		Criteria crit = criteria();
		crit.add(Restrictions.eq("email", email));
		return list(crit).get(0);
	}

	public long create(Poster poster) {
		long id = persist(poster).getId();
		return id;
	}

	public List<Poster> findAll() {
		return list(namedQuery("iop.poster.rest.Poster.findAll"));
	}

	public void delete(Poster poster) {
	    currentSession().delete(poster);
	}
}
