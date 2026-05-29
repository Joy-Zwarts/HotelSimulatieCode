package Controller;

import Controller.Events.EventHandler;
import Controller.Faciliteiten.BioscoopController;
import Controller.Faciliteiten.FitnessController;
import Controller.Faciliteiten.RestaurantController;
import Controller.Layout.LayoutLoader;
import Controller.PersoonManagement.*;
import Controller.Systeem.*;
import Controller.Systeem.Intefaces.reset;
import Controller.Timer.WachtTimer;
import Model.Layout.LayoutModel;
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

    // constructor

    public SimulatieController() {

        this.started = false;

        this.scenario = 1;

        DarkModeModel darkModeModel = new DarkModeModel();

        HotelSimulatieView view = new HotelSimulatieView(darkModeModel);

        HotelEventManager manager = new HotelEventManager(false);

        EventHandler eventHandler = new EventHandler(manager);

        LayoutModel model = null;

        PauseController pauseController = new PauseController(manager, null);

        WachtTimer timer = new WachtTimer();

        OverzichtView overzichtView = new OverzichtView(view, pauseController, manager);

        pauseController.setView(overzichtView);

        ReceptieController receptieController = new ReceptieController(overzichtView);

        GastController gastController = new GastController();

        SchoonmakerController schoonmakerController = new SchoonmakerController(receptieController, overzichtView, timer);

        gastController.injecteerOverzichtView(overzichtView);

        schoonmakerController.injecteerOverzichtView(overzichtView);

        KamerAssign kamerAssign = new KamerAssign(receptieController);

        BioscoopController bioscoopController = new BioscoopController(timer);

        RestaurantController restaurantController = new RestaurantController(timer);

        FitnessController fitnessController = new FitnessController(timer);

        // EVENT LISTENERS

        gastController.setNewGuestListener(fitnessController);

        gastController.setNewGuestListener(restaurantController);

        gastController.setNewGuestListener(bioscoopController);

        eventHandler.setEventListenerCheckIn(gastController);

        eventHandler.setEventListenerCheckOut(gastController);

        eventHandler.setEventListenerFood(gastController);

        eventHandler.setEventListenerFood(restaurantController);

        eventHandler.setEventListenerCinema(gastController);

        eventHandler.setEventListenerFitness(gastController);

        eventHandler.setEventListenerCleaning(schoonmakerController);

        eventHandler.setEventListenerNoneEvent(timer);

        eventHandler.setEventListenerCheckOut(schoonmakerController);

        eventHandler.setEventListenerCinema(bioscoopController);

        eventHandler.setEventListenerFitness(fitnessController);

        eventHandler.setEventListenerNoneEvent(overzichtView);

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

        overzichtView.verbindDataBronnen(receptieController.getGasten(), receptieController.getKamers(), schoonmakerController);

        buttonController.setListeners(this);
        buttonController.setListeners(plaatsHelper);
        buttonController.setListeners(gastController);
        buttonController.setListeners(schoonmakerController);
        buttonController.setListeners(receptieController);
        buttonController.setListeners(eventHandler);
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
}