package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysPaymentMethod {
    private String id;
    private String name;
    private String provider;
    private String status; // ACTIVE, INACTIVE

    public SysPaymentMethod() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public StringProperty nameProperty() { return new SimpleStringProperty(name); }
    public StringProperty providerProperty() { return new SimpleStringProperty(provider); }
}
