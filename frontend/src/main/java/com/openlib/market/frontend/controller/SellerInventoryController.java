package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.SellerBook;
import com.openlib.market.frontend.service.SellerInventoryService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;

public class SellerInventoryController {

    @FXML private TableView<SellerBook>              booksTable;
    @FXML private TableColumn<SellerBook, Long>      colId;
    @FXML private TableColumn<SellerBook, String>    colTitle;
    @FXML private TableColumn<SellerBook, Double>    colPrice;
    @FXML private TableColumn<SellerBook, Integer>   colStock;
    @FXML private TableColumn<SellerBook, String>    colStatus;
    @FXML private TableColumn<SellerBook, Void>      colActions;

    @FXML private TextField searchField;
    @FXML private VBox loadingContainer;

    private final SellerInventoryService inventoryService = new SellerInventoryService();
    private ObservableList<SellerBook> masterList;

    @FXML
    public void initialize() {
        configureColumns();
        loadInventory();

        // Live search filter
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            if (masterList == null) return;
            if (newText == null || newText.isBlank()) {
                booksTable.setItems(masterList);
                return;
            }
            FilteredList<SellerBook> filtered = new FilteredList<>(masterList,
                book -> book.getTitle() != null &&
                        book.getTitle().toLowerCase().contains(newText.toLowerCase().trim())
            );
            booksTable.setItems(filtered);
        });
    }

    private void configureColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Price — formatted as currency
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("$%.2f", price));
            }
        });

        // Status — colour chips
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                Label chip = new Label(status.toUpperCase());
                chip.getStyleClass().add("ACTIVE".equalsIgnoreCase(status) ? "status-active" : "status-paused");
                setGraphic(chip);
                setText(null);
                setAlignment(Pos.CENTER);
            }
        });

        // Actions column — custom CellFactory
        colActions.setCellFactory(buildActionsCellFactory());
    }

    private Callback<TableColumn<SellerBook, Void>, TableCell<SellerBook, Void>> buildActionsCellFactory() {
        return col -> new TableCell<>() {
            private final Button editBtn  = new Button("✏ Editar Precio");
            private final Button pauseBtn = new Button("⏸ Pausar");
            private final HBox  btnBox   = new HBox(8, editBtn, pauseBtn);

            {
                editBtn.getStyleClass().add("action-edit-btn");
                pauseBtn.getStyleClass().add("action-pause-btn");
                btnBox.setAlignment(Pos.CENTER);

                editBtn.setOnAction(e -> {
                    SellerBook book = getTableView().getItems().get(getIndex());
                    handleEditPrice(book);
                });
                pauseBtn.setOnAction(e -> {
                    SellerBook book = getTableView().getItems().get(getIndex());
                    handleTogglePause(book);
                });
            }

            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btnBox);
            }
        };
    }

    private void handleEditPrice(SellerBook book) {
        TextInputDialog dialog = new TextInputDialog(String.format("%.2f", book.getPrice()));
        dialog.setTitle("Editar Precio");
        dialog.setHeaderText("Libro: " + book.getTitle());
        dialog.setContentText("Nuevo precio ($):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(value -> {
            try {
                double newPrice = Double.parseDouble(value.replace(",", "."));
                book.setPrice(newPrice);
                booksTable.refresh();
                showInfo("Precio actualizado",
                        "El precio de «" + book.getTitle() + "» fue actualizado a $" + String.format("%.2f", newPrice) +
                        ".\n(Pendiente sincronización con el servidor)");
            } catch (NumberFormatException ex) {
                showError("Valor inválido", "El precio ingresado no es un número válido.");
            }
        });
    }

    private void handleTogglePause(SellerBook book) {
        boolean isActive = "ACTIVE".equalsIgnoreCase(book.getStatus());
        String newStatus = isActive ? "PAUSED" : "ACTIVE";
        String action    = isActive ? "pausada" : "reactivada";

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar acción");
        confirm.setHeaderText("¿Cambiar estado del libro?");
        confirm.setContentText("«" + book.getTitle() + "» será " + action + ".");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                book.setStatus(newStatus);
                booksTable.refresh();
                showInfo("Estado actualizado",
                        "El libro «" + book.getTitle() + "» ahora está " + newStatus.toLowerCase() + "." +
                        "\n(Pendiente sincronización con el servidor)");
            }
        });
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        searchField.clear();
        loadInventory();
    }

    private void loadInventory() {
        loadingContainer.setVisible(true);
        booksTable.setItems(FXCollections.emptyObservableList());

        inventoryService.getInventory().whenComplete((books, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (throwable != null) {
                    showError("Error al cargar inventario", throwable.getMessage());
                } else {
                    populate(books);
                }
            });
        });
    }

    private void populate(List<SellerBook> books) {
        masterList = FXCollections.observableArrayList(books);
        booksTable.setItems(masterList);
    }

    @FXML public void handleGoToDashboard(ActionEvent e) { SceneManager.navigateTo("dashboard_vendedor"); }
    @FXML public void handleBackToCatalog(ActionEvent e) { SceneManager.navigateTo("catalogo"); }
    @FXML public void handleGoToPublish(ActionEvent e)   { SceneManager.navigateTo("publicar_libro"); }

    private void showInfo(String header, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Información"); a.setHeaderText(header); a.setContentText(content);
        a.showAndWait();
    }

    private void showError(String header, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText(header); a.setContentText(content);
        a.showAndWait();
    }
}
