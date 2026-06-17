package View.JoyOpdracht;

import Controller.Systeem.Interfaces.settingsListener;
import Model.Entiteiten.GastModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.TijdsDuur;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FactuurPrint implements settingsListener {

    private final HotelSimulatieView hotelSimulatieView;
    private final HotelEventManager eventManager;
    private Boolean showFactuurBonnen = false;
    private Boolean paused = false;
    static int openstaandeBonnen = 0;

    public FactuurPrint(HotelSimulatieView view, HotelEventManager hotelEventManager) {
        this.hotelSimulatieView = view;
        this.eventManager = hotelEventManager;
    }

    public void PrintBon(HashMap<String, Integer> factuur, GastModel gast, String checkInTijstip, String checkOutTijdstip) {
        if (showFactuurBonnen == false) {
            return;
        }

        JFrame frame = new JFrame("Factuur - Gast " + gast.getID());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(330, 450);
        frame.setLocationRelativeTo(hotelSimulatieView);
        frame.setResizable(false);

        frame.getRootPane().putClientProperty("noTheme", true);

        openstaandeBonnen++;

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                openstaandeBonnen--;
                if (openstaandeBonnen == 0 && paused) {
                    eventManager.pauze();
                    paused = false;
                }
            }
        });

        // hoofdpaneel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.putClientProperty("noTheme", true);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
        headerContainer.putClientProperty("noTheme", true);
        headerContainer.setBackground(Color.WHITE);

        // de hoofdtitel
        JLabel headerLabel = new JLabel("FACTUUR VOOR GAST " + gast.getID());
        headerLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerContainer.add(headerLabel);

        headerContainer.add(Box.createVerticalStrut(8));

        // check-in regel
        JLabel checkInLabel = new JLabel("Inchecken: " + checkInTijstip);
        checkInLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));
        checkInLabel.setForeground(Color.DARK_GRAY);
        checkInLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerContainer.add(checkInLabel);

        // check-out regel
        JLabel checkOutLabel = new JLabel("Uitchecken: " + checkOutTijdstip);
        checkOutLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));
        checkOutLabel.setForeground(Color.DARK_GRAY);
        checkOutLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerContainer.add(checkOutLabel);

        mainPanel.add(headerContainer, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel(new GridBagLayout());
        itemsPanel.putClientProperty("noTheme", true);
        itemsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridy = 0;

        int totaalbedrag = 0;
        Font bonFont = new Font("Monospaced", Font.PLAIN, 12);

        // loop door elke entry van de rekening heen
        for (Map.Entry<String, Integer> regel : factuur.entrySet()) {
            // productnaam (links)
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            JLabel itemLabel = new JLabel(regel.getKey());
            itemLabel.setFont(bonFont);
            itemLabel.setForeground(Color.BLACK);
            itemsPanel.add(itemLabel, gbc);

            // prijs (rechts)
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            JLabel priceLabel = new JLabel(String.format("€%4d,-", regel.getValue()));
            priceLabel.setFont(bonFont);
            priceLabel.setForeground(Color.BLACK);
            itemsPanel.add(priceLabel, gbc);

            // tel het op bij het totaal
            totaalbedrag += regel.getValue();
            gbc.gridy++; // volgende regel
        }

        // scrollPane voor het geval de lijst lang is
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.putClientProperty("noTheme", true);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.putClientProperty("noTheme", true);
        footerPanel.setBackground(Color.WHITE);

        JLabel separator = new JLabel("--------------------------------");
        separator.setFont(bonFont);
        separator.setForeground(Color.BLACK);
        separator.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(separator, BorderLayout.NORTH);

        // totaalbedrag
        JLabel totaalLabel = new JLabel(String.format("TOTAAL: €%d,-", totaalbedrag));
        totaalLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        totaalLabel.setForeground(Color.BLACK);
        totaalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        footerPanel.add(totaalLabel, BorderLayout.SOUTH);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);

        if (paused == false) {
            eventManager.pauze();
            paused = true;
        }

        frame.setVisible(true);
    }

    @Override
    public void schoonmaakTijdVeranderd(TijdsDuur tijdsDuur) {

    }

    @Override
    public void filmDuurVeranderd(TijdsDuur tijdsDuur) {

    }

    @Override
    public void showFactuurBonnen(boolean bool) {
        this.showFactuurBonnen = bool;
    }

    @Override
    public void restaurantCapaciteitVeranderd(int restaurantCapaciteit) {

    }

    @Override
    public void trapLoopDuurVeranderd(int trapLoopDuur) {

    }

    @Override
    public void gastMaxWachttijdVeranderd(int gastMaxWachttijd) {

    }

    public boolean isGepauzeerd() {
        return this.paused;
    }
}