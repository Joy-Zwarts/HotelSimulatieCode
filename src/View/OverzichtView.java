package View;

import Controller.PauseController;
import Model.GastModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OverzichtView {

    public JFrame pauseFrame;
    private final JPanel content;

    public OverzichtView(HotelSimulatieView view, PauseController pauseController) {

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
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseController.resume();
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(resumeButton);

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