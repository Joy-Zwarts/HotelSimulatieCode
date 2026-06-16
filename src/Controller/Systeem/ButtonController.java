package Controller.Systeem;

import Controller.*;
import Controller.Layout.LayoutLoader;
import Controller.Systeem.Interfaces.reset;
import Model.Layout.LayoutModel;
import View.Systeem.EventPanel;
import View.Systeem.HotelSimulatieView;
import hotelevents.HotelEvent;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;

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
        view.getBrandalarmButton().addActionListener(this);
    }

    // reactie op een button klik, kijkt van welke button het komt en reageert accordingly
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); // welke button komt de action event van

        // load scenario button
        if (source == view.getLoadScenarioButton()) {
            new ScenarioPicker(view, simulatieManager);
        }

        // load layout button
        else if (source == view.getLoadLayoutButton()) {
            model = layoutLoader.loadLayout();
        }

        // start simulation button
        else if (source == view.getStartSimulationButton()) {
            if (model == null) {
                JOptionPane.showMessageDialog(view, "Load eerst een layout bestand", "Error", JOptionPane.ERROR_MESSAGE);

            } else if (!(simulatieManager.getStarted())) {
                EventPanel eventPanel = new EventPanel(eventManager);
                view.setRightView(eventPanel.getContainer());

                // Start het gekozen scenario dat zojuist door de ScenarioPicker is gezet!
                eventManager.start(simulatieManager.getScenario());
                simulatieManager.setStarted(true);

            } else if (simulatieManager.getStarted()) {
                JOptionPane.showMessageDialog(view, "De simulatie is al gestart!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // settings button
        else if (source == view.getSettingsButton()) {
            settingsController.getSettingsFrame().getFrame().setVisible(true);
        }

        // stop simulation button
        else if (source == view.getStopSimulationButton()) {
            if (simulatieManager.getStarted()) {
                eventManager.stop();
                for (reset listener : listeners) {
                    listener.resetSimulatie();
                }
                simulatieManager.setStarted(false);
            } else if (!(simulatieManager.getStarted())) {
                JOptionPane.showMessageDialog(view, "De simulatie is nog niet gestart!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        //Brandalarm button
        if (source == view.getBrandalarmButton()) {
            HotelEvent Evacuate = new HotelEvent(0, HotelEventType.EVACUATE, -1, -1);
        }

    }

    public void setListeners(reset listener) {
        this.listeners.add(listener);
    }
}