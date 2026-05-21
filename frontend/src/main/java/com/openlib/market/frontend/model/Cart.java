package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart {
    private List<CartItem> items = new ArrayList<>();
    private Double subtotal;
    private Double taxes;
    private Double total;
    private final double TAX_RATE = 0.19; // 19% tax for example

    public Cart() {}

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public double getSubtotal() {
        if (subtotal != null) return subtotal;
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public double getTaxes() {
        if (taxes != null) return taxes;
        return getSubtotal() * TAX_RATE;
    }
    public void setTaxes(Double taxes) { this.taxes = taxes; }

    public double getTotal() {
        if (total != null) return total;
        return getSubtotal() + getTaxes();
    }
    public void setTotal(Double total) { this.total = total; }
}
