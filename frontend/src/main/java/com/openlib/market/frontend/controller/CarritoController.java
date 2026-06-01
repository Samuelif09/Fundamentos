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

        // Qty & Subtotal Controls
        HBox qtyControls = new HBox(5);
        qtyControls.setAlignment(Pos.CENTER_RIGHT);

        javafx.scene.control.Button btnMinus = new javafx.scene.control.Button("-");
        btnMinus.setStyle("-fx-min-width: 24; -fx-min-height: 24; -fx-font-weight: bold;");
        btnMinus.setOnAction(e -> {
            if (book != null) {
                int newQty = item.getQuantity() - 1;
                if (newQty <= 0) {
                    handleRemoveItem(book.getId());
                } else {
                    handleUpdateQuantity(book.getId(), newQty);
                }
            }
        });

        Label qtyLabel = new Label(String.format(" %d ", item.getQuantity()));
        qtyLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        javafx.scene.control.Button btnPlus = new javafx.scene.control.Button("+");
        btnPlus.setStyle("-fx-min-width: 24; -fx-min-height: 24; -fx-font-weight: bold;");
        btnPlus.setOnAction(e -> {
            if (book != null) {
                handleUpdateQuantity(book.getId(), item.getQuantity() + 1);
            }
        });

        qtyControls.getChildren().addAll(btnMinus, qtyLabel, btnPlus);

        javafx.scene.control.Button btnDelete = new javafx.scene.control.Button("Eliminar");
        btnDelete.setStyle("-fx-text-fill: #EF4444; -fx-background-color: transparent; -fx-border-color: #F3F4F6; -fx-border-radius: 4;");
        btnDelete.setOnAction(e -> {
            if (book != null) {
                handleRemoveItem(book.getId());
            }
        });

        VBox amountsBox = new VBox(8);
        amountsBox.setAlignment(Pos.CENTER_RIGHT);
        Label subtotal = new Label(String.format("$%.2f", item.getSubtotal()));
        subtotal.getStyleClass().add("cart-item-subtotal");

        amountsBox.getChildren().addAll(subtotal, qtyControls, btnDelete);

        card.getChildren().addAll(coverView, infoBox, amountsBox);
        return card;
    }

    private void handleUpdateQuantity(String isbn, int quantity) {
        loadingContainer.setVisible(true);
        cartService.updateQuantity(isbn, quantity).whenComplete((v, throwable) -> {
            Platform.runLater(() -> {
                if (throwable != null) {
                    loadingContainer.setVisible(false);
                    showError("Error al actualizar cantidad", throwable.getMessage());
                } else {
                    loadCart();
                }
            });
        });
    }

    private void handleRemoveItem(String isbn) {
        loadingContainer.setVisible(true);
        cartService.removeItem(isbn).whenComplete((v, throwable) -> {
            Platform.runLater(() -> {
                if (throwable != null) {
                    loadingContainer.setVisible(false);
                    showError("Error al eliminar item", throwable.getMessage());
                } else {
                    loadCart();
                }
            });
        });
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
