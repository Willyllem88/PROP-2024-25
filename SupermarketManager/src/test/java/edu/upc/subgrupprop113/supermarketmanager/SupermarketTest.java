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
    private Product product1, product2, bread, product3;
    private Catalog catalog;

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
        } catch (Exception _) {}

        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.eraseDistribution();
        supermarket.logOut();

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
        product3 = new Product("ice cream", 10.0f, ProductTemperature.FROZEN, "path");
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
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.createDistribution(2, distribution);
            fail("Expected IllegalStateException, the current user is not an administrator.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.createDistribution(2, distribution);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }

        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
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
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.eraseDistribution();
            fail("Expected IllegalStateException, the current user is not an administrator.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.eraseDistribution();
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }

        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution);
        supermarket.eraseDistribution();
        assertEquals("The shelving unit height should be 0.", 0, supermarket.getShelvingUnitHeight());
        assertTrue("The shelving unit should be empty.", supermarket.getShelvingUnits().isEmpty());
    }

    @Test
    public void testSortSupermarket() {
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.sortSupermarketCatalog();
            fail("Expected IllegalStateException, the current user is not an administrator.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.sortSupermarketCatalog();
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }

        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
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
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.sortSupermarketProducts();
            fail("Expected IllegalStateException, the current user is not an administrator.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.sortSupermarketProducts();
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
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
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
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
    public void testExportSupermarket() {
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
    public void testImportSupermarket() {
        catalog = Catalog.getInstance();
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.setImportFileStrategy(new ImportFileStub());
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.importSupermarket("path/to/file");
            fail("Expected IllegalStateException, the current user is not an administrator.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.importSupermarket("path/to/file");
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

    @Test
    public void testAddShelvingUnit() {
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution); // Create initial distribution for the test
        int initialSize = supermarket.getShelvingUnits().size();
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.addShelvingUnit(initialSize - 1, ProductTemperature.AMBIENT);
            fail("Expected IllegalStateException, the current user is not an administrator.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.addShelvingUnit(initialSize - 1, ProductTemperature.AMBIENT);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
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
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution);
        int initialSize = supermarket.getShelvingUnits().size();
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.addShelvingUnit(initialSize, ProductTemperature.AMBIENT);
            supermarket.removeShelvingUnit(initialSize);
            supermarket.removeShelvingUnit(1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.addShelvingUnit(initialSize, ProductTemperature.AMBIENT);
            supermarket.removeShelvingUnit(initialSize);
            supermarket.removeShelvingUnit(1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
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
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution);
        int tot_prod = supermarket.getAllProductsShelvingUnits().size();
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.addProductToShelvingUnit(1,0, product1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.addProductToShelvingUnit(1,0, product1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
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
        supermarket.addProductToShelvingUnit(1,0, product1);
        assertEquals("The product was not added to the shelving unit", tot_prod + 1, supermarket.getAllProductsShelvingUnits().size());
        assertEquals("The product was not added to the shelving unit", product1, supermarket.getShelvingUnits().get(1).getProduct(0));
        supermarket.addProductToShelvingUnit(1,0, product2);
        assertEquals("The product was not added to the shelving unit", product2, supermarket.getShelvingUnits().get(1).getProduct(0));
    }

    @Test
    public void testDeleteProductFromShelvingUnit() {
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product1);
        List<Product> x = supermarket.getShelvingUnits().get(1).getProducts();
        supermarket.addProductToShelvingUnit(1,1, product2);
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.removeProductFromShelvingUnit(1, 1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.removeProductFromShelvingUnit(1, 1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
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
    public void testHasProduct1() {
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product1);
        supermarket.addProductToShelvingUnit(2,1, product2);
        assertTrue("It occurred an error while searching if the product exists", supermarket.hasProduct(product1));
        supermarket.removeProductFromShelvingUnit(1, 0);
        supermarket.addProductToShelvingUnit(1,0, product3);
        assertFalse("The product is not suposed to be in the shelving unit", supermarket.hasProduct(product1));
        assertTrue("It occurred an error while searching if the product exists", supermarket.hasProduct(product3));
    }

    @Test
    public void testHasProduct2() {
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product1);
        supermarket.addProductToShelvingUnit(2,1, product2);
        assertTrue("It occurred an error while searching if the product exists", supermarket.hasProduct(product1.getName()));
        supermarket.removeProductFromShelvingUnit(1, 0);
        supermarket.addProductToShelvingUnit(1,0, product3);
        assertFalse("The product is not suposed to be in the shelving unit", supermarket.hasProduct(product1.getName()));
        assertTrue("It occurred an error while searching if the product exists", supermarket.hasProduct(product3.getName()));
    }

    @Test
    public void testSwapProducts() {
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product1);
        supermarket.addProductToShelvingUnit(2,1, product2);
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.swapProducts(1,0, 2, 1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.swapProducts(1,0, 2, 1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        try {
            supermarket.swapProducts(1,0, -2, 1);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("The position is not correct", e.getMessage());
        }
        try {
            supermarket.swapProducts(1,0, 2, 10);
            fail("Expected IllegalArgumentException, the position is not valid.");
        } catch (IndexOutOfBoundsException e) {
            assertEquals(("Invalid height: 10"), e.getMessage());
        }
        supermarket.swapProducts(1,0, 2, 1);
        assertEquals("The products were not swapped", product2, supermarket.getShelvingUnits().get(1).getProduct(0));
        assertEquals("The products were not swapped", product1, supermarket.getShelvingUnits().get(2).getProduct(1));
    }

    @Test
    public void testSwapShelvingUnits() {
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product1);
        supermarket.addProductToShelvingUnit(2,1, product2);
        ShelvingUnit u1 = supermarket.getShelvingUnits().get(1);
        ShelvingUnit u2 = supermarket.getShelvingUnits().get(2);
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.swapShelvingUnits(1, 2);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.swapShelvingUnits(1, 2);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
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
        supermarket.swapShelvingUnits(1, 2);
        assertEquals("The shelving units were not swapped", u2.getProducts(), supermarket.getShelvingUnits().get(1).getProducts());
        assertEquals("The shelving units were not swapped", u1.getProducts(), supermarket.getShelvingUnits().get(2).getProducts());
    }

    @Test
    public void testEmptyShelvingUnit(){
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product1);
        supermarket.addProductToShelvingUnit(2,1, product2);
        supermarket.addProductToShelvingUnit(2,0, product3);
        supermarket.addProductToShelvingUnit(0,0, product1);
        supermarket.addProductToShelvingUnit(0,1, product2);
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.emptyShelvingUnit(1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.emptyShelvingUnit(1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
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
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        supermarket.createDistribution(2, distribution);
        supermarket.addProductToShelvingUnit(1,0, product1);
        supermarket.addProductToShelvingUnit(2,1, product2);
        supermarket.addProductToShelvingUnit(2,0, product3);
        supermarket.addProductToShelvingUnit(0,0, product1);
        supermarket.addProductToShelvingUnit(0,1, product2);
        supermarket.logOut();
        supermarket.logIn(EMPLOYEE_NAME, EMPLOYEE_PASSWORD);
        try {
            supermarket.removeAllInstancesOfProduct(product1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("The logged in user is not admin.", e.getMessage());
        }
        supermarket.logOut();
        try {
            supermarket.removeAllInstancesOfProduct(product1);
            fail("Expected IllegalStateException, there should be no logged in user.");
        } catch (IllegalStateException e) {
            assertEquals("There is no logged in user.", e.getMessage());
        }
        supermarket.logIn(ADMIN_NAME, ADMIN_PASSWORD);
        try {
            supermarket.removeAllInstancesOfProduct(null);
            fail("Expected IllegalArgumentException, the product is not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("The product cannot be null", e.getMessage());
        }
        supermarket.removeAllInstancesOfProduct(product1);
        assertNull(supermarket.getShelvingUnits().get(1).getProduct(0));
        assertNull(supermarket.getShelvingUnits().get(0).getProduct(0));
        supermarket.removeAllInstancesOfProduct(product2);
        assertNull(supermarket.getShelvingUnits().get(0).getProduct(1));
        assertNull(supermarket.getShelvingUnits().get(2).getProduct(1));
        supermarket.removeAllInstancesOfProduct(product3);
        assertNull(supermarket.getShelvingUnits().get(2).getProduct(0));
    }

}
