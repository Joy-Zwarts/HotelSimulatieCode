package Controller;

import Controller.Events.EventHandler;
import Controller.Layout.LayoutLoader;
import Controller.PersoonManagement.*;
import Controller.Systeem.*;
import Model.Layout.LayoutModel;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.OverzichtView;
import View.Systeem.TimeManagementPanel;
import View.Systeem.TimePanel;
import hotelevents.HotelEventManager;

public class SimulatieController {

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

        OverzichtView overzichtView = new OverzichtView(view, pauseController, manager);

        pauseController.setView(overzichtView);

        ReceptieController receptieController = new ReceptieController(overzichtView);

        GastController gastController = new GastController();

        SchoonmakerController schoonmakerController = new SchoonmakerController(receptieController);

        KamerAssign kamerAssign = new KamerAssign(receptieController);

        // EVENT LISTENERS

        eventHandler.setEventListenerCheckIn(gastController);

        eventHandler.setEventListenerCheckOut(gastController);

        eventHandler.setEventListenerFood(gastController);

        eventHandler.setEventListenerCinema(gastController);

        eventHandler.setEventListenerFitness(gastController);

        eventHandler.setEventListenerCleaning(schoonmakerController);

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


        TimePanel timePanel = new TimePanel(manager, view.getTopBar());

        eventHandler.setEventListenerNoneEvent(timePanel);

        TimeManagementPanel timeManagementPanel = new TimeManagementPanel(view.getTopBar(), darkModeModel);

        TimeManagementController timeManagement = new TimeManagementController(manager, this, view, timeManagementPanel);

        timeManagement.setListener(gastController);

        SettingsController settingsController = new SettingsController(view, timeManagementPanel, darkModeModel);

        view.setTopbar(timePanel, timeManagementPanel);

        new ButtonController(view, this, manager, layoutLoader, settingsController);
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
}