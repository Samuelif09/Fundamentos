package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.AdminOrder;
import com.openlib.market.frontend.model.AdminUser;
import com.openlib.market.frontend.service.AdminManagementService;
import com.openlib.market.frontend.session.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class AdminManagementController {

    // Users Tab
    @FXML private TextField userSearchField;
    @FXML private TableView<AdminUser> usersTable;
    @FXML private TableColumn<AdminUser, String> colUserEmail;
    @FXML private TableColumn<AdminUser, String> colUserName;
    @FXML private TableColumn<AdminUser, String> colUserRole;
    @FXML private TableColumn<AdminUser, String> colUserStatus;
    @FXML private TableColumn<AdminUser, String> colUserDate;
    @FXML private TableColumn<AdminUser, Void> colUserAction;

    // Orders Tab
    @FXML private TextField orderSearchField;
    @FXML private TableView<AdminOrder> ordersTable;
    @FXML private TableColumn<AdminOrder, String> colOrderId;
    @FXML private TableColumn<AdminOrder, String> colOrderEmail;
    @FXML private TableColumn<AdminOrder, Double> colOrderAmount;
    @FXML private TableColumn<AdminOrder, String> colOrderStatus;
    @FXML private TableColumn<AdminOrder, String> colOrderDate;
    @FXML private TableColumn<AdminOrder, Void> colOrderAction;

    @FXML private VBox loadingContainer;

    private final AdminManagementService service = new AdminManagementService();
    private ObservableList<AdminUser> masterUserData = FXCollections.observableArrayList();
    private ObservableList<AdminOrder> masterOrderData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configureUserTable();
        configureOrderTable();
        loadData();
    }

    // ── CONFIGURACIÓN DE TABLA DE USUARIOS ────────────────────────────

    private void configureUserTable() {
        colUserEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colUserDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        colUserRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colUserRole.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String role, boolean empty) {
                super.updateItem(role, empty);
                if (empty || role == null) { setGraphic(null); setText(null); return; }
                Label chip = new Label(role.equals("S") ? "VENDEDOR" : role.equals("A") ? "ADMIN" : "COMPRADOR");
                chip.getStyleClass().addAll("chip", role.equals("S") ? "chip-role-s" : "chip-role-c");
                setGraphic(chip);
            }
        });

        colUserStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colUserStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) { setGraphic(null); setText(null); return; }
                Label chip = new Label(status);
                chip.getStyleClass().addAll("chip", status.equalsIgnoreCase("ACTIVE") ? "chip-active" : "chip-suspend");
                setGraphic(chip);
            }
        });

        colUserAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnToggle = new Button();

            {
                btnToggle.setOnAction(event -> {
                    AdminUser user = getTableView().getItems().get(getIndex());
                    if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
                        handleSuspendUser(user);
                    } else {
                        handleReactivateUser(user);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    AdminUser user = getTableView().getItems().get(getIndex());
                    if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
                        btnToggle.setText("Bloquear");
                        btnToggle.getStyleClass().setAll("btn-suspend");
                    } else {
                        btnToggle.setText("Reactivar");
                        btnToggle.getStyleClass().setAll("btn-reactivate");
                    }
                    // Prevent modifying own account
                    if (user.getEmail().equals(SessionManager.getInstance().getEmail())) {
                        btnToggle.setDisable(true);
                    } else {
                        btnToggle.setDisable(false);
                    }
                    setGraphic(btnToggle);
                }
            }
        });

        // Configurar Filtro
        FilteredList<AdminUser> filteredUsers = new FilteredList<>(masterUserData, p -> true);
        userSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUsers.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return user.getEmail().toLowerCase().contains(lowerCaseFilter) ||
                       (user.getFullName() != null && user.getFullName().toLowerCase().contains(lowerCaseFilter));
            });
        });
        usersTable.setItems(filteredUsers);
    }

    // ── CONFIGURACIÓN DE TABLA DE PEDIDOS ─────────────────────────────

    private void configureOrderTable() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderEmail.setCellValueFactory(new PropertyValueFactory<>("buyerEmail"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        colOrderAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colOrderAmount.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) setText(null);
                else setText(String.format("$%.2f", amount));
            }
        });

        colOrderStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colOrderStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) { setGraphic(null); return; }
                Label chip = new Label(status);
                chip.getStyleClass().addAll("chip", status.equalsIgnoreCase("REFUNDED") ? "chip-refunded" : "chip-active");
                setGraphic(chip);
            }
        });

        colOrderAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnRefund = new Button("Reembolsar");

            {
                btnRefund.getStyleClass().add("btn-refund");
                btnRefund.setOnAction(event -> {
                    AdminOrder order = getTableView().getItems().get(getIndex());
                    handleRefundOrder(order);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    AdminOrder order = getTableView().getItems().get(getIndex());
                    btnRefund.setDisable("REFUNDED".equalsIgnoreCase(order.getStatus()));
                    setGraphic(btnRefund);
                }
            }
        });

        // Configurar Filtro
        FilteredList<AdminOrder> filteredOrders = new FilteredList<>(masterOrderData, p -> true);
        orderSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredOrders.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return (order.getOrderId() != null && order.getOrderId().toLowerCase().contains(lowerCaseFilter)) ||
                       (order.getBuyerEmail() != null && order.getBuyerEmail().toLowerCase().contains(lowerCaseFilter));
            });
        });
        ordersTable.setItems(filteredOrders);
    }

    // ── CARGA DE DATOS ──────────────────────────────────────────────────

    private void loadData() {
        loadingContainer.setVisible(true);

        var usersFuture = service.getUsers();
        var ordersFuture = service.getOrders();

        java.util.concurrent.CompletableFuture.allOf(usersFuture, ordersFuture).whenComplete((v, t) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (t != null) {
                    showError("Error de red", t.getMessage());
                } else {
                    masterUserData.setAll(usersFuture.join());
                    masterOrderData.setAll(ordersFuture.join());
                }
            });
        });
    }

    // ── ACCIONES API ────────────────────────────────────────────────────

    private void handleSuspendUser(AdminUser user) {
        if (!confirmAction("Bloquear Usuario", "¿Suspender acceso a " + user.getEmail() + "?")) return;
        loadingContainer.setVisible(true);
        service.suspendUser(user.getId()).whenComplete((res, t) -> {
            Platform.runLater(() -> { loadData(); });
        });
    }

    private void handleReactivateUser(AdminUser user) {
        if (!confirmAction("Reactivar Usuario", "¿Permitir acceso a " + user.getEmail() + "?")) return;
        loadingContainer.setVisible(true);
        service.reactivateUser(user.getId()).whenComplete((res, t) -> {
            Platform.runLater(() -> { loadData(); });
        });
    }

    private void handleRefundOrder(AdminOrder order) {
        if (!confirmAction("Reembolsar Pedido", "¿Emitir reembolso completo por $" + String.format("%.2f", order.getTotalAmount()) + " a " + order.getBuyerEmail() + "? Esta acción es irreversible.")) return;
        loadingContainer.setVisible(true);
        service.refundOrder(order.getOrderId()).whenComplete((res, t) -> {
            Platform.runLater(() -> { loadData(); });
        });
    }

    private boolean confirmAction(String title, String content) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(title);
        confirm.setHeaderText(null);
        confirm.setContentText(content);
        return confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void showError(String header, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText(header); a.setContentText(content);
        a.showAndWait();
    }

    // ── NAVEGACIÓN ──────────────────────────────────────────────────────

    @FXML public void handleRefresh(ActionEvent event) { loadData(); }
    @FXML public void handleGoToDashboard(ActionEvent event) { SceneManager.navigateTo("dashboard_admin"); }
    @FXML public void handleGoToCuration(ActionEvent event) { SceneManager.navigateTo("curaduria_admin"); }
    @FXML public void handleGoToSupport(ActionEvent event) { SceneManager.navigateTo("soporte_admin"); }
    @FXML public void handleGoToConfig(ActionEvent event) { SceneManager.navigateTo("config_admin"); }
    @FXML public void handleLogout(ActionEvent event) {
        SessionManager.getInstance().cerrarSesion();
        SceneManager.navigateTo("login");
    }
}
