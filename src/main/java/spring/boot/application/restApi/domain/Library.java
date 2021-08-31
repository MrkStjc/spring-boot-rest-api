package spring.boot.application.restApi.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Books")
public class Library {

  @Column(name = "book_name")
  private String book_name;

  @Id // Primary key of table
  @Column(name = "id")
  private String id;

  @Column(name = "isbn")
  private String isbn;

  @Column(name = "aisle")
  private Integer aisle;

  @Column(name = "author")
  private String author;

}
