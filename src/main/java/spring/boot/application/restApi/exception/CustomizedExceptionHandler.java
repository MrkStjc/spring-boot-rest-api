package spring.boot.application.restApi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorDetail> handleResourceNotFoundException(ResourceNotFoundException re, WebRequest rq){

    ErrorDetail errDetail = new ErrorDetail(new Date(), re.getMessage(), rq.getDescription(false));

    return new ResponseEntity<> (errDetail, HttpStatus.NOT_FOUND);
  }

}
