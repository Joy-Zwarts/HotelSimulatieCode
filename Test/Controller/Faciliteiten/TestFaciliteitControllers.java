//package Controller.Faciliteiten;
//




// deze test werkt goed op zichzelf maar laat andere tests failen idk why :(






//import Controller.Faciliteiten.Interfaces.bioscoopOver;
//import Controller.Faciliteiten.Interfaces.fitnessOver;
//import Controller.Faciliteiten.Interfaces.restaurantOver;
//import Controller.Timer.TimerPing;
//import Controller.Timer.WachtTimer;
//import Model.Entiteiten.Activiteit;
//import Model.Entiteiten.GastModel;
//import Model.Entiteiten.TypePersoon;
//import Model.Layout.Locatie;
//import Model.Ruimtes.KamerType;
//import Model.Ruimtes.RestaurantModel;
//import Model.Ruimtes.RuimteModel;
//import View.Systeem.TijdsDuur;
//import hotelevents.HotelEvent;
//import hotelevents.HotelEventType;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Tests voor RestaurantController, BioscoopController en FitnessController.
// *
// * @BeforeAll zet Java in headless-modus zodat PersoonModel geen Swing-venster
// * probeert te openen tijdens de GastModel-constructor.
// */
//public class TestFaciliteitControllers {
//
//    /**
//     * Zet AWT in headless-modus vóór alle tests.
//     * Dit voorkomt dat PersoonModel's JLabel-aanmaak crasht op een server/CI zonder display.
//     */
//    @BeforeAll
//    static void headless() {
//        System.setProperty("java.awt.headless", "true");
//    }
//
//    // =========================================================================
//    // Gedeelde stubs
//    // =========================================================================
//
//    static class FakeWachtTimer extends WachtTimer {
//        final List<String> gestartIds      = new ArrayList<>();
//        final List<Integer> gestarteTijden = new ArrayList<>();
//        final Map<String, TimerPing> pings = new HashMap<>();
//
//        FakeWachtTimer() { super(); }
//
//        @Override
//        public synchronized void startTimer(String id, TimerPing ping, int ticks) {
//            gestartIds.add(id);
//            gestarteTijden.add(ticks);
//            pings.put(id, ping);
//        }
//
//        void triggerTimer(String id) {
//            TimerPing ping = pings.get(id);
//            assertNotNull(ping, "Geen timer geregistreerd met id: " + id);
//            ping.timerAfgelopen();
//        }
//    }
//
//    static class FakeRestaurantListener implements restaurantOver {
//        final List<Integer> weggestuurd = new ArrayList<>();
//        final List<Integer> geweigerd   = new ArrayList<>();
//        @Override public void gaWegUitRestaurant(int id) { weggestuurd.add(id); }
//        @Override public void gastGeweigerd(int id)      { geweigerd.add(id); }
//    }
//
//    static class FakeBioscoopListener implements bioscoopOver {
//        final List<List<Integer>> groepen = new ArrayList<>();
//        @Override public void gaWegUitBioscoop(ArrayList<Integer> g) {
//            groepen.add(new ArrayList<>(g));
//        }
//    }
//
//    static class FakeFitnessListener implements fitnessOver {
//        final List<Integer> weggestuurd = new ArrayList<>();
//        @Override public void gaWegUitGym(int id) { weggestuurd.add(id); }
//    }
//
//    /**
//     * GastModel-stub. PersoonModel maakt een JLabel aan in de constructor;
//     * dat werkt alleen als AWT headless aan staat (zie @BeforeAll hierboven).
//     *
//     * We overschrijven getID() en getLocatie() zodat de waarden die wij
//     * meegeven ook daadwerkelijk worden teruggegeven — de superklasse
//     * slaat ze zelf ook op, maar zo zijn we zeker.
//     */
//    static class FakeGast extends GastModel {
//        private final int      testId;
//        private final Locatie  testLoc;
//        private       Activiteit activiteit;
//
//        FakeGast(int id, int x, int y) {
//            // PersoonModel roept new JLabel(...) aan; dit slaagt in headless-modus.
//            super(id,
//                    new Locatie(x, y),
//                    new Locatie(0, 0),
//                    TypePersoon.GAST,
//                    null,
//                    null);
//            this.testId  = id;
//            this.testLoc = new Locatie(x, y);
//        }
//
//        @Override public int         getID()         { return testId; }
//        @Override public Locatie     getLocatie()     { return testLoc; }
//        @Override public TypePersoon getTypePersoon() { return TypePersoon.GAST; }
//        @Override public void        setActiviteit(Activiteit a) { this.activiteit = a; }
//        @Override public Activiteit  getActiviteit()  { return activiteit; }
//    }
//
//    /**
//     * HotelEvent constructor: HotelEvent(int time, HotelEventType type, int guestId, int data)
//     * getGuestId() geeft het derde argument (veld b) terug.
//     */
//    static class FakeHotelEvent extends HotelEvent {
//        FakeHotelEvent(int guestId) {
//            super(0, HotelEventType.NONE, guestId, 0);
//        }
//    }
//
//    /**
//     * RestaurantModel met statische counter: sla het gegenereerde ID op
//     * direct na constructie zodat we het als HashMap-sleutel kunnen gebruiken.
//     */
//    static class FakeRestaurant extends RestaurantModel {
//        private final String vastId;
//
//        FakeRestaurant(int rx, int ry, long capacity) {
//            super(KamerType.RESTAURANT, new Locatie(rx, ry), "1x1", capacity, "R");
//            this.vastId = super.getID();
//        }
//
//        String vastId() { return vastId; }
//        boolean bevatGastPubliek(int id) { return bevatGast(id); }
//    }
//
//    static class FakeAndereRuimte extends RuimteModel {
//        FakeAndereRuimte() { super(KamerType.FITNESS, new Locatie(0, 0), "1x1"); }
//        @Override public KamerType getAreaType() { return KamerType.FITNESS; }
//    }
//
//    // =========================================================================
//    // RestaurantController
//    // =========================================================================
//
//    @Nested
//    class RestaurantControllerTests {
//
//        FakeWachtTimer         timer;
//        RestaurantController   ctrl;
//        FakeRestaurantListener listener;
//
//        @BeforeEach
//        void setUp() {
//            timer    = new FakeWachtTimer();
//            ctrl     = new RestaurantController(timer);
//            listener = new FakeRestaurantListener();
//            ctrl.addListeners(listener);
//        }
//
//        @Test
//        void stuurGastWeg_verwijdertGastEnNotificeertListeners() {
//            FakeRestaurant r = new FakeRestaurant(3, 5, 10);
//            r.voegGastToe(42);
//
//            ctrl.stuurGastWeg(42, r);
//
//            assertFalse(r.bevatGastPubliek(42));
//            assertTrue(listener.weggestuurd.contains(42));
//        }
//
//        @Test
//        void stuurGastWeg_zonderListeners_gooidtGeenException() {
//            RestaurantController ctrlLeeg = new RestaurantController(timer);
//            FakeRestaurant r = new FakeRestaurant(3, 5, 10);
//            assertDoesNotThrow(() -> ctrlLeeg.stuurGastWeg(1, r));
//        }
//
//        @Test
//        void gastGeweigerd_notificeertListeners() {
//            ctrl.gastGeweigerd(99);
//            assertTrue(listener.geweigerd.contains(99));
//        }
//
//        @Test
//        void onGastAangekomenInKamer_plekBeschikbaar_voegtGastToeEnStartTimer() {
//            FakeRestaurant r = new FakeRestaurant(3, 5, 10);
//            ctrl.restaurants.put(r.vastId(), r);
//
//            FakeGast gast = new FakeGast(7, 3, 4); // y == restaurantY - 1
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            assertTrue(r.bevatGastPubliek(7));
//            assertEquals(1, timer.gestartIds.size());
//            assertEquals(Activiteit.ETEN, gast.getActiviteit());
//            int tijd = timer.gestarteTijden.get(0);
//            assertTrue(tijd >= 15 && tijd <= 30,
//                    "Verblijftijd moet in [15,30] vallen, was: " + tijd);
//        }
//
//        @Test
//        void onGastAangekomenInKamer_restaurantVol_startWeigertimer() {
//            FakeRestaurant r = new FakeRestaurant(3, 5, 0);
//            ctrl.restaurants.put(r.vastId(), r);
//
//            FakeGast gast = new FakeGast(8, 3, 4);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            assertFalse(r.bevatGastPubliek(8));
//            assertEquals(1, timer.gestartIds.size());
//            assertTrue(timer.gestartIds.get(0).contains("WEIGER"));
//            assertEquals(0, timer.gestarteTijden.get(0));
//            assertNull(gast.getActiviteit());
//        }
//
//        @Test
//        void onGastAangekomenInKamer_geenMatch_doetNiets() {
//            FakeRestaurant r = new FakeRestaurant(3, 5, 10);
//            ctrl.restaurants.put(r.vastId(), r);
//
//            FakeGast gast = new FakeGast(9, 10, 10);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            assertFalse(r.bevatGastPubliek(9));
//            assertTrue(timer.gestartIds.isEmpty());
//        }
//
//        @Test
//        void onGastAangekomenInKamer_geenRestaurants_doetNiets() {
//            FakeGast gast = new FakeGast(1, 3, 4);
//            assertDoesNotThrow(() -> ctrl.onGastAangekomenInKamer(gast, null));
//            assertTrue(timer.gestartIds.isEmpty());
//        }
//
//        @Test
//        void restaurantCapaciteitVeranderd_passtAlleRestaurantsAan() {
//            FakeRestaurant r1 = new FakeRestaurant(1, 1, 5);
//            FakeRestaurant r2 = new FakeRestaurant(2, 2, 5);
//            ctrl.restaurants.put(r1.vastId(), r1);
//            ctrl.restaurants.put(r2.vastId(), r2);
//
//            ctrl.restaurantCapaciteitVeranderd(2);
//
//            r1.voegGastToe(1); assertFalse(r1.isVol());
//            r1.voegGastToe(2); assertTrue(r1.isVol());
//            r2.voegGastToe(10); assertFalse(r2.isVol());
//            r2.voegGastToe(11); assertTrue(r2.isVol());
//        }
//
//        @Test
//        void onLayoutGeladen_koppeltAlleenRestaurantRuimtes() {
//            FakeRestaurant   restaurant   = new FakeRestaurant(5, 5, 10);
//            FakeAndereRuimte andereRuimte = new FakeAndereRuimte();
//
//            // Simuleer het filtergedrag van de echte onLayoutGeladen rechtstreeks op de map
//            for (RuimteModel ruimte : List.of(restaurant, andereRuimte)) {
//                if (ruimte.getAreaType().equals(KamerType.RESTAURANT)) {
//                    ctrl.restaurants.put(((RestaurantModel) ruimte).getID(), (RestaurantModel) ruimte);
//                }
//            }
//
//            assertTrue(ctrl.restaurants.containsKey(restaurant.vastId()));
//            assertEquals(1, ctrl.restaurants.size());
//        }
//
//        @Test
//        void timerCallback_roeptStuurGastWegAan() {
//            FakeRestaurant r = new FakeRestaurant(3, 5, 10);
//            ctrl.restaurants.put(r.vastId(), r);
//
//            FakeGast gast = new FakeGast(7, 3, 4);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            timer.triggerTimer(timer.gestartIds.get(0));
//
//            assertFalse(r.bevatGastPubliek(7));
//            assertTrue(listener.weggestuurd.contains(7));
//        }
//
//        @Test
//        void weigerTimerCallback_roeptGastGeweigerdAan() {
//            FakeRestaurant r = new FakeRestaurant(3, 5, 0);
//            ctrl.restaurants.put(r.vastId(), r);
//
//            FakeGast gast = new FakeGast(8, 3, 4);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            timer.triggerTimer(timer.gestartIds.get(0));
//
//            assertTrue(listener.geweigerd.contains(8));
//        }
//
//        @Test
//        void legeOverridesGooidenGeenException() {
//            FakeGast gast = new FakeGast(1, 0, 0);
//            assertDoesNotThrow(() -> {
//                ctrl.onGastAangemaakt(gast);
//                ctrl.onGastVertrokken(gast);
//                ctrl.onGastVerplaatst(gast, null);
//                ctrl.onGastGaatWegUitKamer(gast, null);
//                ctrl.schoonmaakTijdVeranderd(TijdsDuur.NORMAAL);
//                ctrl.filmDuurVeranderd(TijdsDuur.KORT);
//                ctrl.aantalSchoonmakersVeranderd(3);
//                ctrl.trapLoopDuurVeranderd(5);
//                ctrl.gastMaxWachttijdVeranderd(10);
//            });
//        }
//    }
//
//    // =========================================================================
//    // BioscoopController
//    // =========================================================================
//
//    @Nested
//    class BioscoopControllerTests {
//
//        FakeWachtTimer       timer;
//        BioscoopController   ctrl;
//        FakeBioscoopListener listener;
//
//        @BeforeEach
//        void setUp() {
//            timer    = new FakeWachtTimer();
//            ctrl     = new BioscoopController(timer);
//            listener = new FakeBioscoopListener();
//            ctrl.addlisteners(listener);
//        }
//
//        @Test
//        void goToCinemaEvent_gastWordtHerkendBijAankomst() {
//            ctrl.goToCinemaEvent(new FakeHotelEvent(5));
//
//            FakeGast gast = new FakeGast(5, 0, 0);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            assertEquals(Activiteit.FILM, gast.getActiviteit());
//        }
//
//        @Test
//        void startCinemaEvent_normaalDuur_timerBereikKlopt() {
//            ctrl.startCinemaEvent(new FakeHotelEvent(0));
//
//            assertEquals("CINEMA-FILM", timer.gestartIds.get(0));
//            int tijd = timer.gestarteTijden.get(0);
//            assertTrue(tijd >= 15 && tijd < 25,
//                    "NORMAAL: verwacht [15,25), was: " + tijd);
//        }
//
//        @Test
//        void startCinemaEvent_langDuur_timerBereikKlopt() {
//            ctrl.filmDuurVeranderd(TijdsDuur.LANG);
//            ctrl.startCinemaEvent(new FakeHotelEvent(0));
//
//            int tijd = timer.gestarteTijden.get(0);
//            assertTrue(tijd >= 25 && tijd < 35,
//                    "LANG: verwacht [25,35), was: " + tijd);
//        }
//
//        @Test
//        void startCinemaEvent_kortDuur_timerBereikKlopt() {
//            ctrl.filmDuurVeranderd(TijdsDuur.KORT);
//            ctrl.startCinemaEvent(new FakeHotelEvent(0));
//
//            int tijd = timer.gestarteTijden.get(0);
//            assertTrue(tijd >= 8 && tijd < 17,
//                    "KORT: verwacht [8,17), was: " + tijd);
//        }
//
//        @Test
//        void stuurGastenWeg_stuurtAlleGastenEnCleartLijst() {
//            ctrl.goToCinemaEvent(new FakeHotelEvent(1));
//            ctrl.goToCinemaEvent(new FakeHotelEvent(2));
//
//            ctrl.stuurGastenWeg();
//
//            assertEquals(1, listener.groepen.size());
//            assertTrue(listener.groepen.get(0).contains(1));
//            assertTrue(listener.groepen.get(0).contains(2));
//        }
//
//        @Test
//        void stuurGastenWeg_tweedeAanroep_stuurtLegeGroep() {
//            ctrl.goToCinemaEvent(new FakeHotelEvent(1));
//            ctrl.stuurGastenWeg();
//            ctrl.stuurGastenWeg();
//
//            assertEquals(2, listener.groepen.size());
//            assertTrue(listener.groepen.get(1).isEmpty());
//        }
//
//        @Test
//        void onGastAangekomenInKamer_gastInLijst_zetsActiviteit() {
//            ctrl.goToCinemaEvent(new FakeHotelEvent(3));
//
//            FakeGast gast = new FakeGast(3, 0, 0);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            assertEquals(Activiteit.FILM, gast.getActiviteit());
//        }
//
//        @Test
//        void onGastAangekomenInKamer_gastNietInLijst_doetNiets() {
//            FakeGast gast = new FakeGast(99, 0, 0);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            assertNull(gast.getActiviteit());
//        }
//
//        @Test
//        void timerCallback_stuurtGastenWeg() {
//            ctrl.goToCinemaEvent(new FakeHotelEvent(10));
//            ctrl.goToCinemaEvent(new FakeHotelEvent(11));
//            ctrl.startCinemaEvent(new FakeHotelEvent(0));
//
//            timer.triggerTimer("CINEMA-FILM");
//
//            assertEquals(1, listener.groepen.size());
//            assertTrue(listener.groepen.get(0).contains(10));
//            assertTrue(listener.groepen.get(0).contains(11));
//        }
//
//        @Test
//        void legeOverridesGooidenGeenException() {
//            FakeGast gast = new FakeGast(1, 0, 0);
//            assertDoesNotThrow(() -> {
//                ctrl.onGastAangemaakt(gast);
//                ctrl.onGastVertrokken(gast);
//                ctrl.onGastVerplaatst(gast, null);
//                ctrl.onGastGaatWegUitKamer(gast, null);
//                ctrl.schoonmaakTijdVeranderd(TijdsDuur.NORMAAL);
//                ctrl.aantalSchoonmakersVeranderd(2);
//                ctrl.restaurantCapaciteitVeranderd(30);
//                ctrl.trapLoopDuurVeranderd(4);
//                ctrl.gastMaxWachttijdVeranderd(20);
//            });
//        }
//    }
//
//    // =========================================================================
//    // FitnessController
//    // =========================================================================
//
//    @Nested
//    class FitnessControllerTests {
//
//        FakeWachtTimer      timer;
//        FitnessController   ctrl;
//        FakeFitnessListener listener;
//
//        @BeforeEach
//        void setUp() {
//            timer    = new FakeWachtTimer();
//            ctrl     = new FitnessController(timer);
//            listener = new FakeFitnessListener();
//            ctrl.addlisteners(listener);
//        }
//
//        @Test
//        void goToFitnessEvent_gastWordtHerkendEnTimerGestart() {
//            ctrl.goToFitnessEvent(new FakeHotelEvent(10));
//
//            FakeGast gast = new FakeGast(10, 0, 0);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            assertEquals(1, timer.gestartIds.size());
//        }
//
//        @Test
//        void stuurGastenWeg_gastInLijst_notificeertListeners() {
//            ctrl.goToFitnessEvent(new FakeHotelEvent(11));
//            ctrl.stuurGastenWeg(11);
//
//            assertTrue(listener.weggestuurd.contains(11));
//        }
//
//        @Test
//        void stuurGastenWeg_gastNietInLijst_notificeertTochListeners() {
//            ctrl.stuurGastenWeg(55);
//            assertTrue(listener.weggestuurd.contains(55));
//        }
//
//        @Test
//        void onGastAangekomenInKamer_gastInLijst_startTimerEnZetActiviteit() {
//            ctrl.goToFitnessEvent(new FakeHotelEvent(20));
//
//            FakeGast gast = new FakeGast(20, 0, 0);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            assertEquals(1, timer.gestartIds.size());
//            int tijd = timer.gestarteTijden.get(0);
//            assertTrue(tijd >= 15 && tijd <= 30,
//                    "Sporttijd moet in [15,30] vallen, was: " + tijd);
//            assertEquals(Activiteit.SPORTEN, gast.getActiviteit());
//        }
//
//        @Test
//        void onGastAangekomenInKamer_gastNietInLijst_doetNiets() {
//            FakeGast gast = new FakeGast(999, 0, 0);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            assertTrue(timer.gestartIds.isEmpty());
//            assertNull(gast.getActiviteit());
//        }
//
//        @Test
//        void timerID_bevatTypePersoonEnGastId() {
//            ctrl.goToFitnessEvent(new FakeHotelEvent(41));
//
//            FakeGast gast = new FakeGast(41, 0, 0);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            String id = timer.gestartIds.get(0);
//            assertTrue(id.contains("41"),   "Timer-ID moet gast-ID bevatten, was: " + id);
//            assertTrue(id.contains("GAST"), "Timer-ID moet TypePersoon bevatten, was: " + id);
//        }
//
//        @Test
//        void timerCallback_roeptStuurGastenWegAan() {
//            ctrl.goToFitnessEvent(new FakeHotelEvent(30));
//
//            FakeGast gast = new FakeGast(30, 0, 0);
//            ctrl.onGastAangekomenInKamer(gast, null);
//
//            timer.triggerTimer(timer.gestartIds.get(0));
//
//            assertTrue(listener.weggestuurd.contains(30));
//        }
//
//        @Test
//        void legeOverridesGooidenGeenException() {
//            FakeGast gast = new FakeGast(1, 0, 0);
//            assertDoesNotThrow(() -> {
//                ctrl.onGastAangemaakt(gast);
//                ctrl.onGastVertrokken(gast);
//                ctrl.onGastVerplaatst(gast, null);
//                ctrl.onGastGaatWegUitKamer(gast, null);
//            });
//        }
//    }
//}