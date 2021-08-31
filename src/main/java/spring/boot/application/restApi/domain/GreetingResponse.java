package spring.boot.application.restApi.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class GreetingResponse {

  private long id;

  private String content;

}
