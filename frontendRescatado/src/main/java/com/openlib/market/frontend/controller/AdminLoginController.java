package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.LoginResponse;
import com.openlib.market.frontend.service.AuthService;
import com.openlib.market.frontend.session.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AdminLoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private VBox loadingContainer;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        // Enlazar la tecla Enter al botón de login
        passwordField.setOnAction(this::handleLogin);
        emailField.setOnAction(this::handleLogin);
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Campos requeridos", "Por favor ingresa tu correo administrativo y contraseña.");
            return;
        }

        setLoading(true);

        authService.loginAdmin(email, password).whenComplete((response, throwable) -> {
            Platform.runLater(() -> {
                setLoading(false);

                if (throwable != null) {
                    showError("Error de Conexión", "No se pudo conectar con el servidor de autenticación:\n" + throwable.getMessage());
                } else if (!response.isSuccess()) {
                    showError("Acceso Denegado", "Credenciales inválidas o cuenta sin privilegios administrativos.");
                } else {
                    LoginResponse loginData = response.getBody();
                    if (loginData != null && loginData.getToken() != null) {
                        // Persistir sesión con privilegios elevados
                        SessionManager.getInstance().iniciarSesionAdmin(loginData.getToken(), email);
                        
                        // Redirigir al dashboard administrativo
                        SceneManager.navigateTo("dashboard_admin");
                    } else {
                        showError("Error", "Respuesta malformada del servidor.");
                    }
                }
            });
        });
    }

    @FXML
    public void handleBackToPublicLogin(ActionEvent event) {
        SceneManager.navigateTo("login");
    }

    private void setLoading(boolean loading) {
        loadingContainer.setVisible(loading);
        loginButton.setDisable(loading);
        emailField.setDisable(loading);
        passwordField.setDisable(loading);
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Autenticación");
        alert.setHeaderText(header);
        alert.setContentText(content);
        // Estilar el alert para que coincida mínimamente con el tema oscuro si es posible
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Segoe UI', sans-serif;");
        alert.showAndWait();
    }
}
