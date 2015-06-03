package iop.poster.rest;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import iop.poster.rest.core.Beloved;
import iop.poster.rest.core.Like;
import iop.poster.rest.core.Poster;
import iop.poster.rest.core.Provider;
import iop.poster.rest.db.PosterDAO;
import iop.poster.rest.db.ProviderDAO;
import iop.poster.rest.resources.IndexResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PosterApplication extends Application<PosterConfiguration> {

	private static Logger log = LoggerFactory
			.getLogger(PosterApplication.class);

	private final HibernateBundle<PosterConfiguration> hibernate = new HibernateBundle<PosterConfiguration>(Poster.class, Provider.class, Like.class, Beloved.class) {
	    @Override
	    public DataSourceFactory getDataSourceFactory(PosterConfiguration configuration) {
	        return configuration.getDataSourceFactory();
	    }
	};
	
	@Override
	public void initialize(Bootstrap<PosterConfiguration> bootstrap) {
		log.debug("initialize==>");
		bootstrap.addBundle(hibernate);
	}

	@Override
	public void run(PosterConfiguration configuration, Environment environment)
			throws Exception {
		log.debug("run==>");
		final PosterDAO posterDAO = new PosterDAO(hibernate.getSessionFactory());
		final ProviderDAO providerDAO = new ProviderDAO(hibernate.getSessionFactory());
				
		environment.jersey().register(
				new IndexResource(posterDAO, providerDAO));
		log.debug("<==run");
	}

	public static void main(String[] args) throws Exception {
		log.info("start==>");
		PosterApplication app = new PosterApplication();
		app.run(args);
	}
}