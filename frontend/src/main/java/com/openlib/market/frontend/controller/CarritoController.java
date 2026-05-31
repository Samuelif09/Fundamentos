package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.Book;
import com.openlib.market.frontend.model.Cart;
import com.openlib.market.frontend.model.CartItem;
import com.openlib.market.frontend.service.CartService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

public class CarritoController {

    @FXML private VBox cartItemsContainer;
    @FXML private Label subtotalLabel;
    @FXML private Label taxesLabel;
    @FXML private Label totalLabel;
    @FXML private VBox loadingContainer;

    private final CartService cartService = new CartService();
    private Cart currentCart;

    @FXML
    public void initialize() {
        loadCart();
    }

    private void loadCart() {
        loadingContainer.setVisible(true);

        cartService.getMyCart().whenComplete((cart, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (throwable != null) {
                    this.currentCart = new Cart();
                    populateView(this.currentCart);
                } else if (cart != null) {
                    this.currentCart = cart;
                    populateView(cart);
                }
            });
        });
    }

    private void populateView(Cart cart) {
        cartItemsContainer.getChildren().clear();

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            Label emptyLabel = new Label("En este momento no hay libros en el carrito");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #64748B;");
            cartItemsContainer.getChildren().add(emptyLabel);
        } else {
            for (CartItem item : cart.getItems()) {
                HBox card = createItemCard(item);
                cartItemsContainer.getChildren().add(card);
            }
        }

        // Formatear resumen
        subtotalLabel.setText(String.format("$%.2f", cart.getSubtotal()));
        taxesLabel.setText(String.format("$%.2f", cart.getTaxes()));
        totalLabel.setText(String.format("$%.2f", cart.getTotal()));
    }

    private HBox createItemCard(CartItem item) {
        HBox card = new HBox(15);
        card.getStyleClass().add("cart-item-card");
        card.setAlignment(Pos.CENTER_LEFT);

        Book book = item.getBook();

        // Cover
        ImageView coverView = new ImageView();
        coverView.setFitWidth(60);
        coverView.setFitHeight(90);
        coverView.setPreserveRatio(true);
        if (book != null && book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
            try {
                coverView.setImage(new Image(book.getCoverUrl(), true));
            } catch (Exception e) {
                // Ignore
            }
        }

        // Info
        VBox infoBox = new VBox(5);
        Label title = new Label(book != null ? book.getTitle() : "Producto Desconocido");
        title.getStyleClass().add("cart-item-title");
        Label author = new Label(book != null ? book.getAuthor() : "");
        author.getStyleClass().add("cart-item-author");
        Label price = new Label(String.format("$%.2f c/u", book != null ? book.getPrice() : 0.0));
        price.getStyleClass().add("cart-item-price");
        
        infoBox.getChildren().addAll(title, author, price);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Qty & Subtotal
        VBox amountsBox = new VBox(5);
        amountsBox.setAlignment(Pos.CENTER_RIGHT);
        Label qty = new Label("Cant: " + item.getQuantity());
        qty.getStyleClass().add("cart-item-qty");
        Label subtotal = new Label(String.format("$%.2f", item.getSubtotal()));
        subtotal.getStyleClass().add("cart-item-subtotal");
        
        amountsBox.getChildren().addAll(qty, subtotal);

        card.getChildren().addAll(coverView, infoBox, amountsBox);
        return card;
    }

    @FXML
    public void handleCheckout(ActionEvent event) {
        if (currentCart == null || currentCart.getItems() == null || currentCart.getItems().isEmpty()) {
            showError("Carrito vacío", "No tienes productos en tu carrito para proceder al pago.");
            return;
        }

        // Navigate to checkout
        SceneManager.navigateTo("checkout");
    }

    @FXML
    public void handleBackToCatalog(ActionEvent event) {
        SceneManager.navigateTo("catalogo");
    }

    @FXML
    public void handleGoToProfile(ActionEvent event) {
        SceneManager.navigateTo("perfil");
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
