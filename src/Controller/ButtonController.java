package Controller;

import Model.LayoutModel;
import View.EventPanel;
import View.HotelSimulatieView;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonController implements ActionListener {

    //attributen

    private final HotelSimulatieView view;
    private final SimulatieController simulatieManager;
    private final HotelEventManager eventManager;
    private final LayoutLoader layoutLoader;
    private LayoutModel model;
    private final SettingsController settingsController;
    private final PauseController pauseController;

    // constructor

    public ButtonController(HotelSimulatieView hotelView, SimulatieController hotelSimulatieManager, HotelEventManager hotelEventManager, LayoutLoader layoutLoader, SettingsController SettingsController, PauseController PauseController) {
        this.simulatieManager = hotelSimulatieManager;
        this.eventManager = hotelEventManager;
        this.view = hotelView;
        this.layoutLoader = layoutLoader;
        this.settingsController = SettingsController;
        this.pauseController = PauseController;
        init();
    }

    // registreert zichtzelf als listener bij alle buttons van de view
    private void init() {
        view.getLoadScenarioButton().addActionListener(this);
        view.getStartSimulationButton().addActionListener(this);
        view.getStopSimulationButton().addActionListener(this);
        view.getLoadLayoutButton().addActionListener(this);
        view.getSettingsButton().addActionListener(this);
    }

    // reactie op een button klik, kijkt van welke button het komt en reageert accordingly
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); // welke button komt de action event van


        // load scenario button
        if (source == view.getLoadScenarioButton()) {
            ScenarioPicker scenarioPicker = new ScenarioPicker(); // open het scenario picker venster
            System.out.println("Selected item: " + scenarioPicker.getSelected());
            simulatieManager.setScenario(scenarioPicker.getSelected()); // geef het gekozen scenario door aan de simulatie manager
        }

        // load layout button
        else if (source == view.getLoadLayoutButton()) {
            model = layoutLoader.loadLayout(); // maakt een nieuwe layout model aan met de functie load layout
        }

        // start simulation button
        else if (source == view.getStartSimulationButton()) {
            if (model == null) { // als er nog geen layout is geupload
                JOptionPane.showMessageDialog(view, "Load eerst een layout bestand", "Error", JOptionPane.ERROR_MESSAGE);

            } else if (!(simulatieManager.getStarted())) { // als het niet al is gestart
                EventPanel eventPanel = new EventPanel(eventManager);
                view.setRightView(eventPanel.getPanelRechts());
                eventManager.start(simulatieManager.getScenario()); // start het gekozen scenario
                simulatieManager.setStarted(true);

            } else if (simulatieManager.getStarted()) { // als het al is gestart
                JOptionPane.showMessageDialog(view, "De simulatie is al gestart!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // settings button
        else if (source == view.getSettingsButton()) {
            settingsController.getSettingsFrame().getFrame().setVisible(true); // open het settings frame
        }

        // stop simulation button
        else if (source == view.getStopSimulationButton()) {
            if (simulatieManager.getStarted()) { // als de simulatie wel al is gestart
                eventManager.stop();// stop simulatie
                simulatieManager.setStarted(false);
            } else if (!(simulatieManager.getStarted())) { // als de simulatie niet al is gestart
                JOptionPane.showMessageDialog(view, "De simulatie is nog niet gestart!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
