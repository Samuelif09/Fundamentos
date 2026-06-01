package com.openlib.market.application.carrito;

public class CartItemDto {
    private final String id;
    private final CartBookDto book;
    private final int quantity;

    public CartItemDto(String id, CartBookDto book, int quantity) {
        this.id = id;
        this.book = book;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public CartBookDto getBook() { return book; }
    public int getQuantity() { return quantity; }
}
