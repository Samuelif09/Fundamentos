package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.OrderHistoryItem;
import com.openlib.market.frontend.model.UserProfile;
import com.openlib.market.frontend.service.ProfileService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProfileController {

    @FXML private Label avatarInitialsLabel;
    @FXML private Label headerNameLabel;
    
    // Datos Personales
    @FXML private Label fullNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label joinedDateLabel;
    
    // Historial
    @FXML private VBox ordersContainer;
    
    // Estadísticas
    @FXML private Label totalBooksLabel;
    @FXML private Label readHoursLabel;
    @FXML private Label favoriteGenreLabel;
    
    @FXML private VBox loadingContainer;

    private final ProfileService profileService = new ProfileService();

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        loadingContainer.setVisible(true);

        CompletableFuture<UserProfile> profileFuture = profileService.getProfile();
        CompletableFuture<List<OrderHistoryItem>> ordersFuture = profileService.getOrderHistory()
                .exceptionally(ex -> java.util.Collections.emptyList()); // nunca falla

        CompletableFuture.allOf(profileFuture, ordersFuture).whenComplete((v, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                try {
                    UserProfile profile = profileFuture.join();
                    populateProfile(profile);
                } catch (Exception e) {
                    // Mostrar datos mínimos de sesión sin bloquear UI
                    String email = com.openlib.market.frontend.session.SessionManager.getInstance().getEmail();
                    UserProfile fallback = new UserProfile();
                    fallback.setEmail(email != null ? email : "");
                    fallback.setFullName(email != null ? email.split("@")[0] : "Usuario");
                    fallback.setJoinedDate("-");
                    populateProfile(fallback);
                }
                try {
                    populateOrders(ordersFuture.join());
                } catch (Exception e) {
                    populateOrders(java.util.Collections.emptyList());
                }
            });
        });
    }

    private void populateProfile(UserProfile profile) {
        if (profile == null) return;
        
        // Header
        headerNameLabel.setText(profile.getFullName() != null ? profile.getFullName() : "Usuario");
        String initials = "?";
        if (profile.getFullName() != null && !profile.getFullName().isEmpty()) {
            initials = String.valueOf(profile.getFullName().charAt(0)).toUpperCase();
        }
        avatarInitialsLabel.setText(initials);
        
        // Datos Personales
        fullNameLabel.setText(profile.getFullName() != null ? profile.getFullName() : "-");
        emailLabel.setText(profile.getEmail() != null ? profile.getEmail() : "-");
        joinedDateLabel.setText(profile.getJoinedDate() != null ? profile.getJoinedDate() : "-");
        
        // Estadísticas
        totalBooksLabel.setText(String.valueOf(profile.getTotalBooksOwned()));
        readHoursLabel.setText(String.valueOf(profile.getReadHours()));
        favoriteGenreLabel.setText(profile.getFavoriteGenre() != null ? profile.getFavoriteGenre() : "-");
    }

    private void populateOrders(List<OrderHistoryItem> orders) {
        ordersContainer.getChildren().clear();
        
        if (orders == null || orders.isEmpty()) {
            Label noOrders = new Label("No has realizado ninguna compra todavía.");
            noOrders.setStyle("-fx-font-size: 16px; -fx-text-fill: #64748B;");
            ordersContainer.getChildren().add(noOrders);
            return;
        }
        
        for (OrderHistoryItem order : orders) {
            ordersContainer.getChildren().add(createOrderCard(order));
        }
    }

    private HBox createOrderCard(OrderHistoryItem order) {
        HBox card = new HBox(20);
        card.getStyleClass().add("order-card");
        card.setAlignment(Pos.CENTER_LEFT);
        
        VBox infoBox = new VBox(5);
        Label idLabel = new Label("Orden #" + order.getOrderId());
        idLabel.getStyleClass().add("order-id");
        Label dateLabel = new Label("Fecha: " + order.getDate());
        dateLabel.getStyleClass().add("order-date");
        infoBox.getChildren().addAll(idLabel, dateLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        VBox amountBox = new VBox(5);
        amountBox.setAlignment(Pos.CENTER_RIGHT);
        Label totalLabel = new Label(String.format("Total: $%.2f", order.getTotal()));
        totalLabel.getStyleClass().add("order-total");
        
        Label statusLabel = new Label(order.getStatus() != null ? order.getStatus().toUpperCase() : "DESCONOCIDO");
        statusLabel.getStyleClass().add("order-status");
        if ("COMPLETED".equalsIgnoreCase(order.getStatus())) {
            statusLabel.getStyleClass().add("status-completed");
        } else {
            statusLabel.getStyleClass().add("status-pending");
        }
        amountBox.getChildren().addAll(totalLabel, statusLabel);
        
        card.getChildren().addAll(infoBox, spacer, amountBox);
        return card;
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
