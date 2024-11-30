package edu.upc.subgrupprop113.supermarketmanager;

import edu.upc.subgrupprop113.supermarketmanager.models.User;
import org.junit.Test;
import static org.junit.Assert.*;


public class UserTest {
    private final String username = "userStub";
    private final String password = "passwordStub";
    private final User user = new UserStub(username, password);

    @Test
    public void testGetUsername() {
        assertEquals("Expected: " + username + ".\nGiven: " + user.getUsername() + ".\nUsername of User incorrect", user.getUsername(), username);
    }

    @Test
    public void testIsPasswordCheck() {
        assertTrue("The password should be " + password + " but another one was saved.\n", user.isPasswordCheck(password));
    }

    @Test
    public void testIsAdmin() {
        assertFalse( "The user shouldn't be admin.\n", user.isAdmin());
    }
}