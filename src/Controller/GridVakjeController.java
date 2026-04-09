package Controller;

import Model.GridVakjeModel;
import Model.RuimteModel;
import View.GridVakjeView;
import View.OverzichtView;
import hotelevents.HotelEventManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GridVakjeController {

    // attributen

    private GridVakjeModel model;
    private GridVakjeView view;
    private HotelEventManager manager;

    // constructor
    public GridVakjeController(GridVakjeModel model, GridVakjeView view, HotelEventManager manager) {
        this.model = model;
        this.view = view;
        this.manager = manager;
        OverzichtView overzichtView = new OverzichtView();

        init();
    }

    // als er op het vakje wordt geklikt roep handle click aan
    private void init() {
        view.getVakjePanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick();
            }
        });
    }

    // als het geklikte vakje de lobby bevat, pauzeer de simulatie
    private void handleClick() {
        RuimteModel ruimte = model.getRuimte();

        if (ruimte != null && ruimte.getAreaType().equals("Lobby")) {
            System.out.println("pause");

            manager.pauze();

            boolean paused = !model.isPaused();
            model.setPaused(paused);

            OverzichtView.setVisibility(paused); // laat het overzicht scherm zien
        }

        System.out.println("Geklikt op vakje: " + ruimte);
    }

    // getters & setters

    public GridVakjeModel getModel() {
        return  model;
    }

    public GridVakjeView getView() {
        return view;
    }

    public void updateView() {
        view.getVakjePanel().repaint();
    }
}