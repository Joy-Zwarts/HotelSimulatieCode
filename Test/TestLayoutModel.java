import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerClassificatie;
import Model.Ruimtes.KamerModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestLayoutModel {

    private LayoutModel layout;

    @BeforeEach
    void setUp() {
        layout = new LayoutModel();
    }

    @Test
    void testVakDimensiesEnGetters() {
        assertEquals(118, layout.getVakBreedte());
        assertEquals(59, layout.getVakHoogte());
        assertNotNull(layout.getGrid());

        layout.setVakDimensies(150, 75);
        assertEquals(150, layout.getVakBreedte());
        assertEquals(75, layout.getVakHoogte());
    }


    @Test
    void testAddRuimte() {
        KamerModel kamer = new KamerModel(
                KamerType.ROOM,
                new Locatie(2, 2),
                "1,1",
                KamerClassificatie.eenSter,
                false
        );

        layout.addRuimte(kamer);

        assertEquals(1, layout.getRuimtes().size());
        assertEquals(kamer, layout.getRuimtes().get(0));
    }

    @Test
    void testAddRuimteMetDummy() {
        RuimteModel dummyRuimte = new RuimteModel(KamerType.ROOM, new Locatie(2, 2), "1,1") {
            @Override public KamerType getAreaType() { return KamerType.ROOM; }
        };

        layout.addRuimte(dummyRuimte);
        assertEquals(1, layout.getRuimtes().size());
        assertEquals(dummyRuimte, layout.getRuimtes().get(0));
    }


    @Test
    void testAddVerplichteElementen() {
        layout.addVerplichteElementen(10, 10);

        ArrayList<RuimteModel> verplicht = layout.getVerplichteElementen();
        assertFalse(verplicht.isEmpty());
        assertEquals(3, verplicht.size());

        boolean heeftSchacht = verplicht.stream().anyMatch(r -> r.getAreaType() == KamerType.SCHACHT);
        boolean heeftTrappen = verplicht.stream().anyMatch(r -> r.getAreaType() == KamerType.TRAPPEN);
        boolean heeftLobby = verplicht.stream().anyMatch(r -> r.getAreaType() == KamerType.LOBBY);

        assertTrue(heeftSchacht);
        assertTrue(heeftTrappen);
        assertTrue(heeftLobby);
    }

    @Test
    void testAddVerplichteElementenKleineGrid() {
        layout.addVerplichteElementen(1, 2);

        ArrayList<RuimteModel> verplicht = layout.getVerplichteElementen();

        assertEquals(1, verplicht.size());
        assertEquals(KamerType.TRAPPEN, verplicht.get(0).getAreaType());
    }

    @Test
    void testAddKamerBuitenJson() {
        layout.addKamerBuitenJson(KamerType.SCHACHT, new Locatie(1, 1), "1,5", 5);
        assertEquals(1, layout.getVerplichteElementen().size());
        assertEquals(KamerType.SCHACHT, layout.getVerplichteElementen().get(0).getAreaType());

        layout.getVerplichteElementen().clear();

        layout.addKamerBuitenJson(KamerType.TRAPPEN, new Locatie(5, 1), "1,5", 5);
        assertEquals(1, layout.getVerplichteElementen().size());
        assertEquals(KamerType.TRAPPEN, layout.getVerplichteElementen().get(0).getAreaType());

        layout.getVerplichteElementen().clear();

        layout.addKamerBuitenJson(KamerType.LOBBY, new Locatie(2, 5), "3,1", 5);
        assertEquals(1, layout.getVerplichteElementen().size());
        assertEquals(KamerType.LOBBY, layout.getVerplichteElementen().get(0).getAreaType());
    }

    @Test
    void testAddKamerBuitenJsonDefault() {
        assertDoesNotThrow(() -> layout.addKamerBuitenJson(KamerType.ROOM, new Locatie(1,1), "1,1", 5));
        assertTrue(layout.getVerplichteElementen().isEmpty());
    }

    @Test
    void testGetRuimteBijLocatie() {
        Locatie kamerLoc = new Locatie(2, 2);
        Locatie zoekLoc = new Locatie(2, 1);

        RuimteModel normaleKamer = new RuimteModel(KamerType.ROOM, kamerLoc, "1,1") {
            @Override public KamerType getAreaType() { return KamerType.ROOM; }
        };
        layout.addRuimte(normaleKamer);

        RuimteModel gevonden1 = layout.getRuimteBijLocatie(zoekLoc);
        assertEquals(normaleKamer, gevonden1);

        Locatie verplichtLoc = new Locatie(5, 5);
        Locatie zoekVerplichtLoc = new Locatie(5, 4);
        RuimteModel verplichteKamer = new RuimteModel(KamerType.LOBBY, verplichtLoc, "1,1") {
            @Override public KamerType getAreaType() { return KamerType.LOBBY; }
        };
        layout.getVerplichteElementen().add(verplichteKamer);

        RuimteModel gevonden2 = layout.getRuimteBijLocatie(zoekVerplichtLoc);
        assertEquals(verplichteKamer, gevonden2);

        assertNull(layout.getRuimteBijLocatie(new Locatie(100, 100)));
    }
}