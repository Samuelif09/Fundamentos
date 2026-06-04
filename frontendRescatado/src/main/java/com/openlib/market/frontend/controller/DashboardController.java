package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.session.SessionManager;
import com.openlib.market.frontend.service.BookService;
import com.openlib.market.frontend.model.Book;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    
    @FXML private Label greetingLabel;
    @FXML private FlowPane recommendedFlowPane;
    
    private BookService bookService = new BookService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String email = SessionManager.getInstance().getEmail();
        if (email != null && !email.isEmpty()) {
            String display = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
            greetingLabel.setText("¡Hola, " + display + "!");
        } else {
            greetingLabel.setText("¡Hola, Lector!");
        }
        
        loadRecommendedBooks();
    }

    private void loadRecommendedBooks() {
        bookService.getBooks(null, null).thenAccept(books -> {
            Platform.runLater(() -> {
                if (recommendedFlowPane != null) {
                    recommendedFlowPane.getChildren().clear();
                    int limit = Math.min(books.size(), 4);
                    for (int i = 0; i < limit; i++) {
                        Book book = books.get(i);
                        VBox card = createBookCard(book);
                        recommendedFlowPane.getChildren().add(card);
                    }
                }
            });
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    private VBox createBookCard(Book book) {
        VBox card = new VBox(10);
        card.getStyleClass().add("book-card");
        card.setPrefWidth(180);

        VBox placeholder = new VBox();
        placeholder.getStyleClass().add("book-cover-placeholder");
        placeholder.setPrefHeight(220);
        placeholder.setPrefWidth(150);
        
        String[] colors = {"#3b2a52", "#2a4052", "#522a36", "#2a3152"};
        String color = colors[Math.abs(book.getId().hashCode()) % colors.length];
        placeholder.setStyle("-fx-background-color: " + color + ";");

        Label icon = new Label("📚");
        icon.setStyle("-fx-font-size: 40px; -fx-text-fill: #94a3b8;");
        placeholder.getChildren().add(icon);
        placeholder.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(book.getTitle());
        titleLabel.getStyleClass().add("book-title");
        titleLabel.setWrapText(true);

        Label authorLabel = new Label(book.getAuthor());
        authorLabel.getStyleClass().add("book-author");

        HBox bottomRow = new HBox(10);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        Label priceLabel = new Label(String.format("$ %.2f", book.getPrice()));
        priceLabel.getStyleClass().add("book-price");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Button viewBtn = new Button("Ver");
        viewBtn.getStyleClass().add("btn-buy");
        viewBtn.setOnAction(e -> {
            SessionManager.getInstance().setCurrentBookId(book.getId());
            SceneManager.navigateTo("detalleLibro");
        });

        bottomRow.getChildren().addAll(priceLabel, spacer, viewBtn);

        card.getChildren().addAll(placeholder, titleLabel, authorLabel, bottomRow);
        return card;
    }

    @FXML
    public void handleGoToCatalog(javafx.event.ActionEvent event) {
        SceneManager.navigateTo("catalogo");
    }

    @FXML
    public void handleGoToLibrary(javafx.event.ActionEvent event) {
        SceneManager.navigateTo("biblioteca");
    }

    @FXML
    public void handleGoToCart(javafx.event.ActionEvent event) {
        SceneManager.navigateTo("carrito");
    }

    @FXML
    public void handleGoToProfile(javafx.event.ActionEvent event) {
        SceneManager.navigateTo("perfil");
    }

    @FXML
    public void handleLogout(javafx.event.ActionEvent event) {
        SessionManager.getInstance().cerrarSesion();
        SceneManager.navigateTo("login");
    }
}
