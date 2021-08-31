package spring.boot.application.restApi.repository;

import org.springframework.beans.factory.annotation.Autowired;
import spring.boot.application.restApi.domain.Library;

import java.util.List;
import java.util.stream.Collectors;

public class LibraryRepositoryImpl implements LibraryRepositoryCustom {

  @Autowired
  LibraryRepository repository;

  public List<Library> findAllBooksByAuthor(String authorName) {
    return repository.findAll()
            .stream()
            .filter(entity -> entity.getAuthor().equals(authorName))
            .collect(Collectors.toList());
  }

}
