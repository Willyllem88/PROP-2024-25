package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.controllers.components.*;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.feather.Feather;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.PrimaryButtonController;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Controller for managing the edit distribution view of the supermarket manager application.
 * Handles interactions with the user interface, including shelving unit management,
 * ordering, swapping, and creation of new distributions.
 */
public class EditDistributionController {
    private final PresentationController presentationController;
    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    private TopBarController topBarController;

    @FXML
    private Region spacer;

    @FXML
    private VBox leftButtonContainer;

    @FXML
    private VBox rightButtonContainer;

    @FXML
    private VBox primaryButton1;
    @FXML
    private VBox primaryButton2;

    @FXML
    private HBox topBar;

    @FXML
    private HBox shelvingUnitContainer;

    @FXML
    private Label swapMessage;

    @FXML
    private FontIcon leftButton;

    @FXML
    private FontIcon rightButton;

    private final List<Node> shelvingUnits;
    private final int visibleUnits;
    private int currentIndex;

    private boolean swapping;

    private final List<FontIcon> plusIcons;

    private final ArrayList<Pair<Integer, Integer>> swappedProducts;
    private final ArrayList<Integer> swappedUnits;

    /**
     * Constructs the EditDistributionController with a reference to the presentation controller.
     *
     * @param presentationController The presentation controller used for navigation and view handling.
     */
    public EditDistributionController(PresentationController presentationController) {
        this.presentationController = presentationController;
        visibleUnits = 3;
        currentIndex = 0;
        shelvingUnits = new ArrayList<>();
        swapping = false;
        swappedProducts = new ArrayList<>();
        swappedUnits = new ArrayList<>();
        plusIcons = new ArrayList<>();
    }

    /**
     * Initializes the EditDistributionController, setting up bindings, event handlers,
     * and preparing the user interface components.
     */
    @FXML
    private void initialize() {
        PrimaryButtonController primaryButtonController1 = (PrimaryButtonController) primaryButton1.getProperties().get("controller");
        PrimaryButtonController primaryButtonController2 = (PrimaryButtonController) primaryButton2.getProperties().get("controller");
        topBarController = (TopBarController) topBar.getProperties().get("controller");
        leftButton.iconSizeProperty().bind(Bindings.createIntegerBinding(
                () -> (int) ((this.leftButtonContainer.getHeight()*0.7 + this.leftButtonContainer.getWidth()*0.3) * 0.15),
                this.leftButtonContainer.heightProperty(),
                this.leftButtonContainer.widthProperty()
        ));
        rightButton.iconSizeProperty().bind(Bindings.createIntegerBinding(
                () -> (int) ((this.leftButtonContainer.getHeight()*0.7 + this.leftButtonContainer.getWidth()*0.3) * 0.15),
                rightButtonContainer.heightProperty(),
                rightButtonContainer.widthProperty()
        ));
        reloadShelvingUnits();
        topBarController.setOnImportHandler(_ -> reloadShelvingUnits());
        topBarController.setOnNewDistributionHandler(_ -> handleNewDistribution());
        topBarController.setOnGoBackHandler(_ -> GoBackHandler());
        topBarController.showDistributionSettings(false);
        topBarController.showNewDistributionButton(true);
        topBarController.showImportButton(true);
        if (primaryButtonController1 != null) {
            primaryButtonController1.setLabelText("Order By");
            primaryButtonController1.setOnClickHandler(_ -> handleOrder());
        }
        if (primaryButtonController2 != null) {
            primaryButtonController2.setLabelText("Swap");
            primaryButtonController2.setOnClickHandler(_ -> handleSwap());
        }
        initializeMouseMovementDetection();
    }

    private ContextMenu contextMenu;

    /**
     * Displays a confirmation popup for deleting the current distribution.
     *
     * @return The ButtonType selected by the user in the confirmation dialog.
     */
    @FXML
    private ButtonType confirmationPopup() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("\"Warning: This action cannot be undone\"!\"");
        confirmationAlert.setContentText(
                "Please note:\n" +
                "- Any unsaved changes will be permanently lost.\n" +
                "Are you sure you want to create a new distribution?");
        ButtonType yesButton = new ButtonType("Yes", ButtonType.OK.getButtonData());
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

        return confirmationAlert.showAndWait().orElse(noButton);
    }

    /**
     * Displays a popup for creating a new supermarket distribution with temperature settings
     * and shelving unit height configuration.
     *
     * @return The created Stage instance for the popup.
     */
    @FXML
    private Stage popupDistribution() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("New Distribution Settings");

        Image frozenImage = new Image(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/FROZEN.png").toExternalForm());
        Image refrigeratedImage = new Image(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/REFRIGERATED.png").toExternalForm());
        Image ambientImage = new Image(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/assets/temperatureIcons/AMBIENT.png").toExternalForm());

        ImageView frozenIcon = new ImageView(frozenImage);
        ImageView refrigeratedIcon = new ImageView(refrigeratedImage);
        ImageView ambientIcon = new ImageView(ambientImage);

        frozenIcon.setFitWidth(30);
        frozenIcon.setFitHeight(30);
        refrigeratedIcon.setFitWidth(30);
        refrigeratedIcon.setFitHeight(30);
        ambientIcon.setFitWidth(30);
        ambientIcon.setFitHeight(30);

        Spinner<Integer> frozenSpinner = new Spinner<>(0, 100, 0);
        Spinner<Integer> refrigeratedSpinner = new Spinner<>(0, 100, 0);
        Spinner<Integer> ambientSpinner = new Spinner<>(0, 100, 0);

        Spinner<Integer> heightSpinner = new Spinner<>(1, 10, 1);

        Label heightLabel = new Label("Height of shelving units:");

        Button setButton = new Button("SET");
        setButton.setOnAction(e -> {
            int frozenValue = frozenSpinner.getValue();
            int refrigeratedValue = refrigeratedSpinner.getValue();
            int ambientValue = ambientSpinner.getValue();
            int heightValue = heightSpinner.getValue();
            if(frozenValue == 0 && refrigeratedValue == 0 && ambientValue == 0) {
                topBarController.toastError("Minimum one shelving unit.", 4500);
                popupStage.close();
            }
            else {
                List<String> temperatureTypes = new ArrayList<>(Arrays.asList("AMBIENT", "REFRIGERATED", "FROZEN"));
                List<Integer> temperatureValues = new ArrayList<>(Arrays.asList(ambientValue, refrigeratedValue, frozenValue));

                domainController.eraseSupermarketDistribution();
                domainController.createSupermarketDistribution(heightValue, temperatureTypes, temperatureValues);
                reloadShelvingUnits();

                popupStage.close();
            }
        });

        HBox frozenBox = new HBox(10, frozenIcon, frozenSpinner);
        HBox refrigeratedBox = new HBox(10, refrigeratedIcon, refrigeratedSpinner);
        HBox ambientBox = new HBox(10, ambientIcon, ambientSpinner);
        HBox heightBox = new HBox(10, heightLabel, heightSpinner);

        frozenBox.setAlignment(Pos.CENTER);
        refrigeratedBox.setAlignment(Pos.CENTER);
        ambientBox.setAlignment(Pos.CENTER);
        heightBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(15, frozenBox, refrigeratedBox, ambientBox, heightBox, setButton);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainLayout);
        popupStage.setScene(scene);

    return popupStage;
    }

    /**
     * Handles the creation of a new supermarket distribution.
     * If a distribution already exists, it confirms deletion before creating a new one.
     */
    @FXML
    private void handleNewDistribution() {

        if (!domainController.getShelvingUnits().isEmpty()) {

            ButtonType result = confirmationPopup();
            if (result.getButtonData() == ButtonType.OK.getButtonData()) {
                Stage popupStage = popupDistribution();
                popupStage.showAndWait();
            }
    } else {
            Stage popupStage = popupDistribution();
            popupStage.showAndWait();
        }
    }

    /**
     * Handles the "Go Back" action. If swapping is active, it cancels the swapping process.
     * Otherwise, it navigates back to the previous view.
     */
    private void GoBackHandler() {
        if(!swapping) presentationController.goBackEditDistribution();
        else {
            swapping = false;
            reloadShelvingUnitsStatic();
            swappedProducts.clear();
            swappedUnits.clear();
        }
    }


    /**
     * Handles the ordering action, displaying a context menu with sorting algorithms
     * for catalog and supermarket orders.
     */
    private void handleOrder() {
        if (contextMenu != null && contextMenu.isShowing()) {
            contextMenu.hide();
        } else {
            contextMenu = new ContextMenu();
            Menu catalogMenu = new Menu("Catalog");
            MenuItem bruteForceItemCatalog = new MenuItem("BruteForce (this might take a while)");
            bruteForceItemCatalog.setOnAction(_ -> {
                contextMenu.hide();
                handleBruteForce("Catalog");
                topBarController.toastSuccess("Ordered successfully", 4500);
            });
            MenuItem approximationItemCatalog = new MenuItem("Approximation");
            approximationItemCatalog.setOnAction(_ -> {
                contextMenu.hide();
                handleApproximation("Catalog");
                topBarController.toastSuccess("Ordered successfully", 4500);
            });
            MenuItem greedyItemCatalog = new MenuItem("Greedy");
            greedyItemCatalog.setOnAction(_ -> {
                contextMenu.hide();
                handleGreedy("Catalog");
                topBarController.toastSuccess("Ordered successfully", 4500);
            });
            catalogMenu.getItems().addAll(bruteForceItemCatalog, approximationItemCatalog, greedyItemCatalog);

            Menu supermarketMenu = new Menu("Supermarket");
            MenuItem bruteForceItemSuper = new MenuItem("BruteForce (this might take a while)");
            bruteForceItemSuper.setOnAction(_ -> {
                contextMenu.hide();
                handleBruteForce("Supermarket");
                topBarController.toastSuccess("Ordered successfully", 4500);
            });
            MenuItem approximationItemSuper = new MenuItem("Approximation");
            approximationItemSuper.setOnAction(_ -> {
                contextMenu.hide();
                handleApproximation("Supermarket");
                topBarController.toastSuccess("Ordered successfully", 4500);
            });
            MenuItem greedyItemSuper = new MenuItem("Greedy");
            greedyItemSuper.setOnAction(_ -> {
                contextMenu.hide();
                handleGreedy("Supermarket");
                topBarController.toastSuccess("Ordered successfully", 4500);
            });
            supermarketMenu.getItems().addAll(bruteForceItemSuper, approximationItemSuper, greedyItemSuper);

            contextMenu.getItems().addAll(catalogMenu, supermarketMenu);

            Point2D screenPosition = primaryButton1.localToScreen(primaryButton1.getBoundsInLocal().getMinX(), primaryButton1.getBoundsInLocal().getMinY());

            double x = screenPosition.getX();
            double y = screenPosition.getY();

            double offsetY = -primaryButton1.getHeight() * 1.5;
            double adjustedY = y + offsetY;

            contextMenu.show(primaryButton1, x, adjustedY);
        }
    }

    /**
     * Sorts products using the bruteForce algorithm for either the catalog or supermarket.
     *
     * @param orderType Specifies whether to order "Catalog" or "Supermarket".
     */
    private void handleBruteForce(String orderType) {
        if(Objects.equals(orderType, "Supermarket")) {
            domainController.sortSupermarketProducts("BruteForce");
        }
        else domainController.sortSupermarketByCatalogProducts("BruteForce");
        reloadShelvingUnitsStatic();
    }

    /**
     * Sorts products using the approximation algorithm for either the catalog or supermarket.
     *
     * @param orderType Specifies whether to order "Catalog" or "Supermarket".
     */
    private void handleApproximation(String orderType) {
        if(Objects.equals(orderType, "Supermarket")) {
            domainController.sortSupermarketProducts("Approximation");
        }
        else domainController.sortSupermarketByCatalogProducts("Approximation");
        reloadShelvingUnitsStatic();
    }

    /**
     * Sorts products using the greedy algorithm for either the catalog or supermarket.
     *
     * @param orderType Specifies whether to order "Catalog" or "Supermarket".
     */
    private void handleGreedy(String orderType) {
        if(Objects.equals(orderType, "Supermarket")) {
            domainController.sortSupermarketProducts("Greedy");
        }
        else domainController.sortSupermarketByCatalogProducts("Greedy");
        reloadShelvingUnitsStatic();
    }

    /**
     * Enables the swapping mode for shelving units or products.
     * Updates the user interface to facilitate swapping actions.
     */
    @FXML
    private void handleSwap() {
        swapping = true;
        reloadShelvingUnitsStaticSwap();
    }

    /**
     * Reloads the shelving units and updates the visible units.
     * Resets the swapping mode and visibility of related UI elements.
     */
    private void reloadShelvingUnitsStaticSwap() {
        if(!swappedProducts.isEmpty()) {
            loadShelvingUnitsSwap(swappedProducts.get(0).getKey(), swappedProducts.get(0).getValue());
        }
        else loadShelvingUnitsSwap(-1,-1);
        updateVisibleUnitsSwap();
        this.primaryButton1.setVisible(false);
        this.primaryButton2.setVisible(false);
        this.swapMessage.setVisible(true);
        this.spacer.setVisible(false);
    }

    /**
     * Loads shelving units into the container in swapping mode, allowing users to select products for swapping.
     * Each shelving unit is associated with a controller that handles toggle button state changes for swapping.
     *
     * @param pos   The position of the product that is highlighted during swapping.
     * @param height The height of the product that is highlighted during swapping.
     */
    private void loadShelvingUnitsSwap(Integer pos, Integer height) {
        shelvingUnits.clear();
        for (int i = 0; i < domainController.getShelvingUnits().size(); i++) {
            final int index = i;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/edu/upc/subgrupprop113/supermarketmanager/fxml/components/shelvingUnit.fxml"));
                ShelvingUnitSwapController controller = new ShelvingUnitSwapController(presentationController, index, pos, height);

                controller.setOnToggleButtonStateChanged((productIndex, isSelected) -> handleToggleStateChanged(index, productIndex, isSelected));

                loader.setController(controller);

                HBox shelvingUnit = loader.load();
                shelvingUnits.add(shelvingUnit);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load Shelving Unit Component", e);
            }
        }
        shelvingUnitContainer.getChildren().addAll(shelvingUnits);
    }

    /**
     * Handles changes in the toggle button state for a product within a shelving unit.
     * Adds or clears selected products for swapping based on the toggle state.
     *
     * @param shelvingUnitIndex The index of the shelving unit containing the product.
     * @param productIndex      The height of the product within the shelving unit.
     * @param isSelected        Whether the product toggle is selected or deselected.
     */
    private void handleToggleStateChanged(int shelvingUnitIndex, int productIndex, boolean isSelected) {
        if(isSelected){
            this.swappedProducts.add(new Pair<>(shelvingUnitIndex, productIndex));
            checkSwapProducts();
        }
        else this.swappedProducts.clear();
    }

    /**
     * Checks if two products have been selected for swapping. If two products are selected,
     * attempts to swap them and updates the shelving units.
     * Provides feedback to the user based on the success or failure of the swap.
     */
    private void checkSwapProducts() {
        if(swappedProducts.size() == 2) {
            Pair<Integer, Integer> p1 = swappedProducts.get(0);
            Pair<Integer, Integer> p2 = swappedProducts.get(1);
            try {
                domainController.swapProductsFromShelvingUnits(p1.getKey(), p2.getKey(), p1.getValue(), p2.getValue());
                topBarController.toastSuccess("Swapped Successfully!", 4500);
            } catch (Exception e) {
                if(e.getMessage().equals("The temperature of the product is not compatible with the shelving unit.")) {
                    topBarController.toastError("Cannot do the swap, incompatible temperatures.", 4500);
                }
            }
            swappedProducts.clear();
            swappedUnits.clear();
            swapping = false;
            reloadShelvingUnitsStatic();
        }
    }

    /**
     * Reloads the shelving units and updates the visible units.
     * Resets the swapping mode and visibility of related UI elements.
     */
    private void reloadShelvingUnits() {
        currentIndex = 0;
        loadShelvingUnits();
        updateVisibleUnits();
        this.primaryButton1.setVisible(true);
        this.primaryButton2.setVisible(true);
        this.swapMessage.setVisible(false);
        this.spacer.setVisible(true);
    }

    /**
     * Reloads shelving units setting the current index.
     * Updates the visible units in the user interface.
     */
    private void reloadShelvingUnitsIndex(Integer index) {
        currentIndex = index;
        reloadShelvingUnitsStatic();
    }

    /**
     * Reloads shelving units without resetting the current index.
     * Updates the visible units in the user interface.
     */
    private void reloadShelvingUnitsStatic() {
        loadShelvingUnits();
        updateVisibleUnits();
        this.primaryButton1.setVisible(true);
        this.primaryButton2.setVisible(true);
        this.swapMessage.setVisible(false);
        this.spacer.setVisible(true);
    }

    /**
     * Loads shelving unit components from the FXML file and populates the list of shelving units.
     */
    private void loadShelvingUnits() {
        shelvingUnits.clear();
        for (int i = 0; i < domainController.getShelvingUnits().size(); i++) {
            final int index = i;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/edu/upc/subgrupprop113/supermarketmanager/fxml/components/shelvingUnit.fxml"));
                loader.setController(new ShelvingUnitController(presentationController, index));

                HBox shelvingUnit = loader.load();
                shelvingUnits.add(shelvingUnit);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load Shelving Unit Component", e);
            }
        }

        shelvingUnitContainer.getChildren().addAll(shelvingUnits);
    }

    /**
     * Adds a "plus" icon to a shelving unit with proximity-based visibility behavior.
     *
     * @param shelvingUnitContainer The container to which the icon is added.
     * @param index                 The index of the shelving unit.
     */
    private void addPlusIconWithProximityBehavior(HBox shelvingUnitContainer, int index) {
        StackPane iconWrapper = new StackPane();
        iconWrapper.setMinSize(50, 50);
        iconWrapper.setStyle("-fx-background-color: transparent;");

        FontIcon plusIcon = new FontIcon(Feather.PLUS_CIRCLE);
        plusIcon.getStyleClass().add("responsive-icon");
        plusIcon.iconSizeProperty().bind(Bindings.createIntegerBinding(
                () -> (int) ((shelvingUnitContainer.getHeight() * 0.2 + shelvingUnitContainer.getWidth() * 0.2) * 0.15),
                shelvingUnitContainer.heightProperty(),
                shelvingUnitContainer.widthProperty()
        ));
        plusIcon.setUserData(index);
        plusIcon.setVisible(false);

        iconWrapper.getChildren().add(plusIcon);
        shelvingUnitContainer.getChildren().add(iconWrapper);

        plusIcons.add(plusIcon);

        plusIcon.setOnMouseClicked(event -> {
            Integer clickedIndex = (Integer) plusIcon.getUserData();
            handleAddIconClick(clickedIndex);
        });
    }

    /**
     * Initializes mouse movement detection to manage the visibility of proximity-based icons.
     */
    @FXML
    private void initializeMouseMovementDetection() {
        shelvingUnitContainer.setOnMouseMoved(event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();

            for (FontIcon plusIcon : plusIcons) {
                Node iconWrapper = plusIcon.getParent();
                Point2D iconPositionInScene = iconWrapper.localToScene(iconWrapper.getBoundsInLocal().getWidth() / 2,
                        iconWrapper.getBoundsInLocal().getHeight() / 2);

                double distance = calculateDistance(mouseX, mouseY, iconPositionInScene.getX(), iconPositionInScene.getY());
                double screenWidth = Screen.getPrimary().getBounds().getWidth();
                double screenHeight = Screen.getPrimary().getBounds().getHeight();
                Stage stage = (Stage) shelvingUnitContainer.getScene().getWindow();

                double currentWidth = stage.getWidth();
                double currentHeight = stage.getHeight();
                plusIcon.setVisible(distance < (400 * ((currentHeight*currentWidth)/(screenHeight*screenWidth))) );
            }
        });
    }

    /**
     * Calculates the distance between two points in 2D space.
     *
     * @param x1 The x-coordinate of the first point.
     * @param y1 The y-coordinate of the first point.
     * @param x2 The x-coordinate of the second point.
     * @param y2 The y-coordinate of the second point.
     * @return The Euclidean distance between the two points.
     */
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Updates the visible shelving units in the user interface based on the current index.
     * Adds icons for editing, deleting, or adding shelving units.
     */
    private void updateVisibleUnits() {
        shelvingUnitContainer.getChildren().clear();
        int showingUnits = Math.min(visibleUnits, shelvingUnits.size());

        addPlusIconWithProximityBehavior(shelvingUnitContainer, (currentIndex)%shelvingUnits.size());

        for (int i = 0; i < showingUnits; i++) {
            int index = (currentIndex + i) % shelvingUnits.size();
            VBox shelvingUnitWithIcons = new VBox();

            shelvingUnitWithIcons.getChildren().add(shelvingUnits.get(index));

            HBox iconsContainer = new HBox();
            iconsContainer.getStyleClass().add("container-icons");
            FontIcon editIcon = new FontIcon(Feather.EDIT_2);
            editIcon.getStyleClass().add("responsive-icon-1");
            editIcon.setIconSize(36);
            editIcon.setUserData(index);

            editIcon.setOnMouseClicked(event -> {
                Integer clickedIndex = (Integer) editIcon.getUserData();
                presentationController.shelvingUnitEdited(clickedIndex);
            });

            FontIcon trashIcon = new FontIcon(Feather.TRASH_2);
            trashIcon.getStyleClass().add("responsive-icon-2");
            trashIcon.setIconSize(36);
            trashIcon.setUserData(index);

            trashIcon.setOnMouseClicked(event -> {
                Integer clickedIndex = (Integer) trashIcon.getUserData();
                handleTrashIconClick(clickedIndex);
            });


            iconsContainer.getChildren().addAll(editIcon, trashIcon);
            shelvingUnitWithIcons.getChildren().add(iconsContainer);

            shelvingUnitContainer.getChildren().add(shelvingUnitWithIcons);

            if (i < showingUnits - 1) {
                addPlusIconWithProximityBehavior(shelvingUnitContainer, (index + 1) % shelvingUnits.size());
            }
        }
        addPlusIconWithProximityBehavior(shelvingUnitContainer, (currentIndex + showingUnits) % shelvingUnits.size());
    }

    /**
     * Updates the visible shelving units in swapping mode, enabling toggles for selecting units to swap.
     */
    private void updateVisibleUnitsSwap() {
        shelvingUnitContainer.getChildren().clear();
        int showingUnits = Math.min(visibleUnits, shelvingUnits.size());

        for (int i = 0; i < showingUnits; i++) {
            int index = (currentIndex + i) % shelvingUnits.size();
            VBox shelvingUnitWithIcons = new VBox();

            shelvingUnitWithIcons.getChildren().add(shelvingUnits.get(index));

            HBox iconsContainer = new HBox();
            iconsContainer.getStyleClass().add("container-icons");

            ToggleButton toggleButton = new ToggleButton();
            FontIcon icon;
            if(!swappedUnits.isEmpty()) {
                if(swappedUnits.getFirst() != index)  icon = new FontIcon(Feather.SQUARE);
                else  {
                    icon = new FontIcon(Feather.CHECK_SQUARE);
                    toggleButton.setSelected(true);
                }
            }
            else icon = new FontIcon(Feather.SQUARE);

            toggleButton.setGraphic(icon);
            toggleButton.setMinHeight(25);
            toggleButton.setMinWidth(25);
            toggleButton.setStyle("-fx-background-color: transparent;");
            toggleButton.setPadding(new Insets(5, 5, 5, 5));


            int finalI = index;
            toggleButton.setOnAction(event -> {
                boolean isSelected = toggleButton.isSelected();
                icon.setIconCode(isSelected ? Feather.CHECK_SQUARE : Feather.SQUARE);
                if(isSelected) handleSwapShelvingUnits(finalI);
                else swappedUnits.clear();
            });
            toggleButton.setVisible(true);


            iconsContainer.getChildren().addAll(toggleButton);
            shelvingUnitWithIcons.getChildren().add(iconsContainer);

            shelvingUnitContainer.getChildren().add(shelvingUnitWithIcons);
        }
    }

    /**
     * Handles the swapping of shelving units based on user selection.
     *
     * @param index The index of the selected shelving unit.
     */
    private void handleSwapShelvingUnits(Integer index) {
        swappedUnits.add(index);
        if(swappedUnits.size() == 2) {
            domainController.swapShelvingUnits(swappedUnits.get(0), swappedUnits.get(1));
            swappedUnits.clear();
            swappedProducts.clear();
            reloadShelvingUnitsStatic();
            swapping = false;
            topBarController.toastSuccess("Swapped Successfully!", 4500);
        }
    }

    /**
     * Moves the visible shelving units to the right, updating the displayed units accordingly.
     */
    @FXML
    private void moveShelvingUnitsRight() {
        if (shelvingUnits.size() <= visibleUnits) return;

        currentIndex = (currentIndex + 1) % shelvingUnits.size();

        if(!swapping) updateVisibleUnits();
        else {
            updateVisibleUnitsSwap();
            reloadShelvingUnitsStaticSwap();
        }
    }

    /**
     * Moves the visible shelving units to the left, updating the displayed units accordingly.
     */
    @FXML
    private void moveShelvingUnitsLeft() {
        if (shelvingUnits.size() <= visibleUnits) return;

        currentIndex = (currentIndex - 1 + shelvingUnits.size()) % shelvingUnits.size();

        if(!swapping) updateVisibleUnits();
        else {
            updateVisibleUnitsSwap();
            reloadShelvingUnitsStaticSwap();
        }
    }

    /**
     * Handles the deletion of a shelving unit. Prompts for confirmation if the unit is not empty.
     *
     * @param clickedIndex The index of the shelving unit to delete.
     */
    private void handleTrashIconClick(Integer clickedIndex) {

        try {
            domainController.removeShelvingUnit(clickedIndex);
            reloadShelvingUnits();
            topBarController.toastSuccess("Shelving Unit deleted correctly.", 4500);
        }
        catch (Exception e) {
            if(e.getMessage().equals("The shelving unit must be empty.")) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Delete Confirmation");
                confirmationAlert.setHeaderText("Are you sure you want to delete this shelving unit?");
                confirmationAlert.setContentText("This action cannot be undone.");
                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                confirmationAlert.getButtonTypes().setAll(yesButton, noButton);

                ButtonType result = confirmationAlert.showAndWait().orElse(noButton);

                if (result == yesButton) {
                    domainController.emptyShelvingUnit(clickedIndex);
                    domainController.removeShelvingUnit(clickedIndex);
                    topBarController.toastSuccess("Shelving Unit deleted correctly.", 4500);
                    reloadShelvingUnits();
                }
            }
        }
    }

    /**
     * Handles the addition of a new shelving unit with a temperature setting.
     *
     * @param clickedIndex The index of the shelving unit where the new unit is added.
     */
    private void handleAddIconClick(Integer clickedIndex) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/fxml/components/setTemperature.fxml"));
            Pane dialogContent = loader.load();
            SetTemperatureController setTemperatureController = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Set Temperature");
            dialog.getDialogPane().setContent(dialogContent);
            ButtonType yesButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(yesButton, noButton);

            dialog.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    String selectedTemperature = setTemperatureController.getTemperature();

                    domainController.addShelvingUnit(clickedIndex, selectedTemperature);
                    topBarController.toastSuccess("Shelving Unit added correctly.", 4500);
                    reloadShelvingUnitsIndex(clickedIndex);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
