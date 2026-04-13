package Controller;

import Model.GridVakjeModel;
import Model.RuimteModel;
import View.GridVakjeView;
import View.HotelSimulatieView;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GridVakjeController {

    // attributen

    private GridVakjeModel model;
    private GridVakjeView gridView;
    private HotelEventManager manager;
    private PauseController pauseController;
    private HotelSimulatieView simulatieView;

    public GridVakjeController(GridVakjeModel model,
                               GridVakjeView view,
                               HotelEventManager manager,
                               PauseController pauseController,
                               HotelSimulatieView SimulatieView) {

        this.model = model;
        this.gridView = view;
        this.manager = manager;
        this.pauseController = pauseController;
        this.simulatieView = SimulatieView;

        init();
    }

    // als er op het vakje wordt geklikt roep handle click aan
    private void init() {
        gridView.getVakjePanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick();
            }
        });
    }

    // als het geklikte vakje de lobby bevat, pauzeer de simulatie
    private void handleClick() {
        RuimteModel ruimte = model.getRuimte();

        if (pauseController.isPaused()) {
            JOptionPane.showMessageDialog(simulatieView, "Hervat de simulatie eerst", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (ruimte != null && "Lobby".equals(ruimte.getAreaType())) {
                System.out.println("pause toggle");
                SwingUtilities.invokeLater(() -> {
                    pauseController.pause();
                });
            }
        }

        System.out.println("Geklikt op vakje: " + ruimte);
    }

    // getters & setters

    public GridVakjeModel getModel() {
        return  model;
    }

    public GridVakjeView getGridView() {
        return gridView;
    }

    public void updateView() {
        gridView.getVakjePanel().repaint();
    }
}