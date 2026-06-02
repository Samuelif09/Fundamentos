package com.openlib.market.application.dashboardVendedor;

import java.util.Map;

public class VendedorMetricasDto {
    private int totalBooksSold;
    private Map<String, Integer> monthlySales;

    public VendedorMetricasDto(int totalBooksSold, Map<String, Integer> monthlySales) {
        this.totalBooksSold = totalBooksSold;
        this.monthlySales = monthlySales;
    }

    public int getTotalBooksSold() {
        return totalBooksSold;
    }

    public Map<String, Integer> getMonthlySales() {
        return monthlySales;
    }
}
