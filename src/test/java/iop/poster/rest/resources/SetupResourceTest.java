package iop.poster.rest.resources;

import static com.jayway.restassured.RestAssured.given;
import iop.poster.rest.core.Beloved;
import iop.poster.rest.core.Like;
import iop.poster.rest.core.Poster;
import iop.poster.rest.core.Provider;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.http.ContentType;

public class SetupResourceTest {
	
	static Connection conn;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	
	@BeforeClass
	public static void beforeClass() {
		String url = "jdbc:postgresql://localhost/gcr";
		Properties props = new Properties();
		props.setProperty("user","gcr");
		props.setProperty("password","");
		try {
			conn = DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
        Handler fh = new ConsoleHandler(); // FileHandler("/tmp/jersey_test.log");
        Logger.getLogger("").addHandler(fh);
        Logger.getLogger("com.sun.jersey").setLevel(Level.FINEST);
	}
	
	@Before
	public void setUp() {
//		try {
//			Statement stmt = conn.createStatement();
//			stmt.execute("delete from poster");
//			stmt.execute("delete from poster_aux");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
	
	@Test
	public void testGetByEmail() {
		final String email = "ghi@gmail.com";
		given()
		.when()
		.get("/app/" + email)
		.then()
		.statusCode(200);
	}
	
	@Test
	public void testListAll() {
		given()
		.when()
		.get("/app/listall")
		.then()
		.statusCode(200);
	}
	
	@Test
	public void testPostGetSetup() {
		final String email = "abc@gmail.com";
//		final String email = "def@gmail.com";
//		final String email = "ghi@gmail.com";
//		final String email = "jkl@gmail.com";
//		final String email = "mno@gmail.com";
		Poster poster = new Poster();
		poster.setEmail(email);
		poster.setFirstName("Maisel1");
		poster.setLastName("La Fong1");
		poster.setBackground("#FFFFFF");
		poster.setColor("#000000");
		poster.setFrom(createDate("05-May-2015"));
		poster.setTo(createDate("07-May-2015"));
		poster.setLikes(createLikes());
		poster.setBeloved(createBeloved());
		
		String pojoAsString = null;
		try {
			Provider provider = createProvider();
			poster.setProvider(provider);
			ObjectMapper mapper = new ObjectMapper();
			StringWriter writer = new StringWriter();
			mapper.writeValue(writer, poster);
			pojoAsString = writer.toString();
			System.out.println("json=" + pojoAsString);
			Poster poster1 = mapper.readValue(pojoAsString, Poster.class);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		given()
		.content(pojoAsString)
		.with()
		.contentType(ContentType.JSON)
		.and()
		.expect()
		.when()
		.post("/app/setup/put")
		.then()
		.statusCode(200);	   
		   
//		given()
//		.when()
//		.get("/app/setup/" + email)
//		.then()
//		.statusCode(200);
	}
	
	Date createDate(String sDate) {
		try {
	 
			System.out.println(sDate);
			return sdf.parse(sDate);
	 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}
	
	Provider createProvider() throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from providers where title like 'Med%' ");
		rs.next();
		Provider provider = new Provider();
		provider.setId(rs.getLong(1));
		provider.setLogo(rs.getString(2));
		provider.setTitle(rs.getString(3));
		return provider;
	}
	
	Set<Like> createLikes() {
		Like like1 = new Like();
		like1.setLike("food");
		Like like2 = new Like();
		like2.setLike("fun");
		Like like3 = new Like();
		like3.setLike("tv");
		Set<Like> likes = new HashSet<Like>();
		likes.add(like1);
		likes.add(like2);
		likes.add(like3);
		return likes;
	}
	
	Set<Beloved> createBeloved() {
		Beloved beloved1 = new Beloved();
		beloved1.setBeloved("Ruth");
		Beloved beloved2 = new Beloved();
		beloved2.setBeloved("Ely");
		Beloved beloved3 = new Beloved();
		beloved3.setBeloved("Jesse");
		Set<Beloved> beloved = new HashSet<Beloved>();
		beloved.add(beloved1);
		beloved.add(beloved2);
		beloved.add(beloved3);
		return beloved;
	}
}
