package com.openlib.market.infrastructure.adapter.in.web.dto;

public class PendingBookDto {
    private String id;
    private String title;
    private String author;
    private String sellerName;
    private double price;
    private String submittedAt;

    public PendingBookDto() {}

    public PendingBookDto(String id, String title, String author, String sellerName, double price, String submittedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.sellerName = sellerName;
        this.price = price;
        this.submittedAt = submittedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }
}
