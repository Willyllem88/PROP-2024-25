package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.Main;
import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;
import edu.upc.subgrupprop113.supermarketmanager.services.ExportFileJSON;
import edu.upc.subgrupprop113.supermarketmanager.services.ExportFileStrategy;
import edu.upc.subgrupprop113.supermarketmanager.services.ImportFileJSON;
import edu.upc.subgrupprop113.supermarketmanager.services.ImportFileStrategy;

import java.net.URL;
import java.nio.file.Paths;

/**
 * Implementation of the {@link IPersistenceController} interface for managing supermarket data persistence.
 * <p>
 * This controller handles the import and export of supermarket data using configurable strategies.
 * By default, it utilizes JSON-based strategies for both import and export.
 * </p>
 */
public class PersistenceController implements IPersistenceController {
    /**
     * The strategy used for importing supermarket data from files.
     */
    private ImportFileStrategy importFileStrategy;

    /**
     * The strategy used for exporting supermarket data to files.
     */
    private ExportFileStrategy exportFileStrategy;

    /**
     * Constructs a new {@code PersistenceController} with default strategies for
     * importing and exporting supermarket data in JSON format.
     */
    public PersistenceController() {
        importFileStrategy = new ImportFileJSON();
        exportFileStrategy = new ExportFileJSON();
    }

    /**
     * Sets the strategy for importing files into the supermarket system.
     * <p>This method allows the selection of a specific import strategy to manage
     * the way data is read and incorporated from files.</p>
     *
     * @param importFileStrategy the {@link ImportFileStrategy} to be used for importing files.
     */
    public void setImportFileStrategy(ImportFileStrategy importFileStrategy) {
        this.importFileStrategy = importFileStrategy;
    }

    /**
     * Sets the strategy for exporting files from the supermarket system.
     * <p>This method allows the selection of a specific export strategy to manage
     * the way data is written and saved to files.</p>
     *
     * @param exportFileStrategy the {@link ExportFileStrategy} to be used for exporting files.
     */
    public void setExportFileStrategy(ExportFileStrategy exportFileStrategy) {
        this.exportFileStrategy = exportFileStrategy;
    }

    /**
     * Imports supermarket data from a file using the configured import strategy.
     *
     * @param filePath the path to the file containing the supermarket data. If the path is null uses the default file path.
     * @return a {@link SupermarketData} object containing the imported data.
     */
    @Override
    public SupermarketData importSupermarket(String filePath) {
        return importFileStrategy.importSupermarket((filePath == null || filePath.isEmpty()) ? getDefaultFile() : filePath);
    }

    /**
     * Exports supermarket data to a file using the configured export strategy.
     *
     * @param supermarketData the {@link SupermarketData} object containing the data to be exported.
     * @param filePath the path to the file where the data will be saved. If the path is null uses the default file path.
     */
    @Override
    public void exportSupermarket(SupermarketData supermarketData, String filePath) {
        exportFileStrategy.exportSupermarket(supermarketData, (filePath == null || filePath.isEmpty()) ? getDefaultFile() : filePath);
    }

    /**
     * Retrieves the path to the default JSON file.
     *
     * <p>Attempts to locate the default resource file named <code>default.json</code> within the application's resources.
     * If the file is not found or an error occurs while resolving its path, an {@link IllegalStateException} is thrown.</p>
     *
     * @return the absolute path to the default JSON file
     * @throws IllegalStateException if the default file is not found or its path cannot be resolved
     */
    private String getDefaultFile() {
        URL defaultResource = Main.class.getResource("default.json");
        if (defaultResource == null) throw new IllegalStateException("Default file not found");
        try {
            return Paths.get(defaultResource.toURI()).toString();
        } catch (Exception e) {
            throw new IllegalStateException("Default file not found");
        }
    }
}

