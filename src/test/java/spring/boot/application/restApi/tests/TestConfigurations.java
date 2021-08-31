package spring.boot.application.restApi.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@TestConfiguration
public class TestConfigurations {

  @Bean
  public RestTemplateBuilder restTemplateBuilder() {
    return new RestTemplateBuilder()
            .basicAuthentication("mkyong", "password")
            .setConnectTimeout(Duration.ofSeconds(5));
  }

  @Bean
  public TestRestTemplate restTemplate() {
    return new TestRestTemplate();
  }

  @Bean
  public ObjectMapper mapper() {
    return new ObjectMapper();
  }

}
