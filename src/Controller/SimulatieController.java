package Controller;

import Controller.KamerManagement.PersoonController;
import Controller.KamerManagement.ReceptieController;
import Controller.Layout.LayoutLoader;
import Controller.Systeem.ButtonController;
import Controller.Systeem.PauseController;
import Controller.Systeem.SettingsController;
import Controller.Systeem.TimeManagementController;
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

        LayoutModel model = null;

        PauseController pauseController = new PauseController(manager, null);

        OverzichtView overzichtView = new OverzichtView(view, pauseController, manager);

        pauseController.setView(overzichtView);

        PersoonController persoonController = new PersoonController(manager, overzichtView);

        ReceptieController receptieController = new ReceptieController();

        LayoutLoader layoutLoader = new LayoutLoader(manager, view, model, pauseController, view);

        layoutLoader.setNewRoomListener(receptieController);

        persoonController.setNewGuestListener(receptieController);

        TimePanel timePanel = new TimePanel(manager, view.getTopBar());

        TimeManagementPanel timeManagementPanel = new TimeManagementPanel(view.getTopBar(), darkModeModel);

        TimeManagementController timeManagementListener = new TimeManagementController(manager, this, view, timeManagementPanel);

        SettingsController settingsController = new SettingsController(view, timeManagementPanel, darkModeModel);

        view.setTopbar(timePanel, timeManagementPanel);

        ButtonController buttonManager = new ButtonController(view, this, manager, layoutLoader, settingsController, pauseController);
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
    public void setScenario(int scenario) {
        this.scenario = scenario;
    }
}