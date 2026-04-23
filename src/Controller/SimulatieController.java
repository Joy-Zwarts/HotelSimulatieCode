package Controller;

import Controller.Events.EventHandler;
import Controller.GastManagement.GastPlaatser;
import Controller.GastManagement.PersoonController;
import Controller.GastManagement.ReceptieController;
import Controller.GastManagement.RoomAssign;
import Controller.Layout.LayoutLoader;
import Controller.Systeem.*;
import Model.Systeem.DarkModeModel;
import Model.Layout.LayoutModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.OverzichtView;
import View.Systeem.TimeManagementPanel;
import View.Systeem.TimePanel;
import hotelevents.HotelEventManager;

public class SimulatieController {

    // attributen

    private Boolean started;
    private int scenario;

    // constructor
    public SimulatieController() {
        this.scenario = 1; // default scenario is 1
        this.started = false;

        // initialiseer alle klassen nodig in de simulatie

        DarkModeModel darkModeModel = new DarkModeModel();

        HotelSimulatieView view = new HotelSimulatieView(darkModeModel);

        HotelEventManager manager = new HotelEventManager(false);

        EventHandler eventHandler = new EventHandler(manager);

        LayoutModel model = null;

        PauseController pauseController = new PauseController(manager, null);

        OverzichtView overzichtView = new OverzichtView(view, pauseController, manager);

        pauseController.setView(overzichtView);

        ReceptieController receptieController = new ReceptieController(overzichtView);

        PersoonController persoonController = new PersoonController(manager, overzichtView, receptieController);

        eventHandler.setEventListenerCheckIn(persoonController);

        eventHandler.setEventListenerCheckOut(persoonController);

        eventHandler.setEventListenerFood(persoonController);

        eventHandler.setEventListenerCinema(persoonController);

        eventHandler.setEventListenerFitness(persoonController);

        RoomAssign roomAssign = new RoomAssign(receptieController);

        LayoutLoader layoutLoader = new LayoutLoader(manager, view, model, pauseController, view);

        GastPlaatser gastPlaatser = new GastPlaatser(null);

        layoutLoader.setNewLayoutListener(gastPlaatser);

        layoutLoader.setNewRoomListener(receptieController);

        layoutLoader.setNewLayoutListener(persoonController);

        persoonController.setNewGuestListener(roomAssign);

        persoonController.setNewGuestListener(gastPlaatser);

        persoonController.setNewGuestListener(receptieController);

        TimePanel timePanel = new TimePanel(manager, view.getTopBar());

        eventHandler.setEventListenerNoneEvent(timePanel);

        TimeManagementPanel timeManagementPanel = new TimeManagementPanel(view.getTopBar(), darkModeModel);

        TimeManagementController timeManagement = new TimeManagementController(manager, this, view, timeManagementPanel);

        timeManagement.setListener(persoonController);

        SettingsController settingsController = new SettingsController(view, timeManagementPanel, darkModeModel);

        view.setTopbar(timePanel, timeManagementPanel);

        ButtonController buttonManager = new ButtonController(view, this, manager, layoutLoader, settingsController);
    }

    // getters & setters

    public boolean getStarted() {
        return started;
    }
    public void setStarted(Boolean started) {
        this.started = started;
    }
    public int getScenario() {
        return scenario;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }
    public void setScenario(int scenario) {
        this.scenario = scenario;
    }
}