package edu.upc.subgrupprop113.supermarketmanager.factories;


public class PersistenceControllerFactory {
    private static PersistenceControllerFactory instance;

    private PersistenceControllerFactory() {}

    public static PersistenceControllerFactory getInstance() {
        if(instance == null) {
            instance = new PersistenceControllerFactory();
        }
        return instance;
    }
}
