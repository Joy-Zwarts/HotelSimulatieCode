package Controller;

import Model.DarkModeModel;
import Model.LayoutModel;
import View.*;
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

        OverzichtView overzichtView = new OverzichtView(view, pauseController);

        pauseController.setView(overzichtView);

        PersoonController persoonController = new PersoonController(manager, overzichtView);

        LayoutLoader layoutLoader = new LayoutLoader(manager, view, model, pauseController, view);

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