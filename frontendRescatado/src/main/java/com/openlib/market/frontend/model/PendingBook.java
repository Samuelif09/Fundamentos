package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PendingBook {
    private String id;
    private String title;
    private String author;
    private String sellerName;
    private double price;
    private String submittedAt;

    public PendingBook() {}

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

    // Observable properties for JavaFX TableView
    public StringProperty titleProperty()      { return new SimpleStringProperty(title); }
    public StringProperty authorProperty()     { return new SimpleStringProperty(author); }
    public StringProperty sellerNameProperty() { return new SimpleStringProperty(sellerName); }
    public StringProperty submittedAtProperty(){ return new SimpleStringProperty(submittedAt); }
    public DoubleProperty priceProperty()      { return new SimpleDoubleProperty(price); }
}
