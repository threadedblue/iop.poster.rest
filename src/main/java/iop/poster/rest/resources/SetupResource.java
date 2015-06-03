package iop.poster.rest.resources;

import io.dropwizard.hibernate.UnitOfWork;
import iop.poster.rest.core.Poster;
import iop.poster.rest.db.PosterDAO;
import iop.poster.rest.db.ProviderDAO;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/app/setup")
@Produces(MediaType.APPLICATION_JSON)
public class SetupResource {

	private static Logger log = LoggerFactory.getLogger(SetupResource.class);

	PosterDAO posterDAO;
	ProviderDAO providerDAO;

	public SetupResource(PosterDAO posterDAO, ProviderDAO providerDAO) {
		super();
		this.posterDAO = posterDAO;
		this.providerDAO = providerDAO;
	}
	
	@GET
	public Poster health() {
		return new Poster();
	}

	@GET
	@Path("id/{id}")
	@UnitOfWork
	public Poster getById(@PathParam("id") Long id) {
		Poster poster = findSafely(id);
		if (poster == null) {
			poster = new Poster();
		}
		return poster;
	}


	@GET
	@Path("email/{email}")
	@UnitOfWork
	public Poster getByEmail(@PathParam("email") String email) {
		Poster poster = findSafely(email);
		return poster;
	}

	@GET
	@Path("listall")
	@UnitOfWork
	public List<Poster> listAll() {
		List<Poster> posters = posterDAO.findAll();
		return posters;
	}
	
	@POST
	@Path("persist")
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public Poster persist(Poster poster) {
		posterDAO.create(poster);
		return poster;
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.TEXT_PLAIN)
	@UnitOfWork
	public Long deleteTwo(@PathParam("id") Long id) {
		log.debug("posterId=" + id);		
//		posterDAO.delete(id);
		return id;
	}

	private Poster findSafely(long id) {
		Poster poster = posterDAO.findById(id);
		if (poster== null) {
			log.debug("posters was null");
			poster = new Poster();
		}
		return poster;
	}
	
	private Poster findSafely(String email) {
		Poster poster = posterDAO.findByEmail(email);
		if (poster== null) {
			log.debug("posters was null");
			poster = new Poster();
			poster.setEmail(email);
		}
		return poster;
	}
}
