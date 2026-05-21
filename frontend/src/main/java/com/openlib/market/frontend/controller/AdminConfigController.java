package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.SysCategory;
import com.openlib.market.frontend.model.SysPaymentMethod;
import com.openlib.market.frontend.service.SystemConfigService;
import com.openlib.market.frontend.session.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.concurrent.CompletableFuture;

public class AdminConfigController {

    // Payment Methods
    @FXML private ListView<SysPaymentMethod> paymentMethodsList;
    
    // Commissions
    @FXML private TextField txtCommission;
    
    // Categories
    @FXML private ListView<SysCategory> categoriesList;
    @FXML private TextField txtNewCategory;

    @FXML private VBox loadingContainer;

    private final SystemConfigService configService = new SystemConfigService();

    @FXML
    public void initialize() {
        configurePaymentList();
        configureCategoryList();
        loadData();
    }

    // ── CONFIGURACIÓN DE VISTAS ──────────────────────────────────────────

    private void configurePaymentList() {
        paymentMethodsList.setCellFactory(param -> new ListCell<>() {
            private final Label title = new Label();
            private final Label subtitle = new Label();
            private final Button btnToggle = new Button();
            private final HBox leftBox = new HBox(10, new VBox(2, title, subtitle));
            private final HBox layout = new HBox(leftBox, new Region(), btnToggle);

            {
                title.getStyleClass().add("list-item-title");
                subtitle.getStyleClass().add("list-item-sub");
                HBox.setHgrow(layout.getChildren().get(1), javafx.scene.layout.Priority.ALWAYS);
                layout.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                btnToggle.setOnAction(e -> {
                    SysPaymentMethod pm = getItem();
                    if (pm == null) return;
                    boolean isCurrentlyActive = "ACTIVE".equalsIgnoreCase(pm.getStatus());
                    String newStatus = isCurrentlyActive ? "INACTIVE" : "ACTIVE";
                    
                    // Optimistic UI Update
                    updateToggleStyle(btnToggle, newStatus);
                    
                    configService.updatePaymentMethodStatus(pm.getId(), newStatus).whenComplete((res, t) -> {
                        Platform.runLater(() -> {
                            if (t != null || !res.isSuccess()) {
                                // Revert on failure
                                updateToggleStyle(btnToggle, pm.getStatus());
                                showError("Error al actualizar método", t != null ? t.getMessage() : res.getErrorMessage());
                            } else {
                                pm.setStatus(newStatus);
                            }
                        });
                    });
                });
            }

            @Override
            protected void updateItem(SysPaymentMethod item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    title.setText(item.getName());
                    subtitle.setText(item.getProvider());
                    updateToggleStyle(btnToggle, item.getStatus());
                    setGraphic(layout);
                }
            }
        });
    }

    private void updateToggleStyle(Button btn, String status) {
        btn.getStyleClass().removeAll("toggle-btn", "toggle-active", "toggle-inactive");
        btn.getStyleClass().add("toggle-btn");
        if ("ACTIVE".equalsIgnoreCase(status)) {
            btn.setText("ON");
            btn.getStyleClass().add("toggle-active");
        } else {
            btn.setText("OFF");
            btn.getStyleClass().add("toggle-inactive");
        }
    }

    private void configureCategoryList() {
        categoriesList.setCellFactory(param -> new ListCell<>() {
            private final Label title = new Label();
            private final Button btnDelete = new Button("🗑");
            private final HBox layout = new HBox(title, new Region(), btnDelete);

            {
                title.getStyleClass().add("list-item-title");
                HBox.setHgrow(layout.getChildren().get(1), javafx.scene.layout.Priority.ALWAYS);
                layout.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                btnDelete.getStyleClass().add("btn-danger-icon");

                btnDelete.setOnAction(e -> {
                    SysCategory cat = getItem();
                    if (cat == null) return;
                    handleDeleteCategory(cat);
                });
            }

            @Override
            protected void updateItem(SysCategory item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    title.setText(item.getName());
                    setGraphic(layout);
                }
            }
        });
    }

    // ── CARGA Y ACCIONES ────────────────────────────────────────────────

    private void loadData() {
        loadingContainer.setVisible(true);

        var paymentsFuture = configService.getPaymentMethods();
        var commissionsFuture = configService.getCommissions();
        var categoriesFuture = configService.getCategories();

        CompletableFuture.allOf(paymentsFuture, commissionsFuture, categoriesFuture).whenComplete((v, t) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (t != null) {
                    showError("Error al cargar configuración", t.getMessage());
                } else {
                    paymentMethodsList.setItems(FXCollections.observableArrayList(paymentsFuture.join()));
                    txtCommission.setText(String.valueOf(commissionsFuture.join().getPlatformFeePercentage()));
                    categoriesList.setItems(FXCollections.observableArrayList(categoriesFuture.join()));
                }
            });
        });
    }

    @FXML
    public void handleSaveCommission(ActionEvent event) {
        try {
            double fee = Double.parseDouble(txtCommission.getText().replace(",", "."));
            if (fee < 0 || fee > 100) throw new NumberFormatException("Debe ser entre 0 y 100");

            loadingContainer.setVisible(true);
            configService.updateCommissions(fee).whenComplete((res, t) -> {
                Platform.runLater(() -> {
                    loadingContainer.setVisible(false);
                    if (t != null || !res.isSuccess()) {
                        showError("Error al guardar", t != null ? t.getMessage() : res.getErrorMessage());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Éxito");
                        alert.setHeaderText(null);
                        alert.setContentText("Comisión global actualizada correctamente.");
                        alert.showAndWait();
                    }
                });
            });
        } catch (NumberFormatException e) {
            showError("Formato Inválido", "Por favor ingresa un número válido (ej. 15.0)");
        }
    }

    @FXML
    public void handleAddCategory(ActionEvent event) {
        String newCat = txtNewCategory.getText().trim();
        if (newCat.isEmpty()) return;

        loadingContainer.setVisible(true);
        configService.createCategory(newCat).whenComplete((res, t) -> {
            Platform.runLater(() -> {
                if (t != null || !res.isSuccess()) {
                    loadingContainer.setVisible(false);
                    showError("Error al añadir categoría", t != null ? t.getMessage() : res.getErrorMessage());
                } else {
                    txtNewCategory.clear();
                    // Refrescar sólo categorías
                    configService.getCategories().whenComplete((cats, t2) -> {
                        Platform.runLater(() -> {
                            loadingContainer.setVisible(false);
                            if (t2 == null) categoriesList.setItems(FXCollections.observableArrayList(cats));
                        });
                    });
                }
            });
        });
    }

    private void handleDeleteCategory(SysCategory category) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar Categoría");
        confirm.setHeaderText("¿Eliminar la categoría '" + category.getName() + "'?");
        confirm.setContentText("Esto no afectará a los libros existentes, pero ya no estará disponible para nuevos registros.");
        
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        loadingContainer.setVisible(true);
        configService.deleteCategory(category.getId()).whenComplete((res, t) -> {
            Platform.runLater(() -> {
                if (t != null || !res.isSuccess()) {
                    loadingContainer.setVisible(false);
                    showError("Error al eliminar", t != null ? t.getMessage() : res.getErrorMessage());
                } else {
                    configService.getCategories().whenComplete((cats, t2) -> {
                        Platform.runLater(() -> {
                            loadingContainer.setVisible(false);
                            if (t2 == null) categoriesList.setItems(FXCollections.observableArrayList(cats));
                        });
                    });
                }
            });
        });
    }

    private void showError(String header, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error de Configuración"); a.setHeaderText(header); a.setContentText(content);
        a.showAndWait();
    }

    // ── NAVEGACIÓN ──────────────────────────────────────────────────────

    @FXML public void handleRefresh(ActionEvent event) { loadData(); }
    @FXML public void handleGoToDashboard(ActionEvent event) { SceneManager.navigateTo("dashboard_admin"); }
    @FXML public void handleGoToCuration(ActionEvent event) { SceneManager.navigateTo("curaduria_admin"); }
    @FXML public void handleGoToManagement(ActionEvent event) { SceneManager.navigateTo("gestion_admin"); }
    @FXML public void handleGoToSupport(ActionEvent event) { SceneManager.navigateTo("soporte_admin"); }
    @FXML public void handleLogout(ActionEvent event) {
        SessionManager.getInstance().cerrarSesion();
        SceneManager.navigateTo("login");
    }
}
