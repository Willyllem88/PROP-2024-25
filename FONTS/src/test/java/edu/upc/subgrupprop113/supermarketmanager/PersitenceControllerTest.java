package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.controllers.PersistenceController;
import edu.upc.subgrupprop113.supermarketmanager.models.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PersitenceControllerTest {
    private PersistenceController persistenceController;

    @Before
    public void setUp() {
        persistenceController = new PersistenceController();
    }

    @Test
    public void testExportSupermarket() {
        persistenceController.setExportFileStrategy(new ExportFileStub());
        persistenceController.exportSupermarket(new SupermarketData(), "path/to/file");
    }

    @Test
    public void testImportSupermarket() {
        persistenceController.setImportFileStrategy(new ImportFileStub());
        //todo
    }
}
