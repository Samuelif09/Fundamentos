package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.Book;
import com.openlib.market.frontend.service.BookService;
import com.openlib.market.frontend.service.CartService;
import com.openlib.market.frontend.session.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class DetalleLibroController {

    @FXML
    private ImageView coverImage;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label synopsisLabel;
    @FXML
    private VBox loadingContainer;

    private final BookService bookService = new BookService();
    private final CartService cartService = new CartService();
    private Book currentBook;

    @FXML
    public void initialize() {
        String bookId = SessionManager.getInstance().getCurrentBookId();
        if (bookId == null || bookId.isEmpty()) {
            showError("Error", "No se ha seleccionado ningún libro.");
            handleBackToCatalog(null);
            return;
        }

        loadBookDetails(bookId);
    }

    private void loadBookDetails(String bookId) {
        loadingContainer.setVisible(true);

        bookService.getBookById(bookId).whenComplete((book, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (throwable != null) {
                    showError("Error al cargar libro", throwable.getMessage());
                    handleBackToCatalog(null);
                } else if (book != null) {
                    this.currentBook = book;
                    populateView(book);
                }
            });
        });
    }

    private void populateView(Book book) {
        titleLabel.setText(book.getTitle() != null ? book.getTitle() : "Sin título");
        authorLabel.setText(book.getAuthor() != null ? book.getAuthor() : "Autor desconocido");
        priceLabel.setText(String.format("$%.2f", book.getPrice()));

        String synopsis = book.getSynopsis();
        if (synopsis == null || synopsis.isEmpty()) {
            synopsisLabel.setText("No hay sinopsis disponible para este libro.");
        } else {
            synopsisLabel.setText(synopsis);
        }

        try {
            if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
                coverImage.setImage(new Image(book.getCoverUrl(), true));
            }
        } catch (Exception e) {
            // Ignore image load error
        }
    }

    @FXML
    public void handleAddToCart(ActionEvent event) {
        if (currentBook == null)
            return;

        loadingContainer.setVisible(true);

        cartService.addToCart(currentBook.getId(), 1).whenComplete((v, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (throwable != null) {
                    showError("Error al agregar al carrito", throwable.getMessage());
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Carrito");
                    alert.setHeaderText("¡Libro agregado!");
                    alert.setContentText("El libro '" + currentBook.getTitle() + "' ha sido agregado a tu carrito.");
                    alert.showAndWait();
                }
            });
        });
    }

    @FXML
    public void handleBackToCatalog(ActionEvent event) {
        SceneManager.navigateTo("catalogo");
    }

    @FXML
    public void handleGoToDashboard(ActionEvent event) {
        SceneManager.navigateTo("dashboard");
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
