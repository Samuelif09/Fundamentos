package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreProfile {
    private String storeName;
    private String description;
    private String bannerUrl;
    private String contactEmail;

    public StoreProfile() {}

    public String getStoreName()    { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getDescription()  { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBannerUrl()    { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
}
