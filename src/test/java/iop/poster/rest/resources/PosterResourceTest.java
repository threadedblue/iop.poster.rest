package iop.poster.rest.resources;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PosterResourceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

//	@Test
	public void testGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testPut() {
		given()
		.contentType("application/json")
		.body("{{'email', 'geoffry.roberts@gmail.com'}, {'provider', 'medstar'}}")
//		.param("provider", "medstar")
//		.queryParam("foreground", "#FFFFFF")
//		.queryParam("background", "#000000")
//		.queryParam("from", "2015-05-04")
//		.queryParam("to", "2015-05-07")
//		.queryParam("likes", "Food", "TV", "Car")
//		.queryParam("beloved", "Ruth", "Ely", "Jesse")		
		.when()
		.post("/app/poster")
		.then()
		.statusCode(200);
//		given()
//		.queryParam("email", "geoffry.roberts@gmail.com")
//		.when()
//		.get("/app/setup")
//		.then()
//		.statusCode(200);	
	}
}
