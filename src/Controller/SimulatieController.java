package Controller;

import Controller.Events.EventHandler;
import Controller.Faciliteiten.BioscoopController;
import Controller.Faciliteiten.FitnessController;
import Controller.Faciliteiten.RestaurantController;
import Controller.Layout.LayoutLoader;
import Controller.PersoonManagement.*;
import Controller.Systeem.*;
import Model.Layout.LayoutModel;
import Model.Ruimtes.FitnessModel;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.OverzichtView;
import View.Systeem.TimeManagementPanel;
import View.Systeem.TimePanel;
import hotelevents.HotelEventManager;

public class SimulatieController implements reset {

    // attributen

    private boolean started;
    private int scenario;
    private GastController gastController;
    private SchoonmakerController schoonmakerController;
    private ReceptieController receptieController;
    private EventHandler eventHandler;

    // constructor

    public SimulatieController() {

        this.started = false;

        this.scenario = 1;

        DarkModeModel darkModeModel = new DarkModeModel();

        HotelSimulatieView view = new HotelSimulatieView(darkModeModel);

        HotelEventManager manager = new HotelEventManager(false);

        eventHandler = new EventHandler(manager);

        LayoutModel model = null;

        PauseController pauseController = new PauseController(manager, null);

        OverzichtView overzichtView = new OverzichtView(view, pauseController, manager);

        pauseController.setView(overzichtView);

        receptieController = new ReceptieController(overzichtView);

        gastController = new GastController();

        schoonmakerController = new SchoonmakerController(receptieController, overzichtView);

        gastController.injecteerOverzichtView(overzichtView);

        schoonmakerController.injecteerOverzichtView(overzichtView);

        KamerAssign kamerAssign = new KamerAssign(receptieController);

        BioscoopController bioscoopController = new BioscoopController();

        RestaurantController restaurantController = new RestaurantController(overzichtView);

        FitnessController fitnessController = new FitnessController();

        // EVENT LISTENERS

        eventHandler.setEventListenerCheckIn(gastController);

        eventHandler.setEventListenerCheckOut(gastController);

        eventHandler.setEventListenerFood(gastController);

        eventHandler.setEventListenerCinema(gastController);

        eventHandler.setEventListenerFitness(gastController);

        eventHandler.setEventListenerCleaning(schoonmakerController);

        eventHandler.setEventListenerNoneEvent(schoonmakerController);

        eventHandler.setEventListenerCheckOut(schoonmakerController);

        eventHandler.setEventListenerCinema(bioscoopController);

        eventHandler.setEventListenerNoneEvent(bioscoopController);

        eventHandler.setEventListenerFood(restaurantController);

        eventHandler.setEventListenerNoneEvent(restaurantController);

        eventHandler.setEventListenerFitness(fitnessController);

        eventHandler.setEventListenerNoneEvent(fitnessController);

        bioscoopController.addlisteners(gastController);

        restaurantController.addlisteners(gastController);

        fitnessController.addlisteners(gastController);

        LayoutLoader layoutLoader = new LayoutLoader(manager, view, model, pauseController, view);

        PlaatsHelper plaatsHelper = new PlaatsHelper(null);

        layoutLoader.setNewLayoutListener(plaatsHelper);

        layoutLoader.setNewLayoutListener(gastController);

        layoutLoader.setNewLayoutListener(schoonmakerController);

        layoutLoader.setNewRoomListener(receptieController);

        gastController.setNewGuestListener(kamerAssign);

        gastController.setNewGuestListener(plaatsHelper);

        gastController.setNewGuestListener(receptieController);

        schoonmakerController.setNewSchoonmakerListener(plaatsHelper);

        TimePanel timePanel = new TimePanel(manager, view.getTopBar(), view);

        eventHandler.setEventListenerNoneEvent(timePanel);

        TimeManagementPanel timeManagementPanel = new TimeManagementPanel(view.getTopBar(), darkModeModel);

        TimeManagementController timeManagement = new TimeManagementController(manager, this, view, timeManagementPanel);

        timeManagement.setListener(gastController);

        timeManagement.setListener(schoonmakerController);

        SettingsController settingsController = new SettingsController(view, timeManagementPanel, darkModeModel);

        view.setTopbar(timePanel, timeManagementPanel);

        ButtonController buttonController = new ButtonController(view, this, manager, layoutLoader, settingsController);

        buttonController.setListeners(this);
        buttonController.setListeners(plaatsHelper);
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
        System.out.println("Simulatie wordt gereset...");

        this.started = false;
        this.scenario = 1;

        // 1. Reset de gasten data en stopt hun bewegingstimer
        if (gastController != null) {
            gastController.reset();
        }

        // 2. Reset de schoonmakers, hun queues en zet ze terug op hun station
        if (schoonmakerController != null) {
            schoonmakerController.reset();
        }

        // 3. Maak de receptie leeg en zet alle kamers op 'vrij'
        if (receptieController != null) {
            receptieController.reset();
        }

        if (eventHandler != null) {
            eventHandler.reset();
        }

        System.out.println("Reset succesvol afgerond!");
    }
}