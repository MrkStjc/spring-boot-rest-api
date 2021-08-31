package spring.boot.application.restApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.boot.application.restApi.domain.Library;

@Repository
public interface LibraryRepository extends JpaRepository<Library, String>, LibraryRepositoryCustom {
}