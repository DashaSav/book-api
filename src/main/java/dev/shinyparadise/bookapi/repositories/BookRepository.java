package dev.shinyparadise.bookapi.repositories;

import dev.shinyparadise.bookapi.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "books", collectionResourceRel = "books")
public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findByAuthor(String author);
    Optional<Book> findByTitle(String title);
}
