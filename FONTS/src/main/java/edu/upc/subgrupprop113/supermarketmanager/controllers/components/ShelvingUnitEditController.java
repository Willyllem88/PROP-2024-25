package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.util.Objects;


public class ShelvingUnitEditController extends ShelvingUnitController {

    public ShelvingUnitEditController(PresentationController presentationController, int supermarketPosition) {
        super(presentationController, supermarketPosition);
    }

    @Override
    protected void initView() {
        //Call father's function initView
        super.initView();

        /*int nbElements = shelvingUnitInfo.getProducts().size();

        root.

        for (int i = 0; i < n; ++i) {
            root.
        }

        //afegir botons*/
    }
}
