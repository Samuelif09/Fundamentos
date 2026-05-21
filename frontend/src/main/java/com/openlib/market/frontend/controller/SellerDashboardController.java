package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.SellerFinance;
import com.openlib.market.frontend.model.SellerSalesMetrics;
import com.openlib.market.frontend.service.SellerDashboardService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SellerDashboardController {

    @FXML private Label totalRevenueLabel;
    @FXML private Label pendingBalanceLabel;
    @FXML private Label totalOrdersLabel;
    @FXML private Label totalBooksSoldLabel;
    
    @FXML private LineChart<String, Number> salesChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    
    @FXML private VBox loadingContainer;

    private final SellerDashboardService dashboardService = new SellerDashboardService();

    @FXML
    public void initialize() {
        loadData();
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        loadData();
    }

    private void loadData() {
        loadingContainer.setVisible(true);

        CompletableFuture<SellerFinance> financeFuture = dashboardService.getFinances();
        CompletableFuture<SellerSalesMetrics> metricsFuture = dashboardService.getMetrics();

        CompletableFuture.allOf(financeFuture, metricsFuture).whenComplete((v, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (throwable != null) {
                    showError("Error al cargar dashboard", throwable.getMessage());
                } else {
                    try {
                        populateFinances(financeFuture.join());
                        populateMetrics(metricsFuture.join());
                    } catch (Exception e) {
                        showError("Error procesando datos", e.getMessage());
                    }
                }
            });
        });
    }

    private void populateFinances(SellerFinance finance) {
        if (finance == null) return;
        totalRevenueLabel.setText(String.format("$%.2f", finance.getTotalRevenue()));
        pendingBalanceLabel.setText(String.format("$%.2f", finance.getPendingBalance()));
        totalOrdersLabel.setText(String.valueOf(finance.getTotalOrders()));
    }

    private void populateMetrics(SellerSalesMetrics metrics) {
        if (metrics == null) return;
        
        totalBooksSoldLabel.setText(String.valueOf(metrics.getTotalBooksSold()));
        
        salesChart.getData().clear();
        
        if (metrics.getMonthlySales() != null && !metrics.getMonthlySales().isEmpty()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Ventas");
            
            for (Map.Entry<String, Integer> entry : metrics.getMonthlySales().entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            
            salesChart.getData().add(series);
        }
    }

    @FXML
    public void handleBackToCatalog(ActionEvent event) { SceneManager.navigateTo("catalogo"); }

    @FXML
    public void handleGoToInventory(ActionEvent event) { SceneManager.navigateTo("inventario_vendedor"); }

    @FXML
    public void handleGoToWallet(ActionEvent event) { SceneManager.navigateTo("billetera_vendedor"); }

    @FXML
    public void handleGoToMyStore(ActionEvent event) { SceneManager.navigateTo("mi_tienda"); }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
