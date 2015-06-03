package iop.poster.rest.db;

import io.dropwizard.hibernate.AbstractDAO;
import iop.poster.rest.core.Provider;

import java.util.List;

import org.hibernate.SessionFactory;

import com.google.common.base.Optional;

public class ProviderDAO extends AbstractDAO<Provider> {

	public ProviderDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Optional<Provider> findById(Long id) {
		return Optional.fromNullable(get(id));
	}

	public Provider create(Provider provider) {
		return persist(provider);
	}

	
	public long check(Provider provider) {
		return persist(provider).getId();
	}

	public List<Provider> findAll() {
		return list(namedQuery("iop.poster.rest.Provider.findAll"));
	}
}
