package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.stream.Collectors;


public class ShelvingUnitEditionController extends ShelvingUnitController {

    @FXML
    VBox editButtonsVB;

    public ShelvingUnitEditionController(PresentationController presentationController, int supermarketPosition) {
        super(presentationController, supermarketPosition);
    }

    @Override
    protected void initView() {
        super.initView();

        // Clear
        editButtonsVB.getChildren().clear();
        editButtonsVB.setVisible(true);
        editButtonsVB.setManaged(true);

        // Fixed size of 70: temperature img (50 px) + 20 px of spacing
        addFixedSpacer(editButtonsVB, 70);

        // Add a region to the VBox to make the buttons appear at the bottom
        addFlexibleSpacer(editButtonsVB);

        // Add the buttons
        for (int i = 0; i < super.shelvingUnitDto.getProducts().size(); i++) {
            final int index = i;
            if (super.shelvingUnitDto.getProducts().get(i) != null) {
                // Add a minus icon
                FontIcon minusIcon = createFontIcon("fth-trash-2", 50);
                minusIcon.setOnMouseClicked(event -> eliminateProductHandler(index));
                editButtonsVB.getChildren().add(minusIcon);
            } else {
                // Add a plus icon
                FontIcon plusIcon = createFontIcon("fth-plus-circle", 50);
                plusIcon.setOnMouseClicked(event -> addProductHandler(index));
                editButtonsVB.getChildren().add(plusIcon);
            }

            // Add a spacer between icons
            addFlexibleSpacer(editButtonsVB);

            // Add an extra spacer if not the last product
            if (i < super.shelvingUnitDto.getProducts().size() - 1) {
                addFlexibleSpacer(editButtonsVB);
            }
        }

        // Add a spacer at the bottom
        addFixedSpacer(editButtonsVB, 10);
    }

    private FontIcon createFontIcon(String iconName, int iconSize) {
        FontIcon icon = new FontIcon(iconName);
        icon.setIconSize(iconSize);
        if (iconName.equals("fth-trash-2")) {
            icon.getStyleClass().add("delete-icon");
        } else {
            icon.getStyleClass().add("add-icon");
        }
        return icon;
    }

    private void addFixedSpacer(VBox vbox, double height) {
        Region spacer = new Region();
        spacer.setMinHeight(height);
        spacer.setMaxHeight(height);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        vbox.getChildren().add(spacer);
    }

    private void addFlexibleSpacer(VBox vbox) {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        vbox.getChildren().add(spacer);
    }

    private void eliminateProductHandler(int height) {
        // Eliminate the product
        domainController.removeProductFromShelvingUnit(height, supermarketPosition);

        // Update the view
        this.updateView();
    }

    private void addProductHandler(int height) {
        // Create the search text field
        TextField searchField = new TextField();
        searchField.setPromptText("Search product by name");

        // Create the ListView for products
        ListView<String> productListView = new ListView<>();
        productListView.setPrefHeight(150);  // Fixed height, adjust as needed

        // Create a ScrollPane to allow scrolling if there are many options
        ScrollPane productScrollPane = new ScrollPane(productListView);
        productScrollPane.setFitToWidth(true);  // Ensures the ListView takes up the full width available

        // Create the error message label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");  // Red text for error messages

        // Create the VBox to contain the search field and ScrollPane
        VBox vbox = new VBox(10);  // 10 is the space between elements
        vbox.getChildren().addAll(searchField, productScrollPane, errorLabel);

        // Create and configure the scene in a new stage
        Scene scene = new Scene(vbox, 450, 250);
        Stage stage = createProductStage(scene);

        // Populate the ListView with all products names
        List<String> productNames = domainController.getProducts().stream()
                .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                .map(ProductDto::getName).toList();

        productListView.getItems().setAll(productNames);

        // Filter products based on search text
        searchField.textProperty().addListener((_, _, newValue) -> {
            List<String> filteredProductNames = domainController.searchProduct(newValue).stream()
                    .map(ProductDto::getName)
                    .collect(Collectors.toList());

            productListView.getItems().setAll(filteredProductNames);
        });

        // Action when the user selects a product from the ListView
        productListView.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                try {
                    // Attempt to add the product to the shelving unit
                    domainController.addProductToShelvingUnit(newValue, height, supermarketPosition);
                    this.updateView();
                    stage.close();  // Close the popup or modal window
                } catch (Exception e) {
                    // Show error message if exception occurs
                    errorLabel.setText("Error: " + e.getMessage());
                }
            }
        });
    }


    private Stage createProductStage(Scene scene) {
        Stage stage = new Stage();
        stage.setTitle("Add product to shelving unit");
        stage.setScene(scene);
        stage.show();
        return stage;
    }

}
