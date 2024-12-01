package edu.upc.subgrupprop113.supermarketmanager.factories;


import edu.upc.subgrupprop113.supermarketmanager.controllers.PersistenceController;

public class PersistenceControllerFactory {
    private static PersistenceControllerFactory instance;
    private PersistenceController persistenceController;

    private PersistenceControllerFactory() {}

    public static PersistenceControllerFactory getInstance() {
        if(instance == null) {
            instance = new PersistenceControllerFactory();
        }
        return instance;
    }

    public PersistenceController getPersistenceController() {
        if (persistenceController == null) {
            persistenceController = new PersistenceController();
        }
        return persistenceController;
    }
}
