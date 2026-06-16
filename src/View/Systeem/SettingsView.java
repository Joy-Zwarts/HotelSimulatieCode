package View.Systeem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsView {

    // attributen
    private final JFrame settingsFrame;
    private final JButton darkModeButton;
    private final JButton factuurButton;
    private final JComboBox<String> schoonmaakTijd;
    private final JComboBox<String> filmDuur;
    private final JSlider restaurantCapaciteit;
    private final JSlider trapLoopDuur;
    private final JSlider gastMaxWachttijd;
    private final Color dark = new Color(30,31,31);

    // constructor
    public SettingsView(HotelSimulatieView view) {
        settingsFrame = new JFrame("Instellingen");
        settingsFrame.setSize(550, 550);
        settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        settingsFrame.setLayout(new BorderLayout());

        // titel
        JLabel titleLabel = new JLabel("Instellingen", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 15, 0));
        settingsFrame.add(titleLabel, BorderLayout.NORTH);

        JPanel tabelPaneel = new JPanel(new GridBagLayout());
        tabelPaneel.setBorder(new EmptyBorder(10, 25, 25, 25));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(12, 10, 12, 10);
        c.fill = GridBagConstraints.HORIZONTAL; // Zorg dat componenten netjes de breedte vullen

        // knoppen & dropdowns
        darkModeButton = new JButton("Toggle Dark Mode");
        darkModeButton.setBackground(dark);
        darkModeButton.setForeground(Color.WHITE);
        darkModeButton.setFont(new Font("Arial", Font.BOLD, 13));

        factuurButton = new JButton("Toggle gast Bonnen");
        factuurButton.setBackground(dark);
        factuurButton.setForeground(Color.WHITE);
        factuurButton.setFont(new Font("Arial", Font.BOLD, 13));

        schoonmaakTijd = new JComboBox<>();
        schoonmaakTijd.setBackground(dark);
        schoonmaakTijd.setForeground(Color.WHITE);
        schoonmaakTijd.addItem("Normaal");
        schoonmaakTijd.addItem("Lang");
        schoonmaakTijd.addItem("Kort");

        filmDuur = new JComboBox<>();
        filmDuur.setBackground(dark);
        filmDuur.setForeground(Color.WHITE);
        filmDuur.addItem("Normaal");
        filmDuur.addItem("Lang");
        filmDuur.addItem("Kort");

        // sliders
        Font sliderFont = new Font("Arial", Font.PLAIN, 11);

        restaurantCapaciteit = new JSlider(1, 10, 5);
        restaurantCapaciteit.setPaintTicks(true);
        restaurantCapaciteit.setMajorTickSpacing(1);
        restaurantCapaciteit.setPaintLabels(true);
        restaurantCapaciteit.setForeground(dark);
        restaurantCapaciteit.setFont(sliderFont);

        trapLoopDuur = new JSlider(1, 5, 1);
        trapLoopDuur.setPaintTicks(true);
        trapLoopDuur.setMajorTickSpacing(1);
        trapLoopDuur.setPaintLabels(true);
        trapLoopDuur.setForeground(dark);
        trapLoopDuur.setFont(sliderFont);

        gastMaxWachttijd = new JSlider(10, 100, 50);
        gastMaxWachttijd.setPaintTicks(true);
        gastMaxWachttijd.setMajorTickSpacing(10);
        gastMaxWachttijd.setPaintLabels(true);
        gastMaxWachttijd.setForeground(dark);
        gastMaxWachttijd.setFont(sliderFont);

        // weergave
        JLabel lbl1 = new JLabel("Dark / Light mode:"); lbl1.setFont(new Font("Arial", Font.BOLD, 13));
        c.gridx = 0; c.gridy = 0; c.weightx = 0.35; tabelPaneel.add(lbl1, c);
        c.gridx = 1; c.gridy = 0; c.weightx = 0.65; tabelPaneel.add(darkModeButton, c);

        //factuurbutton
        JLabel lbl2 = new JLabel("Show Gast Bonnen bij Checkout:"); lbl2.setFont(new Font("Arial", Font.BOLD, 13));
        c.gridx = 0; c.gridy = 1; c.weightx = 0.35; tabelPaneel.add(lbl2, c);
        c.gridx = 1; c.gridy = 1; c.weightx = 0.65; tabelPaneel.add(factuurButton, c);

        // schoonmaaktijd
        JLabel lbl3 = new JLabel("Schoonmaak Tijd:"); lbl3.setFont(new Font("Arial", Font.BOLD, 13));
        c.gridx = 0; c.gridy = 2; c.weightx = 0.35; tabelPaneel.add(lbl3, c);
        c.gridx = 1; c.gridy = 2; c.weightx = 0.65; tabelPaneel.add(schoonmaakTijd, c);

        // filmduur
        JLabel lbl4 = new JLabel("Film Duur:"); lbl4.setFont(new Font("Arial", Font.BOLD, 13));
        c.gridx = 0; c.gridy = 3; c.weightx = 0.35; tabelPaneel.add(lbl4, c);
        c.gridx = 1; c.gridy = 3; c.weightx = 0.65; tabelPaneel.add(filmDuur, c);

        // restaurant
        JLabel lbl5 = new JLabel("Restaurant Capaciteit:"); lbl5.setFont(new Font("Arial", Font.BOLD, 13));
        c.gridx = 0; c.gridy = 4; c.weightx = 0.35; tabelPaneel.add(lbl5, c);
        c.gridx = 1; c.gridy = 4; c.weightx = 0.65; tabelPaneel.add(restaurantCapaciteit, c);

        // trap
        JLabel lbl6 = new JLabel("Trap-Loop duur:"); lbl6.setFont(new Font("Arial", Font.BOLD, 13));
        c.gridx = 0; c.gridy = 5; c.weightx = 0.35; tabelPaneel.add(lbl6, c);
        c.gridx = 1; c.gridy = 5; c.weightx = 0.65; tabelPaneel.add(trapLoopDuur, c);

        // wachttijd
        JLabel lbl7 = new JLabel("Gast Max Wachttijd:"); lbl7.setFont(new Font("Arial", Font.BOLD, 13));
        c.gridx = 0; c.gridy = 6; c.weightx = 0.35; tabelPaneel.add(lbl7, c);
        c.gridx = 1; c.gridy = 6; c.weightx = 0.65; tabelPaneel.add(gastMaxWachttijd, c);


        settingsFrame.add(tabelPaneel, BorderLayout.CENTER);


        settingsFrame.setLocationRelativeTo(view);
    }

    // getters & setters
    public JButton getDarkModeButton() { return darkModeButton; }
    public JButton getFactuurButton() { return factuurButton; }
    public JComboBox<String> getSchoonmaakTijd() { return schoonmaakTijd; }
    public JComboBox<String> getFilmDuur() { return filmDuur; }
    public JSlider getRestaurantCapaciteit() { return restaurantCapaciteit; }
    public JSlider getTrapLoopDuur() { return trapLoopDuur; }
    public JSlider getGastMaxWachttijd() { return gastMaxWachttijd; }
    public JFrame getFrame() { return settingsFrame; }
}