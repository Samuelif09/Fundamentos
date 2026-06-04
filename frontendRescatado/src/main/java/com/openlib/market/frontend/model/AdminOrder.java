package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminOrder {
    private String orderId;
    private String buyerEmail;
    private double totalAmount;
    private String status;    // "COMPLETED", "REFUNDED", "PENDING"
    private String createdAt;

    public AdminOrder() {}

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getBuyerEmail() { return buyerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // JavaFX Properties
    public StringProperty orderIdProperty()    { return new SimpleStringProperty(orderId); }
    public StringProperty buyerEmailProperty() { return new SimpleStringProperty(buyerEmail); }
    public StringProperty statusProperty()     { return new SimpleStringProperty(status); }
    public StringProperty createdAtProperty()  { return new SimpleStringProperty(createdAt); }
    public DoubleProperty totalAmountProperty(){ return new SimpleDoubleProperty(totalAmount); }
}
