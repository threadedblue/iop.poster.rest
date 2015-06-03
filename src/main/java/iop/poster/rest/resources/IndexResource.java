package iop.poster.rest.resources;

import io.dropwizard.hibernate.UnitOfWork;
import iop.poster.rest.core.Poster;
import iop.poster.rest.db.PosterDAO;
import iop.poster.rest.db.ProviderDAO;
import iop.poster.rest.pdf.PosterWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("app")
@Produces(MediaType.APPLICATION_JSON)
public class IndexResource {

	private static Logger log = LoggerFactory.getLogger(IndexResource.class);

	PosterDAO posterDAO;
	ProviderDAO providerDAO;

	public IndexResource(PosterDAO posterDAO, ProviderDAO providerDAO) {
		super();
		log.trace("start==>");
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
		return findSafely(id);
	}

	@GET
	@Path("email/{email}")
	@UnitOfWork
	public Poster getByEmail(@PathParam("email") String email) {
		return findSafely(email);
	}

	@GET
	@Path("listall")
	@UnitOfWork
	public List<Poster> listAll() {
		log.trace("listAll==>");
		List<Poster> posters = posterDAO.findAll();
		log.trace("<==listAll size=" + posters.size());
		return posters;
	}

	@GET
	@Path("print/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	@UnitOfWork
	public String getPDF(@PathParam("id") Long id) {
		log.trace("getPDF==> id=" + id);
		Poster poster = findSafely(id);
		log.debug("poster=" + poster);
		PosterWriter writer = new PosterWriter(poster);
		String fileName = writer.createPDF();
		return "pdf/" + fileName;
	}
	
//	@GET
//	@Path("print/{id}")
//	@Produces("application/pdf")
//	@UnitOfWork
//	public Response getPDF(@PathParam("id") Long id) {
//		log.trace("getPDF==> id=" + id);
//		Poster poster = findSafely(id);
//		log.debug("poster=" + poster);
//		PosterWriter writer = new PosterWriter(poster);
//		ByteArrayOutputStream baos = writer.createPDF();
//		StreamingOutput stream = new PDFStream(baos);
//		ResponseBuilder response = Response.ok(stream);
//		response.header("Expires", "0");
//		response.header("Cache-Control",
//	            "must-revalidate, post-check=0, pre-check=0");
//		response.header("Pragma", "public");
//		response.type("application/pdf");
//		log.trace("<==getPDF");
//		return response.build();
//	}

	@POST
	@Path("persist")
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public Poster persist(Poster poster) {
		log.trace("persist==>");
		posterDAO.create(poster);
		return poster;
	}

	@DELETE
	@Path("listall/{id}")
	@UnitOfWork
	public void delete(@PathParam("id") Long id) {
		log.debug("poster=" + id);
		Poster poster = findSafely(id);
		posterDAO.delete(poster);
	}

	private Poster findSafely(long id) {
		log.trace("findSafely==>");
		Poster poster = posterDAO.findById(id);
		if (poster == null) {
			poster = new Poster();
			log.info("poster with id=" + id + " not found new poster created.");
		}
		log.trace("<==findSafely");
		return poster;
	}

	private Poster findSafely(String email) {
		Poster poster = posterDAO.findByEmail(email);
		if (poster == null) {
			poster = new Poster();
			log.info("poster with email=" + email
					+ " not found new poster created.");
		}
		return poster;
	}

	class PDFStream implements StreamingOutput {
		
		private final ByteArrayOutputStream baos;
		
		public PDFStream(ByteArrayOutputStream baos) {
			this.baos = baos;
		}

		@Override
		public void write(OutputStream os) throws IOException,
				WebApplicationException {
			log.debug("write==> os=" + os);
			baos.writeTo(os);
			log.debug("<==wrote baos=" + baos.size());
		}
	}
}