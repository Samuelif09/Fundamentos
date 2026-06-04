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
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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

        // Columna de Rol
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colUserRole.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String role, boolean empty) {
                super.updateItem(role, empty);
                if (empty || role == null) { setGraphic(null); setText(null); return; }
                String label;
                String style;
                switch (role.toUpperCase()) {
                    case "S", "SELLER", "VENDEDOR" -> { label = "VENDEDOR"; style = "chip-role-s"; }
                    case "C", "BUYER", "COMPRADOR" -> { label = "COMPRADOR"; style = "chip-role-c"; }
                    default                         -> { label = role.toUpperCase(); style = "chip-role-c"; }
                }
                Label chip = new Label(label);
                chip.getStyleClass().addAll("chip", style);
                setGraphic(chip);
                setText(null);
            }
        });

        // Columna de Estado
        colUserStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colUserStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) { setGraphic(null); setText(null); return; }
                String label;
                String style;
                switch (status.toUpperCase()) {
                    case "ACTIVO", "ACTIVE"         -> { label = "ACTIVO";    style = "chip-active"; }
                    case "SUSPENDIDO", "SUSPENDED"  -> { label = "SUSPENDIDO"; style = "chip-suspend"; }
                    case "BLOQUEADO", "BLOCKED"     -> { label = "BLOQUEADO"; style = "chip-suspend"; }
                    case "PENDIENTE", "PENDING"     -> { label = "PENDIENTE"; style = "chip-pending"; }
                    default                         -> { label = status;       style = "chip-pending"; }
                }
                Label chip = new Label(label);
                chip.getStyleClass().addAll("chip", style);
                setGraphic(chip);
                setText(null);
            }
        });

        // Columna de Acciones (Suspender / Reactivar / Aprobar)
        colUserAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnSuspend    = new Button("Bloquear");
            private final Button btnReactivate = new Button("Reactivar");
            private final Button btnApprove    = new Button("Aprobar");
            private final HBox box = new HBox(5, btnSuspend, btnReactivate, btnApprove);

            {
                btnSuspend.getStyleClass().add("btn-suspend");
                btnReactivate.getStyleClass().add("btn-reactivate");
                btnApprove.getStyleClass().add("btn-approve");

                btnSuspend.setOnAction(e -> {
                    AdminUser user = getTableView().getItems().get(getIndex());
                    handleSuspendUser(user);
                });
                btnReactivate.setOnAction(e -> {
                    AdminUser user = getTableView().getItems().get(getIndex());
                    handleReactivateUser(user);
                });
                btnApprove.setOnAction(e -> {
                    AdminUser user = getTableView().getItems().get(getIndex());
                    handleApproveUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                AdminUser user = getTableView().getItems().get(getIndex());
                boolean isSelf = user.getEmail().equals(SessionManager.getInstance().getEmail());
                String st = user.getStatus() != null ? user.getStatus().toUpperCase() : "";

                boolean isActive    = st.contains("ACTIV");
                boolean isSuspended = st.contains("SUSPEN") || st.contains("BLOQ") || st.contains("BLOCK");
                boolean isPending   = st.contains("PENDIE") || st.contains("PENDING");

                btnSuspend.setVisible(!isSelf && isActive);
                btnSuspend.setManaged(!isSelf && isActive);
                btnReactivate.setVisible(!isSelf && isSuspended);
                btnReactivate.setManaged(!isSelf && isSuspended);
                btnApprove.setVisible(!isSelf && isPending);
                btnApprove.setManaged(!isSelf && isPending);

                setGraphic(box);
            }
        });

        // Filtro de búsqueda
        FilteredList<AdminUser> filteredUsers = new FilteredList<>(masterUserData, p -> true);
        userSearchField.textProperty().addListener((obs, old, val) -> {
            filteredUsers.setPredicate(user -> {
                if (val == null || val.isEmpty()) return true;
                String lc = val.toLowerCase();
                return user.getEmail().toLowerCase().contains(lc) ||
                       (user.getFullName() != null && user.getFullName().toLowerCase().contains(lc));
            });
        });
        usersTable.setItems(filteredUsers);
    }

    // ── CONFIGURACIÓN DE TABLA DE PEDIDOS ─────────────────────────────

    private void configureOrderTable() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderEmail.setCellValueFactory(new PropertyValueFactory<>("buyerEmail"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Monto formateado
        colOrderAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colOrderAmount.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) setText(null);
                else setText(String.format("$%.2f", amount));
            }
        });

        // Estado con chip de color
        colOrderStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colOrderStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) { setGraphic(null); setText(null); return; }
                String label;
                String style;
                switch (status.toUpperCase()) {
                    case "COMPLETADO", "COMPLETED" -> { label = "COMPLETADO"; style = "chip-completed"; }
                    case "REEMBOLSADO", "REFUNDED" -> { label = "REEMBOLSADO"; style = "chip-refunded"; }
                    case "CANCELADO", "CANCELLED"  -> { label = "CANCELADO";  style = "chip-cancelled"; }
                    case "PENDIENTE", "PENDING"    -> { label = "PENDIENTE";  style = "chip-pending-tx"; }
                    default                        -> { label = status;        style = "chip-pending-tx"; }
                }
                Label chip = new Label(label);
                chip.getStyleClass().addAll("chip", style);
                setGraphic(chip);
                setText(null);
            }
        });

        // Acción de reembolso
        colOrderAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnRefund = new Button("Reembolsar");

            {
                btnRefund.getStyleClass().add("btn-refund");
                btnRefund.setOnAction(e -> {
                    AdminOrder order = getTableView().getItems().get(getIndex());
                    handleRefundOrder(order);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                AdminOrder order = getTableView().getItems().get(getIndex());
                String st = order.getStatus() != null ? order.getStatus().toUpperCase() : "";
                boolean alreadyRefunded = st.contains("REEMBOLSADO") || st.contains("REFUNDED") || st.contains("CANCELADO") || st.contains("CANCELLED");
                btnRefund.setDisable(alreadyRefunded);
                setGraphic(btnRefund);
            }
        });

        // Filtro de búsqueda
        FilteredList<AdminOrder> filteredOrders = new FilteredList<>(masterOrderData, p -> true);
        orderSearchField.textProperty().addListener((obs, old, val) -> {
            filteredOrders.setPredicate(order -> {
                if (val == null || val.isEmpty()) return true;
                String lc = val.toLowerCase();
                return (order.getOrderId() != null && order.getOrderId().toLowerCase().contains(lc)) ||
                       (order.getBuyerEmail() != null && order.getBuyerEmail().toLowerCase().contains(lc));
            });
        });
        ordersTable.setItems(filteredOrders);
    }

    // ── CARGA DE DATOS ──────────────────────────────────────────────────

    private void loadData() {
        loadingContainer.setVisible(true);

        var usersFuture  = service.getUsers();
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
        Optional<String> motivo = showMotivoDialog(
                "Bloquear Usuario",
                "Ingresa el motivo para bloquear la cuenta de:\n" + user.getEmail(),
                "Ej: Incumplimiento de términos de servicio"
        );
        if (motivo.isEmpty()) return;

        loadingContainer.setVisible(true);
        service.suspendUser(user.getId(), motivo.get()).whenComplete((res, t) ->
                Platform.runLater(() -> loadData()));
    }

    private void handleReactivateUser(AdminUser user) {
        if (!confirmAction("Reactivar Usuario",
                "¿Permitir el acceso nuevamente a:\n" + user.getEmail() + "?")) return;

        loadingContainer.setVisible(true);
        service.reactivateUser(user.getId()).whenComplete((res, t) ->
                Platform.runLater(() -> loadData()));
    }

    private void handleApproveUser(AdminUser user) {
        if (!confirmAction("Aprobar Cuenta",
                "¿Aprobar y activar la cuenta de:\n" + user.getEmail() + "?")) return;

        loadingContainer.setVisible(true);
        service.approveUser(user.getId()).whenComplete((res, t) ->
                Platform.runLater(() -> loadData()));
    }

    private void handleRefundOrder(AdminOrder order) {
        Optional<String> motivo = showMotivoDialog(
                "Emitir Reembolso",
                "Pedido: " + order.getOrderId() + "\nMonto: $" + String.format("%.2f", order.getTotalAmount()) +
                "\nComprador: " + order.getBuyerEmail() + "\n\nEsta acción es irreversible. Ingresa el motivo:",
                "Ej: Producto no entregado, error de cobro, etc."
        );
        if (motivo.isEmpty()) return;

        loadingContainer.setVisible(true);
        service.refundOrder(order.getOrderId(), motivo.get()).whenComplete((res, t) ->
                Platform.runLater(() -> loadData()));
    }

    // ── DIÁLOGOS ────────────────────────────────────────────────────────

    /**
     * Muestra un diálogo con un TextArea para ingresar el motivo de la acción.
     * @return Optional con el motivo si el usuario confirma, vacío si cancela.
     */
    private Optional<String> showMotivoDialog(String title, String headerText, String placeholder) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        ButtonType confirmarBtn = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarBtn, ButtonType.CANCEL);

        TextArea areaMotivo = new TextArea();
        areaMotivo.setPromptText(placeholder);
        areaMotivo.setWrapText(true);
        areaMotivo.setPrefRowCount(3);
        areaMotivo.setPrefWidth(400);

        VBox content = new VBox(10, new Label("Motivo:"), areaMotivo);
        content.setPadding(new Insets(15));
        dialog.getDialogPane().setContent(content);

        // Deshabilitar el botón de confirmar si el motivo está vacío
        javafx.scene.Node confirmButton = dialog.getDialogPane().lookupButton(confirmarBtn);
        confirmButton.setDisable(true);
        areaMotivo.textProperty().addListener((obs, old, val) ->
                confirmButton.setDisable(val == null || val.trim().isEmpty()));

        dialog.setResultConverter(btn -> {
            if (btn == confirmarBtn) return areaMotivo.getText().trim();
            return null;
        });

        return dialog.showAndWait();
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
