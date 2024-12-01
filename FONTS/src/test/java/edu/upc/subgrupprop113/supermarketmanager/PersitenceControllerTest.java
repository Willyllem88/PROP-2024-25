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
    public void testImportSupermarket() {
        persistenceController.setImportFileStrategy(new ImportFileStub());
        SupermarketData data = persistenceController.importSupermarket("path/to/file");
        List<ShelvingUnit> units = data.getDistribution();
        //Unit0
        assertEquals("The first unit should have uid 0", 0, units.getFirst().getUid());
        assertEquals("The first unit should have height 2", 2, units.getFirst().getHeight());
        assertEquals("The first unit should be of ambient temperature", ProductTemperature.AMBIENT, units.getFirst().getTemperature());
        assertEquals("The first unit should have bread in the height 0", "bread", units.getFirst().getProduct(0).getName());
        assertNull("No product should be in the height 1 of the first unit", units.getFirst().getProduct(1));
        //Unit1
        assertEquals("The second unit should have uid 1", 1, units.getLast().getUid());
        assertEquals("The second unit should have height 2", 2, units.getLast().getHeight());
        assertEquals("The second unit should have be of frozen temperature", ProductTemperature.FROZEN, units.getLast().getTemperature());
        assertNull("No product should be in the height 0 of the second unit", units.getLast().getProduct(0));
        assertNull("No product should be in the height 1 of the second unit", units.getLast().getProduct(1));
    }
}
