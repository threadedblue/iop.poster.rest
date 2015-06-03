package iop.poster.rest.pdf;

import iop.poster.rest.core.Beloved;
import iop.poster.rest.core.Like;
import iop.poster.rest.core.Poster;
import iop.poster.rest.core.Provider;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class PosterWriterTest {

	@Test
	public void testRun() {
		Poster poster = createPoster();
		PosterWriter app = new PosterWriter(poster);
		File[] files = new File(PosterWriter.RESULT).listFiles();
		for (File file : files) {
			file.delete();
		}
		app.createPDF();
	}

	Poster createPoster() {
		Poster poster = new Poster();
		poster.setId(1);
		poster.setEmail("geoffry.roberts@gmailcom");
		poster.setFirstName("Geoffry");
		poster.setLastName("Roberts");
		poster.setDescription("Wild and crazy guy!!");
		poster.setColor("#000000");
		poster.setBackground("#FF9999");
		poster.setPractitioner("Dr. James Hong");
		Provider provider = new Provider();
		provider.setId(100L);
		provider.setLogo("medstar-logo.png");
		provider.setTitle("Medstar");
		poster.setProvider(provider);
		poster.setFrom(new Date());
		poster.setTo(new Date());
		Set<Like> likes = new HashSet<Like>();
		likes.add(new Like(10L, "Food"));
		likes.add(new Like(20L, "TV"));
		likes.add(new Like(30L, "prickly heat"));
		poster.setLikes(likes);
		Set<Beloved> beloved = new HashSet<Beloved>();
		beloved.add(new Beloved(10L, "Ruth"));
		beloved.add(new Beloved(20L, "Ely"));
		beloved.add(new Beloved(30L, "Jesse"));
		poster.setBeloved(beloved);
		return poster;
	}

}
