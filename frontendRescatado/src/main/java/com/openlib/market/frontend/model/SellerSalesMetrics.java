package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerSalesMetrics {
    private int totalBooksSold;
    private Map<String, Integer> monthlySales; // e.g. "Jan": 12, "Feb": 25

    public SellerSalesMetrics() {}

    public int getTotalBooksSold() { return totalBooksSold; }
    public void setTotalBooksSold(int totalBooksSold) { this.totalBooksSold = totalBooksSold; }

    public Map<String, Integer> getMonthlySales() { return monthlySales; }
    public void setMonthlySales(Map<String, Integer> monthlySales) { this.monthlySales = monthlySales; }
}
