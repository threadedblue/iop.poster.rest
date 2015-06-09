package iop.poster.rest.pdf;

import iop.poster.rest.core.Beloved;
import iop.poster.rest.core.Like;
import iop.poster.rest.core.Poster;

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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
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

	final float margin = width / (8.5F / .5F) / 2;

	final Rectangle rectPage = new Rectangle(width, height);
	final Rectangle rectInset = new Rectangle(rectPage.getLeft(margin),
			rectPage.getBottom(margin), rectPage.getRight(margin),
			rectPage.getTop(margin));

	final Rectangle rectStay = new Rectangle(rectInset.getLeft(),
			rectInset.getBottom(), rectInset.getLeft() + rectInset.getWidth()
					/ 2, rectInset.getBottom() + rectInset.getHeight() / 15 * 3);
	final Rectangle rectLogo = new Rectangle(rectStay.getRight(),
			rectStay.getBottom(), rectStay.getRight() + rectInset.getWidth()
					/ 2, rectInset.getBottom() + rectInset.getHeight() / 15 * 3);

	final Rectangle rectLikes = new Rectangle(rectInset.getLeft(20),
			rectStay.getTop(), rectInset.getLeft() + rectInset.getWidth() / 3,
			rectStay.getTop() + rectInset.getHeight() / 15 * 9);
	final Rectangle rectDescription = new Rectangle(rectLikes.getRight(),
			rectLikes.getBottom(), rectInset.getLeft() + rectInset.getWidth()
					/ 3 * 2, rectStay.getTop() + rectInset.getHeight() / 15 * 9);
	final Rectangle rectBeloved = new Rectangle(rectDescription.getRight(),
			rectLikes.getBottom(), rectInset.getLeft() + rectInset.getWidth() - 20
					/ 3 * 3, rectStay.getTop() + rectInset.getHeight() / 15 * 9);

	final Rectangle rectName = new Rectangle(rectInset.getLeft(),
			rectLikes.getTop(), rectInset.getLeft() + rectInset.getWidth() / 3
					* 2, rectLikes.getTop() + rectInset.getHeight() / 15 * 3);
	final Rectangle rectPractitioner = new Rectangle(rectName.getRight(),
			rectLikes.getTop(), rectName.getRight() + rectInset.getWidth() / 3,
			rectLikes.getTop() + rectInset.getHeight() / 15 * 3);

	Font helvetica;
	BaseFont bf_helv;
	Document document;
	PdfContentByte over;
	PdfContentByte under;
	Poster poster;
	
	final BaseColor color;
	final BaseColor background;

	public PosterWriter(Poster poster) {
		super();
		this.poster = poster;
		this.color = new BaseColor(poster.getForegroundAsColor()
				.getRGB());
		this.background = new BaseColor(poster.getBackgroundAsColor()
				.getRGB());
		}

	@Override
	public void run() {

		try {
			// File tmp = File.createTempFile("poster-", ".pdf", new
			// File(RESULT));
			File tmp = new File("/Users/gcr/temp/poster.pdf");
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
			helvetica = new Font(FontFamily.HELVETICA, 12);
			bf_helv = helvetica.getCalculatedBaseFont(false);
			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();
			drawBackground(writer);
			over = writer.getDirectContent();
			placeDescription(rectDescription, poster.getDescription());

			over.beginText();
			over.setColorFill(color);
			placeName(rectName, poster.getName());
			placePractitioner(rectPractitioner, poster.getPractitioner());
			Set<Like> likes = poster.getLikes();
			placeLikes(rectLikes, likes);
			Set<Beloved> beloved = poster.getBeloved();
			placeBeloveds(rectBeloved, beloved);
			placeStay(rectStay, poster.getFromAsString(),
					poster.getToAsString());
			over.endText();
			over.saveState();
			URL url = getClass().getResource(
					"/img/" + poster.getProvider().getLogo());
			placeLogo(rectLogo, url);
			over.restoreState();
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return os;
	}

	public void drawBackground(PdfWriter writer) {
		under = writer.getDirectContentUnder();
		under.setColorFill(background);
		under.rectangle(rectInset);
		under.setColorFill(background);
		under.rectangle(rectInset.getLeft(), rectInset.getBottom(),
				rectInset.getWidth(), rectInset.getHeight());
		under.fill();
	}

	public void placeName(final Rectangle rect, final String name) {
		float margin = 10F;
		over.setFontAndSize(bf_helv, PT_NAME);
		over.setColorFill(new BaseColor(poster.getForegroundAsColor().getRGB()));
		float x = rect.getLeft(margin);
		float y = calcTextBsl(name, rect);
		placeText(x, y, name, Element.ALIGN_LEFT);
	}

	public void placePractitioner(final Rectangle rect, final String text) {

		float margin = 20F;
		final String LABEL = "My Dr.";
		over.setFontAndSize(bf_helv, PT_LABEL_LG);
		float x = rect.getLeft(margin);
		float btm = rect.getBottom(rect.getHeight() - PT_LABEL_LG);
		Rectangle rectLabel = new Rectangle(x, btm, rect.getRight(),
				rect.getTop(margin));
		float y = calcTextBsl(LABEL, rectLabel);

		placeText(x, y, LABEL, Element.ALIGN_LEFT);

		float margin1 = 30F;
		over.setFontAndSize(bf_helv, PT_TEXT_LG);
		float x1 = rect.getLeft(margin1);
		float top1 = rect.getTop(rectLabel.getHeight() + margin1);
		Rectangle rectText = new Rectangle(x1, rect.getBottom(margin1), rect.getRight(),
				top1);
		float y1 = calcTextBsl(text, rectText);
		placeText(x1, y1, text, Element.ALIGN_LEFT);
	}

	float calcTextBsl(final String text, final Rectangle rect) {
		// Calculates the baseline for positioning text.
		// We get the full height i.e. the ascender.
		float asc = bf_helv.getAscentPoint(text, PT_NAME);
		// We calculate the vertical midpoint of the bounding rectangle.
		float mid = rect.getHeight() / 2;
		// We get the bottom of the bounding rectangle.
		float btm = rect.getBottom();
		// We calculate the baseline by subtracting the vertical midpoint from
		// the midpoint of
		// the full height;
		float bsl = mid - asc / 2;
		// We calculate the return value as the sum of bottom and the baseline.
		float pos = btm + bsl;
		return pos;
	}

	public void placeLikes(final Rectangle rect, final Set<Like> likes) {

		final String LABEL = "I Like";
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(1);
		rect.setBorderColor(color);
		under.rectangle(rect);
		float margin = 10F;
		over.setFontAndSize(bf_helv, PT_LABEL_SM);
		float x = rect.getLeft() + (rect.getRight() - rect.getLeft()) / 2;
		float btm = rect.getBottom(rect.getHeight() - PT_LABEL_SM);
		Rectangle rectLabel = new Rectangle(x, btm, rect.getRight(),
				rect.getTop());
		float y = calcTextBsl(LABEL, rectLabel);
		placeText(x, y, LABEL, Element.ALIGN_CENTER);

		float margin1 = 20F;
		over.setFontAndSize(bf_helv, PT_TEXT_SM);
		float x1 = x;
		float top1 = rect.getTop(rectLabel.getHeight());
		Rectangle rectText = new Rectangle(x, rect.getBottom(),
				rect.getRight(), top1);
		float y1 = rectText.getTop(margin1);
		for (Like like : likes) {
			y1 -= PT_TEXT_SM + 5F;
			placeText(x1, y1, like.getLike(), Element.ALIGN_CENTER);
		}
	}

	public void placeDescription(final Rectangle rect, final String text) {
		Font font = new Font(FontFamily.HELVETICA, PT_TEXT_SM, Font.BOLD, new BaseColor(poster.getForegroundAsColor().getRGB()));
		Phrase pg = new Phrase(text, font);
		ColumnText col = new ColumnText(over);
		col.setSimpleColumn(pg, rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop(), 35F, Element.ALIGN_CENTER);
		URL url = getClass().getResource("/img/crazy-guy.png");
		
		Rectangle rectImg = new Rectangle(rect.getLeft(), rect.getBottom(margin), rect.getRight(), rect.getTop(margin * 6));
		try {
			col.go();
			placeImage(rectImg, url);			
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
	}

	public void placeBeloveds(final Rectangle rect, final Set<Beloved> beloveds) {

		final String LABEL = "My beloved";
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(1);
		rect.setBorderColor(color);
		under.rectangle(rect);
		float margin = 10F;
		over.setFontAndSize(bf_helv, PT_LABEL_SM);
		float x = rect.getLeft() + (rect.getRight() - rect.getLeft()) / 2;
		float btm = rect.getBottom(rect.getHeight() - PT_LABEL_SM);
		Rectangle rectLabel = new Rectangle(x, btm, rect.getRight(),
				rect.getTop());
		float y = calcTextBsl(LABEL, rectLabel);
		placeText(x, y, LABEL, Element.ALIGN_CENTER);

		float margin1 = 20F;
		over.setFontAndSize(bf_helv, PT_TEXT_SM);
		float x1 = x;
		float top1 = rect.getTop(rectLabel.getHeight());
		Rectangle rectText = new Rectangle(x, rect.getBottom(),
				rect.getRight(), top1);
		float y1 = rectText.getTop(margin1);
		for (Beloved beloved : beloveds) {
			y1 -= PT_TEXT_SM + 5F;
			placeText(x1, y1, beloved.getBeloved(), Element.ALIGN_CENTER);
		}
	}

	public void placeStay(final Rectangle rect, final String from,
			final String to) {

		final String LABEL = "My time here";
		float margin = 20F;
		over.setFontAndSize(bf_helv, PT_LABEL_LG);
		float x = rect.getLeft(margin);
		float btm = rect.getBottom(rect.getHeight() - PT_LABEL_LG);
		Rectangle rectLabel = new Rectangle(x, btm, rect.getRight(),
				rect.getTop(margin));
		float y = calcTextBsl(LABEL, rectLabel);

		placeText(x, y, LABEL, Element.ALIGN_LEFT);

		float margin1 = 20F;
		over.setFontAndSize(bf_helv, PT_TEXT_LG);
		float x1 = rect.getLeft(margin1);
		float top1 = rect.getTop(rectLabel.getHeight());
		Rectangle rectText = new Rectangle(x1, rect.getBottom(),
				rect.getRight(), top1);
		float y1 = calcTextBsl(from, rectText);
		placeText(x1, y1, from + " -- " + to, Element.ALIGN_LEFT);
	}

	public void placeText(final float x, final float y, String text, int align) {
		over.showTextAligned(align, text, x, y, 0);
	}

	public void placeLogo(final Rectangle rect, URL url) {
		
//		placeImage(rectLogo, url);
		try {
//			float wid = rect.getWidth();
			float hgt = rect.getHeight();
			Image img = Image.getInstance(url);
			float imgWid = img.getWidth();
			float imgHgt = img.getHeight();
//			float mgnX = (wid - imgWid) / 2F;
			float mgnY = (hgt - imgHgt) / 2F;
			float x = rect.getRight(250);
			float y = rect.getBottom(mgnY);
			img.setAbsolutePosition(x, y);
			document.add(img);
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public void placeImage(final Rectangle rect, URL url) {
		try {
			float wid = rect.getWidth();
			float hgt = rect.getHeight();
			Image img = Image.getInstance(url);
			float imgWid = img.getWidth();
			float imgHgt = img.getHeight();
			float mgnX = (wid - imgWid) / 2F;
			float mgnY = (hgt - imgHgt) / 2F;
			float x = rect.getLeft(mgnX);
			float y = rect.getBottom(mgnY);
			img.setAbsolutePosition(x, y);
			document.add(img);
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	void fill(PdfContentByte under) {
		under.saveState();

//		under.setColorFill(BaseColor.CYAN);
//		under.rectangle(rectPage.getLeft(), rectPage.getBottom(),
//				rectPage.getWidth(), rectPage.getHeight());
//		under.fill();

//		under.setColorFill(BaseColor.ORANGE);
//		under.rectangle(rectInset.getLeft(), rectInset.getBottom(),
//				rectInset.getWidth(), rectInset.getHeight());
//		under.fill();

//		under.setColorFill(BaseColor.PINK);
//		under.rectangle(rectStay.getLeft(), rectStay.getBottom(),
//				rectStay.getWidth(), rectStay.getHeight());
//		under.fill();

//		under.setColorFill(BaseColor.GREEN);
//		under.rectangle(rectLogo.getLeft(), rectLogo.getBottom(),
//				rectLogo.getWidth(), rectLogo.getHeight());
//		under.fill();

//		under.setColorFill(BaseColor.BLUE);
//		under.rectangle(rectLikes.getLeft(), rectLikes.getBottom(),
//				rectLikes.getWidth(), rectLikes.getHeight());
//		under.fill();

//		under.setColorFill(BaseColor.WHITE);
//		under.rectangle(rectDescription.getLeft(), rectDescription.getBottom(),
//				rectDescription.getWidth(), rectDescription.getHeight());
//		under.fill();

//		under.setColorFill(BaseColor.RED);
//		under.rectangle(rectBeloved.getLeft(), rectBeloved.getBottom(),
//				rectBeloved.getWidth(), rectBeloved.getHeight());
//		under.fill();

//		under.setColorFill(BaseColor.MAGENTA);
//		under.rectangle(rectPractitioner.getLeft(),
//				rectPractitioner.getBottom(), rectPractitioner.getWidth(),
//				rectPractitioner.getHeight());
//		under.fill();

//		under.setColorFill(BaseColor.YELLOW);
//		under.rectangle(rectName.getLeft(), rectName.getBottom(),
//				rectName.getWidth(), rectName.getHeight());
//		under.fill();

		under.restoreState();
	}
}
