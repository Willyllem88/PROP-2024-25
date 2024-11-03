package edu.upc.subgrupprop113.supermarketmanager;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SupermarketTest {
    Supermarket supermarket = Supermarket.getInstance();

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
        ArrayList<Pair<ProductTemperature, Integer>> distribution = new ArrayList<Pair<ProductTemperature, Integer>>();
        distribution.add(new Pair<>(ProductTemperature.FROZEN, 1));
        distribution.add(new Pair<>(ProductTemperature.REFRIGERATED, 2));
        distribution.add(new Pair<>(ProductTemperature.AMBIENT, 1));

        ArrayList<ShelvingUnit> shelvingUnits = new ArrayList<>();
        shelvingUnits.add(new ShelvingUnit(0, 2, ProductTemperature.FROZEN));
        shelvingUnits.add(new ShelvingUnit(1, 2, ProductTemperature.REFRIGERATED));
        shelvingUnits.add(new ShelvingUnit(2, 2, ProductTemperature.REFRIGERATED));
        shelvingUnits.add(new ShelvingUnit(3, 2, ProductTemperature.AMBIENT));

        supermarket.createDistribution(2, distribution);
        IllegalStateException notEmpty = assertThrows(IllegalStateException.class, () -> supermarket.createDistribution(2, distribution));
        assertEquals(notEmpty.getMessage(), "The supermarket distribution must be empty.");

        assertEquals(supermarket.getShelvingUnitHeight(), 2, "The shelving unit height should be 2.");
        ArrayList<ShelvingUnit> supermarketShelvingUnits = supermarket.getShelvingUnits();
        for (int i = 0; i < supermarketShelvingUnits.size(); i++) {
            ShelvingUnit expectedUnit = shelvingUnits.get(i);
            ShelvingUnit actualUnit = supermarketShelvingUnits.get(i);
            assertEquals(expectedUnit.getUid(), actualUnit.getUid(), "The shelving unit should have the same uid.");
            assertEquals(expectedUnit.getHeight(), actualUnit.getHeight(), "The shelving unit should have the same height");
            assertEquals(expectedUnit.getTemperature(), actualUnit.getTemperature(), "The shelving unit should have the same temperature");
        }
    }
}
