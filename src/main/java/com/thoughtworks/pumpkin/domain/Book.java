package com.thoughtworks.pumpkin.domain;

public class Book {
    int id;
    String title;
    String snippet;
    String description;
    String isbn10;
    String isbn13;
    String thumbnail;
    String smallThumbnail;
    int pageCount;
    int rating;
    String authors;
    String publishers;
    String category;

    public Book(int id,String title, String snippet, String description, String isbn10, String isbn13, String thumbnail,
                String smallThumbnail, int pageCount, int rating, String authors, String publishers, String category) {
        this.id = id;
        this.title = title;
        this.snippet = snippet;
        this.description = description;
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.thumbnail = thumbnail;
        this.smallThumbnail = smallThumbnail;
        this.pageCount = pageCount;
        this.rating = rating;
        this.authors = authors;
        this.publishers = publishers;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getDescription() {
        return description;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getRating() {
        return rating;
    }

    public String getAuthors() {
        return authors;
    }

    public String getPublishers() {
        return publishers;
    }

    public String getCategory() {
        return category;
    }
}
