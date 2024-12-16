package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.models.*;
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
    private Product product1, product2, bread, product3, cocacola, ice;
    private Catalog catalog;
    private String pathPersistenceTestsCorrect;
    private String pathPersistenceTestsExport;
    private String pathPersistenceTestsDifferentHeights;
    private String pathPersistenceTestsDuplicatedUIDs;

    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String EMPLOYEE_NAME = "employee";
    private static final String EMPLOYEE_PASSWORD = "employee";

    @Before
    /* Sets a supermarket with the administrator logged in some products, distributions and the expected shelving units from it.
    *
    */
    public void setUp() {
        supermarket = Supermarket.getInstance();
        try {
            supermarket.logOut();
        }
        catch(Exception _) {
            //This is intentional
        }

        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.eraseDistribution();

        distribution = new ArrayList<>();
        distribution.add(new Pair<>(ProductTemperature.FROZEN, 2));
        distribution.add(new Pair<>(ProductTemperature.REFRIGERATED, 2));
        distribution.add(new Pair<>(ProductTemperature.AMBIENT, 2));

        expectedShelvingUnits = new ArrayList<>();
        expectedShelvingUnits.add(new ShelvingUnit(0, 2, ProductTemperature.FROZEN));
        expectedShelvingUnits.add(new ShelvingUnit(1, 2, ProductTemperature.FROZEN));
        expectedShelvingUnits.add(new ShelvingUnit(2, 2, ProductTemperature.REFRIGERATED));
        expectedShelvingUnits.add(new ShelvingUnit(3, 2, ProductTemperature.REFRIGERATED));
        expectedShelvingUnits.add(new ShelvingUnit(4, 2, ProductTemperature.AMBIENT));
        expectedShelvingUnits.add(new ShelvingUnit(5, 2, ProductTemperature.AMBIENT));

        expectedProducts = new ArrayList<>();

        product1 = new Product("bread", 10.0f, ProductTemperature.AMBIENT, "path");
        product2 = new Product("water", 10.0f, ProductTemperature.REFRIGERATED, "path");
        product3 = new Product("ice cream", 10.0f, ProductTemperature.FROZEN, "path");
        bread = new Product("bread", 0.4f, ProductTemperature.AMBIENT, "path/to/img");
        cocacola = new Product("cocacola",2.5f, ProductTemperature.REFRIGERATED, "path/to/img");
        ice = new Product("ice", 0.5f, ProductTemperature.FROZEN, "path/to/img");

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            pathPersistenceTestsCorrect = "./src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExamples/testPersistenceCorrect.json";
            pathPersistenceTestsExport = "./src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExamples/testPersistenceExport.json";
            pathPersistenceTestsDifferentHeights = "./src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExamples/testPersistenceDifferentHeights.json";
            pathPersistenceTestsDuplicatedUIDs = "./src/main/resources/edu/upc/subgrupprop113/supermarketmanager/dataExamples/testPersistenceDuplicatedUIDs.json";

        }
        else {
            pathPersistenceTestsCorrect = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExamples\\testPersistenceCorrect.json";
            pathPersistenceTestsExport = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExamples\\testPersistenceExport.json";
            pathPersistenceTestsDifferentHeights = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExamples\\testPersistenceDifferentHeights.json";
            pathPersistenceTestsDuplicatedUIDs = ".\\src\\main\\resources\\edu\\upc\\subgrupprop113\\supermarketmanager\\dataExamples\\testPersistenceDuplicatedUIDs.json";

        }
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
        assertEquals("The logged in user should be the admin.", admin, supermarket.getLoggedUser());

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

        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.logOut();
        assertNull("No user should be logged in.", supermarket.getLoggedUser());
    }

    @Test
    public void testCreateDistribution() {
        supermarket.createDistribution(2, distribution);
        assertEquals("The shelving unit height should be 2.", 2, supermarket.getShelvingUnitHeight());
        List<ShelvingUnit> supermarketShelvingUnits = supermarket.getShelvingUnits();
        for (int i = 0; i < supermarketShelvingUnits.size(); i++) {
            ShelvingUnit expectedUnit = expectedShelvingUnits.get(i);
            ShelvingUnit actualUnit = supermarketShelvingUnits.get(i);
            assertEquals("The shelving unit should have the same uid.", expectedUnit.getUid(), actualUnit.getUid());
            assertEquals("The shelving unit should have the same height.", expectedUnit.getHeight(), actualUnit.getHeight());
            assertEquals("The shelving unit should have the same temperature.", expectedUnit.getTemperature(), actualUnit.getTemperature());
        }

        try {
            supermarket.createDistribution(2, distribution);
            fail("Expected IllegalStateException when distribution is not empty.");
        } catch (IllegalStateException e) {
            assertEquals("The supermarket distribution must be empty.", e.getMessage());
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

        supermarket.addProductToShelvingUnit(0, 0, product3);
        supermarket.addProductToShelvingUnit(1, 0, ice);
        supermarket.addProductToShelvingUnit(1, 1, ice);

        expectedProducts.add(product3);
        expectedProducts.add(ice);
        expectedProducts.add(ice);
        assertEquals("The given products should be in the shelving units", expectedProducts, supermarket.getAllProductsShelvingUnits());
    }

    @Test
    public void testExportSupermarket() {
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.exportSupermarket(pathPersistenceTestsExport);
            fail("Expected IllegalStateException, there current user is not an administrator.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.exportSupermarket(pathPersistenceTestsExport);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.exportSupermarket(pathPersistenceTestsExport);
    }

    @Test
    public void testImportSupermarket() {
        supermarket.createDistribution(2, distribution);
        try {
            supermarket.importSupermarket(pathPersistenceTestsCorrect);
            fail("Expected IllegalStateException, there should be a non empty distribution.");
        } catch (IllegalStateException e) {
            assertEquals("The supermarket distribution must be empty.", e.getMessage());
        }
        supermarket.eraseDistribution();
        try {
            supermarket.importSupermarket(pathPersistenceTestsDifferentHeights);
            fail("Expected IllegalArgumentException, shelving units with different heights.");
        } catch (IllegalArgumentException e) {
            assertEquals("More than one height is provided.", e.getMessage());
        }
        try {
            supermarket.importSupermarket(pathPersistenceTestsDuplicatedUIDs);
            fail("Expected IllegalArgumentException, dupplicated uids.");
        } catch (IllegalArgumentException e) {
            assertEquals("There is at least one duplicated uid.", e.getMessage());
        }

        //Check the supermarket is the expected one
        catalog = Catalog.getInstance();
        supermarket.importSupermarket(pathPersistenceTestsCorrect);
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

    @Test
    public void testAddShelvingUnit() {
        supermarket.createDistribution(2, distribution); // Create initial distribution for the test
        int initialSize = supermarket.getShelvingUnits().size();
        try {
            supermarket.addShelvingUnit(- 1, ProductTemperature.AMBIENT);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("The position is not correct", e.getMessage());
        }
        supermarket.addShelvingUnit(initialSize - 1, ProductTemperature.AMBIENT);
        List<ShelvingUnit> shelvingUnits = supermarket.getShelvingUnits();
        assertEquals("The number of shelving units should increase by 1", initialSize + 1, shelvingUnits.size());
        ShelvingUnit addedUnit = shelvingUnits.getLast();
        assertEquals("The shelving unit height should be 2", 2, addedUnit.getHeight());
        assertEquals("The shelving unit temperature should be AMBIENT", ProductTemperature.AMBIENT, addedUnit.getTemperature());
        for (int i = 0; i < shelvingUnits.size() - 1; i++) {
            assertNotEquals("The new shelving unit's uid should be unique", shelvingUnits.get(i).getUid(), addedUnit.getUid());
        }
    }

    @Test
    public void testRemoveShelvingUnit() {
        supermarket.createDistribution(2, distribution);
        int initialSize = supermarket.getShelvingUnits().size();
        try {
            supermarket.removeShelvingUnit(-1);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("The position is not correct", e.getMessage());
        }
        supermarket.addShelvingUnit(initialSize, ProductTemperature.AMBIENT);
        supermarket.removeShelvingUnit(initialSize);
        assertEquals("The Shelving unit did not delete correctly", initialSize, supermarket.getShelvingUnits().size());
        supermarket.removeShelvingUnit(1);
        assertEquals("The Shelving unit did not delete correctly", initialSize - 1, supermarket.getShelvingUnits().size());
    }

    @Test
    public void testAddProductToShelvingUnit() {
        supermarket.createDistribution(2, distribution);
        int totProd = supermarket.getAllProductsShelvingUnits().size();
        try {
            supermarket.addProductToShelvingUnit(-1,0, product1);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("The position is not correct", e.getMessage());
        }
        try {
            supermarket.addProductToShelvingUnit(1,10, product1);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IndexOutOfBoundsException e) {
            assertEquals(("Invalid height: 10"), e.getMessage());
        }
        try {
            supermarket.addProductToShelvingUnit(1,0, null);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (NullPointerException e) {
            assertEquals("Product cannot be null.", e.getMessage());
        }
        try {
            supermarket.addProductToShelvingUnit(1,0, product1);
            fail("Expected IllegalArgumentException, the temperature is not valid.");
        } catch (IllegalStateException e) {
            assertEquals("The temperature of the product is not compatible with the shelving unit.", e.getMessage());
        }
        supermarket.addProductToShelvingUnit(4,0, product1);
        assertEquals("The product was not added to the shelving unit", totProd + 1, supermarket.getAllProductsShelvingUnits().size());
        assertEquals("The product was not added to the shelving unit", product1, supermarket.getShelvingUnits().get(4).getProduct(0));
        supermarket.addProductToShelvingUnit(2,0, product2);
        assertEquals("The product was not added to the shelving unit", product2, supermarket.getShelvingUnits().get(2).getProduct(0));
    }

    @Test
    public void testDeleteProductFromShelvingUnit() {
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product3);
        List<Product> x = supermarket.getShelvingUnits().get(1).getProducts();
        supermarket.addProductToShelvingUnit(1,1, ice);
        try {
            supermarket.removeProductFromShelvingUnit(10, 1);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("The position is not correct", e.getMessage());
        }
        try {
            supermarket.removeProductFromShelvingUnit(1, 10);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IndexOutOfBoundsException e) {
            assertEquals(("Invalid height: 10"), e.getMessage());
        }
        supermarket.removeProductFromShelvingUnit(1, 1);
        assertEquals("The product was not deleted", x, supermarket.getShelvingUnits().get(1).getProducts());
        assertNull("The product was not deleted", supermarket.getShelvingUnits().get(1).getProducts().get(1));
    }

    @Test
    public void testHasProductByObject() {
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product3);
        supermarket.addProductToShelvingUnit(2,1, product2);
        assertTrue("The product should be in the supermarket.", supermarket.hasProduct(product3));
        supermarket.removeProductFromShelvingUnit(1, 0);
        supermarket.addProductToShelvingUnit(1,0, ice);
        assertFalse("The product is not suposed to be in the supermarket.", supermarket.hasProduct(product1));
        assertTrue("The product should be in the supermarket.", supermarket.hasProduct(ice));
    }

    @Test
    public void testHasProductByProductName() {
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product3);
        supermarket.addProductToShelvingUnit(2,1, product2);
        assertTrue("The product should be in the supermarket", supermarket.hasProduct(product3.getName()));
        supermarket.removeProductFromShelvingUnit(1, 0);
        supermarket.addProductToShelvingUnit(1,0, ice);
        assertFalse("The product is not suposed to be in the supermarket.", supermarket.hasProduct(product1.getName()));
        assertTrue("The product should be in the supermarket.", supermarket.hasProduct(ice.getName()));
    }

    @Test
    public void testSwapProducts() {
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product3);
        supermarket.addProductToShelvingUnit(0,0, ice);
        supermarket.addProductToShelvingUnit(2,1, product2);
        try {
            supermarket.swapProducts(1,0, -2, 1);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("The position is not correct", e.getMessage());
        }
        try {
            supermarket.swapProducts(1,0, 0, 10);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IndexOutOfBoundsException e) {
            assertEquals(("Invalid height: 10"), e.getMessage());
        }
        try {
            supermarket.swapProducts(1,0, 2, 1);
            fail("Expected IllegalArgumentException, the temperature is not valid.");
        } catch (IllegalStateException e) {
            assertEquals("The temperature of the product is not compatible with the shelving unit.", e.getMessage());
        }

        supermarket.swapProducts(1,0, 0, 0);
        assertEquals("The products were not swapped 1", ice, supermarket.getShelvingUnits().get(1).getProduct(0));
        assertEquals("The products were not swapped 2", product3, supermarket.getShelvingUnits().get(0).getProduct(0));
    }

    @Test
    public void testSwapShelvingUnits() {
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product3);
        supermarket.addProductToShelvingUnit(0,1, ice);
        ShelvingUnit u1 = supermarket.getShelvingUnits().get(1);
        ShelvingUnit u2 = supermarket.getShelvingUnits().get(0);
        try {
            supermarket.swapShelvingUnits(1, -2);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("The position is not correct", e.getMessage());
        }
        try {
            supermarket.swapShelvingUnits(-1, 2);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("The position is not correct", e.getMessage());
        }
        supermarket.swapShelvingUnits(0, 1);
        assertEquals("The shelving units were not swapped", u2.getProducts(), supermarket.getShelvingUnits().get(1).getProducts());
        assertEquals("The shelving units were not swapped", u1.getProducts(), supermarket.getShelvingUnits().get(0).getProducts());
    }

    @Test
    public void testEmptyShelvingUnit(){
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product3);
        supermarket.addProductToShelvingUnit(2,1, product2);
        supermarket.addProductToShelvingUnit(2,0, cocacola);
        supermarket.addProductToShelvingUnit(0,0, ice);
        supermarket.addProductToShelvingUnit(0,1, product3);
        try {
            supermarket.emptyShelvingUnit(-1);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("The position is not correct", e.getMessage());
        }
        supermarket.emptyShelvingUnit(1);
        for(Product product : supermarket.getShelvingUnits().get(1).getProducts()) {
            assertNull("The shelving unit is not empty", product);
        }
        supermarket.emptyShelvingUnit(2);
        for(Product product : supermarket.getShelvingUnits().get(2).getProducts()) {
            assertNull("The shelving unit is not empty", product);
        }
        supermarket.emptyShelvingUnit(0);
        for(Product product : supermarket.getShelvingUnits().get(0).getProducts()) {
            assertNull("The shelving unit is not empty", product);
        }
    }

    @Test
    public void testRemoveAllInstancesOfProduct() {
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product3);
        supermarket.addProductToShelvingUnit(2,1, product2);
        supermarket.addProductToShelvingUnit(2,0, cocacola);
        supermarket.addProductToShelvingUnit(0,0, ice);
        supermarket.addProductToShelvingUnit(0,1, product3);
        try {
            supermarket.removeAllInstancesOfProduct(null);
            fail("Expected IllegalArgumentException, the product is not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("The product cannot be null", e.getMessage());
        }
        supermarket.removeAllInstancesOfProduct(product3);
        assertNull(supermarket.getShelvingUnits().get(1).getProduct(0));
        assertNull(supermarket.getShelvingUnits().get(0).getProduct(1));
        supermarket.removeAllInstancesOfProduct(product2);
        assertNull(supermarket.getShelvingUnits().get(2).getProduct(1));
        supermarket.removeAllInstancesOfProduct(ice);
        assertNull(supermarket.getShelvingUnits().get(0).getProduct(0));
        supermarket.removeAllInstancesOfProduct(cocacola);
        assertNull(supermarket.getShelvingUnits().get(2).getProduct(0));
    }

    @Test
    public void testModifyShelvingUnitTemperature() {
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(3,0, cocacola);
        try {
            supermarket.modifyShelvingUnitTemperature(3, ProductTemperature.REFRIGERATED);
            fail("Expected IllegalStateException, there should a product in the shelving unit with AMBIENT temperature.");
        }
        catch (IllegalStateException e) {
            assertEquals("The temperature of the product is not compatible with the shelving unit.", e.getMessage());
        }
        supermarket.removeProductFromShelvingUnit(3, 0);
        supermarket.addProductToShelvingUnit(2,1, product2);
        try {
            supermarket.modifyShelvingUnitTemperature(2, ProductTemperature.AMBIENT);
            fail("Expected IllegalStateException, there should a product in the shelving unit with AMBIENT temperature.");
        }
        catch (IllegalStateException e) {
            assertEquals("The temperature of the product is not compatible with the shelving unit.", e.getMessage());
        }
        supermarket.modifyShelvingUnitTemperature(0, ProductTemperature.AMBIENT);
        assertEquals(ProductTemperature.AMBIENT, supermarket.getShelvingUnits().getFirst().getTemperature());
    }

    @Test
    public void testCheckLoggedUserIsAdmin() {
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.checkLoggedUserIsAdmin();
            fail("Expected IllegalStateException, the current user is not an administrator.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.checkLoggedUserIsAdmin();
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.checkLoggedUserIsAdmin();
    }

    @Test
    public void testGetShelvingUnit() {
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(3,0, product2);
        try {
            supermarket.getShelvingUnit(100);
            fail("Expected IllegalArgumentException, the shelving unit position is not valid.");
        }
        catch (IllegalArgumentException e) {
            assertEquals("The position is not correct", e.getMessage());
        }

        ShelvingUnit unit = supermarket.getShelvingUnit(3);
        assertEquals("UID", 3, unit.getUid());
        assertEquals("Prdouct name", product2.getName(), unit.getProduct(0).getName());
        assertNull(unit.getProduct(1));
    }
}
