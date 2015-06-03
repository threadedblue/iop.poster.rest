package iop.poster.rest.pdf;

import iop.poster.rest.core.Beloved;
import iop.poster.rest.core.Like;
import iop.poster.rest.core.Poster;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Set;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfWriter;

public class PosterWriter implements Runnable {

	public static final String RESULT = "/Users/gcr/Documents/proquoWorkspace/iop.poster.ajax/pdf";

	final int PT_NAME = 48;
	final int PT_LABEL_LG = 32;
	final int PT_TEXT_LG = 24;
	final int PT_LABEL_SM = 32;
	final int PT_TEXT_SM = 28;

	final float width = PageSize.LETTER.rotate().getWidth();
	final float height = PageSize.LETTER.rotate().getHeight();
	Font helvetica;
	BaseFont bf_helv;
	Document document;
	PdfContentByte canvas;
	Poster poster; 

	public PosterWriter(Poster poster) {
		super();
		this.poster = poster;
	}

	@Override
	public void run() {
		
		try {
			File tmp = File.createTempFile("poster-", ".pdf", new File(RESULT));
			FileOutputStream fos = new FileOutputStream(tmp);
			createPDF(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public String createPDF() {
		File tmp = null;
		try {
			tmp = File.createTempFile("poster-", ".pdf", new File(RESULT));
			FileOutputStream fos = new FileOutputStream(tmp);
			createPDF(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tmp.getName();
	}
	
	public OutputStream createPDF(OutputStream os) {

		document = new Document(PageSize.LETTER.rotate());
		try {
			helvetica = new Font(FontFamily.HELVETICA, 48);
			helvetica.setColor(25, 54, 24);
			bf_helv = helvetica.getCalculatedBaseFont(false);
			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();
			canvas = writer.getDirectContent();

			PdfContentByte under = writer.getDirectContentUnder();
			drawBackground(under, width, height);
	        under.setColorFill(new BaseColor(poster.getBackgroundAsColor().getRGB()));
	        under.rectangle(0F, 0F, width, height);
	        under.fill();

			canvas.beginText();
			canvas.setColorFill(new BaseColor(poster.getForegroundAsColor()
					.getRGB()));
			placeName(poster.getName());
			placeDoc(poster.getPractitioner());
			Set<Like> likes = poster.getLikes();
			placeLikes(likes);
			placeDescription(poster.getDescription());
			Set<Beloved> beloved = poster.getBeloved();
			placeBeloveds(beloved);
			placeStay(poster.getFromAsString(), poster.getToAsString());
			canvas.endText();
			canvas.saveState();
			URL url = null;
			url = getClass().getResource("/img/"
					+ poster.getProvider().getLogo());
			placeLogo(url);
			canvas.restoreState();
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return os;
	}
	
    public static void drawBackground(PdfContentByte content, float width, float height) {
        content.saveState();
        PdfGState state = new PdfGState();
        state.setFillOpacity(0.6f);
        content.setGState(state);
        content.setRGBColorFill(0xFF, 0xFF, 0xFF);
        content.setLineWidth(3);
        content.rectangle(0, 0, width, height);
        content.fillStroke();
        content.restoreState();
    }
	
	public void placeName(final String name) {

		canvas.setFontAndSize(bf_helv, PT_NAME);
		final float x = 24F;
		final float y = height - 22F - PT_NAME;
		placeText(name, x, y);
	}

	public void placeDoc(final String name) {

		canvas.setFontAndSize(bf_helv, PT_LABEL_LG);
		float x = 583F;
		float y = height - 27F - PT_LABEL_LG;
		placeText("Dr", x, y);

		canvas.setFontAndSize(bf_helv, PT_TEXT_LG);
		float x1 = 586F;
		float y1 = height - 64F - PT_TEXT_LG;
		placeText(name, x1, y1);
	}

	public void placeLikes(final Set<Like> likes) {

		canvas.setFontAndSize(bf_helv, PT_LABEL_SM);
		float x = 60F;
		float y = height - 160F - PT_LABEL_SM;
		placeText("Likes:", x, y);

		canvas.setFontAndSize(bf_helv, PT_TEXT_SM);
		float x1 = 80F;
		int i = 1;
		for (Like like : likes) {
			float y1 = height - 212F - PT_TEXT_SM * i;
			placeText(like.getLike(), x1, y1);
			i++;
		}
	}
	
	public void placeDescription(final String description) {
		canvas.setFontAndSize(bf_helv, PT_TEXT_LG);
		float x1 = 310F;
		float y1 = height - 160F - PT_TEXT_LG;
		placeText(description, x1, y1);
	}

	public void placeBeloveds(final Set<Beloved> beloveds) {

		canvas.setFontAndSize(bf_helv, PT_LABEL_SM);
		float x = 547F;
		float y = height - 160F - PT_LABEL_SM;
		placeText("Beloved:", x, y);

		canvas.setFontAndSize(bf_helv, PT_TEXT_SM);
		float x1 = 611F;
		int i = 1;
		for (Beloved beloved : beloveds) {
			float y1 = height - 212F - PT_TEXT_SM * i;
			placeText(beloved.getBeloved(), x1, y1);
			i++;
		}
	}

	public void placeStay(final String from, final String to) {

		canvas.setFontAndSize(bf_helv, PT_LABEL_LG);
		float x = 60F;
		float y = height - 477F - PT_LABEL_LG;
		placeText("Stay:", x, y);

		canvas.setFontAndSize(bf_helv, PT_TEXT_LG);
		float x1 = 60F;
		float y1 = height - 550F - PT_TEXT_LG;
		placeText(from + "--" + to, x1, y1);
	}

	public void placeText(String name, final float x, final float y) {
		if (name == null) {
			name = "";
		}
		canvas.showTextAligned(Element.ALIGN_LEFT, name, x, y, 0);
	}
	
	public void placeLogo(URL url) {
		try {
			Image img = Image.getInstance(url);
			final float x = 535F;
			final float y = height - 515F - 66F;

			img.setAbsolutePosition(x, y);
			document.add(img);
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
