package View.Systeem;

import Controller.Systeem.PauseController;
import Model.Personen.GastModel;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OverzichtView {

    public JFrame pauseFrame;
    private final JPanel content;
    private final HotelEventManager manager;

    public OverzichtView(HotelSimulatieView view, PauseController pauseController, HotelEventManager hotelEventManager) {
        this.manager = hotelEventManager;

        pauseFrame = new JFrame("Pauze");
        pauseFrame.setSize(1500, 800);
        pauseFrame.setLocationRelativeTo(view);
        pauseFrame.setAlwaysOnTop(true);

        pauseFrame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Gasten in het hotel:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pauseFrame.add(titleLabel, BorderLayout.NORTH);

        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.putClientProperty("noTheme", true);

        JScrollPane scrollPane = new JScrollPane(content);
        pauseFrame.add(scrollPane, BorderLayout.CENTER);

        JButton resumeButton = new JButton("Hervat simulatie");
        resumeButton.addActionListener(_ -> pauseController.resume());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(resumeButton);

        pauseFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        pauseFrame.addWindowListener(new java.awt.event.WindowAdapter() { // bij window close
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                manager.pauze();   // pauzeer
                pauseFrame.dispose(); // frame sluiten
            }
        });

        pauseFrame.add(bottomPanel, BorderLayout.SOUTH);

        pauseFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public void show() {
        pauseFrame.setVisible(true);
        pauseFrame.toFront();
    }

    public void hide() {
        pauseFrame.setVisible(false);
    }

    public void tekenGastLijst(ArrayList<GastModel> gasten){
        content.removeAll();

        for (GastModel gast: gasten) {
            JLabel label = new JLabel(
                    "Gast: " + gast.getGastID() +
                            " | Locatie: " + gast.getLocatie() +
                            " | Target Locatie: " + gast.getTargetLocatie() +
                            " | Kamer Wensen: " + gast.getWensen() +
                            " | Kamer: " + gast.getKamer()
            );

            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(label);
        }

        content.revalidate();
        content.repaint();
    }
}