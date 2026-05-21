package com.openlib.market.frontend.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestor centralizado de escenas. Evita acoplamiento entre controladores.
 */
public class SceneManager {

    private static Stage primaryStage;
    private static final Map<String, String> ROUTES = new HashMap<>();

    static {
        ROUTES.put("login",     "/com/openlib/market/frontend/views/login.fxml");
        ROUTES.put("dashboard", "/com/openlib/market/frontend/views/dashboard.fxml");
        ROUTES.put("catalogo",  "/com/openlib/market/frontend/views/catalogo.fxml");
        ROUTES.put("detalleLibro", "/com/openlib/market/frontend/views/detalle_libro.fxml");
        ROUTES.put("carrito", "/com/openlib/market/frontend/views/carrito.fxml");
        ROUTES.put("checkout", "/com/openlib/market/frontend/views/checkout.fxml");
        ROUTES.put("biblioteca", "/com/openlib/market/frontend/views/biblioteca.fxml");
        ROUTES.put("perfil", "/com/openlib/market/frontend/views/perfil.fxml");
    }

    public static void initialize(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("OpenLib Market");
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(720);
        primaryStage.setResizable(true);
    }

    public static void navigateTo(String route) {
        String fxmlPath = ROUTES.get(route);
        if (fxmlPath == null) throw new IllegalArgumentException("Ruta desconocida: " + route);

        try {
            URL resource = SceneManager.class.getResource(fxmlPath);
            if (resource == null) throw new IOException("FXML no encontrado: " + fxmlPath);

            FXMLLoader loader = new FXMLLoader(resource);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la vista: " + route, e);
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
