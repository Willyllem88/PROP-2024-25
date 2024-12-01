package edu.upc.subgrupprop113.supermarketmanager.factories;


import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;

public class DomainControllerFactory {
    private static DomainControllerFactory instance;
    private DomainController domainController;

    private DomainControllerFactory() {}

    public static DomainControllerFactory getInstance() {
        if(instance == null) {
            instance = new DomainControllerFactory();
        }
        return instance;
    }

    public DomainController getDomainController() {
        if (domainController == null) {
            domainController = new DomainController();
        }
        return domainController;
    }
}
