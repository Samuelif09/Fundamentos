package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.http.ApiResponse;
import com.openlib.market.frontend.model.LoginResponse;
import com.openlib.market.frontend.service.AuthService;
import com.openlib.market.frontend.session.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador MVC de la vista de Login.
 *
 * Reglas de asincronía:
 * - La llamada HTTP se lanza en un hilo separado (CompletableFuture via AuthService).
 * - TODA actualización de UI se encapsula en Platform.runLater().
 */
public class LoginController implements Initializable {

    @FXML private TextField         emailField;
    @FXML private PasswordField     passwordField;
    @FXML private Button            loginButton;
    @FXML private Label             errorLabel;
    @FXML private ProgressIndicator loadingIndicator;

    private final AuthService authService = new AuthService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Limpiar el error al escribir
        emailField.textProperty().addListener((obs, old, nv) -> clearError());
        passwordField.textProperty().addListener((obs, old, nv) -> clearError());

        // Enter en el campo de email salta al password
        emailField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) passwordField.requestFocus();
        });

        // Enter en el campo de password dispara el login
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) handleLogin();
        });
    }

    // ── Handlers de botones ──────────────────────────────────────────────

    @FXML
    public void handleLogin() {
        String email    = emailField.getText().trim();
        String password = passwordField.getText();

        if (!validarCampos(email, password)) return;

        setLoading(true);

        // ← Llamada asíncrona: NO bloquea el hilo de JavaFX
        authService.login(email, password)
                .thenAccept(this::procesarRespuestaLogin);
    }

    @FXML
    public void handleForgotPassword() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            mostrarError("Ingresa tu email primero para recuperar la contraseña.");
            emailField.requestFocus();
            return;
        }

        setLoading(true);
        authService.recuperarPassword(email)
                .thenAccept(resp -> Platform.runLater(() -> {
                    setLoading(false);
                    if (resp.isSuccess()) {
                        mostrarInfo("Si el correo existe, recibirás un enlace de recuperación.");
                    } else {
                        mostrarError("Error al enviar el correo de recuperación.");
                    }
                }));
    }

    @FXML
    public void handleGoToRegister(ActionEvent event) {
        SceneManager.navigateTo("registro");
    }

    @FXML
    public void handleGoToSellerRegister(ActionEvent event) {
        SceneManager.navigateTo("registro_vendedor");
    }

    @FXML
    public void handleGoToAdminLogin(ActionEvent event) {
        SceneManager.navigateTo("admin_login");
    }

    // ── Procesamiento de respuesta (SIEMPRE en hilo bg, UI via runLater) ─

    private void procesarRespuestaLogin(ApiResponse<LoginResponse> response) {
        Platform.runLater(() -> {
            setLoading(false);

            if (!response.isSuccess()) {
                String msg = switch (response.getStatusCode()) {
                    case 401 -> "Credenciales incorrectas. Verifica tu email y contraseña.";
                    case 0   -> "No se pudo conectar con el servidor. ¿Está ejecutándose el backend?";
                    default  -> "Error inesperado (" + response.getStatusCode() + "). Intenta de nuevo.";
                };
                mostrarError(msg);
                return;
            }

            LoginResponse loginData = response.getBody();
            if (loginData == null || loginData.getToken() == null) {
                mostrarError("El servidor respondió sin token. Contacta al administrador.");
                return;
            }

            // Guardar token en sesión
            SessionManager.getInstance().iniciarSesion(
                    loginData.getToken(),
                    emailField.getText().trim(),
                    "USUARIO" // El backend actual no devuelve el rol en la respuesta de login
            );

            // Navegar al dashboard principal
            SceneManager.navigateTo("dashboard");
        });
    }

    // ── Helpers de UI ───────────────────────────────────────────────────

    private boolean validarCampos(String email, String password) {
        if (email.isEmpty()) {
            mostrarError("El correo electrónico es obligatorio.");
            emailField.requestFocus();
            return false;
        }
        if (!email.contains("@") || !email.contains(".")) {
            mostrarError("Ingresa un correo electrónico válido.");
            emailField.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            mostrarError("La contraseña es obligatoria.");
            passwordField.requestFocus();
            return false;
        }
        if (password.length() < 4) {
            mostrarError("La contraseña debe tener al menos 4 caracteres.");
            passwordField.requestFocus();
            return false;
        }
        return true;
    }

    private void mostrarError(String mensaje) {
        errorLabel.setText("⚠ " + mensaje);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void mostrarInfo(String mensaje) {
        errorLabel.setStyle("-fx-text-fill: #63ffb5; -fx-background-color: rgba(99,255,181,0.1);");
        errorLabel.setText("✓ " + mensaje);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void clearError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private void setLoading(boolean loading) {
        loginButton.setDisable(loading);
        loadingIndicator.setVisible(loading);
        loadingIndicator.setManaged(loading);
        if (loading) {
            clearError();
        }
    }
}
