package edu.upc.subgrupprop113.supermarketmanager.factories;


import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.IDomainController;

public class PresentationControllerFactory {
    private static PresentationControllerFactory instance;
    private IDomainController domainController;

    private PresentationControllerFactory() {}

    public static PresentationControllerFactory getInstance() {
        if(instance == null) {
            instance = new PresentationControllerFactory();
        }
        return instance;
    }

    public IDomainController getDomainController() {
        if(domainController == null) {
            //domainController = new DomainController();
        }
        return domainController;
    }
}
