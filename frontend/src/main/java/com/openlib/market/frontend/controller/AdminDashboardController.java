package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.AdminChartData;
import com.openlib.market.frontend.model.AdminKpi;
import com.openlib.market.frontend.service.AdminDashboardService;
import com.openlib.market.frontend.session.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.concurrent.CompletableFuture;

public class AdminDashboardController {

    @FXML private Label totalUsersLabel;
    @FXML private Label pendingSellersLabel;
    @FXML private Label totalBooksLabel;
    @FXML private Label platformRevenueLabel;
    
    @FXML private VBox pendingSellersCard;

    @FXML private LineChart<String, Number> userGrowthChart;
    @FXML private BarChart<String, Number>  revenueGrowthChart;

    @FXML private VBox loadingContainer;

    private final AdminDashboardService adminService = new AdminDashboardService();

    @FXML
    public void initialize() {
        loadDashboardData();
    }

    private void loadDashboardData() {
        loadingContainer.setVisible(true);

        CompletableFuture<AdminKpi> kpiFuture = adminService.getGlobalKpis();
        CompletableFuture<AdminChartData> chartFuture = adminService.getChartData();

        CompletableFuture.allOf(kpiFuture, chartFuture).whenComplete((v, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);

                if (throwable != null) {
                    showError("Error al cargar dashboard", throwable.getMessage());
                } else {
                    try {
                        populateKpis(kpiFuture.join());
                        populateCharts(chartFuture.join());
                    } catch (Exception e) {
                        showError("Error procesando datos", e.getMessage());
                    }
                }
            });
        });
    }

    private void populateKpis(AdminKpi kpis) {
        totalUsersLabel.setText(String.valueOf(kpis.getTotalUsers()));
        pendingSellersLabel.setText(String.valueOf(kpis.getPendingSellers()));
        totalBooksLabel.setText(String.valueOf(kpis.getTotalBooks()));
        platformRevenueLabel.setText(String.format("$%.2f", kpis.getPlatformRevenue()));

        // Highlight pending sellers if there are any
        if (kpis.getPendingSellers() > 0) {
            if (!pendingSellersCard.getStyleClass().contains("kpi-card-warning")) {
                pendingSellersCard.getStyleClass().add("kpi-card-warning");
            }
            pendingSellersLabel.getStyleClass().add("text-warning");
        } else {
            pendingSellersCard.getStyleClass().remove("kpi-card-warning");
            pendingSellersLabel.getStyleClass().remove("text-warning");
        }
    }

    private void populateCharts(AdminChartData chartData) {
        // LineChart (User Growth)
        userGrowthChart.getData().clear();
        if (chartData.getUserGrowth() != null && !chartData.getUserGrowth().isEmpty()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Nuevos Usuarios");
            for (AdminChartData.DataPoint dp : chartData.getUserGrowth()) {
                series.getData().add(new XYChart.Data<>(dp.getLabel(), dp.getValue()));
            }
            userGrowthChart.getData().add(series);
        }

        // BarChart (Revenue Growth)
        revenueGrowthChart.getData().clear();
        if (chartData.getRevenueGrowth() != null && !chartData.getRevenueGrowth().isEmpty()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Ingresos ($)");
            for (AdminChartData.DataPoint dp : chartData.getRevenueGrowth()) {
                series.getData().add(new XYChart.Data<>(dp.getLabel(), dp.getValue()));
            }
            revenueGrowthChart.getData().add(series);
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        loadDashboardData();
    }

    @FXML
    public void handleGoToCuration(ActionEvent event) {
        SceneManager.navigateTo("curaduria_admin");
    }

    @FXML
    public void handleGoToManagement(ActionEvent event) {
        SceneManager.navigateTo("gestion_admin");
    }

    @FXML
    public void handleGoToSupport(ActionEvent event) {
        SceneManager.navigateTo("soporte_admin");
    }

    @FXML
    public void handleGoToConfig(ActionEvent event) {
        SceneManager.navigateTo("config_admin");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        SessionManager.getInstance().cerrarSesion();
        SceneManager.navigateTo("login");
    }

    private void showError(String header, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error Dashboard");
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }
}
