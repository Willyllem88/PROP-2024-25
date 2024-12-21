package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller for the EditKeywords component
 * <p>
 * This controller is used to handle the user interaction with the EditKeywords component.
 * The component allows the user to edit a list of keywords.
 * <p>
 */
public class EditKeywordsController {

    /**
     * List view to display the keywords
     */
    @FXML
    private ListView<String> keywordsListView;

    /**
     * Flag to indicate if the keywords have been saved
     */
    private boolean isSaved = false;

    /**
     * Set the keywords to be displayed in the list view
     *
     * @param keywords List of keywords
     */
    public void setKeywords(List<String> keywords) {
        // Set the items and make the list editable
        keywordsListView.setItems(FXCollections.observableArrayList(keywords));
        keywordsListView.setEditable(true);

        // Use TextFieldListCell for inline editing
        keywordsListView.setCellFactory(TextFieldListCell.forListView());
    }

    /**
     * Get the keywords from the list view
     *
     * @return List of keywords
     */
    public List<String> getKeywords() {
        return keywordsListView.getItems();
    }

    /**
     * Check if the keywords have been saved
     *
     * @return True if the keywords have been saved, false otherwise
     */
    public boolean isSaved() {
        return isSaved;
    }

    /**
     * Handle the add keyword button click
     */
    @FXML
    private void handleAddKeyword() {
        keywordsListView.getItems().add("New Keyword");
        keywordsListView.edit(keywordsListView.getItems().size() - 1);
    }

    /**
     * Handle the remove keyword button click
     */
    @FXML
    private void handleRemoveKeyword() {
        String selected = keywordsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            keywordsListView.getItems().remove(selected);
        }
    }

    /**
     * Handle the save button click
     */
    @FXML
    private void handleSave() {
        isSaved = true;
        Stage stage = (Stage) keywordsListView.getScene().getWindow();
        stage.close();
    }

    /**
     * Handle the cancel button click
     */
    @FXML
    private void handleCancel() {
        isSaved = false;
        Stage stage = (Stage) keywordsListView.getScene().getWindow();
        stage.close();
    }
}
