package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;

/**
 * Interface for managing the persistence of supermarket data.
 * Provides methods for importing and exporting the supermarket's state to and from files.
 */
public interface IPersistenceController {

    /**
     * Imports the data of a supermarket from a specified file.
     *
     * @param filePath the path to the file containing the supermarket data.
     * @return a {@link SupermarketData} object containing the imported data.
     */
    SupermarketData importSupermarket(String filePath);

    /**
     * Exports the current data of the supermarket to a specified file.
     *
     * @param supermarketData the {@link SupermarketData} object containing the data to be exported.
     * @param filePath the path to the file where the data will be saved.
     */
    void exportSupermarket(SupermarketData supermarketData, String filePath);
}

