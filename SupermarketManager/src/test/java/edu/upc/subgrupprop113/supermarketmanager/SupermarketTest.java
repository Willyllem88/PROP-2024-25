package edu.upc.subgrupprop113.supermarketmanager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupermarketTest {
    Supermarket supermarket = Supermarket.getInstance();

    @Test
    public void testOneInstance() {
        assertEquals(supermarket, Supermarket.getInstance(), "The two instances of supermarket should be the same");
    }
}
