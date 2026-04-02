package View;

import javax.swing.*;
import java.awt.*;

public class HotelSimulatieView extends JFrame {
    private JPanel leftPanel;
    private JPanel middlePanel;
    private JPanel legendaPanel;
    private JPanel layoutPanel;
    private JPanel rightPanel;
    private JPanel topbar;

    private JButton loadScenarioButton;
    private JButton loadLayoutButton;
    private JButton startSimulationButton;
    private JButton stopSimulationButton;
    private JButton settingsButton;

    public HotelSimulatieView() {
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

    private void initTopbar() {
        topbar = new JPanel(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        topbar.setPreferredSize(new Dimension(1500, 130));

        JLabel hotelLabel = new JLabel(
                "Topbar, logo, tijd, settings en tijd management",
                SwingConstants.CENTER
        );
        hotelLabel.setFont(new Font("Arial", Font.BOLD, 24));

        topbar.add(hotelLabel, BorderLayout.CENTER);
        add(topbar, BorderLayout.NORTH);
    }

    private void initLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(210, 670));
        setBackground(UIManager.getColor("Panel.background"));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        loadScenarioButton = new JButton("Load Scenario");
        loadLayoutButton = new JButton("Load Layout");
        startSimulationButton = new JButton("Start Simulation");
        stopSimulationButton = new JButton("Stop Simulation");
        settingsButton = new JButton("Settings");

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

    private void initMiddlePanel() {
        middlePanel = new JPanel(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));

        initLayoutPanel();
        initLegendaPanel();

        add(middlePanel, BorderLayout.CENTER);
    }

    private void initLayoutPanel() {
        layoutPanel = new JPanel(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        layoutPanel.setPreferredSize(new Dimension(960, 540));

        JLabel layoutLabel = new JLabel("Hotel Layout", SwingConstants.CENTER);
        layoutLabel.setFont(new Font("Arial", Font.BOLD, 24));

        layoutPanel.add(layoutLabel, BorderLayout.CENTER);
        middlePanel.add(layoutPanel, BorderLayout.CENTER);
    }

    private void initLegendaPanel() {
        legendaPanel = new JPanel(new FlowLayout());
        setBackground(UIManager.getColor("Panel.background"));
        legendaPanel.setPreferredSize(new Dimension(960, 95));

        JLabel legendaLabel = new JLabel("Legenda", SwingConstants.CENTER);
        legendaLabel.setFont(new Font("Arial", Font.BOLD, 24));

        legendaPanel.add(legendaLabel);
        middlePanel.add(legendaPanel, BorderLayout.SOUTH);
    }

    private void initRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(330, 670));
        setBackground(UIManager.getColor("Panel.background"));

        JLabel label = new JLabel("Event weergave", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));

        rightPanel.add(label, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    // 🔹 Methode om layout dynamisch te vervangen
    public void setLayoutView(JPanel newLayout) {
        layoutPanel.removeAll();
        layoutPanel.add(newLayout, BorderLayout.CENTER);
        layoutPanel.revalidate();
        layoutPanel.repaint();
    }

    // 🔹 Methode om legenda te vervangen
    public void setLegendaView(JPanel newLegenda) {
        legendaPanel.removeAll();
        legendaPanel.add(newLegenda);
        legendaPanel.revalidate();
        legendaPanel.repaint();
    }

    // 🔹 Methode om event panel rechts te vervangen
    public void setRightView(JPanel newRightPanel) {
        rightPanel.removeAll();
        rightPanel.add(newRightPanel, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    // 🔹 Getters voor controller
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
}