package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerBook {
    @JsonProperty("isbn")
    private String id;
    
    @JsonProperty("titulo")
    private String title;
    
    @JsonProperty("precio")
    private double price;
    
    @JsonProperty("stock")
    private int stock;
    
    @JsonProperty("estado")
    private String status;

    public SellerBook() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // JavaFX Observable properties for TableView binding
    public StringProperty idProperty() { return new SimpleStringProperty(id); }
    public StringProperty titleProperty() { return new SimpleStringProperty(title); }
    public DoubleProperty priceProperty() { return new SimpleDoubleProperty(price); }
    public IntegerProperty stockProperty() { return new SimpleIntegerProperty(stock); }
    public StringProperty statusProperty() { return new SimpleStringProperty(status); }
}
