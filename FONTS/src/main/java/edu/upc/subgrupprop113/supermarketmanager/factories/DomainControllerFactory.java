package edu.upc.subgrupprop113.supermarketmanager.factories;


import edu.upc.subgrupprop113.supermarketmanager.controllers.DomainController;

/**
 * Factory class for managing the creation and retrieval of the {@link DomainController} instance.
 * <p>
 * Implements the Singleton design pattern to ensure that only one instance of the
 * {@code DomainControllerFactory} and its associated {@code DomainController} exist during runtime.
 * </p>
 */
public class DomainControllerFactory {

    /**
     * The single instance of the {@code DomainControllerFactory}.
     */
    private static DomainControllerFactory instance;

    /**
     * The single instance of the {@link DomainController} managed by this factory.
     */
    private DomainController domainController;

    /**
     * Private constructor to prevent direct instantiation and enforce the Singleton pattern.
     */
    private DomainControllerFactory() {}

    /**
     * Retrieves the single instance of the {@code DomainControllerFactory}.
     * If no instance exists, a new one is created.
     *
     * @return the {@code DomainControllerFactory} instance.
     */
    public static DomainControllerFactory getInstance() {
        if(instance == null) {
            instance = new DomainControllerFactory();
        }
        return instance;
    }

    /**
     * Retrieves the single instance of the {@link DomainController}.
     * If no instance exists, a new one is created.
     *
     * @return the {@link DomainController} instance.
     */
    public DomainController getDomainController() {
        if (domainController == null) {
            domainController = new DomainController();
        }
        return domainController;
    }
}

