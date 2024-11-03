package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SupermarketTest {
    private Supermarket supermarket;
    private ArrayList<Pair<ProductTemperature, Integer>> distribution;
    private ArrayList<ShelvingUnit> expectedShelvingUnits;
    private Product product1, product2;

    @BeforeEach
    public void setUp() {
        supermarket = Supermarket.getInstance();
        supermarket.eraseDistribution();
        try {
            supermarket.logOut();
        }
        catch (Exception _) {}

        distribution = new ArrayList<Pair<ProductTemperature, Integer>>();
        distribution.add(new Pair<>(ProductTemperature.FROZEN, 1));
        distribution.add(new Pair<>(ProductTemperature.REFRIGERATED, 2));
        distribution.add(new Pair<>(ProductTemperature.AMBIENT, 1));

        expectedShelvingUnits = new ArrayList<>();
        expectedShelvingUnits.add(new ShelvingUnit(0, 2, ProductTemperature.FROZEN));
        expectedShelvingUnits.add(new ShelvingUnit(1, 2, ProductTemperature.REFRIGERATED));
        expectedShelvingUnits.add(new ShelvingUnit(2, 2, ProductTemperature.REFRIGERATED));
        expectedShelvingUnits.add(new ShelvingUnit(3, 2, ProductTemperature.AMBIENT));

        product1 = new Product("bread", 10.0f, ProductTemperature.AMBIENT, "path");
        product2 = new Product("water", 10.0f, ProductTemperature.REFRIGERATED, "path");
    }



    @Test
    public void testOneInstance() {
        assertEquals(supermarket, Supermarket.getInstance(), "The two instances of supermarket should be the same.");
    }

    @Test
    public void testFindUser() {
        assertEquals(supermarket.findUser("employee").getUsername(), "employee", "The finded user should have the username given.");
        assertNull(supermarket.findUser("Employee"), "The given user should not exist.");
    }

    @Test
    public void testLogIn() {
        Throwable noUserFound = assertThrows(IllegalArgumentException.class, () -> supermarket.logIn("marc", "marc"));
        assertEquals(noUserFound.getMessage(), "No such user found.");

        Throwable wrongPassword = assertThrows(IllegalArgumentException.class, () -> supermarket.logIn("admin", "marc"));
        assertEquals(wrongPassword.getMessage(), "Wrong password.");

        supermarket.logIn("admin", "admin");
        User admin = supermarket.findUser("admin");
        assertEquals(supermarket.getLogedUser(), admin, "The given user should be the admin." );

        Throwable alreadyLogged = assertThrows(IllegalStateException.class, () -> supermarket.logIn("admin", "marc"));
        assertEquals(alreadyLogged.getMessage(), "There is already a logged in user.");
    }

    @Test
    public void testLogOut() {
        Throwable noUserLogged = assertThrows(IllegalStateException.class, () -> supermarket.logOut());
        assertEquals(noUserLogged.getMessage(), "There is no logged user.");

        supermarket.logIn("admin", "admin");
        supermarket.logOut();
        assertNull(supermarket.getLogedUser(), "The no user should be logged");
    }

    @Test
    public void testCreateDistribution() {
        supermarket.createDistribution(2, distribution);
        IllegalStateException notEmpty = assertThrows(IllegalStateException.class, () -> supermarket.createDistribution(2, distribution));
        assertEquals(notEmpty.getMessage(), "The supermarket distribution must be empty.");

        assertEquals(supermarket.getShelvingUnitHeight(), 2, "The shelving unit height should be 2.");
        ArrayList<ShelvingUnit> supermarketShelvingUnits = supermarket.getShelvingUnits();
        for (int i = 0; i < supermarketShelvingUnits.size(); i++) {
            ShelvingUnit expectedUnit = expectedShelvingUnits.get(i);
            ShelvingUnit actualUnit = supermarketShelvingUnits.get(i);
            assertEquals(expectedUnit.getUid(), actualUnit.getUid(), "The shelving unit should have the same uid.");
            assertEquals(expectedUnit.getHeight(), actualUnit.getHeight(), "The shelving unit should have the same height");
            assertEquals(expectedUnit.getTemperature(), actualUnit.getTemperature(), "The shelving unit should have the same temperature");
        }
    }

    @Test
    public void testEraseDistribution() {
        supermarket.createDistribution(2, distribution);
        supermarket.eraseDistribution();
        assertEquals(supermarket.getShelvingUnitHeight(), 0, "The shelving unit height should be 0.");
        assertTrue(supermarket.getShelvingUnits().isEmpty(), "The shelving unit should be empty.");
    }

    @Test
    public void testSortSupermarket() {
        supermarket.createDistribution(2, distribution);
        supermarket.setOrderingStrategy(new OrderingStrategyStub());
        supermarket.sortSupermarketCatalog();

        ArrayList<ShelvingUnit> supermarketShelvingUnits = supermarket.getShelvingUnits();
        assertNull(supermarketShelvingUnits.getFirst().getProduct(0));
        assertNull(supermarketShelvingUnits.getFirst().getProduct(1));
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

        ArrayList<ShelvingUnit> supermarketShelvingUnits = supermarket.getShelvingUnits();
        assertNull(supermarketShelvingUnits.getFirst().getProduct(0));
        assertNull(supermarketShelvingUnits.getFirst().getProduct(1));
        assertEquals("water", supermarketShelvingUnits.get(1).getProduct(0).getName());
        assertNull(supermarketShelvingUnits.get(1).getProduct(1));
        assertNull(supermarketShelvingUnits.get(2).getProduct(0));
        assertNull(supermarketShelvingUnits.get(2).getProduct(1));
        assertEquals("bread", supermarketShelvingUnits.get(3).getProduct(0).getName());
        assertNull(supermarketShelvingUnits.get(3).getProduct(1));
    }

    @Test
    public void testGetAllProductsShelvingUnits() {
        //TO DO
    }
}
