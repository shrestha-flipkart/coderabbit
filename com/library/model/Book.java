package com.library.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a book in the library system.
 * Contains all the metadata and state information about a book.
 */
public class Book {
    private final String id;
    private String title;
    private String author;
    private String isbn;
    private LocalDate publishDate;
    private BookStatus status;
    private BookCategory category;

    /**
     * Constructor for creating a new book.
     * 
     * @param title The title of the book
     * @param author The author of the book
     * @param isbn The ISBN of the book
     * @param publishDate The publication date of the book
     * @param category The category of the book
     */
    public Book(String title, String author, String isbn, LocalDate publishDate, BookCategory category) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.status = BookStatus.AVAILABLE;
        this.category = category;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publishDate=" + publishDate +
                ", status=" + status +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
