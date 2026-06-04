package Controller;

import Controller.Events.EventHandler;
import Controller.Faciliteiten.BioscoopController;
import Controller.Faciliteiten.FitnessController;
import Controller.Faciliteiten.RestaurantController;
import Controller.Layout.LayoutLoader;
import Controller.PersoonManagement.*;
import Controller.Systeem.*;
import Controller.Systeem.Interfaces.reset;
import Controller.Timer.WachtTimer;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.OverzichtView;
import View.Systeem.TimeManagementPanel;
import View.Systeem.TimePanel;
import hotelevents.HotelEventManager;

public class SimulatieController implements reset {

    // attributen

    private boolean started;
    public int scenario;

    // constructor

    public SimulatieController() {

        this.started = false;

        this.scenario = 1;

        DarkModeModel darkModeModel = new DarkModeModel();


        HotelSimulatieView view = new HotelSimulatieView(darkModeModel);

        HotelEventManager manager = new HotelEventManager(false);

        EventHandler eventHandler = new EventHandler(manager);

        PauseController pauseController = new PauseController(manager, null);

        WachtTimer timer = new WachtTimer();

        OverzichtView overzichtView = new OverzichtView(view, pauseController, manager);

        pauseController.setView(overzichtView);

        ReceptieController receptieController = new ReceptieController(overzichtView);

        GastController gastController = new GastController();

        SchoonmakerController schoonmakerController = new SchoonmakerController(receptieController, overzichtView, timer);

        LiftController liftController = new LiftController();

        gastController.injecteerOverzichtView(overzichtView);

        schoonmakerController.injecteerOverzichtView(overzichtView);

        liftController.injecteerOverzichtView(overzichtView);

        KamerAssign kamerAssign = new KamerAssign(receptieController);

        BioscoopController bioscoopController = new BioscoopController(timer);

        RestaurantController restaurantController = new RestaurantController(timer);

        FitnessController fitnessController = new FitnessController(timer);

        gastController.setNewGuestListener(fitnessController);

        gastController.setNewGuestListener(restaurantController);

        gastController.setNewGuestListener(bioscoopController);

        eventHandler.setEventListenerCheckIn(gastController);

        eventHandler.setEventListenerCheckOut(gastController);

        eventHandler.setEventListenerFood(gastController);

        eventHandler.setEventListenerCinema(gastController);

        eventHandler.setEventListenerFitness(gastController);

        eventHandler.setEventListenerCleaning(schoonmakerController);

        eventHandler.setEventListenerNoneEvent(timer);

        eventHandler.setEventListenerCheckOut(schoonmakerController);

        eventHandler.setEventListenerCinema(bioscoopController);

        eventHandler.setEventListenerFitness(fitnessController);

        eventHandler.setEventListenerNoneEvent(overzichtView);

        bioscoopController.addlisteners(gastController);

        restaurantController.addListeners(gastController);

        fitnessController.addlisteners(gastController);

        LayoutLoader layoutLoader = new LayoutLoader(manager, view, null, pauseController);

        PlaatsHelper plaatsHelper = new PlaatsHelper(null, null);

        layoutLoader.setNewLayoutListener(plaatsHelper);

        layoutLoader.setNewLayoutListener(gastController);

        layoutLoader.setNewLayoutListener(schoonmakerController);

        layoutLoader.setNewLayoutListener(restaurantController);

        layoutLoader.setNewLayoutListener(liftController);

        layoutLoader.setNewRoomListener(receptieController);

        gastController.setNewGuestListener(kamerAssign);

        gastController.setNewGuestListener(plaatsHelper);

        gastController.setNewGuestListener(receptieController);

        liftController.setNewLiftListener(plaatsHelper);

        schoonmakerController.setNewSchoonmakerListener(plaatsHelper);

        TimePanel timePanel = new TimePanel(manager, view.getTopBar(), view, this);

        eventHandler.setEventListenerNoneEvent(timePanel);

        TimeManagementPanel timeManagementPanel = new TimeManagementPanel(view.getTopBar(), darkModeModel);

        TimeManagementController timeManagement = new TimeManagementController(manager, this, view, timeManagementPanel);

        timeManagement.setListener(gastController);

        timeManagement.setListener(schoonmakerController);

        timeManagement.setListener(liftController);

        view.setTopbar(timePanel, timeManagementPanel);

        DarkModeController darkController = new DarkModeController(view, timeManagementPanel, darkModeModel);

        SettingsController settingsController = new SettingsController(view, timeManagementPanel, darkController);

        settingsController.addListener(bioscoopController);

        settingsController.addListener(schoonmakerController);

        settingsController.addListener(restaurantController);

        settingsController.addListener(gastController);

        darkController.verbindExtraSchermen(settingsController.getSettingsFrame(), overzichtView);

        darkController.applyTheme();

        ButtonController buttonController = new ButtonController(view, this, manager, layoutLoader, settingsController);

        overzichtView.verbindDataBronnen(receptieController.getGasten(), receptieController.getKamers(), schoonmakerController);

        buttonController.setListeners(this);
        buttonController.setListeners(plaatsHelper);
        buttonController.setListeners(gastController);
        buttonController.setListeners(schoonmakerController);
        buttonController.setListeners(receptieController);
        buttonController.setListeners(eventHandler);
        buttonController.setListeners(liftController);
    }
    // getters & setters

    public boolean getStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getScenario() {
        return scenario;
    }

    public void setScenario(int scenario) {
        this.scenario = scenario;
    }

    @Override
    public void resetSimulatie() {
        this.started = false;
        this.scenario = 1;

        System.out.println("Simulatie gereset");
    }

    // In SimulatieController.java (of jouw centrale controller)
    public int getMaxHteVoorScenario() {
        if (this.scenario == 1) {
            return 500;
        } else if (this.scenario == 2) {
            return 1000;
        } else if (this.scenario == 3) {
            return 2000;
        }

        return 2000; // fallback
    }
}