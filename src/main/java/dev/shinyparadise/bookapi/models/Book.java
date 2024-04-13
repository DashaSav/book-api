package dev.shinyparadise.bookapi.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Book {
    @Id private String id;
    private String title;
    private String author;
    private String description;
    private List<Genre> genres;
}
