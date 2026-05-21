package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.CheckoutRequest;
import com.openlib.market.frontend.service.CheckoutService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class CheckoutController {

    @FXML private TextField fullNameField;
    @FXML private TextField addressField;
    @FXML private TextField cityField;
    @FXML private TextField zipCodeField;
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private Label errorLabel;
    @FXML private Button confirmButton;
    @FXML private VBox loadingContainer;

    private final CheckoutService checkoutService = new CheckoutService();

    @FXML
    public void initialize() {
        // Inicialización de la vista, si se requiere algo extra
    }

    @FXML
    public void handleConfirmOrder(ActionEvent event) {
        String fullName = fullNameField.getText();
        String address = addressField.getText();
        String city = cityField.getText();
        String zipCode = zipCodeField.getText();
        String paymentMethod = paymentMethodCombo.getValue();

        if (fullName == null || fullName.trim().isEmpty() ||
            address == null || address.trim().isEmpty() ||
            city == null || city.trim().isEmpty() ||
            zipCode == null || zipCode.trim().isEmpty() ||
            paymentMethod == null) {
            
            showError("Por favor, completa todos los campos requeridos.");
            return;
        }

        clearError();
        setLoading(true);

        CheckoutRequest request = new CheckoutRequest(
                fullName.trim(),
                address.trim(),
                city.trim(),
                zipCode.trim(),
                paymentMethod
        );

        checkoutService.processCheckout(request).whenComplete((response, throwable) -> {
            Platform.runLater(() -> {
                setLoading(false);
                if (throwable != null) {
                    showError("Error al procesar el pago: " + throwable.getMessage());
                } else if (response != null) {
                    if (response.getStatus() == null || "COMPLETED".equalsIgnoreCase(response.getStatus()) || "SUCCESS".equalsIgnoreCase(response.getStatus())) {
                        showSuccess("¡Pago completado con éxito! Tu orden #" + response.getOrderId() + " está en camino.");
                        SceneManager.navigateTo("catalogo");
                    } else {
                        showError("El pago fue rechazado o está pendiente: " + response.getMessage());
                    }
                }
            });
        });
    }

    @FXML
    public void handleBackToCart(ActionEvent event) {
        SceneManager.navigateTo("carrito");
    }

    private void showError(String message) {
        errorLabel.setText("⚠ " + message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void clearError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private void setLoading(boolean loading) {
        confirmButton.setDisable(loading);
        loadingContainer.setVisible(loading);
        if (loading) {
            clearError();
        }
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText("Transacción Exitosa");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
