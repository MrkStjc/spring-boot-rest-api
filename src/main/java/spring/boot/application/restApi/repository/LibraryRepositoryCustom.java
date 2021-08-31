package spring.boot.application.restApi.repository;

import spring.boot.application.restApi.domain.Library;

import java.util.List;

public interface LibraryRepositoryCustom {

  List<Library> findAllBooksByAuthor(String authorName);

}
