package View.Systeem;

import Controller.Events.Interfaces.noneEvent;
import Controller.SimulatieController;
import View.JoyOpdracht.NewDag;
import hotelevents.HotelEvent;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TimePanel implements noneEvent {

    private final JLabel timeLabel;
    private final HotelSimulatieView view;
    private final SimulatieController controller;
    private static final int ticksPerDag = 500;
    private ArrayList<NewDag> listeners;
    private int huidigeDag = 1;

    // constructor
    public TimePanel(HotelEventManager manager, JPanel panelRechts, HotelSimulatieView hotelSimulatieView, SimulatieController controller) {
        this.view = hotelSimulatieView;
        this.controller = controller;

        // panel voor tijd
        JPanel panelTime = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeLabel = new JLabel("Dag 1 - 00:00");
        timeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30)); // Iets kleiner font omdat de tekst langer is
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);
        timeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        panelTime.add(timeLabel);
        panelRechts.add(panelTime);

        this.listeners = new ArrayList<>();
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    @Override
    public void HTETick(HotelEvent event) {
        long totalTicks = event.getTime();

        // bereken de huidige dag
        int dag = (int) (totalTicks / ticksPerDag) + 1;

        if (dag > huidigeDag) {
            huidigeDag = dag;
            for (NewDag listener: listeners) {
                listener.dagVoorbij(dag);
            }
        }

        // bereken hoeveel ticks er binnen de huidige dag zijn verstreken
        int ticksVandaag = (int) (totalTicks % ticksPerDag);

        // reken de ticks van vandaag om naar 24 uur en 60 minuten
        double fractieVanDag = (double) ticksVandaag / ticksPerDag;

        // totaal aantal minuten in een dag
        int totaleMinutenVandaag = (int) (fractieVanDag * 1440);

        int uren = totaleMinutenVandaag / 60;
        int minuten = totaleMinutenVandaag % 60;

        SwingUtilities.invokeLater(() -> {
            timeLabel.setText(String.format("Dag %d - %02d:%02d", dag, uren, minuten));

            // vraag de eindtijd op aan de controller
            int maxHte = controller.getMaxHteVoorScenario();

            if (totalTicks >= maxHte) {
                view.toonEindScherm();
            }
        });
    }

    public void setListeners(NewDag listener) {
        this.listeners.add(listener);
    }
}