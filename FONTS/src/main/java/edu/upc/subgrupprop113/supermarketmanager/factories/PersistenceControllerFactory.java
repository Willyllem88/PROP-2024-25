package edu.upc.subgrupprop113.supermarketmanager.factories;


import edu.upc.subgrupprop113.supermarketmanager.controllers.PersistenceController;

/**
 * Factory class for managing the creation and retrieval of the {@link PersistenceController} instance.
 * <p>
 * Implements the Singleton design pattern to ensure that only one instance of the
 * {@code PersistenceControllerFactory} and its associated {@code PersistenceController} exist during runtime.
 * </p>
 */
public class PersistenceControllerFactory {

    /**
     * The single instance of the {@code PersistenceControllerFactory}.
     */
    private static PersistenceControllerFactory instance;

    /**
     * The single instance of the {@link PersistenceController} managed by this factory.
     */
    private PersistenceController persistenceController;

    /**
     * Private constructor to prevent direct instantiation and enforce the Singleton pattern.
     */
    private PersistenceControllerFactory() {}

    /**
     * Retrieves the single instance of the {@code PersistenceControllerFactory}.
     * If no instance exists, a new one is created.
     *
     * @return the {@code PersistenceControllerFactory} instance.
     */
    public static PersistenceControllerFactory getInstance() {
        if(instance == null) {
            instance = new PersistenceControllerFactory();
        }
        return instance;
    }

    /**
     * Retrieves the single instance of the {@link PersistenceController}.
     * If no instance exists, a new one is created.
     *
     * @return the {@link PersistenceController} instance.
     */
    public PersistenceController getPersistenceController() {
        if (persistenceController == null) {
            persistenceController = new PersistenceController();
        }
        return persistenceController;
    }
}

