package IBM.LP_12;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dataproviderexample.valuesfromexcel;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import pojoPackage.PojoClass;

import org.apache.commons.io.IOUtils;
import static org.hamcrest.Matchers.*;

public class Testing_basics {
	
	@Test
	public void firstTestCase(){
		System.out.println("Hello World!");
	}
	@Test(enabled=false)
	public void postMethod(){
		RestAssured.baseURI = "http://localhost:3000/";
		String requestBody = "{\"empid\":\"003\",\"firstname\":\"Pranjal2\",\"lastname\":\"Ranjan2\"}";
		
		RestAssured.given()
		.headers("Content-Type","application/json")
		.body(requestBody).
		when()
		.post("IBM").
		then()
		.statusCode(201).log().all();
		
	}
	
	@Test(enabled=false)
	public void putMethod(){
		RestAssured.baseURI = "http://localhost:3000/";
		String requestBody = "{\"empid\":\"003\",\"firstname\":\"Pranjal2\",\"lastname\":\"Srivastava\"}";
		
		RestAssured.given()
		.headers("Content-Type","application/json")
		.body(requestBody).
		when()
		.put("IBM/3").
		then()
		.statusCode(200).log().all();
		
	}
	
	@Test(enabled=false)
	public void patchMethod(){
		RestAssured.baseURI = "http://localhost:3000/";
		String requestBody = "{\"empid\":\"007\"}";
		
		RestAssured.given()
		.headers("Content-Type","application/json")
		.body(requestBody).
		when()
		.patch("IBM/3").
		then()
		.statusCode(200).log().all();
		
	}
	@Test(enabled=false)
	public void deleteMethod(){
		RestAssured.baseURI = "http://localhost:3000/";
		String requestBody = "{\"empid\":\"007\"}";
		
		RestAssured.given()
		
		.delete("IBM/3").
		then()
		.statusCode(200).log().all();
		
	}
	
	@Test(enabled=false)
	public void postMethodResponse(){
		RestAssured.baseURI = "http://localhost:3000/";
		String requestBody = "{\"empid\":\"003\",\"firstname\":\"Pranjal2\",\"lastname\":\"Ranjan2\"}";
		
		Response res=RestAssured.given()
		.headers("Content-Type","application/json")
		.body(requestBody).
		when()
		.post("IBM").
		then()
		.extract().response();
		String actFirstName = res.jsonPath().getString("firstname");
		System.out.println("firstname : "+ actFirstName);

Assert.assertEquals(actFirstName, "Pranjal2");
		
		
	}
	
	@Test(enabled=false)
	public void jsonBody(){
		RestAssured.baseURI = "http://localhost:3000/";
		JSONObject obj = new JSONObject();
		obj.put("empid", "004");
		obj.put("firstname", "Pranjal4");
		obj.put("lastname", "Ranjan4");
		
		RestAssured.given()
		.headers("Content-Type","application/json")
		.body(obj.toJSONString()).
		when()
		.post("IBM").
		then()
		.statusCode(201).log().all();
	}
	
	@Test(enabled=false)
	public void jsonNestedBody(){
		RestAssured.baseURI = "http://localhost:3000/";
		JSONObject obj = new JSONObject();
		JSONObject category = new JSONObject();
		JSONObject tags = new JSONObject();
		obj.put("empid", "004");
		obj.put("firstname", "Pranjal4");
		obj.put("lastname", "Ranjan4");
		category.put("id", 0);
		category.put("domain", "SDET");
		tags.put("project1", "Idea");
		tags.put("project2", "Amex");
		obj.put("category", category);
		obj.put("tags", tags);
		
		JSONArray arrayBody = new JSONArray();
		arrayBody.add("abc");
		arrayBody.add("123");
		
		obj.put("photo", arrayBody);
		
		
		System.out.println("jsonBody : " +obj.toJSONString());
		
		
		/*RestAssured.given()
		.headers("Content-Type","application/json")
		.body(obj.toJSONString()).
		when()
		.post("IBM").
		then()
		.statusCode(201).log().all();*/
	}
	
	@Test(enabled=false)
	public void fetchJsonBody() throws IOException{
		RestAssured.baseURI = "http://localhost:3000/";
        FileInputStream fis = new FileInputStream(".\\PayLoad\\postrequestbody.json");		
		
        given().headers("Content-Type","Application/json")
        .body(IOUtils.toString(fis,"UTF-8"))
        .when()
        .post("IBM").
		then()
		.statusCode(201).log().all();
        
	}
	
	@Test(enabled = false)
	public void assertions(){
		
		RestAssured.baseURI = "http://localhost:3000/";
		RestAssured.given()
		.get("IBM").
		then().body("firstname", hasItems("Pranjal","Pranjal1","Pranjal2")).log().all();
	}
	
	@Test(enabled = false)
	public void pojoExample() throws JsonProcessingException{
		RestAssured.baseURI = "http://localhost:3000/";
		
		PojoClass pojoObject = new PojoClass();
		pojoObject.setEmpid("008");
		pojoObject.setFirstname("Pranjal8");
		pojoObject.setLastname("Ranjan8 ");
		System.out.println(pojoObject.getFirstname());
		
		ObjectMapper obj = new ObjectMapper();
		String  reqBody = obj.writerWithDefaultPrettyPrinter().writeValueAsString(pojoObject);
		System.out.println(reqBody);
		
		given()
		  .contentType(ContentType.JSON)
		  .body(reqBody)
		.when()
		  .post("IBM")
	    .then()
		  .statusCode(201)
		  .log()
		  .all();
	}
	
	@Test(enabled = false)
	public void queryParameterExample() throws JsonProcessingException{
		RestAssured.baseURI = "https://petstore.swagger.io/v2/";
		given()
		   .param("status", "sold").log().all().
		when()
		   .get("pet/findByStatus").
		then()
		   .statusCode(200)
		   .log()
		   .all();
		   
		
	}
	
	@Test(enabled=true, dataProvider="Sample1")
	public void dataSourceExample(String empid, String firstname, String lastname)  {
		RestAssured.baseURI = "http://localhost:3000/";
		
		JSONObject reqBody = new JSONObject();
		reqBody.put("empid", empid);
		reqBody.put("firstname", firstname);
		reqBody.put("lastname", lastname);
		
		given()
		   .body(reqBody.toJSONString())
		   .headers("Content-Type","Application/json").
		when()
		   .post("IBM").
		then()
		   .log()
		   .all();
		   
		
	}
	@DataProvider(name="Sample1")
	public Object[][] data(){
		
		Object[][] data = new Object[2][3];
		data[0][0] ="001C";
		data[0][1] ="PranjalC";
		data[0][2] ="RanjanC";
		data[1][0] ="001D";
		data[1][1] ="PranjalD";
		data[1][2] ="RanjanD";
		
		
		
		
		return data;
		
	}
	
	@DataProvider(name = "valuesfromExcel")
	public Object[][] exceldata() throws IOException {
		Object[][] data = valuesfromexcel.gettestdata();

		return data;

	}
	
	@Test(enabled=true, dataProvider="valuesfromExcel")
	public void dataSourceExcelExample(String empid, String firstname, String lastname)  {
		RestAssured.baseURI = "http://localhost:3000/";
		
		JSONObject reqBody = new JSONObject();
		reqBody.put("empid", empid);
		reqBody.put("firstname", firstname);
		reqBody.put("lastname", lastname);
		
		given()
		   .body(reqBody.toJSONString())
		   .headers("Content-Type","Application/json").
		when()
		   .post("IBM").
		then()
		   .log()
		   .all();
		   
		
	}
	
	
	
	@Test
	public void soapexample() throws IOException {
		RestAssured.baseURI = "http://www.dneonline.com";

		FileInputStream fis = new FileInputStream(".\\Payload\\addreq.xml");

		given()
			.headers("content-type","text/xml")
			.body(IOUtils.toString(fis, "UTF-8")).
		when()
			.post("/calculator.asmx").
		then()
			.log().all();
	}
}
