package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.UserRegistrationRequest;
import com.openlib.market.frontend.service.AuthService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class RegistroController {

    @FXML private TextField nombreField;
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
        String nombre = nombreField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Por favor completa todos los campos requeridos.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showError("Ingresa un correo electrónico válido.");
            return;
        }

        if (password.length() < 4) {
            showError("La contraseña debe tener al menos 4 caracteres.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Las contraseñas no coinciden.");
            return;
        }

        clearError();
        setLoading(true);

        UserRegistrationRequest request = new UserRegistrationRequest(nombre, email, password);

        authService.registerUser(request).whenComplete((response, throwable) -> {
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
        alert.setHeaderText("Cuenta Creada Correctamente");
        alert.setContentText("Tu cuenta de usuario ha sido creada. Ahora puedes iniciar sesión.");
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
