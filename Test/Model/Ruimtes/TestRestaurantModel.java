package Model.Ruimtes;

import Model.Layout.Locatie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestRestaurantModel {

    private RestaurantModel restaurant;

    @BeforeEach
    void setUp() {

        // Restaurant met plaats voor 2 gasten.
        restaurant = new RestaurantModel(
                KamerType.RESTAURANT,
                new Locatie(5, 5),
                "2x2",
                2,
                "R");
    }

    // Controleert of de ID is aangemaakt.
    @Test
    void testGetID() {
        assertNotNull(restaurant.getID());
    }

    // Controleert de capaciteit.
    @Test
    void testCapacity() {

        restaurant.setCapacity(5);

        assertEquals(5, restaurant.getCapacity());
    }

    // Controleert dat een leeg restaurant niet vol is.
    @Test
    void testRestaurantNietVol() {

        assertFalse(restaurant.isVol());
    }

    // Controleert dat een restaurant vol wordt.
    @Test
    void testRestaurantVol() {

        restaurant.voegGastToe(1);
        restaurant.voegGastToe(2);

        assertTrue(restaurant.isVol());
    }

    // Controleert dat een gast toegevoegd kan worden.
    @Test
    void testVoegGastToe() {

        assertTrue(restaurant.voegGastToe(1));

        assertTrue(restaurant.bevatGast(1));
    }

    // Controleert dat geen gast toegevoegd kan worden als het restaurant vol is.
    @Test
    void testVoegGastToeWanneerVol() {

        restaurant.voegGastToe(1);
        restaurant.voegGastToe(2);

        assertFalse(restaurant.voegGastToe(3));
    }

    // Controleert dat een gast verwijderd wordt.
    @Test
    void testVerwijderGast() {

        restaurant.voegGastToe(1);

        restaurant.verwijderGast(1);

        assertFalse(restaurant.bevatGast(1));
    }

    // Controleert bevatGast().
    @Test
    void testBevatGast() {

        restaurant.voegGastToe(7);

        assertTrue(restaurant.bevatGast(7));
    }

    // Controleert dat een niet-aanwezige gast niet gevonden wordt.
    @Test
    void testBevatGastNiet() {

        assertFalse(restaurant.bevatGast(99));
    }
}