package com.openlib.market.infrastructure.adapter.in.web.dto;

import java.util.List;

public class AdminChartDataDto {
    private List<DataPoint> userGrowth;
    private List<DataPoint> revenueGrowth;

    public AdminChartDataDto() {}

    public AdminChartDataDto(List<DataPoint> userGrowth, List<DataPoint> revenueGrowth) {
        this.userGrowth = userGrowth;
        this.revenueGrowth = revenueGrowth;
    }

    public List<DataPoint> getUserGrowth() { return userGrowth; }
    public void setUserGrowth(List<DataPoint> userGrowth) { this.userGrowth = userGrowth; }

    public List<DataPoint> getRevenueGrowth() { return revenueGrowth; }
    public void setRevenueGrowth(List<DataPoint> revenueGrowth) { this.revenueGrowth = revenueGrowth; }

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
