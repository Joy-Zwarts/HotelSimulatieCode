package View.JoyOpdracht;

import Controller.Systeem.Interfaces.settingsListener;
import Model.Entiteiten.GastModel;
import View.Systeem.HotelSimulatieView;
import View.Systeem.TijdsDuur;
import hotelevents.HotelEventManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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

    public void PrintBon(HashMap<String, Integer> factuur, GastModel gast) {
        if (showFactuurBonnen == false) {
            return;
        }

        JFrame frame = new JFrame("Factuur - Gast " + gast.getID());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(320, 450);
        frame.setLocationRelativeTo(hotelSimulatieView);
        frame.setResizable(false);

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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // header
        JLabel headerLabel = new JLabel("FACTUUR VOOR GAST " + gast.getID(), SwingConstants.CENTER);
        headerLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel(new GridBagLayout());
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
            itemsPanel.add(itemLabel, gbc);

            // prijs (rechts)
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.EAST;
            JLabel priceLabel = new JLabel(String.format("€%4d,-", regel.getValue()));
            priceLabel.setFont(bonFont);
            itemsPanel.add(priceLabel, gbc);

            // tel het op bij het totaal
            totaalbedrag += regel.getValue();
            gbc.gridy++; // volgende regel
        }

        // scrollPane voor het geval de lijst lang is
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);

        JLabel separator = new JLabel("--------------------------------");
        separator.setFont(bonFont);
        separator.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(separator, BorderLayout.NORTH);

        // totaalbedrag
        JLabel totaalLabel = new JLabel(String.format("TOTAAL: €%d,-", totaalbedrag));
        totaalLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
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
}
