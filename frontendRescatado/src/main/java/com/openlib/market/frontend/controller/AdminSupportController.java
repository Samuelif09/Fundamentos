package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.SupportTicket;
import com.openlib.market.frontend.service.SupportService;
import com.openlib.market.frontend.session.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AdminSupportController {

    @FXML private ListView<SupportTicket> ticketsListView;
    
    // Detail Panel
    @FXML private VBox emptyDetailPane;
    @FXML private VBox ticketDetailPane;
    @FXML private Label lblSubject;
    @FXML private Label lblEmail;
    @FXML private Label lblPriority;
    @FXML private Label lblStatus;
    @FXML private TextArea txtDescription;
    @FXML private VBox replyBox;
    @FXML private TextArea txtReply;

    @FXML private VBox loadingContainer;

    private final SupportService supportService = new SupportService();
    private SupportTicket currentTicket;

    @FXML
    public void initialize() {
        configureListView();
        setupSelectionListener();
        loadTickets();
    }

    private void configureListView() {
        ticketsListView.setCellFactory(param -> new ListCell<>() {
            private final Label title = new Label();
            private final Label subtitle = new Label();
            private final Label prioChip = new Label();
            private final HBox header = new HBox(5, title, new Region(), prioChip);
            private final VBox container = new VBox(3, header, subtitle);

            {
                HBox.setHgrow(header.getChildren().get(1), javafx.scene.layout.Priority.ALWAYS);
                title.getStyleClass().add("list-cell-title");
                subtitle.getStyleClass().add("list-cell-subtitle");
                prioChip.getStyleClass().add("chip");
            }

            @Override
            protected void updateItem(SupportTicket item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    title.setText(item.getSubject());
                    subtitle.setText(item.getUserEmail());
                    prioChip.setText(item.getPriority());
                    
                    // Priority color
                    prioChip.getStyleClass().removeAll("chip-high", "chip-medium", "chip-low", "chip-closed");
                    if ("CLOSED".equalsIgnoreCase(item.getStatus())) {
                        prioChip.getStyleClass().add("chip-closed");
                        prioChip.setText("CERRADO");
                    } else if ("HIGH".equalsIgnoreCase(item.getPriority())) {
                        prioChip.getStyleClass().add("chip-high");
                    } else if ("MEDIUM".equalsIgnoreCase(item.getPriority())) {
                        prioChip.getStyleClass().add("chip-medium");
                    } else {
                        prioChip.getStyleClass().add("chip-low");
                    }
                    
                    setGraphic(container);
                }
            }
        });
    }

    private void setupSelectionListener() {
        ticketsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            showTicketDetails(newVal);
        });
    }

    private void loadTickets() {
        loadingContainer.setVisible(true);
        currentTicket = null;
        ticketsListView.setItems(FXCollections.emptyObservableList());
        showTicketDetails(null);

        supportService.getTickets().whenComplete((tickets, t) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (t != null) {
                    showError("Error de carga", t.getMessage());
                } else {
                    // Sort: OPEN first, then by priority (HIGH > MEDIUM > LOW)
                    tickets.sort(Comparator.comparing((SupportTicket tk) -> tk.getStatus().equals("OPEN") ? 0 : 1)
                            .thenComparing(tk -> {
                                return switch (tk.getPriority().toUpperCase()) {
                                    case "HIGH" -> 1;
                                    case "MEDIUM" -> 2;
                                    default -> 3;
                                };
                            }));
                    ticketsListView.setItems(FXCollections.observableArrayList(tickets));
                }
            });
        });
    }

    private void showTicketDetails(SupportTicket ticket) {
        currentTicket = ticket;
        if (ticket == null) {
            emptyDetailPane.setVisible(true);
            emptyDetailPane.setManaged(true);
            ticketDetailPane.setVisible(false);
            ticketDetailPane.setManaged(false);
            return;
        }

        emptyDetailPane.setVisible(false);
        emptyDetailPane.setManaged(false);
        ticketDetailPane.setVisible(true);
        ticketDetailPane.setManaged(true);

        lblSubject.setText(ticket.getSubject());
        lblEmail.setText(ticket.getUserEmail());
        txtDescription.setText(ticket.getDescription());
        txtReply.clear();

        // Update priority chip
        lblPriority.setText(ticket.getPriority());
        lblPriority.getStyleClass().removeAll("chip-high", "chip-medium", "chip-low");
        if ("HIGH".equalsIgnoreCase(ticket.getPriority())) lblPriority.getStyleClass().add("chip-high");
        else if ("MEDIUM".equalsIgnoreCase(ticket.getPriority())) lblPriority.getStyleClass().add("chip-medium");
        else lblPriority.getStyleClass().add("chip-low");

        // Update status chip and controls
        lblStatus.setText(ticket.getStatus());
        lblStatus.getStyleClass().removeAll("chip-open", "chip-closed");
        boolean isClosed = "CLOSED".equalsIgnoreCase(ticket.getStatus());
        
        if (isClosed) {
            lblStatus.getStyleClass().add("chip-closed");
            replyBox.setDisable(true);
            txtReply.setPromptText("Este caso ya fue cerrado y no admite más respuestas.");
        } else {
            lblStatus.getStyleClass().add("chip-open");
            replyBox.setDisable(false);
            txtReply.setPromptText("Escribe la solución o respuesta al usuario aquí...");
        }
    }

    @FXML
    public void handleSendReply(ActionEvent event) {
        if (currentTicket == null) return;
        String message = txtReply.getText().trim();
        if (message.isEmpty()) {
            showError("Respuesta vacía", "Debes escribir un mensaje para responder al usuario.");
            return;
        }

        loadingContainer.setVisible(true);
        supportService.replyToTicket(currentTicket.getId(), message).whenComplete((res, t) -> {
            Platform.runLater(() -> {
                if (t != null || !res.isSuccess()) {
                    loadingContainer.setVisible(false);
                    showError("Error al enviar", t != null ? t.getMessage() : res.getErrorMessage());
                } else {
                    // Refrescar lista para obtener el estado real
                    loadTickets();
                }
            });
        });
    }

    @FXML
    public void handleCloseTicket(ActionEvent event) {
        if (currentTicket == null) return;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cerrar Caso");
        confirm.setHeaderText("¿Marcar este caso como resuelto?");
        confirm.setContentText("El usuario ya no podrá añadir respuestas a este hilo.");
        
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        loadingContainer.setVisible(true);
        supportService.closeTicket(currentTicket.getId()).whenComplete((res, t) -> {
            Platform.runLater(() -> {
                if (t != null || !res.isSuccess()) {
                    loadingContainer.setVisible(false);
                    showError("Error al cerrar", t != null ? t.getMessage() : res.getErrorMessage());
                } else {
                    loadTickets();
                }
            });
        });
    }

    @FXML public void handleRefresh(ActionEvent event) { loadTickets(); }
    @FXML public void handleGoToDashboard(ActionEvent event) { SceneManager.navigateTo("dashboard_admin"); }
    @FXML public void handleGoToCuration(ActionEvent event) { SceneManager.navigateTo("curaduria_admin"); }
    @FXML public void handleGoToManagement(ActionEvent event) { SceneManager.navigateTo("gestion_admin"); }
    @FXML public void handleGoToConfig(ActionEvent event) { SceneManager.navigateTo("config_admin"); }
    @FXML public void handleLogout(ActionEvent event) {
        SessionManager.getInstance().cerrarSesion();
        SceneManager.navigateTo("login");
    }

    private void showError(String header, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Mesa de Ayuda"); a.setHeaderText(header); a.setContentText(content);
        a.showAndWait();
    }
}
