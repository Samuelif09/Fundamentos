package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.PendingBook;
import com.openlib.market.frontend.service.CurationService;
import com.openlib.market.frontend.session.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class AdminCurationController {

    @FXML private TableView<PendingBook>           pendingBooksTable;
    @FXML private TableColumn<PendingBook, String> colTitle;
    @FXML private TableColumn<PendingBook, String> colAuthor;
    @FXML private TableColumn<PendingBook, String> colSeller;
    @FXML private TableColumn<PendingBook, String> colDate;
    @FXML private TableColumn<PendingBook, Double> colPrice;
    @FXML private TableColumn<PendingBook, Void>   colActions;

    @FXML private VBox loadingContainer;

    private final CurationService curationService = new CurationService();

    @FXML
    public void initialize() {
        configureColumns();
        loadPendingBooks();
    }

    private void configureColumns() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colSeller.setCellValueFactory(new PropertyValueFactory<>("sellerName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("submittedAt"));

        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) setText(null);
                else setText(String.format("$%.2f", price));
            }
        });

        // Setup custom Action buttons cell factory
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnApprove = new Button("✔ Aprobar");
            private final Button btnReject = new Button("✖ Rechazar");
            private final HBox pane = new HBox(8, btnApprove, btnReject);

            {
                btnApprove.getStyleClass().add("btn-approve");
                btnReject.getStyleClass().add("btn-reject");
                pane.setAlignment(Pos.CENTER_LEFT);

                btnApprove.setOnAction(event -> {
                    PendingBook book = getTableView().getItems().get(getIndex());
                    handleApprove(book);
                });

                btnReject.setOnAction(event -> {
                    PendingBook book = getTableView().getItems().get(getIndex());
                    handleReject(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadPendingBooks() {
        loadingContainer.setVisible(true);
        pendingBooksTable.setItems(FXCollections.emptyObservableList());

        curationService.getPendingBooks().whenComplete((books, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (throwable != null) {
                    showError("Error al cargar lista", throwable.getMessage());
                } else {
                    pendingBooksTable.setItems(FXCollections.observableArrayList(books));
                }
            });
        });
    }

    private void handleApprove(PendingBook book) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Aprobar Libro");
        confirm.setHeaderText("¿Estás seguro de aprobar este libro?");
        confirm.setContentText("Título: " + book.getTitle() + "\nSe publicará inmediatamente en el catálogo general.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            loadingContainer.setVisible(true);
            curationService.approveBook(book.getId()).whenComplete((response, throwable) -> {
                Platform.runLater(() -> {
                    loadingContainer.setVisible(false);
                    if (throwable != null || !response.isSuccess()) {
                        showError("Error al aprobar", throwable != null ? throwable.getMessage() : response.getErrorMessage());
                    } else {
                        showInfo("Éxito", "El libro ha sido aprobado y publicado.");
                        loadPendingBooks();
                    }
                });
            });
        }
    }

    private void handleReject(PendingBook book) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rechazar Libro");
        dialog.setHeaderText("Rechazando: " + book.getTitle());
        dialog.setContentText("Motivo del rechazo (obligatorio):");
        dialog.getDialogPane().setPrefWidth(400);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String motivo = result.get().trim();
            if (motivo.isEmpty()) {
                showError("Rechazo cancelado", "Debes especificar un motivo para rechazar el libro.");
                return;
            }

            loadingContainer.setVisible(true);
            curationService.rejectBook(book.getId(), motivo).whenComplete((response, throwable) -> {
                Platform.runLater(() -> {
                    loadingContainer.setVisible(false);
                    if (throwable != null || !response.isSuccess()) {
                        showError("Error al rechazar", throwable != null ? throwable.getMessage() : response.getErrorMessage());
                    } else {
                        showInfo("Rechazado", "El libro fue rechazado y el vendedor ha sido notificado.");
                        loadPendingBooks();
                    }
                });
            });
        }
    }

    @FXML public void handleRefresh(ActionEvent event) { loadPendingBooks(); }
    @FXML public void handleGoToDashboard(ActionEvent event) { SceneManager.navigateTo("dashboard_admin"); }
    @FXML public void handleGoToManagement(ActionEvent event) { SceneManager.navigateTo("gestion_admin"); }
    @FXML public void handleGoToSupport(ActionEvent event) { SceneManager.navigateTo("soporte_admin"); }
    @FXML public void handleGoToConfig(ActionEvent event) { SceneManager.navigateTo("config_admin"); }
    @FXML public void handleLogout(ActionEvent event) {
        SessionManager.getInstance().cerrarSesion();
        SceneManager.navigateTo("login");
    }

    private void showInfo(String header, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Curaduría"); a.setHeaderText(header); a.setContentText(content);
        a.showAndWait();
    }

    private void showError(String header, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText(header); a.setContentText(content);
        a.showAndWait();
    }
}
