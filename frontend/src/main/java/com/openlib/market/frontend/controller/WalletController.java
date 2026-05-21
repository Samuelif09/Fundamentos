package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.Transaction;
import com.openlib.market.frontend.service.WalletService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class WalletController {

    // KPIs
    @FXML private Label saldoDisponibleLabel;
    @FXML private Label totalVentasLabel;
    @FXML private Label totalComisionesLabel;
    @FXML private Button withdrawButton;

    // Table
    @FXML private TableView<Transaction>           transactionsTable;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, String> colType;
    @FXML private TableColumn<Transaction, String> colDescription;
    @FXML private TableColumn<Transaction, Double> colAmount;

    @FXML private VBox loadingContainer;

    private final WalletService walletService = new WalletService();

    @FXML
    public void initialize() {
        configureColumns();
        loadData();
    }

    private void configureColumns() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Type — colour chips
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colType.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String type, boolean empty) {
                super.updateItem(type, empty);
                if (empty || type == null) { setGraphic(null); return; }
                Label chip = new Label(translateType(type));
                chip.getStyleClass().add(chipStyleFor(type));
                setGraphic(chip);
                setText(null);
                setAlignment(Pos.CENTER);
            }
        });

        // Amount — sign + colour
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) { setText(null); return; }
                boolean positive = amount >= 0;
                setText((positive ? "+" : "") + String.format("$%.2f", amount));
                getStyleClass().removeAll("amount-positive", "amount-negative");
                getStyleClass().add(positive ? "amount-positive" : "amount-negative");
                setAlignment(Pos.CENTER_RIGHT);
            }
        });
    }

    private void loadData() {
        loadingContainer.setVisible(true);
        transactionsTable.setItems(FXCollections.emptyObservableList());
        resetKpis();

        walletService.getTransactions().whenComplete((transactions, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (throwable != null) {
                    showError("Error al cargar transacciones", throwable.getMessage());
                } else {
                    populate(transactions);
                }
            });
        });
    }

    private void populate(List<Transaction> transactions) {
        transactionsTable.setItems(FXCollections.observableArrayList(transactions));
        computeKpis(transactions);
    }

    private void computeKpis(List<Transaction> transactions) {
        double totalSales      = 0;
        double totalCommissions = 0;
        double totalWithdrawals = 0;

        for (Transaction t : transactions) {
            switch (t.getType() != null ? t.getType().toUpperCase() : "") {
                case "SALE"       -> totalSales += t.getAmount();
                case "COMMISSION" -> totalCommissions += Math.abs(t.getAmount());
                case "WITHDRAWAL" -> totalWithdrawals += Math.abs(t.getAmount());
            }
        }

        double balance = totalSales - totalCommissions - totalWithdrawals;

        saldoDisponibleLabel.setText(String.format("$%.2f", Math.max(0, balance)));
        totalVentasLabel.setText(String.format("$%.2f", totalSales));
        totalComisionesLabel.setText(String.format("$%.2f", totalCommissions));
    }

    private void resetKpis() {
        saldoDisponibleLabel.setText("$0.00");
        totalVentasLabel.setText("$0.00");
        totalComisionesLabel.setText("$0.00");
    }

    @FXML
    public void handleWithdraw(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Solicitar Retiro");
        dialog.setHeaderText("Ingresa el monto a retirar");
        dialog.setContentText("Monto ($):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(value -> {
            try {
                double monto = Double.parseDouble(value.replace(",", "."));
                if (monto <= 0) throw new NumberFormatException();
                processWithdrawal(monto);
            } catch (NumberFormatException e) {
                showError("Monto inválido", "Por favor ingresa un número positivo.");
            }
        });
    }

    private void processWithdrawal(double monto) {
        withdrawButton.setDisable(true);

        walletService.requestWithdrawal(monto).whenComplete((response, throwable) -> {
            Platform.runLater(() -> {
                withdrawButton.setDisable(false);
                if (throwable != null) {
                    showError("Error en la solicitud", throwable.getMessage());
                } else if (!response.isSuccess()) {
                    showError("Error del servidor", response.getErrorMessage());
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Retiro Solicitado");
                    alert.setHeaderText("Solicitud enviada exitosamente");
                    alert.setContentText(String.format(
                            "Tu solicitud de retiro por $%.2f ha sido recibida.\n\nSerá procesada en los próximos 1-3 días hábiles.", monto));
                    alert.showAndWait();
                    loadData(); // Refresh to show new WITHDRAWAL transaction
                }
            });
        });
    }

    @FXML public void handleRefresh(ActionEvent e)      { loadData(); }
    @FXML public void handleGoToDashboard(ActionEvent e) { SceneManager.navigateTo("dashboard_vendedor"); }
    @FXML public void handleGoToInventory(ActionEvent e) { SceneManager.navigateTo("inventario_vendedor"); }
    @FXML public void handleGoToPublish(ActionEvent e)   { SceneManager.navigateTo("publicar_libro"); }
    @FXML public void handleBackToCatalog(ActionEvent e) { SceneManager.navigateTo("catalogo"); }

    // ── Helpers ──────────────────────────────────────────────────────────

    private String translateType(String type) {
        return switch (type.toUpperCase()) {
            case "SALE"       -> "Venta";
            case "COMMISSION" -> "Comisión";
            case "WITHDRAWAL" -> "Retiro";
            default           -> type;
        };
    }

    private String chipStyleFor(String type) {
        return switch (type.toUpperCase()) {
            case "SALE"       -> "chip-sale";
            case "COMMISSION" -> "chip-commission";
            case "WITHDRAWAL" -> "chip-withdrawal";
            default           -> "chip-sale";
        };
    }

    private void showError(String header, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText(header); a.setContentText(content);
        a.showAndWait();
    }
}
