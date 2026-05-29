package Controller.Systeem;

import Controller.*;
import Controller.Layout.LayoutLoader;
import Controller.Systeem.Intefaces.reset;
import Model.Layout.LayoutModel;
import View.Systeem.EventPanel;
import View.Systeem.HotelSimulatieView;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ButtonController implements ActionListener {

    //attributen

    private final HotelSimulatieView view;
    private final SimulatieController simulatieManager;
    private final HotelEventManager eventManager;
    private final LayoutLoader layoutLoader;
    private LayoutModel model;
    private final SettingsController settingsController;
    private final ArrayList<reset> listeners;

    // constructor

    public ButtonController(HotelSimulatieView hotelView, SimulatieController hotelSimulatieManager, HotelEventManager hotelEventManager, LayoutLoader layoutLoader, SettingsController SettingsController) {
        this.simulatieManager = hotelSimulatieManager;
        this.eventManager = hotelEventManager;
        this.view = hotelView;
        this.layoutLoader = layoutLoader;
        this.settingsController = SettingsController;
        this.listeners = new ArrayList<>();
        init();
    }

    // registreert zichzelf als listener bij alle buttons van de view
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
            // Vervang 'new ScenarioPicker()' door een methode-aanroep
            int scenarioNummer = pickScenario(view);
            System.out.println("Selected item: " + scenarioNummer);
            simulatieManager.setScenario(scenarioNummer);
        }

        // load layout button
        else if (source == view.getLoadLayoutButton()) {
            model = layoutLoader.loadLayout(); // maakt een nieuwe layout model aan met de functie load layout
        }

        // start simulation button
        else if (source == view.getStartSimulationButton()) {
            if (model == null) { // als er nog geen layout is geüpload
                JOptionPane.showMessageDialog(view, "Load eerst een layout bestand", "Error", JOptionPane.ERROR_MESSAGE);

            } else if (!(simulatieManager.getStarted())) { // als het niet al is gestart
                EventPanel eventPanel = new EventPanel(eventManager);
                view.setRightView(eventPanel.getContainer());
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
                for (reset listener : listeners) {
                    listener.resetSimulatie();
                }
                simulatieManager.setStarted(false);
            } else if (!(simulatieManager.getStarted())) { // als de simulatie niet al is gestart
                JOptionPane.showMessageDialog(view, "De simulatie is nog niet gestart!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    protected int pickScenario(HotelSimulatieView view) {
        ScenarioPicker scenarioPicker = new ScenarioPicker(view);
        return scenarioPicker.getSelected(); // getSelected() geeft een String terug
    }

    public void setListeners(reset listener) {
        this.listeners.add(listener);
    }
}
