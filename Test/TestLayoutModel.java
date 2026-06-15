import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestLayoutModel {

    private LayoutModel model;

    @BeforeEach
    void setUp() {
        model = new LayoutModel();
    }

    @Test
    void VakDimensiesEnGetters() {
        assertEquals(118, model.getVakBreedte());
        assertEquals(59, model.getVakHoogte());

        model.setVakDimensies(150, 75);
        assertEquals(150, model.getVakBreedte());
        assertEquals(75, model.getVakHoogte());
        assertNotNull(model.getGrid());
    }

    @Test
    void AddRuimte() {
        RuimteModel dummyRuimte = new RuimteModel(KamerType.ROOM, new Locatie(2, 2), "1,1") {
            @Override public KamerType getAreaType() { return KamerType.ROOM; }
        };

        model.addRuimte(dummyRuimte);
        assertEquals(1, model.getRuimtes().size());
        assertEquals(dummyRuimte, model.getRuimtes().get(0));
    }

    @Test
    void AddVerplichteElementenNormaleGridMaaktAlleElementen() {
        // b = 10, l = 10 -> liftY = 5 (>1), lobbyBreedte = 8 (>0)
        model.addVerplichteElementen(10, 10);

        ArrayList<RuimteModel> verplicht = model.getVerplichteElementen();

        // Verwacht: SCHACHT, TRAPPEN, LOBBY
        assertEquals(3, verplicht.size());

        boolean heeftSchacht = verplicht.stream().anyMatch(r -> r.getAreaType() == KamerType.SCHACHT);
        boolean heeftTrappen = verplicht.stream().anyMatch(r -> r.getAreaType() == KamerType.TRAPPEN);
        boolean heeftLobby = verplicht.stream().anyMatch(r -> r.getAreaType() == KamerType.LOBBY);

        assertTrue(heeftSchacht);
        assertTrue(heeftTrappen);
        assertTrue(heeftLobby);
    }

    @Test
    void AddVerplichteElementenKleineGridSlaatBranchesOver() {
        // l = 1 -> liftY = 0 -> < 1 -> wordt 1. liftY is niet > 1 dus SCHACHT wordt overgeslagen.
        // b = 2 -> lobbyBreedte = 0 -> niet > 0 dus LOBBY wordt overgeslagen.
        model.addVerplichteElementen(1, 2);

        ArrayList<RuimteModel> verplicht = model.getVerplichteElementen();

        // Alleen TRAPPEN mag nu toegevoegd zijn
        assertEquals(1, verplicht.size());
        assertEquals(KamerType.TRAPPEN, verplicht.get(0).getAreaType());
    }

    @Test
    void AddKamerBuitenJsonDefaultSwitchBranch() {
        // Handmatig aanroepen met een type dat niet in de switch staat (bijv. ROOM)
        // Dit zorgt ervoor dat de 'default' (het negeren van de kamer) gedekt is.
        assertDoesNotThrow(() -> model.addKamerBuitenJson(KamerType.ROOM, new Locatie(1,1), "1,1", 5));
        assertTrue(model.getVerplichteElementen().isEmpty());
    }

    @Test
    void GetRuimteBijLocatieVindtRuimtesCorrect() {
        Locatie kamerLoc = new Locatie(2, 2);
        // Let op de interne transformatie in getRuimteBijLocatie: loc.getY() + 1
        // Dus als we zoeken op (2, 1), zoekt de methode intern naar (2, 2)
        Locatie zoekLoc = new Locatie(2, 1);

        RuimteModel normaleKamer = new RuimteModel(KamerType.ROOM, kamerLoc, "1,1") {
            @Override public KamerType getAreaType() { return KamerType.ROOM; }
        };
        model.addRuimte(normaleKamer);

        // 1. Zoek in normale ruimtes
        RuimteModel gevonden1 = model.getRuimteBijLocatie(zoekLoc);
        assertEquals(normaleKamer, gevonden1);

        // 2. Zoek in verplichte elementen
        Locatie verplichtLoc = new Locatie(5, 5);
        Locatie zoekVerplichtLoc = new Locatie(5, 4);
        RuimteModel verplichteKamer = new RuimteModel(KamerType.LOBBY, verplichtLoc, "1,1") {
            @Override public KamerType getAreaType() { return KamerType.LOBBY; }
        };
        model.getVerplichteElementen().add(verplichteKamer);

        RuimteModel gevonden2 = model.getRuimteBijLocatie(zoekVerplichtLoc);
        assertEquals(verplichteKamer, gevonden2);

        // 3. Niet bestaande locatie branch
        assertNull(model.getRuimteBijLocatie(new Locatie(9, 9)));
    }
}