package edu.upc.subgrupprop113.supermarketmanager.controllers;

import edu.upc.subgrupprop113.supermarketmanager.models.SupermarketData;
import edu.upc.subgrupprop113.supermarketmanager.services.ExportFileJSON;
import edu.upc.subgrupprop113.supermarketmanager.services.ExportFileStrategy;
import edu.upc.subgrupprop113.supermarketmanager.services.ImportFileJSON;
import edu.upc.subgrupprop113.supermarketmanager.services.ImportFileStrategy;

public class PersistenceController implements IPersitenceController {
    private final ImportFileStrategy importFileStrategy;
    private final ExportFileStrategy exportFileStrategy;

    public PersistenceController() {
        importFileStrategy = new ImportFileJSON();
        exportFileStrategy = new ExportFileJSON();
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
