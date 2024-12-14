package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;

import java.util.List;

public class EditKeywordsController {

    @FXML
    private ListView<String> keywordsListView;

    private boolean isSaved = false;

    public void setKeywords(List<String> keywords) {
        // Set the items and make the list editable
        keywordsListView.setItems(FXCollections.observableArrayList(keywords));
        keywordsListView.setEditable(true);

        // Use TextFieldListCell for inline editing
        keywordsListView.setCellFactory(TextFieldListCell.forListView());
    }

    public List<String> getKeywords() {
        return keywordsListView.getItems();
    }

    public boolean isSaved() {
        return isSaved;
    }

    @FXML
    private void handleAddKeyword() {
        keywordsListView.getItems().add("New Keyword");
        keywordsListView.edit(keywordsListView.getItems().size() - 1);
    }

    @FXML
    private void handleRemoveKeyword() {
        String selected = keywordsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            keywordsListView.getItems().remove(selected);
        }
    }

    @FXML
    private void handleSave() {
        isSaved = true;
        Stage stage = (Stage) keywordsListView.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        isSaved = false;
        Stage stage = (Stage) keywordsListView.getScene().getWindow();
        stage.close();
    }
}
