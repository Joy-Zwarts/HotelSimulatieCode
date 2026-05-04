package View.Systeem;

import Model.Systeem.DarkModeModel;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class HotelSimulatieView extends JFrame {

    // attributen

    private JPanel middlePanel;
    private JPanel legendaPanel;
    private JPanel layoutPanel;
    private JPanel rightPanel;
    private JPanel topbar;
    private JPanel timePanel;
    private JPanel timeManagementPanel;
    private final ImageIcon legenda = null;
    private JButton loadScenarioButton;
    private JButton loadLayoutButton;
    private JButton startSimulationButton;
    private JButton stopSimulationButton;
    private JButton settingsButton;
    private final DarkModeModel darkMode;


    // constructor
    public HotelSimulatieView(DarkModeModel darkMode) {
        this.darkMode = darkMode;
        setTitle("Hotel Simulator - Sjohn Karma's Hotels");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setLayout(new BorderLayout());

        initTopbar();
        initLeftPanel();
        initMiddlePanel();
        initRightPanel();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // maak topbar aan
    private void initTopbar() {
        topbar = new JPanel();
        topbar.setLayout(new BoxLayout(topbar, BoxLayout.X_AXIS)); // horizontale rij
        topbar.setBackground(UIManager.getColor("Panel.background"));
        topbar.setPreferredSize(new Dimension(1500, 130));
        topbar.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // tijd panel
        timePanel = new JPanel(new GridBagLayout()); // label perfect centreren
        timePanel.setPreferredSize(new Dimension(495, 130));

        // logo panel
        JPanel logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setPreferredSize(new Dimension(495, 130));

        // time management knoppen panel
        timeManagementPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 35));
        timeManagementPanel.setPreferredSize(new Dimension(495, 130));

        // logo toevoegen
        ImageIcon hotelLogo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Res/Logo.png")));
        JLabel hotelLogoLabel = new JLabel(hotelLogo);
        logoPanel.add(hotelLogoLabel);

        // panels toevoegen aan topbar
        topbar.add(timePanel);
        topbar.add(logoPanel);
        topbar.add(timeManagementPanel);

        add(topbar, BorderLayout.NORTH);
    }

    // maak liker panel aan
    private void initLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(210, 670));
        setBackground(UIManager.getColor("Panel.background"));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // verschillende buttons

        loadScenarioButton = new JButton("Load Scenario");
        loadLayoutButton = new JButton("Load Layout");
        startSimulationButton = new JButton("Start Simulation");
        stopSimulationButton = new JButton("Stop Simulation");
        settingsButton = new JButton("Settings");

        // voeg toe aan het linker panel met spacing tussen de knoppen

        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(loadScenarioButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(loadLayoutButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(startSimulationButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(stopSimulationButton);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(settingsButton);

        add(leftPanel, BorderLayout.WEST);
    }

    // maak panel in het midden
    private void initMiddlePanel() {
        middlePanel = new JPanel(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        middlePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        initLayoutPanel();
        initLegendaPanel();

        add(middlePanel, BorderLayout.CENTER);
    }

    // maak panel aan voor de layout in het midden
    private void initLayoutPanel() {
        layoutPanel = new JPanel(new BorderLayout());
        layoutPanel.putClientProperty("noTheme", true); // pas geen dark mode toggle toe aan deze panel
        layoutPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setBackground(UIManager.getColor("Panel.background"));
        layoutPanel.setPreferredSize(new Dimension(960, 540));

        JLabel layoutLabel = new JLabel("Selecteer een layout", SwingConstants.CENTER);
        layoutLabel.setFont(new Font("Arial", Font.BOLD, 24));

        layoutPanel.add(layoutLabel, BorderLayout.CENTER);
        middlePanel.add(layoutPanel, BorderLayout.CENTER);
    }

    // maak panel aan voor de legenda in het midden
    private void initLegendaPanel() {
        legendaPanel = new JPanel(new FlowLayout());
        legendaPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setBackground(UIManager.getColor("Panel.background"));
        legendaPanel.setPreferredSize(new Dimension(960, 95));

        JLabel legendaLabel = new JLabel(legenda);

        legendaPanel.add(legendaLabel);
        middlePanel.add(legendaPanel, BorderLayout.SOUTH);
    }

    // maak rechter panel aan
    private void initRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        rightPanel.setPreferredSize(new Dimension(330, 670));
        setBackground(UIManager.getColor("Panel.background"));

        JLabel label = new JLabel("Event weergave", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));

        rightPanel.add(label, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    // vervang de inhoud van de panels
    public void setLayoutView(JPanel newLayout) {
        layoutPanel.removeAll();
        layoutPanel.add(newLayout, BorderLayout.CENTER);
        layoutPanel.revalidate();
        layoutPanel.repaint();
    }

    public void setTopbar(TimePanel timePanelObj, TimeManagementPanel timeManagementPanelObj) {

        JLabel timeLabel = timePanelObj.getTimeLabel();
        this.timePanel.removeAll();
        this.timePanel.setLayout(new GridBagLayout());
        this.timePanel.add(timeLabel);

        JPanel managementPanel = timeManagementPanelObj.getTimeManagementPanel();
        this.timeManagementPanel.removeAll();
        this.timeManagementPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 35));
        this.timeManagementPanel.add(managementPanel);

        topbar.revalidate();
        topbar.repaint();
    }

    public void setLegendaView() {
        this.legendaPanel.removeAll();

        // kies legenda gebaseerd op de dark mode
        ImageIcon legendaIcon;
        if (darkMode.isDarkMode()) {
            legendaIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Res/LegendaDark.png")));
        } else {
            legendaIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Res/Legenda.png")));
        }

        JLabel legendaLabel = new JLabel(legendaIcon);
        this.legendaPanel.add(legendaLabel);

        this.legendaPanel.revalidate();
        this.legendaPanel.repaint();
    }

        public void setRightView(JPanel newRightPanel) {
        rightPanel.removeAll();
        rightPanel.add(newRightPanel, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    // getters & setters

    public JButton getLoadScenarioButton() {
        return loadScenarioButton;
    }

    public JButton getLoadLayoutButton() {
        return loadLayoutButton;
    }

    public JButton getStartSimulationButton() {
        return startSimulationButton;
    }

    public JButton getSettingsButton() {
        return settingsButton;
    }

    public JButton getStopSimulationButton() {
        return stopSimulationButton;
    }

    public JPanel getTopBar() {
        return topbar;
    }
}