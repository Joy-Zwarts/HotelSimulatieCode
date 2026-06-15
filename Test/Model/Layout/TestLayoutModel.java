package Model.Layout;

import Model.Ruimtes.KamerClassificatie;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestLayoutModel {

    private LayoutModel layout;

    @BeforeEach
    void setUp() {
        layout = new LayoutModel();
    }

    // Controleert of een ruimte toegevoegd wordt.
    @Test
    void testAddRuimte() {

        KamerModel kamer =
                new KamerModel(
                        KamerType.ROOM,
                        new Locatie(2,2),
                        "1,1",
                        KamerClassificatie.eenSter,
                        false
                );

        layout.addRuimte(kamer);

        assertEquals(1, layout.getRuimtes().size());
    }

    // Controleert de standaard vakafmetingen.
    @Test
    void testVakAfmetingen() {

        assertEquals(118, layout.getVakBreedte());
        assertEquals(59, layout.getVakHoogte());
    }

    // Controleert het aanpassen van de vakafmetingen.
    @Test
    void testSetVakDimensies() {

        layout.setVakDimensies(50, 25);

        assertEquals(50, layout.getVakBreedte());
        assertEquals(25, layout.getVakHoogte());
    }

    // Controleert dat verplichte elementen worden toegevoegd.
    @Test
    void testAddVerplichteElementen() {

        layout.addVerplichteElementen(6, 8);

        assertFalse(layout.getVerplichteElementen().isEmpty());
    }

    // Controleert het toevoegen van een schacht.
    @Test
    void testAddSchacht() {

        layout.addKamerBuitenJson(
                KamerType.SCHACHT,
                new Locatie(1, 1),
                "1,5",
                5);

        assertEquals(1, layout.getVerplichteElementen().size());
    }

    // Controleert het toevoegen van een trappenhuis.
    @Test
    void testAddTrap() {

        layout.addKamerBuitenJson(
                KamerType.TRAPPEN,
                new Locatie(5, 1),
                "1,5",
                5);

        assertEquals(1, layout.getVerplichteElementen().size());
    }

    // Controleert het toevoegen van een lobby.
    @Test
    void testAddLobby() {

        layout.addKamerBuitenJson(
                KamerType.LOBBY,
                new Locatie(2, 5),
                "3,1",
                5);

        assertEquals(1, layout.getVerplichteElementen().size());
    }


    // Controleert dat null wordt teruggegeven als er geen ruimte is.
    @Test
    void testGetRuimteBijLocatieNietGevonden() {

        assertNull(layout.getRuimteBijLocatie(new Locatie(100, 100)));
    }

    // Controleert dat het grid bestaat.
    @Test
    void testGetGrid() {

        assertNotNull(layout.getGrid());
    }
}