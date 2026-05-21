package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminChartData {

    private List<DataPoint> userGrowth;
    private List<DataPoint> revenueGrowth;

    public AdminChartData() {}

    public List<DataPoint> getUserGrowth() { return userGrowth; }
    public void setUserGrowth(List<DataPoint> userGrowth) { this.userGrowth = userGrowth; }

    public List<DataPoint> getRevenueGrowth() { return revenueGrowth; }
    public void setRevenueGrowth(List<DataPoint> revenueGrowth) { this.revenueGrowth = revenueGrowth; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataPoint {
        private String label;
        private double value;

        public DataPoint() {}
        public DataPoint(String label, double value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }

        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
    }
}
