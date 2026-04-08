package Controller;

import Model.DarkModeModel;
import Model.LayoutModel;
import View.*;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SimulatieController implements ActionListener {

    private final HotelSimulatieView view;
    private File gekozenFile;
    private Boolean started;
    private HotelEventManager manager;
    private LayoutModel model;
    private DarkModeController darkModeController;
    private TimePanel timePanel;
    private TimeManagementPanel timeManagementPanel;

    public SimulatieController() {
        DarkModeModel darkModeModel = new DarkModeModel();

        this.view = new HotelSimulatieView(darkModeModel);
        this.darkModeController = new DarkModeController(view, darkModeModel);

        this.manager = new HotelEventManager(false);
        this.started = false;
        this.model = null;

        this.timePanel = new TimePanel(manager, view.getTopBar());
        this.timeManagementPanel = new TimeManagementPanel(manager, view.getTopBar(), darkModeModel, this);
        view.setTopbar(timePanel, timeManagementPanel);

        darkModeController.applyTheme();

        init();
    }

    private void init() {
        view.getLoadScenarioButton().addActionListener(this);
        view.getStartSimulationButton().addActionListener(this);
        view.getStopSimulationButton().addActionListener(this);
        view.getLoadLayoutButton().addActionListener(this);
        view.getSettingsButton().addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); // welke button komt de action event van

        if (source == view.getLoadScenarioButton()) {
            System.out.println("Load scenario");
        }

        else if (source == view.getLoadLayoutButton()) {
            loadLayout();
        }

        else if (source == view.getStartSimulationButton()) {
            if (model == null) {
                JOptionPane.showMessageDialog(view, "Load eerst een layout bestand", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!(started)) {
                manager.start(1);
                started = true;
            } else if (started) {
                JOptionPane.showMessageDialog(view, "De simulatie is al gestart!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        else if (source == view.getSettingsButton()) {
            openSettingsFrame();
        }

        else if (source == view.getStopSimulationButton()) {
            if (started) {
                manager.stop();
                started = false;
            } else if (!(started)) {
                JOptionPane.showMessageDialog(view, "De simulatie is nog niet gestart!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openSettingsFrame() {
        JFrame settingsFrame = new JFrame("Settings");
        settingsFrame.setSize(300, 200);
        settingsFrame.setLayout(new FlowLayout());

        JButton darkModeButton = new JButton("Toggle Dark Mode");

        darkModeButton.addActionListener(e -> {
            darkModeController.toggleDarkMode();
            timeManagementPanel.changeIcons();
        });

        settingsFrame.add(darkModeButton);

        settingsFrame.setLocationRelativeTo(view);
        settingsFrame.setVisible(true);
    }

    private void loadLayout() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int resultaat = fileChooser.showOpenDialog(view);

            if (resultaat != JFileChooser.APPROVE_OPTION) {
                System.out.println("Geen bestand gekozen.");
                return;
            }

            File gekozenBestand = fileChooser.getSelectedFile();

            // Alleen JSON toestaan
            if (!gekozenBestand.getName().toLowerCase().endsWith(".json")) {
                JOptionPane.showMessageDialog(view, "Alleen JSON bestanden zijn toegestaan!");
                return;
            }

            gekozenFile = gekozenBestand;
            System.out.println("Bestand gekozen: " + gekozenBestand.getName());

            // (optioneel) kopiëren naar projectmap
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
            }

            // 🔥 Layout laden
            LayoutParser parser = new LayoutParser();
            model = parser.parse(gekozenBestand.getAbsolutePath());
            OverzichtScherm overzichtScherm = new OverzichtScherm();

            LayoutView layoutView = new LayoutView(manager);
            EventPrint eventPrint = new EventPrint(manager);

            LayoutController controller = new LayoutController(model, layoutView, manager);

            view.setLayoutView(layoutView.getHotelPanel());
            view.setLegendaView(view.getLegendaPanel());
            view.setRightView(eventPrint.getPanelRechts());
            view.setLegendaView(view.getLegendaPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Fout bij laden: " + ex.getMessage());
        }
    }

    public boolean getStarted() {
        return started;
    }
}