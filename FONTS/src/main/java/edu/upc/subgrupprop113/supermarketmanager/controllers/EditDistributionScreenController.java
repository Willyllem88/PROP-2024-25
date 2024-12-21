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


public class EditDistributionScreenController {
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

    public EditDistributionScreenController(PresentationController presentationController) {
        this.presentationController = presentationController;
        visibleUnits = 3;
        currentIndex = 0;
        shelvingUnits = new ArrayList<>();
        swapping = false;
        swappedProducts = new ArrayList<>();
        swappedUnits = new ArrayList<>();
        plusIcons = new ArrayList<>();
    }

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
        topBarController.showSuperSettings(false);
        topBarController.showNewDistributionButton(true);
        topBarController.showImportButton(true);
        if (primaryButtonController1 != null) {
            primaryButtonController1.setLabelText("Order");
            primaryButtonController1.setOnClickHandler(_ -> handleOrder());
        }
        if (primaryButtonController2 != null) {
            primaryButtonController2.setLabelText("Swap");
            primaryButtonController2.setOnClickHandler(_ -> handleSwap());
        }
        initializeMouseMovementDetection();
    }

    private ContextMenu contextMenu;

    @FXML
    private ButtonType confirmationPopup() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to delete the current distribution?");
        confirmationAlert.setContentText("This action cannot be undone.");
        return confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);
    }

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
            List<String> temperatureTypes = new ArrayList<>(Arrays.asList("AMBIENT", "REFRIGERATED", "FROZEN"));
            List<Integer> temperatureValues = new ArrayList<>(Arrays.asList(ambientValue, refrigeratedValue, frozenValue));

            domainController.eraseSupermarketDistribution();
            domainController.createSupermarketDistribution(heightValue, temperatureTypes, temperatureValues);
            reloadShelvingUnits();

            popupStage.close();
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

    @FXML
    private void handleNewDistribution() {

        if (!domainController.getShelvingUnits().isEmpty()) {

            ButtonType result = confirmationPopup();
            if (result == ButtonType.OK) {
                Stage popupStage = popupDistribution();
                popupStage.showAndWait();
            }
         else {
            System.out.println("Delete Canceled");
        }
    } else {
            Stage popupStage = popupDistribution();
            popupStage.showAndWait();
        }
    }

    private void GoBackHandler() {
        if(!swapping) presentationController.goBackEditDistribution();
        else {
            swapping = false;
            reloadShelvingUnitsStatic();
            swappedProducts.clear();
            swappedUnits.clear();
        }
    }


    @FXML
    private void handleOrder() {
            if (contextMenu != null && contextMenu.isShowing()) {
                contextMenu.hide();
            }
            else {
                contextMenu = new ContextMenu();

                MenuItem backtrackingItem = new MenuItem("Backtracking");
                backtrackingItem.setOnAction(_ -> handleBacktracking());
                MenuItem approximationItem = new MenuItem("Approximation");
                approximationItem.setOnAction(_ -> handleApproximation());
                MenuItem greedyItem = new MenuItem("Greedy");
                greedyItem.setOnAction(_ -> handleGreedy());

                contextMenu.getItems().add(backtrackingItem);
                contextMenu.getItems().add(approximationItem);
                contextMenu.getItems().add(greedyItem);

                Point2D screenPosition = primaryButton1.localToScreen(primaryButton1.getBoundsInLocal().getMinX(), primaryButton1.getBoundsInLocal().getMinY());

                double x = screenPosition.getX();
                double y = screenPosition.getY();

                double offsetY = -primaryButton1.getHeight()*1.5;
                double adjustedY = y + offsetY;

                contextMenu.show(primaryButton1, x, adjustedY);
            }
    }

    private void handleBacktracking() {
        domainController.sortSupermarketProducts("BruteForce");
        reloadShelvingUnitsStatic();
    }

    private void handleApproximation() {
        domainController.sortSupermarketProducts("Approximation");
        reloadShelvingUnitsStatic();
    }

    private void handleGreedy() {
        domainController.sortSupermarketProducts("Greedy");
        reloadShelvingUnitsStatic();
    }

    @FXML
    private void handleSwap() {
        swapping = true;
        reloadShelvingUnitsStaticSwap();
    }

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

    private void loadShelvingUnitsSwap(Integer pos, Integer height) {
        shelvingUnits.clear();
        for (int i = 0; i < domainController.getShelvingUnits().size(); i++) {
            final int index = i;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/edu/upc/subgrupprop113/supermarketmanager/fxml/components/shelvingUnit.fxml"));
                SwapShelvingUnitController controller = new SwapShelvingUnitController(presentationController, index, pos, height);

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

    private void handleToggleStateChanged(int shelvingUnitIndex, int productIndex, boolean isSelected) {
        if(isSelected){
            this.swappedProducts.add(new Pair<>(shelvingUnitIndex, productIndex));
            checkSwapProducts();
        }
        else this.swappedProducts.clear();
    }

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

    private void reloadShelvingUnits() {
        currentIndex = 0;
        loadShelvingUnits();
        updateVisibleUnits();
        this.primaryButton1.setVisible(true);
        this.primaryButton2.setVisible(true);
        this.swapMessage.setVisible(false);
        this.spacer.setVisible(true);
    }

    private void reloadShelvingUnitsIndex(Integer index) {
        currentIndex = index;
        reloadShelvingUnitsStatic();
    }

    private void reloadShelvingUnitsStatic() {
        loadShelvingUnits();
        updateVisibleUnits();
        this.primaryButton1.setVisible(true);
        this.primaryButton2.setVisible(true);
        this.swapMessage.setVisible(false);
        this.spacer.setVisible(true);
    }

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


    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }



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
                System.out.println("editShelving clickedIndex: " + clickedIndex);
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

    private void handleTrashIconClick(Integer clickedIndex) {

        try {
            domainController.removeShelvingUnit(clickedIndex);
            reloadShelvingUnits();
            topBarController.toastSuccess("Shelving Unit deleted correctly.", 4500);
            System.out.println("La unidad de estantería está vacía.");
        }
        catch (Exception e) {
            if(e.getMessage().equals("The shelving unit must be empty.")) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Delete Confirmation");
                confirmationAlert.setHeaderText("Are you sure you want to delete this shelving unit?");
                confirmationAlert.setContentText("This action cannot be undone.");

                ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

                if (result == ButtonType.OK) {
                    domainController.emptyShelvingUnit(clickedIndex);
                    domainController.removeShelvingUnit(clickedIndex);
                    topBarController.toastSuccess("Shelving Unit deleted correctly.", 4500);
                    reloadShelvingUnits();
                } else {
                    System.out.println("Eliminación cancelada.");
                }
            }
        }
    }

    private void handleAddIconClick(Integer clickedIndex) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/upc/subgrupprop113/supermarketmanager/fxml/components/setTemperature.fxml"));
            Pane dialogContent = loader.load();
            SetTemperatureController setTemperatureController = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Set Temperature");
            dialog.getDialogPane().setContent(dialogContent);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    String selectedTemperature = setTemperatureController.getTemperature();
                    System.out.println("Temperatura seleccionada: " + selectedTemperature);

                    domainController.addShelvingUnit(clickedIndex, selectedTemperature);
                    topBarController.toastSuccess("Shelving Unit added correctly.", 4500);
                    reloadShelvingUnitsIndex(clickedIndex);
                } else {
                    System.out.println("Operación cancelada.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
