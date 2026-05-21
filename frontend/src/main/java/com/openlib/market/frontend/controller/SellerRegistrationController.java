package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.SellerRegistrationRequest;
import com.openlib.market.frontend.service.AuthService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class SellerRegistrationController {

    @FXML private TextField businessNameField;
    @FXML private TextField taxIdField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    
    @FXML private Label errorLabel;
    @FXML private Button registerButton;
    @FXML private HBox loadingContainer;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        // Inicialización
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String businessName = businessNameField.getText().trim();
        String taxId = taxIdField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (businessName.isEmpty() || taxId.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Por favor completa todos los campos requeridos.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showError("Ingresa un correo electrónico válido.");
            return;
        }

        if (password.length() < 6) {
            showError("La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden.");
            return;
        }

        clearError();
        setLoading(true);

        SellerRegistrationRequest request = new SellerRegistrationRequest(email, password, businessName, taxId);

        authService.registerSeller(request).whenComplete((response, throwable) -> {
            Platform.runLater(() -> {
                setLoading(false);
                if (throwable != null) {
                    showError("Error de conexión: " + throwable.getMessage());
                } else if (!response.isSuccess()) {
                    showError("Error al registrar: " + response.getErrorMessage());
                } else {
                    showSuccessAndRedirect();
                }
            });
        });
    }

    private void showSuccessAndRedirect() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registro Exitoso");
        alert.setHeaderText("Cuenta de Vendedor Creada");
        alert.setContentText("Tu solicitud ha sido recibida correctamente.\n\n" +
                "NOTA: Tu cuenta requiere validación por parte de un administrador antes de que puedas iniciar sesión y publicar libros.");
        alert.showAndWait();
        
        SceneManager.navigateTo("login");
    }

    @FXML
    public void handleGoToLogin(ActionEvent event) {
        SceneManager.navigateTo("login");
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
        registerButton.setDisable(loading);
        loadingContainer.setVisible(loading);
        loadingContainer.setManaged(loading);
        if (loading) clearError();
    }
}
