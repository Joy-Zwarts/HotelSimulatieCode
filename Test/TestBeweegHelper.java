import Controller.PersoonManagement.BeweegHelper;
import Controller.PersoonManagement.PathFinder;
import Model.Entiteiten.EntiteitenModel;
import Model.Entiteiten.PersoonModel;
import Model.Entiteiten.TypePersoon;
import Model.Layout.Locatie;
import View.Systeem.OverzichtView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestBeweegHelper implements BeweegHelper.MovementListener {

    private boolean stepTakenCalled;
    private boolean destinationReachedCalled;
    private EntiteitenModel lastEventEntiteit;
    private Locatie lastOudeLocatie;

    // =========================================================================
    // Veilige Fakes / Mocks voor Isolatie
    // =========================================================================

    static class FakePersoon extends PersoonModel {
        private final Locatie locatie;
        public FakePersoon(int id, Locatie startLocatie) {
            super(id, startLocatie, new Locatie(0,0), Color.black, TypePersoon.GAST);
            this.locatie = startLocatie;
        }
        @Override public Locatie getLocatie() { return this.locatie; }
    }

    static class FakePathFinder extends PathFinder {
        private final List<Locatie> stappen = new ArrayList<>();
        private int huidigeStapIndex = 0;
        private boolean berekenRouteAangeroepen = false;
        private boolean forceerPeekNull = false; // Toegevoegd voor specifieke randvoorwaarde-test

        public FakePathFinder() {
            super(new Locatie(0,0), new Locatie(0,0), null);
        }

        public void voegStapToe(Locatie loc) { stappen.add(loc); }
        public void setForceerPeekNull(boolean f) { this.forceerPeekNull = f; }

        @Override public void berekenRoute() { this.berekenRouteAangeroepen = true; }
        @Override public boolean isBestemmingBereikt() { return huidigeStapIndex >= stappen.size(); }

        @Override
        public Locatie peekNextStep() {
            if (forceerPeekNull) return null;
            return isBestemmingBereikt() ? null : stappen.get(huidigeStapIndex);
        }

        @Override public Locatie getNextStep() { return stappen.get(huidigeStapIndex++); }
    }

    static class FakeOverzichtView extends OverzichtView {
        private boolean gepauzeerd = false;
        public FakeOverzichtView() { super(null, null, null); }
        public void setGepauzeerd(boolean g) { this.gepauzeerd = g; }
        @Override public boolean isGepauzeerd() { return gepauzeerd; }
    }

    // =========================================================================
    // Setup & Interface implementatie
    // =========================================================================

    @BeforeEach
    void setUp() {
        stepTakenCalled = false;
        destinationReachedCalled = false;
        lastEventEntiteit = null;
        lastOudeLocatie = null;
    }

    @Override
    public void onStepTaken(EntiteitenModel entiteit, Locatie oudeLocatie) {
        stepTakenCalled = true;
        lastEventEntiteit = entiteit;
        lastOudeLocatie = oudeLocatie;
    }

    @Override
    public void onDestinationReached(EntiteitenModel entiteit) {
        destinationReachedCalled = true;
        lastEventEntiteit = entiteit;
    }

    // =========================================================================
    // De Tests voor 100% Branch Coverage
    // =========================================================================

    @Test
    public void testSetSpeed() throws Exception {
        BeweegHelper gastBeweeg = new BeweegHelper(100, this);
        gastBeweeg.setSpeed(250);

        java.lang.reflect.Field f = BeweegHelper.class.getDeclaredField("bewegingsTimer");
        f.setAccessible(true);
        javax.swing.Timer timer = (javax.swing.Timer) f.get(gastBeweeg);

        assertEquals(250, timer.getDelay());
    }

    @Test
    public void testStartEnReset() {
        BeweegHelper gastBeweeg = new BeweegHelper(100, this);
        assertDoesNotThrow(gastBeweeg::start);
        assertDoesNotThrow(gastBeweeg::reset);
    }

    @Test
    public void testProcessMovement_WanneerOverzichtViewNullIs_LooptGewoonDoor() throws Exception {
        // ONTBREKENDE BRANCH 1: overzichtView is NULL. Moet de if-statement overslaan en bewegen.
        BeweegHelper gb = new BeweegHelper(100, this);
        gb.setOverzichtView(null);

        FakePersoon persoon = new FakePersoon(1, new Locatie(0, 0));
        FakePathFinder pf = new FakePathFinder();
        pf.voegStapToe(new Locatie(1, 0));
        gb.voegRouteToe(persoon, pf);

        Method m = BeweegHelper.class.getDeclaredMethod("processMovement");
        m.setAccessible(true);
        m.invoke(gb);

        assertTrue(stepTakenCalled, "Moet gewoon stappen zetten wanneer er geen overzichtView is gekoppeld.");
    }

    @Test
    public void testProcessMovement_WanneerGepauzeerd_DoetNiets() throws Exception {
        BeweegHelper gb = new BeweegHelper(100, this);
        FakeOverzichtView fakeOverzicht = new FakeOverzichtView();
        fakeOverzicht.setGepauzeerd(true);
        gb.setOverzichtView(fakeOverzicht);

        FakePersoon persoon = new FakePersoon(1, new Locatie(0, 0));
        FakePathFinder pf = new FakePathFinder();
        pf.voegStapToe(new Locatie(1, 0));
        gb.voegRouteToe(persoon, pf);

        Method m = BeweegHelper.class.getDeclaredMethod("processMovement");
        m.setAccessible(true);
        m.invoke(gb);

        assertFalse(stepTakenCalled, "Er mag geen stap gezet worden als de simulatie gepauzeerd is.");
    }

    @Test
    public void testProcessMovement_BestemmingBereiktBranch() throws Exception {
        BeweegHelper gb = new BeweegHelper(100, this);
        FakePersoon persoon = new FakePersoon(2, new Locatie(5, 5));
        FakePathFinder pf = new FakePathFinder();

        gb.voegRouteToe(persoon, pf);
        assertTrue(pf.berekenRouteAangeroepen);

        Method m = BeweegHelper.class.getDeclaredMethod("processMovement");
        m.setAccessible(true);
        m.invoke(gb);

        assertTrue(destinationReachedCalled);
        assertEquals(persoon, lastEventEntiteit);
    }

    @Test
    public void testProcessMovement_HorizontaleStapZonderVertraging() throws Exception {
        BeweegHelper gb = new BeweegHelper(100, this);
        FakePersoon persoon = new FakePersoon(3, new Locatie(0, 0));
        FakePathFinder pf = new FakePathFinder();
        pf.voegStapToe(new Locatie(1, 0));

        gb.voegRouteToe(persoon, pf);

        Method m = BeweegHelper.class.getDeclaredMethod("processMovement");
        m.setAccessible(true);
        m.invoke(gb);

        assertTrue(stepTakenCalled);
        assertEquals(1, persoon.getLocatie().getX());
        assertEquals(0, persoon.getLocatie().getY());
        assertEquals(0, lastOudeLocatie.getX());
    }

    @Test
    public void testProcessMovement_WanneerVolgendeStapNullIs_SlaatWachtCalculatieOver() throws Exception {
        // ONTBREKENDE BRANCH 2: pf.isBestemmingBereikt() == false, maar volgendeStap == null
        BeweegHelper gb = new BeweegHelper(100, this);
        FakePersoon persoon = new FakePersoon(9, new Locatie(0, 0));
        FakePathFinder pf = new FakePathFinder();

        pf.voegStapToe(new Locatie(1, 1));
        pf.setForceerPeekNull(true); // Forceert 'volgendeStap == null' branch condition
        gb.voegRouteToe(persoon, pf);

        Method m = BeweegHelper.class.getDeclaredMethod("processMovement");
        m.setAccessible(true);

        // Mag niet crashen op de Math.max check en moet de stap gewoon uitvoeren
        assertDoesNotThrow(() -> m.invoke(gb));
        assertTrue(stepTakenCalled);
    }

    @Test
    public void testProcessMovement_VerticaleStapMetNegatieveOfNulVertraging() throws Exception {
        // ONTBREKENDE BRANCH 3: trapVertragingTicks <= 0. Moet door Math.max(1, ...) afgevangen worden naar 1.
        BeweegHelper gb = new BeweegHelper(100, this);
        gb.setTrapVertragingTicks(0); // Test de randvoorwaarde van 0 of negatief

        FakePersoon persoon = new FakePersoon(5, new Locatie(0, 0));
        FakePathFinder pf = new FakePathFinder();
        pf.voegStapToe(new Locatie(0, 1)); // Verticaal
        gb.voegRouteToe(persoon, pf);

        Method m = BeweegHelper.class.getDeclaredMethod("processMovement");
        m.setAccessible(true);

        // Eerste tick: registreert vertraging en dwingt minimale fallback af (1 tick wachtstand)
        m.invoke(gb);
        assertTrue(stepTakenCalled);

        stepTakenCalled = false;

        // Tweede tick: Resterende wacht-ticks was 1 -> wordt verlaagd naar 0 -> slaat stap over (continue)
        m.invoke(gb);
        assertFalse(stepTakenCalled, "Moet alsnog 1 tick wachten door de Math.max(1, ...) restrictie.");
    }

    @Test
    public void testProcessMovement_VerticaleStapTriggertTrapVertraging() throws Exception {
        BeweegHelper gb = new BeweegHelper(100, this);
        gb.setTrapVertragingTicks(2);

        FakePersoon persoon = new FakePersoon(4, new Locatie(0, 0));
        FakePathFinder pf = new FakePathFinder();
        pf.voegStapToe(new Locatie(0, 1));

        gb.voegRouteToe(persoon, pf);

        Method m = BeweegHelper.class.getDeclaredMethod("processMovement");
        m.setAccessible(true);

        m.invoke(gb);
        assertTrue(stepTakenCalled);
        assertEquals(0, persoon.getLocatie().getX());
        assertEquals(1, persoon.getLocatie().getY());

        stepTakenCalled = false;

        m.invoke(gb);
        assertFalse(stepTakenCalled, "Tick zou overgeslagen moeten worden door trapvertraging.");

        m.invoke(gb);
        assertFalse(stepTakenCalled, "Tick zou nogmaals overgeslagen moeten worden.");
    }
}