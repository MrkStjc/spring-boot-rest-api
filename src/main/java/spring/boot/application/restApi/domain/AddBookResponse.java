package spring.boot.application.restApi.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AddBookResponse {

  private String msg;

  private String id;
}
