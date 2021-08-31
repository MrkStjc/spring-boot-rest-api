package spring.boot.application.restApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.boot.application.restApi.repository.LibraryRepository;

@Service
public class LibraryService {

  @Autowired
  private LibraryRepository repository;

  public String buildId(String isbn, Integer aisle) {
    if (isbn.startsWith("Z")) {
      return "OLD" + isbn + aisle;
    }
    return isbn + aisle;
  }

  public boolean doesBookExist(String id) {
    return repository.findById(id).isPresent();
  }

}
