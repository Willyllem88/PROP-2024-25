package edu.upc.subgrupprop113.supermarketmanager;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private final String username = "userStub";
    private final String password = "passwordStub";
    private final User user = new UserStub(username, password);

    @Test
    public void testGetUsername() {
        assertEquals(username, user.getUsername(), "Expected: " + username + ".\nGiven: " + user.getUsername() + ".\nUsername of User incorrect");
    }

    @Test
    public void testIsPasswordCheck() {
        assertTrue(user.isPasswordCheck(password), "The password should be " + password + " but another one was saved.\n");
    }

    @Test
    public void testIsAdmin() {
        assertFalse(user.isAdmin(), "The user shouldn't be admin.\n");
    }
}