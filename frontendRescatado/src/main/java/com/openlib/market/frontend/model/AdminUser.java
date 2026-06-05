package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUser {
    private String id;
    private String email;
    private String fullName;
    private String role;     // "C", "S", "A"
    private String status;   // "ACTIVE", "SUSPENDED", "PENDING"
    private String createdAt;

    public AdminUser() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // JavaFX Properties
    public StringProperty emailProperty()    { return new SimpleStringProperty(email); }
    public StringProperty fullNameProperty() { return new SimpleStringProperty(fullName); }
    public StringProperty roleProperty()     { return new SimpleStringProperty(role); }
    public StringProperty statusProperty()   { return new SimpleStringProperty(status); }
    public StringProperty createdAtProperty(){ return new SimpleStringProperty(createdAt); }
}
