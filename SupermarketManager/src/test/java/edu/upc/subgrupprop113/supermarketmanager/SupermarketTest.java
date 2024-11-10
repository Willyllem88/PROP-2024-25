package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SupermarketTest {
    private Supermarket supermarket;
    private ArrayList<Pair<ProductTemperature, Integer>> distribution;
    private ArrayList<ShelvingUnit> expectedShelvingUnits;
    private ArrayList<Product> expectedProducts;
    private Product product1, product2, bread;
    private Catalog catalog;

    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String EMPLOYEE_NAME = "employee";
    private static final String EMPLOYEE_PASSWORD = "employee";

    @Before
    /* Sets a supermaket with the administrator logged in some products, distributions and the expected shelving units from it.
    *
    */
    public void setUp() {
        supermarket = Supermarket.getInstance();
        supermarket.eraseDistribution();
        try {
            supermarket.logOut();
        } catch (Exception _) {}

        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);

        distribution = new ArrayList<>();
        distribution.add(new Pair<>(ProductTemperature.FROZEN, 1));
        distribution.add(new Pair<>(ProductTemperature.REFRIGERATED, 2));
        distribution.add(new Pair<>(ProductTemperature.AMBIENT, 1));

        expectedShelvingUnits = new ArrayList<>();
        expectedShelvingUnits.add(new ShelvingUnit(0, 2, ProductTemperature.FROZEN));
        expectedShelvingUnits.add(new ShelvingUnit(1, 2, ProductTemperature.REFRIGERATED));
        expectedShelvingUnits.add(new ShelvingUnit(2, 2, ProductTemperature.REFRIGERATED));
        expectedShelvingUnits.add(new ShelvingUnit(3, 2, ProductTemperature.AMBIENT));

        expectedProducts = new ArrayList<>();

        product1 = new Product("bread", 10.0f, ProductTemperature.AMBIENT, "path");
        product2 = new Product("water", 10.0f, ProductTemperature.REFRIGERATED, "path");
        bread = new Product("bread", 0.4f, ProductTemperature.AMBIENT, "path/to/img");
    }

    @Test
    public void testOneInstance() {
        assertEquals("The two instances of supermarket should be the same.", supermarket, Supermarket.getInstance());
    }

    @Test
    public void testFindUser() {
        assertEquals("The found user should have the username given.", "employee", supermarket.findUser("employee").getUsername());
        assertNull("The given user should not exist.", supermarket.findUser("Employee"));
    }

    @Test
    public void testLogIn() {
        supermarket.logOut();

        try {
            supermarket.logIn("marc", "marc");
            fail("Expected IllegalArgumentException for non-existent user.");
        } catch (IllegalArgumentException e) {
            assertEquals("No such user found.", e.getMessage());
        }

        try {
            supermarket.logIn("admin", "marc");
            fail("Expected IllegalArgumentException for wrong password.");
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong password.", e.getMessage());
        }

        supermarket.logIn("admin", "admin");
        User admin = supermarket.findUser("admin");
        assertEquals("The logged in user should be the admin.", admin, supermarket.getLogedUser());

        try {
            supermarket.logIn("admin", "marc");
            fail("Expected IllegalStateException for already logged-in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is already a logged in user.", e.getMessage());
        }
    }

    @Test
    public void testLogOut() {
        supermarket.logOut();
        try {
            supermarket.logOut();
            fail("Expected IllegalStateException for no user logged in.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged user.", e.getMessage());
        }

        supermarket.logIn("admin", "admin");
        supermarket.logOut();
        assertNull("No user should be logged in.", supermarket.getLogedUser());
    }

    @Test
    public void testCreateDistribution() {
        supermarket.createDistribution(2, distribution);
        try {
            supermarket.createDistribution(2, distribution);
            fail("Expected IllegalStateException when distribution is not empty.");
        } catch (IllegalStateException e) {
            assertEquals("The supermarket distribution must be empty.", e.getMessage());
        }

        assertEquals("The shelving unit height should be 2.", 2, supermarket.getShelvingUnitHeight());
        List<ShelvingUnit> supermarketShelvingUnits = supermarket.getShelvingUnits();
        for (int i = 0; i < supermarketShelvingUnits.size(); i++) {
            ShelvingUnit expectedUnit = expectedShelvingUnits.get(i);
            ShelvingUnit actualUnit = supermarketShelvingUnits.get(i);
            assertEquals("The shelving unit should have the same uid.", expectedUnit.getUid(), actualUnit.getUid());
            assertEquals("The shelving unit should have the same height.", expectedUnit.getHeight(), actualUnit.getHeight());
            assertEquals("The shelving unit should have the same temperature.", expectedUnit.getTemperature(), actualUnit.getTemperature());
        }
    }

    @Test
    public void testEraseDistribution() {
        supermarket.createDistribution(2, distribution);
        supermarket.eraseDistribution();
        assertEquals("The shelving unit height should be 0.", 0, supermarket.getShelvingUnitHeight());
        assertTrue("The shelving unit should be empty.", supermarket.getShelvingUnits().isEmpty());
    }

    @Test
    public void testSortSupermarket() {
        supermarket.createDistribution(2, distribution);
        supermarket.setOrderingStrategy(new OrderingStrategyStub());
        supermarket.sortSupermarketCatalog();

        List<ShelvingUnit> supermarketShelvingUnits = supermarket.getShelvingUnits();
        assertNull(supermarketShelvingUnits.get(0).getProduct(0));
        assertNull(supermarketShelvingUnits.get(0).getProduct(1));
        assertEquals("water", supermarketShelvingUnits.get(1).getProduct(0).getName());
        assertNull(supermarketShelvingUnits.get(1).getProduct(1));
        assertNull(supermarketShelvingUnits.get(2).getProduct(0));
        assertNull(supermarketShelvingUnits.get(2).getProduct(1));
        assertEquals("bread", supermarketShelvingUnits.get(3).getProduct(0).getName());
        assertNull(supermarketShelvingUnits.get(3).getProduct(1));
    }

    @Test
    public void testSortProducts() {
        supermarket.createDistribution(2, distribution);
        supermarket.setOrderingStrategy(new OrderingStrategyStub());
        supermarket.sortSupermarketProducts();

        List<ShelvingUnit> supermarketShelvingUnits = supermarket.getShelvingUnits();
        assertNull(supermarketShelvingUnits.get(0).getProduct(0));
        assertNull(supermarketShelvingUnits.get(0).getProduct(1));
        assertEquals("water", supermarketShelvingUnits.get(1).getProduct(0).getName());
        assertNull(supermarketShelvingUnits.get(1).getProduct(1));
        assertNull(supermarketShelvingUnits.get(2).getProduct(0));
        assertNull(supermarketShelvingUnits.get(2).getProduct(1));
        assertEquals("bread", supermarketShelvingUnits.get(3).getProduct(0).getName());
        assertNull(supermarketShelvingUnits.get(3).getProduct(1));
    }

    @Test
    public void testGetAllProductsShelvingUnits() {
        supermarket.createDistribution(2, distribution);
        assertEquals("No products should be in the shelving units", expectedProducts, supermarket.getAllProductsShelvingUnits());

        supermarket.addProductToShelvingUnit(0, 0, product1);
        supermarket.addProductToShelvingUnit(1, 0, product1);
        supermarket.addProductToShelvingUnit(1, 1, product2);

        expectedProducts.add(product1);
        expectedProducts.add(product1);
        expectedProducts.add(product2);
        assertEquals("The given products should be in the shelving units", expectedProducts, supermarket.getAllProductsShelvingUnits());
    }

    @Test
    public void testExportSupermaket() {
        supermarket.setExportFileStrategy(new ExportFileStub());
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.exportSupermarket("path/to/file");
            fail("Expected IllegalStateException, there current user is not an administrator.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.exportSupermarket("path/to/file");
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.exportSupermarket("path/to/file");
    }

    @Test
    public void testImportSupermaket() {
        catalog = Catalog.getInstance();
        supermarket.setImportFileStrategy(new ImportFileStub());
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.exportSupermarket("path/to/file");
            fail("Expected IllegalStateException, the current user is not an administrator.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.exportSupermarket("path/to/file");
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution);
        try {
            supermarket.importSupermarket("path/to/file");
            fail("Expected IllegalStateException, there should be a non empty distribution.");
        } catch (IllegalStateException e) {
            assertEquals("The supermarket distribution must be empty.", e.getMessage());
        }
        supermarket.eraseDistribution();

        try {
            supermarket.importSupermarket("different/temps");
            fail("Expected IllegalArgumentException, there are products in temperatures incorrect.");
        } catch (IllegalArgumentException e) {
            assertEquals("There is at least one product in a shelving unit with different temperatures.", e.getMessage());
        }
        try {
            supermarket.importSupermarket("product/not/contained");
            fail("Expected IllegalArgumentException, there is a product not contained in the catalog.");
        } catch (IllegalArgumentException e) {
            assertEquals("There is at least one product not contained in the catalog.", e.getMessage());
        }
        try {
            supermarket.importSupermarket("different/heights");
            fail("Expected IllegalArgumentException, shelving units with different heights.");
        } catch (IllegalArgumentException e) {
            assertEquals("More than one height is provided.", e.getMessage());
        }
        try {
            supermarket.importSupermarket("dupplicated/uids");
            fail("Expected IllegalArgumentException, dupplicated uids.");
        } catch (IllegalArgumentException e) {
            assertEquals("There is at least one duplicated uid.", e.getMessage());
        }

        //Check the supermarket is the expected one
        supermarket.importSupermarket("path/to/file");
        List<ShelvingUnit> units = supermarket.getShelvingUnits();
        //Unit0
        assertEquals("The first unit should have uid 0", 0, units.getFirst().getUid());
        assertEquals("The first unit should have height 2", 2, units.getFirst().getHeight());
        assertEquals("The first unit should be of ambient temperature", ProductTemperature.AMBIENT, units.getFirst().getTemperature());
        assertEquals("The first unit should have bread in the height 0", bread.getName(), units.getFirst().getProduct(0).getName());
        assertNull("No product should be in the height 1 of the first unit", units.getFirst().getProduct(1));
        //Unit1
        assertEquals("The second unit should have uid 1", 1, units.getLast().getUid());
        assertEquals("The second unit should have height 2", 2, units.getLast().getHeight());
        assertEquals("The second unit should have be of frozen temperature", ProductTemperature.FROZEN, units.getLast().getTemperature());
        assertNull("No product should be in the height 0 of the second unit", units.getLast().getProduct(0));
        assertNull("No product should be in the height 1 of the second unit", units.getLast().getProduct(1));
        assertTrue("The catalog should contain bread", catalog.contains("bread"));
    }

}
