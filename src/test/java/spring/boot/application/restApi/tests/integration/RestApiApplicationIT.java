package spring.boot.application.restApi.tests.integration;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import spring.boot.application.restApi.tests.TestConfigurations;
import spring.boot.application.restApi.domain.Library;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfigurations.class)
public class RestApiApplicationIT {

  // We make calls to API using TestRestTemplate - it is provided by spring boot.

  @Autowired
  private RestTemplateBuilder builder;

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private Integer port;

  //TODO: Refactor this shit
  @Test
  public void getAuthorNameBooksTest() throws Exception {
    String expected = "[\n" +
            "    {\n" +
            "        \"book_name\": \"Appium\",\n" +
            "        \"id\": \"fdsefr343\",\n" +
            "        \"isbn\": \"fdsefr3\",\n" +
            "        \"aisle\": 43,\n" +
            "        \"author\": \"Mrkonja\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"book_name\": \"Selenium\",\n" +
            "        \"id\": \"test666\",\n" +
            "        \"isbn\": \"test\",\n" +
            "        \"aisle\": 666,\n" +
            "        \"author\": \"Mrkonja\"\n" +
            "    }\n" +
            "]";
    ResponseEntity<String> response = restTemplate
            .getForEntity("http://localhost:8080/api/library/getBooks/author?authorname=Mrkonja", String.class);
    JSONAssert.assertEquals(response.getBody(), expected, false);
  }


  //TODO: Verify response header also...
  @Test
  public void addBookTest() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Library> request = new HttpEntity(buildLibrary(), headers);
    ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/api/library/addBook", request, String.class);
  }

  private Library buildLibrary() {
    Library lib = new Library();
    lib.setAuthor("Mirko");
    lib.setBook_name("Testonja");
    lib.setAisle(666);
    lib.setIsbn("ZEMUN");
    lib.setId("OLDZEMUN666");
    return lib;
  }

}
