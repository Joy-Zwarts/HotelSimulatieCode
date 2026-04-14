package Controller.Layout;

import Controller.Systeem.PauseController;
import Model.Layout.GridVakjeModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RuimteModel;
import View.Layout.GridVakjeView;
import View.Systeem.HotelSimulatieView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GridVakjeController {

    // attributen

    private final GridVakjeModel model;
    private final GridVakjeView gridView;
    private final PauseController pauseController;

    public GridVakjeController(GridVakjeModel model,
                               GridVakjeView view,
                               PauseController pauseController,
                               HotelSimulatieView SimulatieView) {

        this.model = model;
        this.gridView = view;
        this.pauseController = pauseController;

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

            if (ruimte != null && KamerType.LOBBY.equals(ruimte.getAreaType())) {
                System.out.println("pause toggle");
                SwingUtilities.invokeLater(pauseController::pause);
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