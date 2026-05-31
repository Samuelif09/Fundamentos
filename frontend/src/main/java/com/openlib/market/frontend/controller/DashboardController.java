package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.session.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    
    @FXML private Label greetingLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String email = SessionManager.getInstance().getEmail();
        if (email != null && !email.isEmpty()) {
            // Se puede mostrar el nombre o el email. Por simplicidad usamos el nombre del usuario o parte del email.
            String display = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
            greetingLabel.setText("¡Hola, " + display + "!");
        } else {
            greetingLabel.setText("¡Hola, Lector!");
        }
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
