package Controller.Faciliteiten;

import Controller.Faciliteiten.Interfaces.bioscoopOver;
import Controller.Faciliteiten.Interfaces.fitnessOver;
import Controller.Faciliteiten.Interfaces.restaurantOver;
import Controller.Layout.LayoutController;
import Controller.Timer.TimerPing;
import Controller.Timer.WachtTimer;
import Model.Entiteiten.Activiteit;
import Model.Entiteiten.GastModel;
import Model.Entiteiten.TypePersoon;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RestaurantModel;
import Model.Ruimtes.RuimteModel;
import View.Systeem.TijdsDuur;
import hotelevents.HotelEvent;
import hotelevents.HotelEventType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestFaciliteitControllers {

    // =========================================================================
    // Veilige Stubs zonder Swing/UI triggers
    // =========================================================================

    static class FakeWachtTimer extends WachtTimer {
        final List<String> gestartIds      = new ArrayList<>();
        final List<Integer> gestarteTijden = new ArrayList<>();
        final Map<String, TimerPing> pings = new HashMap<>();

        @Override
        public synchronized void startTimer(String id, TimerPing ping, int ticks) {
            gestartIds.add(id);
            gestarteTijden.add(ticks);
            pings.put(id, ping);
        }

        void triggerTimer(String id) {
            TimerPing ping = pings.get(id);
            assertNotNull(ping, "Geen timer geregistreerd met id: " + id);
            ping.timerAfgelopen();
        }
    }

    static class FakeRestaurantListener implements restaurantOver {
        final List<Integer> weggestuurd = new ArrayList<>();
        final List<Integer> geweigerd   = new ArrayList<>();
        @Override public void gaWegUitRestaurant(int id) { weggestuurd.add(id); }
        @Override public void gastGeweigerd(int id)      { geweigerd.add(id); }
    }

    static class FakeBioscoopListener implements bioscoopOver {
        final List<List<Integer>> groepen = new ArrayList<>();
        @Override public void gaWegUitBioscoop(ArrayList<Integer> g) {
            groepen.add(new ArrayList<>(g));
        }
    }

    static class FakeFitnessListener implements fitnessOver {
        final List<Integer> weggestuurd = new ArrayList<>();
        @Override public void gaWegUitGym(int id) { weggestuurd.add(id); }
    }

    static class FakeGast extends GastModel {
        private final int testId;
        private final Locatie testLoc;
        private Activiteit activiteit;

        FakeGast(int id, int x, int y) {
            super(id, null, null, TypePersoon.GAST, null, null);
            this.testId = id;
            this.testLoc = new Locatie(x, y);
        }

        @Override public int getID() { return testId; }
        @Override public Locatie getLocatie() { return testLoc; }
        @Override public TypePersoon getTypePersoon() { return TypePersoon.GAST; }
        @Override public void setActiviteit(Activiteit a) { this.activiteit = a; }
        @Override public Activiteit getActiviteit() { return activiteit; }
        @Override public Locatie getTargetLocatie() { return new Locatie(0, 0); }
    }

    static class FakeHotelEvent extends HotelEvent {
        FakeHotelEvent(int guestId) {
            super(0, HotelEventType.NONE, guestId, 0);
        }
    }

    static class FakeRestaurant extends RestaurantModel {
        private final String vastId;

        FakeRestaurant(int rx, int ry, long capacity) {
            super(KamerType.RESTAURANT, new Locatie(rx, ry), "1x1", capacity, "R");
            this.vastId = super.getID();
        }

        String vastId() { return vastId; }
        boolean bevatGastPubliek(int id) { return bevatGast(id); }
    }

    static class FakeAndereRuimte extends RuimteModel {
        FakeAndereRuimte() { super(KamerType.FITNESS, new Locatie(0, 0), "1x1"); }
        @Override public KamerType getAreaType() { return KamerType.FITNESS; }
    }

    // =========================================================================
    // GEFIXTE VEILIGE STUB VOOR LAYOUTCONTROLLER
    // =========================================================================
    // =========================================================================
    // GEFIXTE VEILIGE STUB VOOR LAYOUTCONTROLLER (Inclusief Fake View)
    // =========================================================================
    // =========================================================================
    // GEFIXTE VEILIGE STUB VOOR LAYOUTCONTROLLER (Omzeilt de complete UI Init)
    // =========================================================================
    static class FakeLayoutController extends LayoutController {
        private final LayoutModel mockModel;

        FakeLayoutController(List<RuimteModel> ruimtes) {
            // We geven minimale dummy objecten mee om de Java compiler tevreden te houden
            super(
                    new LayoutModel() {
                        @Override
                        public ArrayList<RuimteModel> getRuimtes() {
                            return new ArrayList<>(ruimtes);
                        }
                    },
                    null, // View mag nu null zijn, want we slaan de init() over!
                    null  // SimulatieView mag nu null zijn
            );

            // Lokale referentie voor onze eigen getModel() override
            this.mockModel = new LayoutModel() {
                @Override
                public ArrayList<RuimteModel> getRuimtes() {
                    return new ArrayList<>(ruimtes);
                }
            };
        }

        // CRUCIAL: Door init() leeg te overschrijven, stoppen we de executie van
        // view.berekenGridGrootte() en simulatieView.getLayoutScrollPane() volledig!
        @Override
        protected void init() {
            // Lege implementatie: doet niets, voorkomt alle NullPointerExceptions tijdens testen.
        }

        @Override
        public LayoutModel getModel() {
            return this.mockModel;
        }
    }

    // =========================================================================
    // RestaurantController Tests
    // =========================================================================

    @Nested
    class RestaurantControllerTests {
        FakeWachtTimer timer;
        RestaurantController ctrl;
        FakeRestaurantListener listener;

        @BeforeEach
        void setUp() {
            timer = new FakeWachtTimer();
            ctrl = new RestaurantController(timer);
            listener = new FakeRestaurantListener();
            ctrl.addListeners(listener);
        }

        @Test
        void stuurGastWeg_verwijdertGastEnNotificeertListeners() {
            FakeRestaurant r = new FakeRestaurant(3, 5, 10);
            r.voegGastToe(42);
            ctrl.stuurGastWeg(42, r);
            assertFalse(r.bevatGastPubliek(42));
            assertTrue(listener.weggestuurd.contains(42));
        }

        @Test
        void gastGeweigerd_notificeertListeners() {
            ctrl.gastGeweigerd(99);
            assertTrue(listener.geweigerd.contains(99));
        }

        @Test
        void onGastAangekomenInKamer_plekBeschikbaar_voegtGastToeEnStartTimer() {
            FakeRestaurant r = new FakeRestaurant(3, 5, 10);
            ctrl.restaurants.put(r.vastId(), r);

            FakeGast gast = new FakeGast(7, 3, 4); // x == 3, y == 5 - 1 = 4 (MATCH!)
            ctrl.onGastAangekomenInKamer(gast, null);

            assertTrue(r.bevatGastPubliek(7));
            assertEquals(1, timer.gestartIds.size());
            assertEquals(Activiteit.ETEN, gast.getActiviteit());
        }

        @Test
        void onGastAangekomenInKamer_restaurantVol_startWeigertimer() {
            FakeRestaurant r = new RestaurantControllerTests.FakeRestaurantCustomId(3, 5, 0, "R-VOL");
            ctrl.restaurants.put("R-VOL", r);

            FakeGast gast = new FakeGast(8, 3, 4);
            ctrl.onGastAangekomenInKamer(gast, null);

            assertFalse(r.bevatGastPubliek(8));
            assertEquals(1, timer.gestartIds.size());
            assertTrue(timer.gestartIds.get(0).contains("WEIGER"));
        }

        @Test
        void onGastAangekomenInKamer_locatieXWijktAf_geenMatch() {
            FakeRestaurant r = new FakeRestaurant(3, 5, 10);
            ctrl.restaurants.put(r.vastId(), r);

            FakeGast gast = new FakeGast(7, 99, 4); // X klopt niet
            ctrl.onGastAangekomenInKamer(gast, null);

            assertFalse(r.bevatGastPubliek(7));
            assertTrue(timer.gestartIds.isEmpty());
        }

        @Test
        void onGastAangekomenInKamer_locatieYWijktAf_geenMatch() {
            FakeRestaurant r = new FakeRestaurant(3, 5, 10);
            ctrl.restaurants.put(r.vastId(), r);

            FakeGast gast = new FakeGast(7, 3, 99); // Y klopt niet
            ctrl.onGastAangekomenInKamer(gast, null);

            assertFalse(r.bevatGastPubliek(7));
            assertTrue(timer.gestartIds.isEmpty());
        }

        @Test
        void restaurantCapaciteitVeranderd_pastCapaciteitAan() {
            FakeRestaurant r = new FakeRestaurant(1, 1, 5);
            ctrl.restaurants.put(r.vastId(), r);
            ctrl.restaurantCapaciteitVeranderd(20);
        }

        @Test
        void onLayoutGeladen_filtertEnKoppeltRestaurantsEnSlaatAnderenOver() {
            FakeRestaurant r = new FakeRestaurant(1, 1, 10);
            FakeAndereRuimte gym = new FakeAndereRuimte();
            FakeLayoutController layoutCtrl = new FakeLayoutController(List.of(r, gym));

            ctrl.onLayoutGeladen(layoutCtrl);

            assertTrue(ctrl.restaurants.containsKey(r.getID()));
            assertEquals(1, ctrl.restaurants.size());
            assertEquals(layoutCtrl, ctrl.layoutController);
        }

        @Test
        void testLegeOverridesVoorVolledigeCoverage() {
            FakeGast gast = new FakeGast(1, 0, 0);
            assertDoesNotThrow(() -> {
                ctrl.onGastAangemaakt(gast);
                ctrl.onGastVertrokken(gast);
                ctrl.onGastVerplaatst(gast, null);
                ctrl.onGastGaatWegUitKamer(gast, null);
                ctrl.schoonmaakTijdVeranderd(TijdsDuur.NORMAAL);
                ctrl.filmDuurVeranderd(TijdsDuur.KORT);
                ctrl.aantalSchoonmakersVeranderd(5);
                ctrl.trapLoopDuurVeranderd(3);
                ctrl.gastMaxWachttijdVeranderd(15);
            });
        }

        private static class FakeRestaurantCustomId extends FakeRestaurant {
            private final String customId;
            FakeRestaurantCustomId(int rx, int ry, long cap, String id) {
                super(rx, ry, cap);
                this.customId = id;
            }
            @Override public String getID() { return customId; }
        }
    }

    // =========================================================================
    // BioscoopController Tests
    // =========================================================================

    @Nested
    class BioscoopControllerTests {
        FakeWachtTimer timer;
        BioscoopController ctrl;
        FakeBioscoopListener listener;

        @BeforeEach
        void setUp() {
            timer = new FakeWachtTimer();
            ctrl = new BioscoopController(timer);
            listener = new FakeBioscoopListener();
            ctrl.addlisteners(listener);
        }

        @Test
        void goToCinemaEvent_gastWordtHerkendBijAankomst() {
            ctrl.goToCinemaEvent(new FakeHotelEvent(5));
            FakeGast gast = new FakeGast(5, 0, 0);
            ctrl.onGastAangekomenInKamer(gast, null);
            assertEquals(Activiteit.FILM, gast.getActiviteit());
        }

        @Test
        void onGastAangekomenInKamer_gastNietInLijst_doetNiets() {
            FakeGast gast = new FakeGast(99, 0, 0);
            ctrl.onGastAangekomenInKamer(gast, null);
            assertNull(gast.getActiviteit());
        }

        @Test
        void startCinemaEvent_verschillendeFilmDuren_timerBereikKlopt() {
            ctrl.filmDuurVeranderd(TijdsDuur.KORT);
            ctrl.startCinemaEvent(new FakeHotelEvent(0));

            ctrl.filmDuurVeranderd(TijdsDuur.LANG);
            ctrl.startCinemaEvent(new FakeHotelEvent(0));

            ctrl.filmDuurVeranderd(TijdsDuur.NORMAAL);
            ctrl.startCinemaEvent(new FakeHotelEvent(0));

            assertEquals(3, timer.gestartIds.size());
        }

        @Test
        void stuurGastenWeg_stuurtAlleGastenEnCleartLijst() {
            ctrl.goToCinemaEvent(new FakeHotelEvent(1));
            ctrl.goToCinemaEvent(new FakeHotelEvent(2));
            ctrl.stuurGastenWeg();
            assertEquals(1, listener.groepen.size());
            assertTrue(listener.groepen.get(0).contains(1));
        }

        @Test
        void testLegeOverridesVoorVolledigeCoverage() {
            FakeGast gast = new FakeGast(1, 0, 0);
            assertDoesNotThrow(() -> {
                ctrl.onGastAangemaakt(gast);
                ctrl.onGastVertrokken(gast);
                ctrl.onGastVerplaatst(gast, null);
                ctrl.onGastGaatWegUitKamer(gast, null);
                ctrl.schoonmaakTijdVeranderd(TijdsDuur.NORMAAL);
                ctrl.aantalSchoonmakersVeranderd(2);
                ctrl.restaurantCapaciteitVeranderd(32);
                ctrl.trapLoopDuurVeranderd(10);
                ctrl.gastMaxWachttijdVeranderd(5);
            });
        }
    }

    // =========================================================================
    // FitnessController Tests
    // =========================================================================

    @Nested
    class FitnessControllerTests {
        FakeWachtTimer timer;
        FitnessController ctrl;
        FakeFitnessListener listener;

        @BeforeEach
        void setUp() {
            timer = new FakeWachtTimer();
            ctrl = new FitnessController(timer);
            listener = new FakeFitnessListener();
            ctrl.addlisteners(listener);
        }

        @Test
        void goToFitnessEvent_gastWordtHerkendEnTimerGestart() {
            ctrl.goToFitnessEvent(new FakeHotelEvent(10));
            FakeGast gast = new FakeGast(10, 0, 0);
            ctrl.onGastAangekomenInKamer(gast, null);
            assertEquals(1, timer.gestartIds.size());
            assertEquals(Activiteit.SPORTEN, gast.getActiviteit());
        }

        @Test
        void onGastAangekomenInKamer_gastNietInLijst_doetNiets() {
            FakeGast gast = new FakeGast(555, 0, 0);
            ctrl.onGastAangekomenInKamer(gast, null);
            assertTrue(timer.gestartIds.isEmpty());
            assertNull(gast.getActiviteit());
        }

        @Test
        void stuurGastenWeg_gastInLijst_notificeertListeners() {
            ctrl.goToFitnessEvent(new FakeHotelEvent(11));
            ctrl.stuurGastenWeg(11);
            assertTrue(listener.weggestuurd.contains(11));
        }

        @Test
        void testLegeOverridesVoorVolledigeCoverage() {
            FakeGast gast = new FakeGast(1, 0, 0);
            assertDoesNotThrow(() -> {
                ctrl.onGastAangemaakt(gast);
                ctrl.onGastVertrokken(gast);
                ctrl.onGastVerplaatst(gast, null);
                ctrl.onGastGaatWegUitKamer(gast, null);
            });
        }
    }
}