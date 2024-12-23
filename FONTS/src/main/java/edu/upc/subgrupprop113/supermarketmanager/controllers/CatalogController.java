package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import edu.upc.subgrupprop113.supermarketmanager.controllers.components.*;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.dtos.RelatedProductDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing the Catalog view in the supermarket application.
 * <p>
 * This class handles the user interactions and logic for displaying and managing
 * products, relations, and search functionalities in the Catalog view.
 * </p>
 */
public class CatalogController {

    @FXML
    private HBox mainContent;

    @FXML
    private HBox topBar;

    @FXML
    private VBox leftSide;

    @FXML
    private Label title;

    @FXML
    private Label placeholderMessage;

    @FXML
    private ScrollPane productDetailsScrollPane;

    @FXML
    private VBox productDetailsContainer;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productName;

    @FXML
    private TextField productNameTextField;

    @FXML
    private FontIcon confirmNameIcon;

    @FXML
    private FontIcon cancelNameIcon;

    @FXML
    private Label productPriceLabel;

    @FXML
    private Label productPrice;

    @FXML
    private TextField productPriceTextField;

    @FXML
    private HBox editPriceIconsBox;

    @FXML
    private FontIcon confirmPriceIcon;

    @FXML
    private FontIcon cancelPriceIcon;

    @FXML
    private Label productTemperature;

    @FXML
    private StackPane setTemperatureWrapper;

    @FXML
    private HBox setTemperatureComponent;

    @FXML
    private SetTemperatureController setTemperatureComponentController;

    @FXML
    private HBox editTemperatureIconsBox;

    @FXML
    private FontIcon confirmTemperatureIcon;

    @FXML
    private FontIcon cancelTemperatureIcon;

    @FXML
    private VBox uploadImageContainer;

    @FXML
    private FlowPane productKeywords;

    @FXML
    private FontIcon editPriceIcon;

    @FXML
    private FontIcon editTemperatureIcon;

    @FXML
    private FontIcon editKeywordsIcon;

    @FXML
    private Button editRelationsButton;

    @FXML
    private HBox modifyBottomButtons;

    @FXML
    private Button deleteProductButton;

    @FXML
    private Button findAtSupermarketButton;

    @FXML
    private HBox addBottomButtons;

    @FXML
    private VBox confirmNewProductButton;

    @FXML
    private VBox cancelNewProductButton;

    @FXML
    private VBox rightSide;

    @FXML
    private TextField searchBar;

    @FXML
    private Label searchBarPlaceholder;

    @FXML
    private ScrollPane searchResultsPane;

    @FXML
    private VBox searchResults;

    private HBox selectedItem;
    private Label selectedLabel;

    @FXML
    private TableView<Pair<String, String>> relationsTable;

    @FXML
    private TableColumn<Pair<String, String>, String> relatedProductColumn;

    @FXML
    private TableColumn<Pair<String, String>, String> relationValueColumn;


    @FXML
    private Button addButton;

    private final PresentationController presentationController;

    public CatalogController(PresentationController presentationController) {
        this.presentationController = presentationController;
    }

    private final DomainController domainController = DomainControllerFactory.getInstance().getDomainController();

    private TopBarController topBarController;

    private List<ProductDto> searchResultProducts;

    private String newProductImagePath;

    /**
     * Initializes the controller.
     */
    @FXML
    private void initialize() {
        topBarController = (TopBarController) topBar.getProperties().get("controller");
        PrimaryButtonController confirmButtonController = (PrimaryButtonController) confirmNewProductButton.getProperties().get("controller");
        SecondaryButtonController cancelButtonController = (SecondaryButtonController) cancelNewProductButton.getProperties().get("controller");

        topBarController.showNewDistributionButton(false);
        topBarController.showCatalogButton(false);
        topBarController.showDistributionSettings(false);
        topBarController.showImportButton(false);
        if (!domainController.loggedAdmin()) {
            topBarController.showSaveAsButton(false);
            topBarController.showSaveButton(false);
            editPriceIcon.setVisible(false);
            editTemperatureIcon.setVisible(false);
            editKeywordsIcon.setVisible(false);
            editRelationsButton.setVisible(false);
            deleteProductButton.setVisible(false);
            addButton.setVisible(false);
        }
        topBarController.setOnGoBackHandler(_ -> presentationController.logInSuccessful());
        placeholderMessage.setVisible(true);
        productDetailsScrollPane.setVisible(false);

        if (confirmButtonController != null) {
            confirmButtonController.setLabelText("Confirm");
            confirmButtonController.setOnClickHandler(_ -> handleConfirmAddProduct());
        }
        if (cancelButtonController != null) {
            cancelButtonController.setLabelText("Cancel");
            cancelButtonController.setOnClickHandler(_ -> handleCancelEdit());
        }

        relatedProductColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        relationValueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue()));

        setRelationsTableEditable();

        // Restrict the product names to alphanumeric characters
        restrictTextField(searchBar, "[a-zA-Z0-9\\s]*");
        restrictTextField(productNameTextField, "[a-zA-Z0-9\\s]*");

        // Restrict the price text field to numbers
        restrictTextField(productPriceTextField, "\\d*\\.?\\d{0,2}");

        // Trim leading spaces in text fields
        trimLeadingSpaces(productNameTextField);
        trimLeadingSpaces(searchBar);

        sortCatalogProducts();

        // Automatically focus on the search bar
        searchBar.requestFocus();
        // Add listener to search bar
        searchBar.textProperty().addListener((_, _, newValue) -> handleSearch(newValue));
    }

    /**
     * Populates the search results with the products in the catalog
     *
     * @param products the list of products to display in the search results.
     *
     * @throws IllegalArgumentException if the image path is invalid.
     */
    private void populateSearchResults(List<ProductDto> products) {
        searchResults.getChildren().clear();
        for (ProductDto product : products) {
            HBox resultItem = new HBox();
            resultItem.getStyleClass().add("search-result-item");

            resultItem.setSpacing(10);
            
            ImageView productImage = new ImageView();
            productImage.setFitWidth(30); // Set the preferred width
            productImage.setFitHeight(30); // Set the preferred height
            productImage.setPreserveRatio(true); // Preserve aspect ratio
            String img_path = product.getImgPath();
            try {
                productImage.setImage(new Image(img_path));
            }
            catch (Exception e) {
                if (! (e instanceof IllegalArgumentException)) {
                    productImage.setImage(new Image(domainController.getErrorImage()));
                }
            }

            Label label = new Label(product.getName());
            label.getStyleClass().add("result-label");

            resultItem.getChildren().addAll(productImage, label);
            resultItem.setOnMouseClicked(this::handleResultClick);

            searchResults.getChildren().add(resultItem);
        }
    }

    /**
     * Sorts the catalog products by name and populates the search results.
     */
    private void sortCatalogProducts() {
        this.searchResultProducts = domainController.getProducts().stream()
                .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                .toList();
        populateSearchResults(searchResultProducts);
    }

    /**
     * Restricts the text field to the specified regular expression.
     *
     * @param textField the text field to restrict.
     * @param regex     the regular expression to use for validation.
     */
    private void restrictTextField(TextField textField, String regex) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            // Allow only numbers and a single decimal point
            if (newText.matches(regex)) {
                return change;
            }
            return null; // Reject the change
        });
        textField.setTextFormatter(formatter);
    }

    /**
     * Trims leading spaces in the text field.
     *
     * @param textField the text field to trim.
     */
    private void trimLeadingSpaces(TextField textField) {
        textField.textProperty().addListener((_, _, newValue) -> {
            if (newValue != null && newValue.startsWith(" ")) {
                textField.setText(newValue.stripLeading());
            }
        });
    }

    /**
     * Searches for products based on the specified query and updates the search results accordingly.
     *
     * @param query the search query.
     */
    @FXML
    private void handleSearch(String query) {
        String trimmedQuery = query.trim();
        List<ProductDto> filteredProducts = domainController.searchProduct(trimmedQuery);

        if (relationsTable.isVisible()) {
            ProductDto selectedProduct = domainController.getProduct(productName.getText());
            if (selectedProduct != null) {
                relationsTable.getItems().clear();

                // Populate relationsTable based on filtered products
                selectedProduct.getRelatedProducts().stream()
                        .filter(relatedProduct -> {
                            String relatedProductName = relatedProduct.getProduct1().equals(selectedProduct.getName())
                                    ? relatedProduct.getProduct2()
                                    : relatedProduct.getProduct1();
                            // Check if the related product is in the filtered list
                            return filteredProducts.stream()
                                    .anyMatch(product -> product.getName().equalsIgnoreCase(relatedProductName));
                        })
                        .forEach(relatedProduct -> {
                            if (relatedProduct.getProduct1().equals(selectedProduct.getName())) {
                                relationsTable.getItems().add(new Pair<>(relatedProduct.getProduct2(), String.valueOf(relatedProduct.getValue())));
                            } else {
                                relationsTable.getItems().add(new Pair<>(relatedProduct.getProduct1(), String.valueOf(relatedProduct.getValue())));
                            }
                        });
            }
        } else {
            if (!trimmedQuery.isEmpty()) {
                populateSearchResults(filteredProducts);
            }
        }
    }

    /**
     * Handles the addition of a new product.
     * <p> This method is called whenever the user clicks the "Add Product" button. When this happens,
     * the left side of the Catalog view is updated to allow the user to add a new product. </p>
     */
    @FXML
    private void handleAddProduct() {
        uploadImageContainer.setVisible(true);

        // Reset the image path
        newProductImagePath = domainController.getErrorImage();

        searchBar.clear();
        if (selectedItem != null) {
            selectedItem.getStyleClass().remove("selected");
            selectedItem = null;
        }

        placeholderMessage.setVisible(false);

        // Clear existing details
        productImage.setImage(new Image(newProductImagePath));
        productName.setText("");
        productPrice.setText("");
        productTemperature.setText("Temperature: ");
        productKeywords.getChildren().clear();

        // Switch to add product view
        productDetailsScrollPane.setVisible(true);

        productName.setVisible(false);
        productNameTextField.setVisible(true);

        productPrice.setVisible(false);
        editPriceIcon.setVisible(false);
        productPriceTextField.setVisible(true);

        productTemperature.setVisible(false);
        editTemperatureIcon.setVisible(false);
        setTemperatureWrapper.setVisible(true);

        editRelationsButton.setVisible(false);
        modifyBottomButtons.setVisible(false);
        addBottomButtons.setVisible(true);

        // Darken and disable the right side
        rightSide.getStyleClass().add("darkened");
        rightSide.setDisable(true);
    }

    /**
     * Handles the confirmation of a new product image. Updates the new product image path.
     *
     * @param actionEvent the mouse event that triggered the method.
     */
    @FXML
    private void handleUploadImage(MouseEvent actionEvent) {
        newProductImagePath = presentationController.showSelectDialog("Select an image", "~/", "PNG files (*.png)", "*.png");
        if (newProductImagePath != null) {
            try {
                String imageUri = new File(newProductImagePath).toURI().toString();
                productImage.setImage(new Image(imageUri));
                uploadImageContainer.setVisible(false);
            }
            catch (Exception e) {
               topBarController.toastError("Error uploading the image", 3000);
            }
        }
        else {
            newProductImagePath = domainController.getErrorImage();
        }
    }

    /**
     * Handles the confirmation of a new product.
     * <p> This method is called whenever the user clicks the "Confirm" button after adding a new product.
     * When this happens, the new product is created and added to the catalog. </p>
     *
     * @throws IllegalArgumentException if the product already exists.
     */
    @FXML
    private void handleConfirmAddProduct() {
        String name = productNameTextField.getText().trim();
        String priceText = productPriceTextField.getText().trim();
        String temperature = setTemperatureComponentController.getTemperature();
        List<String> keywords = new ArrayList<>();
        productKeywords.getChildren().forEach(node -> keywords.add(((Label) node).getText()));

        if (name.isEmpty() || priceText.isEmpty() || temperature.isEmpty()) {
            topBarController.toastError("Please fill in all fields", 3000);
            return;
        }

        float price = Float.parseFloat(priceText);
        ProductDto newProduct = new ProductDto(name, price, temperature, newProductImagePath, keywords, new ArrayList<>());
        try {
            domainController.createProduct(newProduct);

            searchBar.clear();
            searchResults.getChildren().clear();
            sortCatalogProducts();

            topBarController.toastSuccess("Product added successfully", 3000);
            switchToViewMode();
        }
        catch (Exception e) {
            topBarController.toastError(e.getMessage(), 3000);
        }
    }

    /**
     * Handles the click event on a search result item.
     * <p> This method is called whenever the user clicks on a search result item. When this happens,
     * the details of the selected product are displayed on the right side of the Catalog view. </p>
     *
     * @param mouseEvent the mouse event that triggered the method.
     */
    @FXML
    private void handleResultClick(MouseEvent mouseEvent) {
        uploadImageContainer.setVisible(false);

        HBox clickedItem = (HBox) mouseEvent.getSource();

        // Remove the selected class from the previously selected item
        if (selectedItem != null) {
            selectedItem.getStyleClass().remove("selected");
        }
        // Add the selected class to the newly clicked item
        clickedItem.getStyleClass().add("selected");
        selectedItem = clickedItem;

        Label label = (Label) clickedItem.getChildren().get(1); // Assuming the label is the second child
        String product = label.getText();

        // Find the product DTO by name (assuming names are unique)
        ProductDto selectedProduct = domainController.getProduct(product);

        if (selectedProduct != null) {
            // Update left panel
            placeholderMessage.setVisible(false);
            productDetailsScrollPane.setVisible(true);

            productName.setText(selectedProduct.getName());
            productPrice.setText(String.valueOf(selectedProduct.getPrice()));
            String temperature = selectedProduct.getTemperature();
            temperature = switch (temperature) {
                case "REFRIGERATED" -> "FRIDGE";
                case "FROZEN" -> "FREEZER";
                default -> "AMBIENT";
            };
            productTemperature.setText("Temperature: " + temperature);


            String img_path = selectedProduct.getImgPath();
            try {
                productImage.setImage(new Image(img_path));
            }
            catch (Exception e) {
                if (! (e instanceof IllegalArgumentException)) {
                    productImage.setImage(new Image(domainController.getErrorImage()));
                }
            }

            // Update keywords
            updateProductKeywords(selectedProduct.getKeywords());
        }
    }

    /**
     * Handles the click event on the "Edit" button for the product price.
     * <p> This method is called whenever the user clicks on the "Edit" button for the product price.
     * When this happens, the product price is switched to editing mode. </p>
     */
    @FXML
    private void handleEditPrice() {
        // Switch to editing mode for price
        switchToViewMode();
        productPrice.setVisible(false);

        productPriceTextField.setText(productPrice.getText());
        productPriceTextField.setVisible(true);
        productPriceTextField.requestFocus();

        editPriceIconsBox.setVisible(true);

        editPriceIcon.setVisible(false);
    }

    /**
     * Handles the confirmation of the edited product price.
     * <p> This method is called whenever the user clicks the "Confirm" button after editing the product price.
     * When this happens, the product price is updated and the view is switched back to view mode. </p>
     */
    @FXML
    private void handleConfirmPrice() {
        // Confirm the edit for price
        String newPriceText = productPriceTextField.getText().trim();
        if (!newPriceText.isEmpty()) {
            float newPrice = Float.parseFloat(newPriceText);
            ProductDto selectedProduct = domainController.getProduct(productName.getText());
            if (selectedProduct != null) {
                selectedProduct.setPrice(newPrice);
                domainController.modifyProduct(selectedProduct);
                productPrice.setText(String.valueOf(newPrice));
            }
        }
        // Switch back to view mode
        switchToViewMode();
    }

    /**
     * Handles the click event on the "Edit" button for the product temperature.
     * <p> This method is called whenever the user clicks on the "Edit" button for the product temperature.
     * When this happens, the product temperature is switched to editing mode. </p>
     */
    @FXML
    private void handleEditTemperature() {
        // Switch to editing mode for temperature
        switchToViewMode();
        productTemperature.setVisible(false);
        setTemperatureWrapper.setVisible(true);

        // Set the choice box to the current temperature
        String currentTemperature = productTemperature.getText().replace("Temperature: ", "");
        currentTemperature = switch (currentTemperature) {
            case "FRIDGE" -> "REFRIGERATED";
            case "FREEZER" -> "FROZEN";
            default -> "AMBIENT";
        };
        setTemperatureComponentController.setTemperature(currentTemperature);

        editTemperatureIconsBox.setVisible(true);
        editTemperatureIcon.setVisible(false);
    }

    /**
     * Handles the confirmation of the edited product temperature.
     * <p> This method is called whenever the user clicks the "Confirm" button after editing the product temperature.
     * When this happens, the product temperature is updated and the view is switched back to view mode. </p>
     */
    @FXML
    private void handleConfirmTemperature() {
        try {
            // Confirm the temperature change
            String newTemperature = setTemperatureComponentController.getTemperature();
            ProductDto selectedProduct = domainController.getProduct(productName.getText());
            if (selectedProduct != null) {
                selectedProduct.setTemperature(newTemperature);
                domainController.modifyProduct(selectedProduct);
                switch (newTemperature) {
                    case "REFRIGERATED" -> newTemperature = "FRIDGE";
                    case "FROZEN" -> newTemperature = "FREEZER";
                    default -> newTemperature = "AMBIENT";
                }
                productTemperature.setText("Temperature: " + newTemperature);
            }

            // Switch back to view mode
            switchToViewMode();
        } catch (Exception e) {
            if (e.getMessage().equals("The product is in a shelving unit, the temperature can not be modified.")) {
                topBarController.toastError("Cannot modify product, it is placed in the shelves", 3000);
            } else {
                throw e;
            }
        }
    }

    /**
     * Handles the click event on the "Edit" button for the product keywords.
     * <p> This method is called whenever the user clicks on the "Edit" button for the product keywords.
     * When this happens, a dialog to edit the keywords is displayed. When the accept button is clicked, the new keywords are updated. </p>
     */
    @FXML
    private void handleEditKeywords() {
        try {
            // Load the FXML for the dialog
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/components/editKeywords.fxml"));
            Parent root = loader.load();
            EditKeywordsController controller = loader.getController();

            if (selectedItem == null) {
                controller.setKeywords(productKeywords.getChildren().stream()
                        .map(node -> ((Label) node).getText())
                        .toList());

                // Create the dialog
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Add Keywords");
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setScene(new Scene(root));
                dialogStage.showAndWait();

                if (controller.isSaved()) {
                    List<String> updatedKeywords = controller.getKeywords();
                    updateProductKeywords(updatedKeywords);
                }
            } else {
                switchToViewMode();

                List<String> currentKeywords = domainController.getProduct(productName.getText()).getKeywords();
                controller.setKeywords(currentKeywords);

                // Create the dialog
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Keywords");
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setScene(new Scene(root));
                dialogStage.showAndWait();

                // Check if the user saved the changes
                if (controller.isSaved()) {
                    List<String> updatedKeywords = controller.getKeywords();

                    // Update the product and the UI
                    ProductDto selectedProduct = domainController.getProduct(productName.getText());
                    if (selectedProduct != null) {
                        selectedProduct.setKeywords(updatedKeywords);
                        domainController.modifyProduct(selectedProduct);
                        updateProductKeywords(updatedKeywords);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the click event on the "Edit Relations" button.
     * <p> This method is called whenever the user clicks on the "Edit Relations" button.
     * When this happens, the user can view and edit the relations of the selected product. </p>
     */
    @FXML
    private void handleEditRelations() {
        searchBar.clear();
        handleSearch("");
        searchBar.requestFocus();

        if (relationsTable.isVisible()) {
            relationsTable.setVisible(false);
            searchResultsPane.setVisible(true);
            editRelationsButton.setText("Edit Relations");
            editRelationsButton.setStyle("");
            sortCatalogProducts();
        } else {
            relationsTable.setVisible(true);
            searchResultsPane.setVisible(false);
            editRelationsButton.setText("View Catalog");
            editRelationsButton.setStyle("-fx-background-color: #4A92FF; -fx-text-fill: white;");

            // Clear the table
            relationsTable.getItems().clear();

            ProductDto selectedProduct = domainController.getProduct(productName.getText());
            if (selectedProduct == null) {
                topBarController.toastError("No product selected", 3000);
                return;
            }

            // Populate the table with the related products
            selectedProduct.getRelatedProducts().forEach(relatedProduct -> {
                String relatedProductName = relatedProduct.getProduct1().equals(selectedProduct.getName())
                        ? relatedProduct.getProduct2()
                        : relatedProduct.getProduct1();
                relationsTable.getItems().add(new Pair<>(relatedProductName, String.valueOf(relatedProduct.getValue())));
            });
        }
    }

    /**
     * Shows a confirmation alert before deleting a product.
     * <p> This method is called whenever the user clicks on the "Delete" button.
     * When this happens, a confirmation alert is displayed to ensure the user wants to delete the product. </p>
     *
     * @return the button type of the alert.
     */
    @FXML
    private ButtonType showDeleteAlert() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete confirmation");
        confirmationAlert.setHeaderText("¿Are you sure you want to remove this product?");
        confirmationAlert.setContentText("This action cannot be undone.");
        return confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);
    }

    /**
     * Handles the click event on the "Delete" button.
     */
    @FXML
    private void handleDeleteProduct() {
        try {
            ButtonType result = showDeleteAlert();
            if (result == ButtonType.OK) {
                domainController.removeProduct(productName.getText());

                placeholderMessage.setVisible(true);
                productDetailsScrollPane.setVisible(false);

                searchBar.clear();
                searchResults.getChildren().clear();

                sortCatalogProducts();
                switchToViewMode();
                topBarController.toastSuccess("Product deleted successfully", 3000);
            }
        } catch (Exception e) {
            if(e.getMessage().equals("The product is in a shelving unit, it can not be removed.")) {
                topBarController.toastError("Cannot delete product, it is placed in the shelves", 4500);
            }
        }
    }

    /**
     * Handles the click event on the "Find at Supermarket" button.
     * <p> This method is called whenever the user clicks on the "Find at Supermarket" button.
     * When this happens, if the product is placed in any shelf of the supermarket, it navigates to the corresponding shelf
     * in the Main screen view. </p>
     */
    @FXML
    private void handleFindAtSupermarket() {
        ProductDto product = domainController.getProduct(productName.getText());
       if (domainController.supermarketHasProduct(product)) {
              presentationController.showProductInShelvingUnits(product);
         } else {
              topBarController.toastError("The product is not available in the supermarket", 4500);
       }
    }

    /**
     * Handles the click event on any of the "Cancel" icons or buttons.
     * <p> This method is called whenever the user clicks on any of the "Cancel" icons or buttons. It switches the view back to view mode. </p>
     */
    @FXML
    private void handleCancelEdit() {
        switchToViewMode();
    }

    /**
     * Switches the view back to view mode.
     */
    @FXML
    private void switchToViewMode() {
        // Restore the view mode
        productName.setVisible(true);
        productNameTextField.setVisible(false);

        productPrice.setVisible(true);
        productPriceTextField.setVisible(false);
        editPriceIconsBox.setVisible(false);
        editPriceIcon.setVisible(true);

        productTemperature.setVisible(true);
        setTemperatureWrapper.setVisible(false);
        editTemperatureIconsBox.setVisible(false);
        editTemperatureIcon.setVisible(true);

        addBottomButtons.setVisible(false);
        editRelationsButton.setVisible(true);
        modifyBottomButtons.setVisible(true);

        if (selectedItem == null) {
            placeholderMessage.setVisible(true);
            productDetailsScrollPane.setVisible(false);
        }

        relationsTable.setVisible(false);
        searchResultsPane.setVisible(true);
        editRelationsButton.setText("Edit Relations");
        editRelationsButton.setStyle("");

        // Restore the right side
        rightSide.getStyleClass().remove("darkened");
        rightSide.setDisable(false);
    }

    /**
     * Updates the product keywords in the UI.
     *
     * @param updatedKeywords the updated list of keywords.
     */
    @FXML
    private void updateProductKeywords(List<String> updatedKeywords) {
        productKeywords.getChildren().clear();
        for (String keyword : updatedKeywords) {
            Label keywordLabel = new Label(keyword);
            keywordLabel.getStyleClass().add("keyword-label");
            productKeywords.getChildren().add(keywordLabel);
        }
    }

    /**
     * Sets up the relations table to be editable.
     * <p> This method makes the relation value column editable and defines the behavior whenever the user edits a cell. </p>
     */
    @FXML
    private void setRelationsTableEditable() {
        // Make the relationValueColumn editable
        relationValueColumn.setCellFactory(_ -> {
            TextFieldTableCell<Pair<String, String>, String> cell = new TextFieldTableCell<>(new StringConverter<>() {
                @Override
                public String toString(String value) {
                    return value;
                }

                @Override
                public String fromString(String value) {
                    return value;
                }
            });

            // Ensure the cell is editable and restrict input for decimals
            cell.setEditable(true);
            cell.itemProperty().addListener((_, _, _) -> {
                if (cell.isEditing() && cell.getGraphic() instanceof TextField textField) {
                    restrictTextField(textField, "\\d*\\.?\\d*"); // Allow only decimals
                }
            });

            return cell;
        });

        relationValueColumn.setOnEditCommit(event -> {

            Pair<String, String> oldRowData = event.getRowValue();
            String oldValue = oldRowData.getValue();
            String newValue = event.getNewValue();

            try {
                // Update the data in the row
                Pair<String, String> newRowData = new Pair<>(oldRowData.getKey(), newValue);

                // Replace the old Pair in the ObservableList
                int rowIndex = relationsTable.getItems().indexOf(oldRowData);
                relationsTable.getItems().set(rowIndex, newRowData);

                // Persist the change to the backend or domain model
                ProductDto selectedProduct = domainController.getProduct(productName.getText());
                if (selectedProduct != null) {
                    for (RelatedProductDto relatedProduct : selectedProduct.getRelatedProducts()) {
                        String product1 = relatedProduct.getProduct1();
                        String product2 = relatedProduct.getProduct2();
                        String relatedProductName = product1.equals(selectedProduct.getName()) ? product2 : product1;
                        if (relatedProductName.equals(newRowData.getKey())) {
                            float value = Float.parseFloat(newValue);
                            relatedProduct.setValue(value);
                            domainController.modifyProductRelation(relatedProduct); // Persist change
                            break;
                        }
                    }

                    relationsTable.refresh();
                }
            } catch (Exception e) {
                // Revert to the old value in case of an exception
                event.getTableView().getItems().set(event.getTablePosition().getRow(),
                        new Pair<>(oldRowData.getKey(), oldValue)); // Restore the old value
                relationsTable.refresh();
                if (e.getMessage().equals("Value must be a float between 0 and 1.0, both included")) {
                    topBarController.toastError("Relation value must be a number between 0 and 1", 3000);
                } else {
                    topBarController.toastError("An error occurred while updating the relation value", 3000);
                }

            }
        });

        relationsTable.setEditable(true);
    }
}
