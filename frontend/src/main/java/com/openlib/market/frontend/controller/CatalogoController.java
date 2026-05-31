package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.model.Book;
import com.openlib.market.frontend.service.BookService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

import java.util.List;

public class CatalogoController {

    @FXML private TextField searchField;
    @FXML private TilePane booksGrid;
    @FXML private VBox loadingContainer;
    
    @FXML private RadioButton catAll;
    @FXML private RadioButton catFiction;
    @FXML private RadioButton catTech;
    @FXML private RadioButton catScience;
    @FXML private RadioButton catHistory;
    
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;

    private ToggleGroup categoryGroup;
    private final BookService bookService = new BookService();

    @FXML
    public void initialize() {
        categoryGroup = new ToggleGroup();
        catAll.setToggleGroup(categoryGroup);
        catFiction.setToggleGroup(categoryGroup);
        catTech.setToggleGroup(categoryGroup);
        catScience.setToggleGroup(categoryGroup);
        catHistory.setToggleGroup(categoryGroup);

        // Load initial catalog
        loadBooks(null, null);
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String query = searchField.getText();
        String category = getSelectedCategory();
        loadBooks(query, category);
    }

    @FXML
    public void handleCategoryFilter(ActionEvent event) {
        String query = searchField.getText();
        String category = getSelectedCategory();
        loadBooks(query, category);
    }

    @FXML
    public void handlePriceFilter(ActionEvent event) {
        // Here we could implement local filtering or send price bounds to backend.
        // For MVP, we simply re-fetch or apply local filter if we had all data.
        System.out.println("Aplicando filtro de precio: " + minPriceField.getText() + " - " + maxPriceField.getText());
    }

    private String getSelectedCategory() {
        RadioButton selected = (RadioButton) categoryGroup.getSelectedToggle();
        if (selected != null && selected != catAll) {
            return selected.getText();
        }
        return null;
    }

    private void loadBooks(String query, String category) {
        loadingContainer.setVisible(true);
        booksGrid.getChildren().clear();

        bookService.getBooks(query, category).whenComplete((books, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (throwable != null) {
                    showError("Error al cargar libros", throwable.getMessage());
                } else if (books != null) {
                    populateGrid(books);
                }
            });
        });
    }

    private void populateGrid(List<Book> books) {
        for (Book book : books) {
            VBox card = createBookCard(book);
            booksGrid.getChildren().add(card);
        }
        
        if (books.isEmpty()) {
            Label noResults = new Label("No se encontraron libros.");
            booksGrid.getChildren().add(noResults);
        }
    }

    private VBox createBookCard(Book book) {
        VBox card = new VBox(10);
        card.getStyleClass().add("book-card");
        card.setAlignment(Pos.TOP_LEFT);
        card.setPrefWidth(220);
        card.setMaxWidth(220);

        // Placeholder cover if image fails or is empty
        ImageView coverImage = new ImageView();
        coverImage.setFitWidth(190);
        coverImage.setFitHeight(250);
        coverImage.setPreserveRatio(true);
        try {
            if (book.getCoverUrl() != null && !book.getCoverUrl().isEmpty()) {
                coverImage.setImage(new Image(book.getCoverUrl(), true));
            } else {
                // Use a placeholder or leave empty with a background color
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

        Label priceLabel = new Label(String.format("$%.2f", book.getPrice()));
        priceLabel.getStyleClass().add("book-price");

        Button detailBtn = new Button("Ver Detalle");
        detailBtn.getStyleClass().add("book-detail-btn");
        detailBtn.setMaxWidth(Double.MAX_VALUE);
        detailBtn.setOnAction(e -> navigateToDetail(book));

        // Push button to the bottom if title is short
        VBox.setVgrow(detailBtn, Priority.ALWAYS);

        card.getChildren().addAll(imageContainer, titleLabel, authorLabel, priceLabel, detailBtn);
        return card;
    }

    private void navigateToDetail(Book book) {
        System.out.println("Navegando a detalle del libro: " + book.getId() + " - " + book.getTitle());
        com.openlib.market.frontend.session.SessionManager.getInstance().setCurrentBookId(book.getId());
        com.openlib.market.frontend.app.SceneManager.navigateTo("detalleLibro");
    }

    @FXML
    public void handleGoToDashboard(ActionEvent event) {
        com.openlib.market.frontend.app.SceneManager.navigateTo("dashboard");
    }

    @FXML
    public void handleGoToCart(ActionEvent event) {
        com.openlib.market.frontend.app.SceneManager.navigateTo("carrito");
    }

    @FXML
    public void handleGoToLibrary(ActionEvent event) {
        com.openlib.market.frontend.app.SceneManager.navigateTo("biblioteca");
    }

    @FXML
    public void handleGoToProfile(ActionEvent event) {
        com.openlib.market.frontend.app.SceneManager.navigateTo("perfil");
    }

    @FXML
    public void handleGoToSellerDashboard(ActionEvent event) {
        com.openlib.market.frontend.app.SceneManager.navigateTo("dashboard_vendedor");
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
