package iop.poster.rest.resources;

import io.dropwizard.hibernate.UnitOfWork;
import iop.poster.rest.core.Provider;
import iop.poster.rest.db.ProviderDAO;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

@Path("app/provider")
public class ProviderResource {
	
	private static Logger log = LoggerFactory.getLogger(SetupResource.class);

	ProviderDAO providerDAO;

	@GET
	@UnitOfWork
	public List<Provider> get() {
		List<Provider> providers = providerDAO.findAll();
		return providers;
	}

	@POST
	@UnitOfWork
	public Provider put(Provider provider) {
		provider = providerDAO.create(provider);
		return provider;
	}
	
	private Provider findSafely(Long id) {
		log.trace("in==>");
		final Optional<Provider> provider = providerDAO.findById(id);
		log.trace("<==out");
		return provider.get();
	}
}