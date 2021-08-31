package spring.boot.application.restApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.boot.application.restApi.domain.GreetingResponse;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

  @Autowired
  private GreetingResponse greetingResponse;

  private AtomicLong counter = new AtomicLong();

  @GetMapping("/greeting")
  public GreetingResponse greeting(@RequestParam(value = "name") String name) {
    greetingResponse.setContent(String.format("Hello %s", name));
    greetingResponse.setId(counter.incrementAndGet());
    return greetingResponse;
  }

}
