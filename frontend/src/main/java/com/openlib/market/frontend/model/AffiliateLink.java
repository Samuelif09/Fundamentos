package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AffiliateLink {
    private String id;
    private String label;   // Display name e.g. "Instagram"
    private String url;     // Destination URL
    private String code;    // Affiliate code

    public AffiliateLink() {}

    public AffiliateLink(String label, String url, String code) {
        this.label = label;
        this.url   = url;
        this.code  = code;
    }

    public String getId()    { return id; }
    public void setId(String id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getUrl()   { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getCode()  { return code; }
    public void setCode(String code) { this.code = code; }
}
