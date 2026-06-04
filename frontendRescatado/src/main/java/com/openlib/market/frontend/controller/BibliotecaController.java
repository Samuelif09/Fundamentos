package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.Book;
import com.openlib.market.frontend.service.LibraryService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.List;

public class BibliotecaController {

    @FXML private TilePane booksGrid;
    @FXML private VBox loadingContainer;

    private final LibraryService libraryService = new LibraryService();

    @FXML
    public void initialize() {
        loadLibrary();
    }

    private void loadLibrary() {
        loadingContainer.setVisible(true);
        booksGrid.getChildren().clear();

        libraryService.getMyLibrary().whenComplete((books, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (throwable != null) {
                    populateGrid(java.util.Collections.emptyList());
                } else if (books != null) {
                    populateGrid(books);
                }
            });
        });
    }

    private void populateGrid(List<Book> books) {
        if (books.isEmpty()) {
            Label noBooksLabel = new Label("No tienes libros en la biblioteca");
            noBooksLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #64748B;");
            booksGrid.getChildren().add(noBooksLabel);
            return;
        }

        for (Book book : books) {
            VBox card = createBookCard(book);
            booksGrid.getChildren().add(card);
        }
    }

    private VBox createBookCard(Book book) {
        VBox card = new VBox(10);
        card.getStyleClass().add("library-book-card");
        card.setAlignment(Pos.TOP_LEFT);
        card.setPrefWidth(220);
        card.setMaxWidth(220);

        ImageView coverImage = new ImageView();
        coverImage.setFitWidth(190);
        coverImage.setFitHeight(250);
        coverImage.setPreserveRatio(true);
        try {
            if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
                coverImage.setImage(new Image(book.getCoverUrl(), true));
            }
        } catch (Exception e) {
            // Ignore image load error
        }
        
        VBox imageContainer = new VBox(coverImage);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.getStyleClass().add("book-cover");
        imageContainer.setPrefHeight(250);

        Label titleLabel = new Label(book.getTitle() != null ? book.getTitle() : "Sin título");
        titleLabel.getStyleClass().add("book-title");
        titleLabel.setWrapText(true);

        Label authorLabel = new Label(book.getAuthor() != null ? book.getAuthor() : "Autor desconocido");
        authorLabel.getStyleClass().add("book-author");

        Button downloadBtn = new Button("⬇ Descargar");
        downloadBtn.getStyleClass().add("download-btn");
        downloadBtn.setMaxWidth(Double.MAX_VALUE);
        downloadBtn.setOnAction(e -> handleDownload(book));

        VBox.setVgrow(downloadBtn, Priority.ALWAYS);

        card.getChildren().addAll(imageContainer, titleLabel, authorLabel, downloadBtn);
        return card;
    }

    private void handleDownload(Book book) {
        try {
            String userId = com.openlib.market.frontend.session.SessionManager.getInstance().getUserId();
            if (userId == null || userId.isEmpty()) {
                showError("No hay sesión", "Debes iniciar sesión para descargar el libro.");
                return;
            }
            String url = "http://localhost:8080/api/v1/biblioteca/" + book.getId() + "/descargar?userId=" + userId;
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            showError("Error de descarga", "No se pudo abrir el archivo PDF: " + e.getMessage());
        }
    }

    @FXML
    public void handleBackToCatalog(ActionEvent event) {
        SceneManager.navigateTo("catalogo");
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
