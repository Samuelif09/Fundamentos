package com.openlib.market.frontend.controller;

import com.openlib.market.frontend.app.SceneManager;
import com.openlib.market.frontend.model.AffiliateLink;
import com.openlib.market.frontend.model.StoreProfile;
import com.openlib.market.frontend.service.StoreService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MyStoreController {

    // Tab 1 – Profile & Banner
    @FXML private TextField  storeNameField;
    @FXML private TextField  contactEmailField;
    @FXML private ImageView  bannerPreview;
    @FXML private Label      bannerPlaceholder;
    @FXML private Label      bannerStatusLabel;

    // Tab 2 – Description
    @FXML private TextArea   descriptionArea;
    @FXML private Label      charCountLabel;

    // Tab 3 – Affiliate links
    @FXML private VBox       linksContainer;
    @FXML private VBox       addLinkForm;
    @FXML private TextField  linkLabelField;
    @FXML private TextField  linkUrlField;
    @FXML private TextField  linkCodeField;

    @FXML private VBox loadingContainer;

    private final StoreService storeService = new StoreService();
    private StoreProfile currentProfile;

    @FXML
    public void initialize() {
        // Live char counter for description
        descriptionArea.textProperty().addListener((obs, oldText, newText) ->
                charCountLabel.setText(newText.length() + " / 1000 caracteres"));
        loadData();
    }

    // ── Data Loading ────────────────────────────────────────────────────

    private void loadData() {
        loadingContainer.setVisible(true);

        CompletableFuture<StoreProfile>    profileFuture = storeService.getStoreProfile();
        CompletableFuture<List<AffiliateLink>> linksFuture = storeService.getAffiliateLinks();

        CompletableFuture.allOf(profileFuture, linksFuture).whenComplete((v, throwable) -> {
            Platform.runLater(() -> {
                loadingContainer.setVisible(false);
                if (throwable != null) {
                    showError("Error al cargar tienda", throwable.getMessage());
                } else {
                    try {
                        populateProfile(profileFuture.join());
                        populateLinks(linksFuture.join());
                    } catch (Exception e) {
                        showError("Error procesando datos", e.getMessage());
                    }
                }
            });
        });
    }

    private void populateProfile(StoreProfile profile) {
        currentProfile = profile;
        storeNameField.setText(profile.getStoreName() != null ? profile.getStoreName() : "");
        contactEmailField.setText(profile.getContactEmail() != null ? profile.getContactEmail() : "");
        descriptionArea.setText(profile.getDescription() != null ? profile.getDescription() : "");

        if (profile.getBannerUrl() != null && !profile.getBannerUrl().isBlank()) {
            try {
                bannerPreview.setImage(new Image(profile.getBannerUrl(), true));
                bannerPlaceholder.setVisible(false);
            } catch (Exception ignored) { /* bad URL, show placeholder */ }
        }
    }

    private void populateLinks(List<AffiliateLink> links) {
        linksContainer.getChildren().clear();
        if (links == null || links.isEmpty()) {
            Label empty = new Label("No tienes links de afiliado configurados todavía.");
            empty.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
            linksContainer.getChildren().add(empty);
            return;
        }
        links.forEach(link -> linksContainer.getChildren().add(buildLinkCard(link)));
    }

    // ── Tab 1: Save Profile ─────────────────────────────────────────────

    @FXML
    public void handleSaveProfile(ActionEvent event) {
        if (currentProfile == null) currentProfile = new StoreProfile();
        currentProfile.setStoreName(storeNameField.getText().trim());
        currentProfile.setContactEmail(contactEmailField.getText().trim());

        storeService.updateStoreProfile(currentProfile).whenComplete((resp, ex) ->
                Platform.runLater(() -> {
                    if (ex != null || !resp.isSuccess())
                        showError("Error al guardar", ex != null ? ex.getMessage() : resp.getErrorMessage());
                    else
                        showInfo("Perfil Guardado", "Los datos de tu tienda fueron actualizados correctamente.");
                }));
    }

    // ── Tab 1: Upload Banner ────────────────────────────────────────────

    @FXML
    public void handleUploadBanner(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar Banner");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes (JPG, PNG)", "*.jpg", "*.jpeg", "*.png"));
        File file = fc.showOpenDialog(SceneManager.getPrimaryStage());
        if (file == null) return;

        bannerStatusLabel.setText("Subiendo banner...");

        storeService.uploadBanner(file).whenComplete((resp, ex) ->
                Platform.runLater(() -> {
                    if (ex != null || !resp.isSuccess()) {
                        bannerStatusLabel.setText("✖ Error al subir banner.");
                        bannerStatusLabel.setStyle("-fx-text-fill: #EF4444;");
                    } else {
                        bannerStatusLabel.setText("✔ Banner actualizado exitosamente.");
                        bannerStatusLabel.setStyle("-fx-text-fill: #059669;");
                        // Preview with local file
                        bannerPreview.setImage(new Image(file.toURI().toString()));
                        bannerPlaceholder.setVisible(false);
                    }
                }));
    }

    // ── Tab 2: Save Description ─────────────────────────────────────────

    @FXML
    public void handleSaveDescription(ActionEvent event) {
        if (currentProfile == null) currentProfile = new StoreProfile();
        currentProfile.setDescription(descriptionArea.getText().trim());

        storeService.updateStoreProfile(currentProfile).whenComplete((resp, ex) ->
                Platform.runLater(() -> {
                    if (ex != null || !resp.isSuccess())
                        showError("Error al guardar descripción", ex != null ? ex.getMessage() : resp.getErrorMessage());
                    else
                        showInfo("Descripción guardada", "Tu descripción pública fue actualizada.");
                }));
    }

    // ── Tab 3: Affiliate links CRUD ─────────────────────────────────────

    @FXML
    public void handleShowAddLink(ActionEvent event) {
        addLinkForm.setVisible(true);
        addLinkForm.setManaged(true);
        linkLabelField.clear(); linkUrlField.clear(); linkCodeField.clear();
    }

    @FXML
    public void handleCancelAddLink(ActionEvent event) {
        addLinkForm.setVisible(false);
        addLinkForm.setManaged(false);
    }

    @FXML
    public void handleCreateLink(ActionEvent event) {
        String label = linkLabelField.getText().trim();
        String url   = linkUrlField.getText().trim();
        String code  = linkCodeField.getText().trim();

        if (label.isEmpty() || url.isEmpty()) {
            showError("Campos requeridos", "El nombre y la URL son obligatorios.");
            return;
        }

        AffiliateLink newLink = new AffiliateLink(label, url, code);
        storeService.createAffiliateLink(newLink).whenComplete((resp, ex) ->
                Platform.runLater(() -> {
                    if (ex != null || !resp.isSuccess())
                        showError("Error al crear link", ex != null ? ex.getMessage() : resp.getErrorMessage());
                    else {
                        addLinkForm.setVisible(false);
                        addLinkForm.setManaged(false);
                        // Add card for the newly created link
                        AffiliateLink created = resp.getBody() != null ? resp.getBody() : newLink;
                        linksContainer.getChildren().add(0, buildLinkCard(created));
                    }
                }));
    }

    private void handleDeleteLink(AffiliateLink link, HBox card) {
        if (link.getId() == null) {
            linksContainer.getChildren().remove(card);
            return;
        }
        storeService.deleteAffiliateLink(link.getId()).whenComplete((resp, ex) ->
                Platform.runLater(() -> {
                    if (ex != null || !resp.isSuccess())
                        showError("Error al eliminar", ex != null ? ex.getMessage() : resp.getErrorMessage());
                    else
                        linksContainer.getChildren().remove(card);
                }));
    }

    private HBox buildLinkCard(AffiliateLink link) {
        HBox card = new HBox(15);
        card.getStyleClass().add("link-card");
        card.setAlignment(Pos.CENTER_LEFT);

        VBox info = new VBox(4);
        Label lblLabel = new Label("🔗 " + (link.getLabel() != null ? link.getLabel() : "Sin nombre"));
        lblLabel.getStyleClass().add("link-label");
        Label lblUrl = new Label(link.getUrl() != null ? link.getUrl() : "");
        lblUrl.getStyleClass().add("link-url");
        Label lblCode = new Label("Código: " + (link.getCode() != null ? link.getCode() : "—"));
        lblCode.getStyleClass().add("link-code");
        info.getChildren().addAll(lblLabel, lblUrl, lblCode);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button delBtn = new Button("✕ Eliminar");
        delBtn.getStyleClass().add("delete-link-btn");
        delBtn.setOnAction(e -> handleDeleteLink(link, card));

        card.getChildren().addAll(info, spacer, delBtn);
        return card;
    }

    @FXML public void handleRefresh(ActionEvent e)       { loadData(); }
    @FXML public void handleGoToDashboard(ActionEvent e) { SceneManager.navigateTo("dashboard_vendedor"); }
    @FXML public void handleGoToInventory(ActionEvent e) { SceneManager.navigateTo("inventario_vendedor"); }
    @FXML public void handleGoToPublish(ActionEvent e)   { SceneManager.navigateTo("publicar_libro"); }
    @FXML public void handleGoToWallet(ActionEvent e)    { SceneManager.navigateTo("billetera_vendedor"); }
    @FXML public void handleBackToCatalog(ActionEvent e) { SceneManager.navigateTo("catalogo"); }

    private void showInfo(String header, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Éxito"); a.setHeaderText(header); a.setContentText(content);
        a.showAndWait();
    }

    private void showError(String header, String content) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error"); a.setHeaderText(header); a.setContentText(content);
        a.showAndWait();
    }
}
