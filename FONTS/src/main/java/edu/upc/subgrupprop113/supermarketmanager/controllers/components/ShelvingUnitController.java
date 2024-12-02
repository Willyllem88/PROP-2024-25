package edu.upc.subgrupprop113.supermarketmanager.controllers.components;

import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PresentationController;
import edu.upc.subgrupprop113.supermarketmanager.dtos.ShelvingUnitDto;
import edu.upc.subgrupprop113.supermarketmanager.factories.DomainControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ShelvingUnitController {
    @FXML
    private VBox root;


    private PresentationController presentationController;
    private final DomainController domainController;

    private int supermarketPosition;
    private ShelvingUnitDto shelvingUnitInfo;

    public ShelvingUnitController(PresentationController presentationController, int supermarketPosition) {
        this.presentationController = presentationController;
        this.domainController = DomainControllerFactory.getInstance().getDomainController();
        this.setSupermarketPosition(supermarketPosition);
    }

    @FXML
    public void initialize() {
        // Store a reference to this controller in the root node's properties
        if (root != null) {
            root.getProperties().put("controller", this);
        }
    }

    public int getSupermarketPosition() {
        return supermarketPosition;
    }

    public void setSupermarketPosition(int supermarketPosition) throws IllegalArgumentException{
        this.supermarketPosition = supermarketPosition;
        this.shelvingUnitInfo = domainController.getShelvingUnitDto(supermarketPosition);
        initView();
    }

    private void initView() {
        //TODO: Posar al fmxl la temperatura les imatges i els noms...
    }


}
