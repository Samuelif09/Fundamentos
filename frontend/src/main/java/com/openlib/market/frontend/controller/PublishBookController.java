package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.service.PublishBookService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

public class PublishBookController {

    // Metadata fields
    @FXML private TextField  tituloField;
    @FXML private TextField  autorField;
    @FXML private TextArea   descripcionField;
    @FXML private TextField  precioField;
    @FXML private TextField  isbnField;
    @FXML private ComboBox<String> categoriaCombo;

    // File zones
    @FXML private VBox   coverDropZone;
    @FXML private Label  coverFileLabel;
    @FXML private VBox   previewDropZone;
    @FXML private Label  previewFileLabel;

    // Upload feedback
    @FXML private Label       errorLabel;
    @FXML private VBox        progressContainer;
    @FXML private ProgressBar uploadProgress;
    @FXML private Label       progressLabel;

    // Actions
    @FXML private Button publishButton;

    private File selectedCoverFile;
    private File selectedPreviewFile;
    private Timeline progressTimeline;

    private final PublishBookService publishService = new PublishBookService();

    private static final List<String> CATEGORIES = List.of(
            "Ficción", "No Ficción", "Ciencia", "Historia",
            "Tecnología", "Educación", "Negocios", "Arte",
            "Filosofía", "Psicología", "Salud", "Viajes"
    );

    @FXML
    public void initialize() {
        categoriaCombo.getItems().addAll(CATEGORIES);
    }

    // ── File selection ───────────────────────────────────────────────────

    @FXML
    public void handleSelectCover(MouseEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar Portada");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes (JPG, PNG, WEBP)", "*.jpg", "*.jpeg", "*.png", "*.webp")
        );
        File file = fc.showOpenDialog(SceneManager.getPrimaryStage());
        if (file != null) {
            selectedCoverFile = file;
            coverFileLabel.setText("✔ " + file.getName());
            coverFileLabel.getStyleClass().setAll("drop-hint-selected");
            coverDropZone.getStyleClass().add("drop-zone-selected");
        }
    }

    @FXML
    public void handleSelectPreview(MouseEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar Archivo de Contenido");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Libros digitales (PDF, EPUB)", "*.pdf", "*.epub")
        );
        File file = fc.showOpenDialog(SceneManager.getPrimaryStage());
        if (file != null) {
            selectedPreviewFile = file;
            previewFileLabel.setText("✔ " + file.getName());
            previewFileLabel.getStyleClass().setAll("drop-hint-selected");
            previewDropZone.getStyleClass().add("drop-zone-selected");
        }
    }

    // ── Submit ───────────────────────────────────────────────────────────

    @FXML
    public void handlePublish(ActionEvent event) {
        if (!validate()) return;

        clearError();
        setFormLocked(true);
        startProgressAnimation();

        publishService.publishBook(
                tituloField.getText().trim(),
                autorField.getText().trim(),
                descripcionField.getText().trim(),
                precioField.getText().trim(),
                categoriaCombo.getValue(),
                isbnField.getText().trim(),
                selectedCoverFile,
                selectedPreviewFile
        ).whenComplete((response, throwable) -> {
            Platform.runLater(() -> {
                stopProgressAnimation();
                setFormLocked(false);

                if (throwable != null) {
                    finishProgress(false);
                    showError("Error al publicar: " + throwable.getMessage());
                } else if (!response.isSuccess()) {
                    finishProgress(false);
                    showError("Error del servidor: " + response.getErrorMessage());
                } else {
                    finishProgress(true);
                    showSuccess();
                }
            });
        });
    }

    // ── Validation ───────────────────────────────────────────────────────

    private boolean validate() {
        if (tituloField.getText().isBlank()) {
            showError("El título es obligatorio."); return false;
        }
        if (autorField.getText().isBlank()) {
            showError("El autor es obligatorio."); return false;
        }
        if (descripcionField.getText().isBlank()) {
            showError("La descripción es obligatoria."); return false;
        }
        if (categoriaCombo.getValue() == null) {
            showError("Debes seleccionar una categoría."); return false;
        }
        try {
            double price = Double.parseDouble(precioField.getText().replace(",", "."));
            if (price <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showError("El precio debe ser un número positivo."); return false;
        }
        if (selectedCoverFile == null) {
            showError("Debes seleccionar una imagen de portada."); return false;
        }
        if (selectedPreviewFile == null) {
            showError("Debes seleccionar el archivo PDF o EPUB del libro."); return false;
        }
        return true;
    }

    // ── Progress animation ───────────────────────────────────────────────

    private void startProgressAnimation() {
        progressContainer.setVisible(true);
        progressContainer.setManaged(true);
        uploadProgress.setProgress(0);

        // Animate to 85% while real upload runs; leaves 15% for server processing
        progressTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,       new KeyValue(uploadProgress.progressProperty(), 0.0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(uploadProgress.progressProperty(), 0.2)),
                new KeyFrame(Duration.seconds(3), new KeyValue(uploadProgress.progressProperty(), 0.6)),
                new KeyFrame(Duration.seconds(7), new KeyValue(uploadProgress.progressProperty(), 0.85))
        );
        progressTimeline.setCycleCount(1);
        progressTimeline.play();
        updateProgressLabel(0.0);

        progressTimeline.currentTimeProperty().addListener((obs, oldT, newT) -> {
            updateProgressLabel(uploadProgress.getProgress());
        });
    }

    private void stopProgressAnimation() {
        if (progressTimeline != null) progressTimeline.stop();
    }

    private void finishProgress(boolean success) {
        double target = success ? 1.0 : 0.0;
        Timeline finish = new Timeline(
                new KeyFrame(Duration.millis(400), new KeyValue(uploadProgress.progressProperty(), target))
        );
        finish.play();
        progressLabel.setText(success ? "✔ ¡Subida completada!" : "✖ Error durante la subida.");
        uploadProgress.setStyle(success
                ? "-fx-accent: #10B981;"
                : "-fx-accent: #EF4444;");
    }

    private void updateProgressLabel(double progress) {
        int pct = (int) (progress * 100);
        if (pct < 20)  progressLabel.setText("Preparando archivos... " + pct + "%");
        else if (pct < 60) progressLabel.setText("Subiendo archivos... " + pct + "%");
        else           progressLabel.setText("Procesando en el servidor... " + pct + "%");
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private void setFormLocked(boolean locked) {
        publishButton.setDisable(locked);
        tituloField.setDisable(locked);
        autorField.setDisable(locked);
        descripcionField.setDisable(locked);
        precioField.setDisable(locked);
        isbnField.setDisable(locked);
        categoriaCombo.setDisable(locked);
    }

    private void showSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Libro Publicado!");
        alert.setHeaderText("Publicación exitosa");
        alert.setContentText("Tu libro ha sido publicado correctamente y ya está disponible en el catálogo.\n\nSerás redirigido a tu inventario.");
        alert.showAndWait();
        SceneManager.navigateTo("inventario_vendedor");
    }

    private void showError(String message) {
        errorLabel.setText("⚠ " + message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void clearError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    // ── Navigation ───────────────────────────────────────────────────────

    @FXML public void handleGoToDashboard(ActionEvent e)  { SceneManager.navigateTo("dashboard_vendedor"); }
    @FXML public void handleGoToInventory(ActionEvent e)  { SceneManager.navigateTo("inventario_vendedor"); }
    @FXML public void handleBackToCatalog(ActionEvent e)  { SceneManager.navigateTo("catalogo"); }
}
