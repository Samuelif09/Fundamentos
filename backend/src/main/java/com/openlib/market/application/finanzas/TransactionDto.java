package com.openlib.market.application.finanzas;

public class TransactionDto {
    private String transactionId;
    private String date;
    private String type;
    private String description;
    private double amount;

    public TransactionDto(String transactionId, String date, String type, String description, double amount) {
        this.transactionId = transactionId;
        this.date = date;
        this.type = type;
        this.description = description;
        this.amount = amount;
    }

    public String getTransactionId() { return transactionId; }
    public String getDate() { return date; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
}
