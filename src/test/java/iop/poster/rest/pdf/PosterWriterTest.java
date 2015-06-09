package iop.poster.rest.pdf;

import iop.poster.rest.core.Beloved;
import iop.poster.rest.core.Like;
import iop.poster.rest.core.Poster;
import iop.poster.rest.core.Provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class PosterWriterTest {

	@Test
	public void testRun() {
		Poster poster = createPoster();
		PosterWriter app = new PosterWriter(poster);
//		File[] files = new File(PosterWriter.RESULT).listFiles();
//		for (File file : files) {
//			file.delete();
//		}
		app.run();
	}
	
//	@Test
	public void testWrite() {
	
		final float width = PageSize.LETTER.rotate().getWidth();
		final float height = PageSize.LETTER.rotate().getHeight();
		
		final float margin = width / (8.5F / .5F) / 2;
		
		final Rectangle rectPage = new Rectangle(width, height);
		final Rectangle rectInset = new Rectangle(rectPage.getLeft(margin), rectPage.getBottom(margin), rectPage.getRight(margin), rectPage.getTop(margin));
		
		final Rectangle rectDuration = new Rectangle(rectInset.getLeft(), rectInset.getBottom(), rectInset.getLeft() + rectInset.getWidth() / 2, rectInset.getBottom() + rectInset.getHeight() / 15 * 3);
		final Rectangle rectLogo = new Rectangle(rectDuration.getRight(), rectDuration.getBottom(), rectDuration.getRight() + rectInset.getWidth() / 2, rectInset.getBottom() + rectInset.getHeight() / 15 * 3);

		final Rectangle rectLikes =       new Rectangle(rectInset.getLeft(),        rectDuration.getTop(), rectInset.getLeft() + rectInset.getWidth() / 3,     rectDuration.getTop() + rectInset.getHeight() / 15 * 9);
		final Rectangle rectDescription = new Rectangle(rectLikes.getRight(),       rectLikes.getBottom(), rectInset.getLeft() + rectInset.getWidth() / 3 * 2, rectDuration.getTop() + rectInset.getHeight() / 15 * 9);
		final Rectangle rectBeloved =     new Rectangle(rectDescription.getRight(), rectLikes.getBottom(), rectInset.getLeft() + rectInset.getWidth() / 3 * 3, rectDuration.getTop() + rectInset.getHeight() / 15 * 9);
		
		final Rectangle rectName =         new Rectangle(rectInset.getLeft(), rectLikes.getTop(), rectInset.getLeft() + rectInset.getWidth() / 3 * 2, rectLikes.getTop() + rectInset.getHeight() / 15 * 3);
		final Rectangle rectPractitioner = new Rectangle(rectName.getRight(), rectLikes.getTop(), rectName.getRight() + rectInset.getWidth() / 3    , rectLikes.getTop() + rectInset.getHeight() / 15 * 3);
		
		FileOutputStream fos = null;
		try {
			File tmp = new File("/Users/gcr/temp/poster.pdf");
			fos = new FileOutputStream(tmp);
			Document document = new Document(PageSize.LETTER.rotate());
			PdfWriter writer = PdfWriter.getInstance(document, fos);
			document.open();
			PdfContentByte over = writer.getDirectContent();
			PdfContentByte under = writer.getDirectContentUnder();
			under.saveState();

			under.setColorFill(BaseColor.CYAN);
			under.rectangle(rectPage.getLeft(), rectPage.getBottom(), rectPage.getWidth(), rectPage.getHeight());
			under.fill();
			
			under.setColorFill(BaseColor.ORANGE);
			under.rectangle(rectInset.getLeft(), rectInset.getBottom(), rectInset.getWidth(), rectInset.getHeight());
			under.fill();
			
			under.setColorFill(BaseColor.PINK);
			under.rectangle(rectDuration.getLeft(), rectDuration.getBottom(), rectDuration.getWidth(), rectDuration.getHeight());
			under.fill();
						
			under.setColorFill(BaseColor.GREEN);
			under.rectangle(rectLogo.getLeft(), rectLogo.getBottom(), rectLogo.getWidth(), rectLogo.getHeight());
			under.fill();
			
			under.setColorFill(BaseColor.BLUE);
			under.rectangle(rectLikes.getLeft(), rectLikes.getBottom(), rectLikes.getWidth(), rectLikes.getHeight());
			under.fill();
			
			under.setColorFill(BaseColor.WHITE);
			under.rectangle(rectDescription.getLeft(), rectDescription.getBottom(), rectDescription.getWidth(), rectDescription.getHeight());
			under.fill();
			
			under.setColorFill(BaseColor.RED);
			under.rectangle(rectBeloved.getLeft(), rectBeloved.getBottom(), rectBeloved.getWidth(), rectBeloved.getHeight());
			under.fill();
			
			under.setColorFill(BaseColor.MAGENTA);
			under.rectangle(rectPractitioner.getLeft(), rectPractitioner.getBottom(), rectPractitioner.getWidth(), rectPractitioner.getHeight());
			under.fill();
			
			under.setColorFill(BaseColor.YELLOW);
			under.rectangle(rectName.getLeft(), rectName.getBottom(), rectName.getWidth(), rectName.getHeight());
			under.fill();
			
			under.restoreState();
			document.close();
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	Poster createPoster() {
		Poster poster = new Poster();
		poster.setId(1);
		poster.setEmail("geoffry.roberts@gmailcom");
		poster.setFirstName("Geoffry");
		poster.setLastName("Roberts");
		poster.setDescription("Wild and crazy guy!!");
		poster.setColor("#990000");
		poster.setBackground("#BDBDBD");
		poster.setPractitioner("James Hong");
		Provider provider = new Provider();
		provider.setId(100L);
		provider.setLogo("medstar-logo.png");
		provider.setTitle("Medstar");
		poster.setProvider(provider);
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-mm-dd");
		try {
			poster.setFrom(sdf.parse("2015-06-05"));
			poster.setTo(sdf.parse("2015-06-08"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
