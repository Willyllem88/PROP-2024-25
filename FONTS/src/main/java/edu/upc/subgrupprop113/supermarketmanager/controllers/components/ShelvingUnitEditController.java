package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ProductDto;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.InputStream;
import java.util.Objects;


public class ShelvingUnitEditController extends ShelvingUnitController {

    @FXML
    VBox editButtonsVB;

    public ShelvingUnitEditController(PresentationController presentationController, int supermarketPosition) {
        super(presentationController, supermarketPosition);
    }

    @Override
    protected void initView() {
        super.initView();

        //Clear
        editButtonsVB.getChildren().clear();
        editButtonsVB.setVisible(true);
        editButtonsVB.setManaged(true);

        editButtonsVB.setSpacing(20);

        for (int i = 0; i < super.shelvingUnitInfo.getProducts().size(); i++) {
            if (super.shelvingUnitInfo.getProducts().get(i) != null) {
                // Crear icono de "menos"
                FontIcon minusIcon = new FontIcon("fth-minus");  // Icono de FontAwesome "menos"
                minusIcon.setIconSize(20);  // Tamaño del icono, ajusta si es necesario
                editButtonsVB.getChildren().add(minusIcon);
            } else {
                // Crear icono de "más"
                FontIcon plusIcon = new FontIcon("fth-plus");  // Icono de FontAwesome "más"
                plusIcon.setIconSize(20);  // Tamaño del icono, ajusta si es necesario
                editButtonsVB.getChildren().add(plusIcon);
            }
        }
    }
}
