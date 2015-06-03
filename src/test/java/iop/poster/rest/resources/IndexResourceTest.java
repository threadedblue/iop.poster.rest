package iop.poster.rest.resources;

import static com.jayway.restassured.RestAssured.given;
import iop.poster.rest.core.Beloved;
import iop.poster.rest.core.Like;
import iop.poster.rest.core.Provider;

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

public class IndexResourceTest {

	static Connection conn;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	
	@BeforeClass
	public static void beforeClass() {
//		String url = "jdbc:postgresql://localhost/gcr";
//		Properties props = new Properties();
//		props.setProperty("user","gcr");
//		props.setProperty("password","");
	}
	
	@Before
	public void setUp() throws Exception {
	}

//	@Test
	public void testGet() {
		given().when().get("/app").then().statusCode(200);
	}
	
//	@Test
	public void testGetById() {
		given()
		.when()
		.get("/app/id/383")
		.then()
		.statusCode(200);
	}
	
//	@Test
	public void testGetByEmail() {
		final String email = "ghi@gmail.com";
		given()
		.when()
		.get("/app/email/" + email)
		.then()
		.statusCode(200);
	}
	
//	@Test
	public void testListAll() {
		given()
		.when()
		.get("/app/listall")
		.then()
		.statusCode(200);
	}
	
	@Test
	public void testPDF() {
		given()
		.when()
		.get("/app/print/383")
		.then()
		.statusCode(200);
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
