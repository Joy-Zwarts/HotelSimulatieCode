package View.Systeem;

import Controller.Systeem.PauseController;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class OverzichtView {
    // attributen

    private final JFrame pauseFrame;

    private final JPanel guestContent;
    private final JPanel roomContent;
    private final HotelEventManager manager;

    // constructor
    public OverzichtView(HotelSimulatieView view, PauseController pauseController, HotelEventManager hotelEventManager) {
        this.manager = hotelEventManager;

        pauseFrame = new JFrame("Overzicht");
        pauseFrame.setSize(1500, 800);
        pauseFrame.setLocationRelativeTo(view);
        pauseFrame.setAlwaysOnTop(true);
        pauseFrame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Overzicht Hotel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pauseFrame.add(titleLabel, BorderLayout.NORTH);

        guestContent = new JPanel();
        guestContent.setLayout(new BoxLayout(guestContent, BoxLayout.Y_AXIS));

        JScrollPane guestScroll = new JScrollPane(guestContent);
        guestScroll.setBorder(BorderFactory.createTitledBorder("Gasten"));

        roomContent = new JPanel();
        roomContent.setLayout(new BoxLayout(roomContent, BoxLayout.Y_AXIS));

        JScrollPane roomScroll = new JScrollPane(roomContent);
        roomScroll.setBorder(BorderFactory.createTitledBorder("Kamers"));

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                guestScroll,
                roomScroll
        );

        splitPane.setResizeWeight(0.5);
        pauseFrame.add(splitPane, BorderLayout.CENTER);

        JButton resumeButton = new JButton("Hervat simulatie");
        resumeButton.addActionListener(_ -> pauseController.resume());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(resumeButton);

        pauseFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // als je het frame sluit in plaats van op hervatten drukken
        pauseFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                manager.pauze();   // unpause
                pauseFrame.dispose(); // frame sluiten

            }
        });

        pauseFrame.add(bottomPanel, BorderLayout.SOUTH);

        pauseFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    // laat frame zien
    public void show() {
        pauseFrame.setVisible(true);
        pauseFrame.toFront();
    }

    // hide de frame
    public void hide() {
        pauseFrame.setVisible(false);
    }

    // herteken de gasten lijst (laat alle gasten in het hotel op dat moment en hun belangrijke data zien)
    public void tekenGastLijst(HashMap<Integer, GastModel> gasten) {

        guestContent.removeAll();

        if (gasten == null || gasten.isEmpty()) {
            guestContent.add(new JLabel("Geen gasten"));
        } else {
            for (GastModel gast : gasten.values()) {

                KamerModel kamer = gast.getKamer();

                String kamerInfo = (kamer == null)
                        ? "null"
                        : String.valueOf(kamer.getRoomNumber());

                guestContent.add(new JLabel(
                        "Gast " + gast.getGastID() +
                                "   |   Locatie nu: " + gast.getLocatie() +
                                "   |   Target Locatie: " + gast.getTargetLocatie() +
                                "   |   Kamer Wensen: " + gast.getWensen() +
                                "   |   Kamer: " + kamerInfo
                ));
            }
        }

        guestContent.revalidate();
        guestContent.repaint();
    }

    // herteken de kamer lijst (laat alle kamers in het hotel en hun belangrijke data zien)
    public void tekenKamerLijst(HashMap<Integer, KamerModel> kamers) {

        roomContent.removeAll();

        if (kamers == null || kamers.isEmpty()) {
            roomContent.add(new JLabel("Geen kamers"));
        } else {
            for (KamerModel kamer : kamers.values()) {

                String verblijvende =
                        (kamer.getVerblijvende() == null)
                                ? "null"
                                : String.valueOf(kamer.getVerblijvende().getGastID());

                roomContent.add(new JLabel(
                        "Kamernummer:  " + kamer.getRoomNumber() +
                                "    |    Aantal sterren: " + kamer.getClassification() +
                                "    |    Positie:  " + kamer.getPositionString() +
                                "    |    Grootte:  " + kamer.getDimension() +
                                "    |    Verblijvende :  gast " + verblijvende
                ));
            }
        }

        roomContent.revalidate();
        roomContent.repaint();
    }
}