package Controller.Systeem;

import Controller.SimulatieController;
import View.Systeem.HotelSimulatieView;
import View.Systeem.TimeManagementPanel;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimeManagementController implements ActionListener {

    // attributen

    private final HotelEventManager manager;
    private final HotelSimulatieView view;
    private final SimulatieController controller;

    private final JButton normal;
    private final JButton fastForward;
    private final JButton doubleFastForward;

    // constructor
    public TimeManagementController(HotelEventManager Manager, SimulatieController Controller, HotelSimulatieView View, TimeManagementPanel Panel) {
        this.manager = Manager;
        this.controller = Controller;
        this.view = View;

        // initialiseer de time-management buttons en registreert zichzelf als listener
        this.normal = Panel.getNormaleTijd();
        normal.addActionListener(this);
        this.fastForward = Panel.getFastForwardTijd();
        fastForward.addActionListener(this);
        this.doubleFastForward = Panel.getDoubleFastForwardTijd();
        doubleFastForward.addActionListener(this);
    }

    // op klik van een button
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); // welke button komt de action event van

        // normale tijd knop
        if (source == normal) {
            if (controller.getStarted()) { // als de simulatie is gestart
                manager.setHte(1000); // zet speed op 1 tick per sec
            } else {
                JOptionPane.showMessageDialog(view, "De simulatie is nog niet gestart!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // fast-forward knop
        else if (source == fastForward) {
            if (controller.getStarted()) { // als de simulatie is gestart
                manager.setHte(600); // zet speed op 1 tick per 0.6 sec
            } else {
                JOptionPane.showMessageDialog(view,
                        "De simulatie is nog niet gestart!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        // double fast-forward knop
        else if (source == doubleFastForward) {
            if (controller.getStarted()) { // als de simulatie is gestart
                manager.setHte(250); // zet speed op 1 tick per 0.25 sec
            } else {
                JOptionPane.showMessageDialog(view,
                        "De simulatie is nog niet gestart!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
