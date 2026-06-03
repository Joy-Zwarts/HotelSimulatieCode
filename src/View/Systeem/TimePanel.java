package View.Systeem;

import Controller.Events.Interfaces.noneEvent;
import Controller.SimulatieController; // Zorg dat deze import klopt
import hotelevents.HotelEvent;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.*;

public class TimePanel implements noneEvent {

    private final JLabel timeLabel;
    private final HotelSimulatieView view;
    private final SimulatieController controller;

    // constructor
    public TimePanel(HotelEventManager manager, JPanel panelRechts, HotelSimulatieView hotelSimulatieView, SimulatieController controller) {
        this.view = hotelSimulatieView;
        this.controller = controller; // <-- Opslaan

        // panel for tijd
        JPanel panelTime = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeLabel = new JLabel("00:00");
        timeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 35));
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);
        timeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        panelTime.add(timeLabel);
        panelRechts.add(panelTime);
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    @Override
    public void HTETick(HotelEvent event) {
        long totalSeconds = event.getTime();
        int minutes = (int) (totalSeconds / 60);
        int seconds = (int) (totalSeconds % 60);

        SwingUtilities.invokeLater(() -> {
            timeLabel.setText(String.format("%02d:%02d", minutes, seconds));

            // vraag de eindtijd op aan de controller
            int maxHte = controller.getMaxHteVoorScenario();

            if (totalSeconds >= maxHte) {
                view.toonEindScherm();
            }
        });
    }
}