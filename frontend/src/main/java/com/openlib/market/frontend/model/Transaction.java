package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    private String transactionId;
    private String date;
    private String type;        // "SALE" | "COMMISSION" | "WITHDRAWAL"
    private String description;
    private double amount;

    public Transaction() {}

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    // Observable properties for TableView binding
    public StringProperty transactionIdProperty() { return new SimpleStringProperty(transactionId); }
    public StringProperty dateProperty()          { return new SimpleStringProperty(date); }
    public StringProperty typeProperty()          { return new SimpleStringProperty(type); }
    public StringProperty descriptionProperty()   { return new SimpleStringProperty(description); }
    public DoubleProperty amountProperty()        { return new SimpleDoubleProperty(amount); }
}
