package View.Systeem;

import Controller.Systeem.PauseController;
import Controller.PersoonManagement.SchoonmakerController;
import Model.Personen.SchoonmakerModel;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Queue;

public class OverzichtView {

    private final JFrame pauseFrame;
    private final JPanel guestContent;
    private final JPanel roomContent;
    private final JPanel cleanerContent;
    private final HotelEventManager manager;
    private boolean gepauzeerd = false;

    public OverzichtView(HotelSimulatieView view, PauseController pauseController, HotelEventManager hotelEventManager) {
        this.manager = hotelEventManager;

        pauseFrame = new JFrame("Overzicht");
        pauseFrame.setSize(1600, 800);
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

        cleanerContent = new JPanel();
        cleanerContent.setLayout(new BoxLayout(cleanerContent, BoxLayout.Y_AXIS));
        JScrollPane cleanerScroll = new JScrollPane(cleanerContent);
        cleanerScroll.setBorder(BorderFactory.createTitledBorder("Schoonmakers & Wachtrijen"));

        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        mainPanel.add(guestScroll);
        mainPanel.add(roomScroll);
        mainPanel.add(cleanerScroll);

        pauseFrame.add(mainPanel, BorderLayout.CENTER);

        JButton resumeButton = new JButton("Hervat simulatie");
        resumeButton.addActionListener(_ -> {
            this.gepauzeerd = false;
            pauseController.resume();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(resumeButton);

        pauseFrame.add(bottomPanel, BorderLayout.SOUTH);
        pauseFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        pauseFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                gepauzeerd = false;
                manager.pauze();
                pauseFrame.dispose();
            }
        });
    }

    public void show() {
        this.gepauzeerd = true;
        pauseFrame.setVisible(true);
        pauseFrame.toFront();
    }

    public void hide() {
        this.gepauzeerd = false;
        pauseFrame.setVisible(false);
    }

    public boolean isGepauzeerd() {
        return this.gepauzeerd;
    }

    public void tekenSchoonmakerStatus(SchoonmakerController controller) {
        cleanerContent.removeAll();

        if (controller == null || controller.getSchoonmaker1() == null) {
            cleanerContent.add(new JLabel("Schoonmakers niet geïnitialiseerd."));
        } else {
            renderSchoonmakerInView(controller.getSchoonmaker1(), controller);
            cleanerContent.add(Box.createVerticalStrut(20));
            renderSchoonmakerInView(controller.getSchoonmaker2(), controller);
        }

        cleanerContent.revalidate();
        cleanerContent.repaint();
    }

    private void renderSchoonmakerInView(SchoonmakerModel sm, SchoonmakerController controller) {
        int id = sm.getID();
        String zone = (id == 1) ? "Bovenverdiepingen" : "Benedenverdiepingen";

        JLabel nameLabel = new JLabel("Schoonmaker " + id + " (" + zone + ")");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cleanerContent.add(nameLabel);

        KamerModel actieveKlus = controller.getActieveKlus(id);
        String statusText = "Status: ";
        if (sm.isCleaning() && actieveKlus != null) {
            statusText += "Aan het schoonmaken in Kamer " + actieveKlus.getRoomNumber() +
                    " (" + sm.getHuidigeSchoonmaakTijd() + "/" + sm.getSchoonmaakTijd() + " ticks)";
        } else if (actieveKlus != null) {
            statusText += "Onderweg naar Kamer " + actieveKlus.getRoomNumber();
        } else {
            statusText += "Is aan het wachten op een nieuwe taak";
        }
        cleanerContent.add(new JLabel(statusText));
        cleanerContent.add(new JLabel("Locatie nu: " + sm.getLocatie()));

        cleanerContent.add(new JLabel("Wachtrij taken:"));
        Queue<KamerModel> q = controller.getWachtrij(id);
        if (q == null || q.isEmpty()) {
            cleanerContent.add(new JLabel("  - Geen opdrachten in wachtrij"));
        } else {
            int teller = 1;
            for (KamerModel km : q) {
                cleanerContent.add(new JLabel("  " + teller + ". Kamer " + km.getRoomNumber() + " (Wacht op beurt)"));
                teller++;
            }
        }
    }

    public void tekenGastLijst(HashMap<Integer, GastModel> gasten) {
        guestContent.removeAll();
        if (gasten == null || gasten.isEmpty()) {
            guestContent.add(new JLabel("Geen gasten"));
        } else {
            for (GastModel gast : gasten.values()) {
                KamerModel kamer = gast.getKamer();
                String kamerInfo = (kamer == null) ? "null" : String.valueOf(kamer.getRoomNumber());
                guestContent.add(new JLabel(
                        "Gast " + gast.getID() +
                                "   |   Locatie nu: " + gast.getLocatie() +
                                "   |   Kamer: " + kamerInfo
                ));
            }
        }
        guestContent.revalidate();
        guestContent.repaint();
    }

    public void tekenKamerLijst(HashMap<Integer, KamerModel> kamers) {
        roomContent.removeAll();
        if (kamers == null || kamers.isEmpty()) {
            roomContent.add(new JLabel("Geen kamers"));
        } else {
            for (KamerModel kamer : kamers.values()) {
                String verblijvende = (kamer.getVerblijvende() == null) ? "null" : String.valueOf(kamer.getVerblijvende().getID());
                roomContent.add(new JLabel(
                        "Kamernummer:  " + kamer.getRoomNumber() +
                                "    |    Verblijvende :  gast " + verblijvende
                ));
            }
        }
        roomContent.revalidate();
        roomContent.repaint();
    }
}