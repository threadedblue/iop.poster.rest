package iop.poster.rest.core;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PosterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		String[] abc = {"abc@yahoo.com", "Maisel1", "LaFong1"};
		String[] def = {"def@hotmail.com", "Maisel2", "LaFong2"};
		String[] ghi = {"ghi@yahoo.com", "Maisel3", "LaFong3"};
		String[] jkl = {"jkl@hotmail.com", "Maisel4", "LaFong4"};
		String[] mno = {"mno@hotmail.com", "Maisel5", "LaFong5"};
		String[][] sss = {abc, def, ghi, jkl, mno};
		for (int i = 0; i < sss.length; i++) {
			Poster app = createPoster(sss[i][0], sss[i][1], sss[i][2]);
			
		}
		
	}

	Poster createPoster(String email, String firstName, String lastName) {
		Poster app = new Poster();
		app.setEmail(email);
		app.setFirstName(firstName);
		app.setLastName(lastName);
		app.setLikes(createLikes());
		app.setBeloved(createBeloveds());
		
		return app;
	}
	
	Set<Like> createLikes() {
		Set<Like> set = new HashSet<Like>();
		Like like1 = new Like();
		like1.setLike("Food");
		set.add(like1);
		Like like2 = new Like();
		like2.setLike("Fun");
		set.add(like2);
		Like like3 = new Like();
		like3.setLike("TV");
		set.add(like3);
		
		return set;
	}

	Set<Beloved> createBeloveds() {
		Set<Beloved> set = new HashSet<Beloved>();
		Beloved like1 = new Beloved();
		like1.setBeloved("Food");
		set.add(like1);
		Beloved like2 = new Beloved();
		like2.setBeloved("Fun");
		set.add(like2);
		Beloved like3 = new Beloved();
		like3.setBeloved("TV");
		set.add(like3);
		
		return set;
	}

}
