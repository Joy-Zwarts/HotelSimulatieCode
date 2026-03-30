package View;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class OpstartScherm {
    private File gekozenFile;

    // callback interface zodat Main weet wanneer een bestand gekozen is
    public interface FileChosenListener {
        void onFileChosen(File file);
    }

    public OpstartScherm(FileChosenListener listener) {
        JFrame frame = new JFrame("Bestand Uploaden");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLayout(new BorderLayout(10, 10)); // marges tussen componenten

        // header image
        ImageIcon headerImage = new ImageIcon(getClass().getResource("/Res/HotelHeader.png"));
        JLabel header = new JLabel(headerImage);
        header.setPreferredSize(new Dimension(250, 150));
        header.setHorizontalAlignment(JLabel.CENTER);
        frame.add(header, BorderLayout.NORTH);

        // text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Welkom bij de Sjohn Karma HotelSimulatie :)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel = new JLabel("Selecteer een layout om te simuleren");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // voeg labels toe
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(descriptionLabel);
        textPanel.add(Box.createVerticalStrut(20));

        frame.add(textPanel, BorderLayout.CENTER);

        // upload button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton uploadButton = new JButton("Kies een bestand");
        uploadButton.setFont(new Font("Arial", Font.PLAIN, 16));
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(uploadButton);
        buttonPanel.add(Box.createVerticalStrut(20));

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // action listener
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int resultaat = fileChooser.showOpenDialog(frame);

            if (resultaat == JFileChooser.APPROVE_OPTION) {
                File gekozenBestand = fileChooser.getSelectedFile();
                gekozenFile = gekozenBestand;

                System.out.println("Bestand gekozen: " + gekozenBestand.getName());

                File doelMap = new File("resources/uploads");
                if (!doelMap.exists()) doelMap.mkdirs();

                File doelBestand = new File(doelMap, gekozenBestand.getName());
                try (InputStream in = new FileInputStream(gekozenBestand);
                     OutputStream out = new FileOutputStream(doelBestand)) {

                    byte[] buffer = new byte[1024];
                    int lengte;
                    while ((lengte = in.read(buffer)) > 0) {
                        out.write(buffer, 0, lengte);
                    }
                    System.out.println("Bestand succesvol toegevoegd aan projectmap: " + doelBestand.getAbsolutePath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                listener.onFileChosen(gekozenBestand);
                frame.dispose();
            } else {
                System.out.println("Geen bestand gekozen.");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public File getGekozenFile() {
        return gekozenFile;
    }
}