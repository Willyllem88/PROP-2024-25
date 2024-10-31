package edu.upc.subgrupprop113.supermarketmanager;

import org.junit.jupiter.api.Test;

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
}
