package View.Systeem;

import Controller.Events.Interfaces.noneEvent;
import Controller.Systeem.PauseController;
import Controller.PersoonManagement.SchoonmakerController;
import Model.Personen.SchoonmakerModel;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerModel;
import hotelevents.HotelEvent;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Queue;

public class OverzichtView implements noneEvent {

    private final JFrame pauseFrame;
    private final JPanel guestContent;
    private final JPanel roomContent;
    private final JPanel cleanerContent;
    private final HotelEventManager manager;
    private boolean gepauzeerd = false;
    private HashMap<Integer, GastModel> gastenLijst;
    private HashMap<Integer, KamerModel> kamerLijst;
    private SchoonmakerController schoonmakerLijst;

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

    // setter om je vanuit de simulatie controller de nodige data door te geven
    public void verbindDataBronnen(HashMap<Integer, GastModel> gasten, HashMap<Integer, KamerModel> kamers, SchoonmakerController schoonmakerController) {
        this.gastenLijst = gasten;
        this.kamerLijst = kamers;
        this.schoonmakerLijst = schoonmakerController;
    }

    public void show() {
        this.gepauzeerd = true;
        updateData(gastenLijst, kamerLijst, schoonmakerLijst);
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

    // kijk of we de data hebben voor schoonmakers, zo ja, roep dan de render functie aan
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

    // zet de schoonmakers in een lijst met hun data erbij (status, locatie & wachtrij aan taken)
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

    // zet gasten in een lijst met hun data erbij (locatie, kamer & status)
    public void tekenGastLijst(HashMap<Integer, GastModel> gasten) {
        guestContent.removeAll();
        if (gasten == null || gasten.isEmpty()) {
            guestContent.add(new JLabel("Geen gasten"));
        } else {
            for (GastModel gast : gasten.values()) {
                KamerModel kamer = gast.getKamer();

                String kamerInfo;
                if (kamer == null) {
                    kamerInfo = "Geen";
                } else {
                    kamerInfo = String.valueOf(kamer.getRoomNumber());
                }

                String statusText;

                if (gast.getActivity() == null) { // failsafe
                    statusText = "Status niet gevonden";
                } else {
                    switch (gast.getActivity()) {
                        case ETEN ->  statusText = "Aan het Eten";
                        case SPORTEN ->  statusText = "Aan het Sporten";
                        case FILM ->   statusText = "In de Bioscoop";
                        case IN_KAMER ->   statusText = "In hun Kamer";
                        default ->  statusText = "Onderweg";
                    }
                }

                guestContent.add(new JLabel(
                        "Gast " + gast.getID() +
                                "   |   Locatie nu: " + gast.getLocatie() +
                                "   |   Kamer: " + kamerInfo +
                                "   |   Status: " + statusText
                ));
            }
        }
        guestContent.revalidate();
        guestContent.repaint();
    }

    // zet kamers in een lijst met hun data erbij (kamernummer en verblijvende gast)
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

    // methode om alle drie de kolommen te tekenen met nieuwe data
    public void updateData(HashMap<Integer, GastModel> gasten, HashMap<Integer, KamerModel> kamers, SchoonmakerController schoonmakerController) {
        if (!gepauzeerd) return;

        tekenGastLijst(gasten);
        tekenKamerLijst(kamers);
        tekenSchoonmakerStatus(schoonmakerController);
    }

    // per tick update je de data
    @Override
    public void HTETick(HotelEvent event) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            updateData(gastenLijst, kamerLijst, schoonmakerLijst);
        });
    }
}