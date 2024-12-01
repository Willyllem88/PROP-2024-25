package edu.upc.subgrupprop113.supermarketmanager.factories;


import edu.upc.subgrupprop113.supermarketmanager.controllers.IPersitenceController;
import edu.upc.subgrupprop113.supermarketmanager.controllers.PersistenceController;

public class DomainControllerFactory {
    private static DomainControllerFactory instance;
    private IPersitenceController persitenceController;

    private DomainControllerFactory() {}

    public static DomainControllerFactory getInstance() {
        if(instance == null) {
            instance = new DomainControllerFactory();
        }
        return instance;
    }

    public IPersitenceController getPersitenceController() {
        if (persitenceController == null) {
            persitenceController = new PersistenceController();
        }
        return persitenceController;
    }
}
