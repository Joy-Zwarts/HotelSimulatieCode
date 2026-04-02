package View;

import hotelevents.HotelEvent;
import hotelevents.HotelEventListener;
import hotelevents.HotelEventManager;
import hotelevents.HotelEventType;

import javax.swing.*;
import java.awt.*;

public class TimePanel implements HotelEventListener {
    private JPanel panelTime;         // contains event labels
    private JLabel timeLabel;
    private HotelEventManager manager;

    public TimePanel(HotelEventManager manager, JPanel panelRechts) {
        this.manager = manager;


        // panel for tijd
        panelTime = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeLabel = new JLabel("Time: 00:00");
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);
        timeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton normaleTijd = new JButton(laadIcon("play.png"));
        normaleTijd.setPreferredSize(new Dimension(25, 25));
        normaleTijd.setBackground(Color.WHITE);
        normaleTijd.setBorder(BorderFactory.createEmptyBorder(0, 35, 0, 35));
        normaleTijd.addActionListener(e -> {
            manager.setHte(1000);
        });

        JButton fastForwardTijd = new JButton(laadIcon("fastForward.png"));
        fastForwardTijd.setPreferredSize(new Dimension(32, 25));
        fastForwardTijd.setBackground(Color.WHITE);
        fastForwardTijd.setBorder(BorderFactory.createEmptyBorder(0, 35, 0, 35));
        fastForwardTijd.addActionListener(e -> {
            manager.setHte(600);
        });

        JButton doubleFastForwardTijd = new JButton(laadIcon("doubleFastForward.png"));
        doubleFastForwardTijd.setPreferredSize(new Dimension(37, 25));
        doubleFastForwardTijd.setBackground(Color.WHITE);
        doubleFastForwardTijd.setBorder(BorderFactory.createEmptyBorder(0, 35, 0, 35));
        doubleFastForwardTijd.addActionListener(e -> {
            manager.setHte(250);
        });

        panelTime.add(timeLabel);
        panelTime.add(normaleTijd);
        panelTime.add(fastForwardTijd);
        panelTime.add(doubleFastForwardTijd);
        panelTime.setPreferredSize(new Dimension(525, 50));

        // hele panel rechts
        panelRechts.add(panelTime);

        // register als listener
        manager.register(this);
    }

    @Override
    public void notify(HotelEvent event) {
        long totalSeconds = event.getTime();
        int minutes = (int) (totalSeconds / 60);
        int seconds = (int) (totalSeconds % 60);

        timeLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    public JPanel getTimePanel() {
        return panelTime;
    }

    public void resetTime(){
        timeLabel.setText("Time: 00:00");

    }

private ImageIcon laadIcon(String bestand) {
    java.net.URL url = getClass().getResource("/Res/" + bestand);

    if (url == null) {
        System.out.println("Niet gevonden: " + bestand);
        return null;
    }

    return new ImageIcon(url);
    }
}

