package View;

import Controller.HotelEventManager;
import Model.HotelEvent;
import Model.HotelEventListener;
import Model.HotelEventType;

import javax.swing.*;
import java.awt.*;

public class EventPrint implements HotelEventListener {
    private JPanel panel;         // contains event labels
    private JPanel panelRechts;   // right-side panel
    private JScrollPane scrollbar;
    private JLabel timeLabel;
    private HotelEventManager manager;
    private int seconds;
    private int minutes;

    String timeLabelInhoud;

    public EventPrint(HotelEventManager manager) {
        this.manager = manager;

        // panel voor events
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // scroll bar voor events
        scrollbar = new JScrollPane(panel);
        scrollbar.setPreferredSize(new Dimension(525, 500)); // width + height
        scrollbar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // panel for tijd
        JPanel panelTime = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeLabel = new JLabel("Time: 00:00");
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);
        timeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton pause = new JButton(laadIcon("pause.png"));
        pause.setPreferredSize(new Dimension(25, 25));
        pause.setBackground(Color.WHITE);
        pause.setBorder(BorderFactory.createEmptyBorder(0,35,0,35));
        pause.addActionListener(e -> {
            manager.pause();
        });

        JButton normaleTijd = new JButton(laadIcon("play.png"));
        normaleTijd.setPreferredSize(new Dimension(25, 25));
        normaleTijd.setBackground(Color.WHITE);
        normaleTijd.setBorder(BorderFactory.createEmptyBorder(0,35,0,35));
        normaleTijd.addActionListener(e -> {
            manager.setHte(1000);
        });

        JButton fastForwardTijd = new JButton(laadIcon("fastForward.png"));
        fastForwardTijd.setPreferredSize(new Dimension(32, 25));
        fastForwardTijd.setBackground(Color.WHITE);
        fastForwardTijd.setBorder(BorderFactory.createEmptyBorder(0,35,0,35));
        fastForwardTijd.addActionListener(e -> {
            manager.setHte(600);
        });

        JButton doubleFastForwardTijd = new JButton(laadIcon("doubleFastForward.png"));
        doubleFastForwardTijd.setPreferredSize(new Dimension(37, 25));
        doubleFastForwardTijd.setBackground(Color.WHITE);
        doubleFastForwardTijd.setBorder(BorderFactory.createEmptyBorder(0,35,0,35));
        doubleFastForwardTijd.addActionListener(e -> {
            manager.setHte(250);
        });

        panelTime.add(timeLabel);
        panelTime.add(pause);
        panelTime.add(normaleTijd);
        panelTime.add(fastForwardTijd);
        panelTime.add(doubleFastForwardTijd);
        panelTime.setPreferredSize(new Dimension(525, 50));

        // hele panel rechts
        panelRechts = new JPanel();
        panelRechts.setLayout(new BorderLayout());
        panelRechts.add(panelTime, BorderLayout.NORTH);
        panelRechts.add(scrollbar, BorderLayout.CENTER);

        // register als listener
        manager.registerListener(this);
    }

    public JPanel getPanelRechts() {
        return panelRechts;
    }

    @Override
    public void notify(HotelEvent hotelEvent) {
        printEvent(hotelEvent);
    }

    public void printEvent(HotelEvent hotelEvent) {
        if (hotelEvent.getHotelEventType() != HotelEventType.NONE) {
            JLabel label = new JLabel(
                    "Time: " + hotelEvent.getTime() +
                            " | Type: " + hotelEvent.getHotelEventType() +
                            " | GuestID: " + hotelEvent.getGuestID() +
                            " | Data: " + hotelEvent.getData()
            );
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(label);

            // update UI
            panel.revalidate();
            panel.repaint();

            // scroll to bottom
            JScrollBar vertical = scrollbar.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        }

        // update tijd per tick
        long totalSeconds = hotelEvent.getTime() / 1000; // milliseconden → seconden
        int minutes = (int) (totalSeconds / 60);
        int seconds = (int) (totalSeconds % 60);
        timeLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
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