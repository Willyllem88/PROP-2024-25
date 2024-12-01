package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;
import edu.upc.subgrupprop113.supermarketmanager.services.ExportFileJSON;
import edu.upc.subgrupprop113.supermarketmanager.services.ExportFileStrategy;
import edu.upc.subgrupprop113.supermarketmanager.services.ImportFileJSON;
import edu.upc.subgrupprop113.supermarketmanager.services.ImportFileStrategy;

public class PersistenceController implements IPersitenceController {
    private ImportFileStrategy importFileStrategy;
    private ExportFileStrategy exportFileStrategy;

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

    @Override
    public SupermarketData importSupermarket(String filePath) {
        return importFileStrategy.importSupermarket(filePath);
    }

    @Override
    public void exportSupermarket(SupermarketData supermarketData, String filePath) {
        exportFileStrategy.exportSupermarket(supermarketData, filePath);
    }
}
