package Controller.Systeem;

import View.Systeem.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SettingsController implements ActionListener, ChangeListener {

    // attributen
    private final SettingsView settingsFrame;
    private final DarkModeController darkModeController;
    private final ArrayList<settingsListener> listeners;

    // constructor
    public SettingsController(HotelSimulatieView view, TimeManagementPanel panel, DarkModeController controller) {
        settingsFrame = new SettingsView(view);
        this.darkModeController = controller;
        this.listeners = new ArrayList<>();
        init();
    }

    // registreert zichzelf als listener voor alle componenten
    public void init() {
        // buttons & drop downs
        settingsFrame.getDarkModeButton().addActionListener(this);
        settingsFrame.getSchoonmaakTijd().addActionListener(this);
        settingsFrame.getFilmDuur().addActionListener(this);

        // sliders
        settingsFrame.getAantalSchoonmakers().addChangeListener(this);
        settingsFrame.getRestaurantCapaciteit().addChangeListener(this);
        settingsFrame.getTrapLoopDuur().addChangeListener(this);
        settingsFrame.getGastMaxWachttijd().addChangeListener(this);
    }

    // acties van de knop en drop-downs
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == settingsFrame.getDarkModeButton()) {
            darkModeController.toggleDarkMode();

        } else if (source == settingsFrame.getSchoonmaakTijd()) {
            String gekozenTijd = (String) settingsFrame.getSchoonmaakTijd().getSelectedItem();
            System.out.println("Schoonmaaktijd veranderd naar: " + gekozenTijd);

            // Moderne Switch Expression: Geen break nodig en direct een waarde teruggeven!
            TijdsDuur tijdsDuur = switch (gekozenTijd) {
                case "Normaal" -> TijdsDuur.NORMAAL;
                case "Lang"    -> TijdsDuur.LANG;
                case "Kort"    -> TijdsDuur.KORT;
                case null, default -> TijdsDuur.NORMAAL;
            };

            for (settingsListener listener : listeners) {
                listener.schoonmaakTijdVeranderd(tijdsDuur);
            }

        } else if (source == settingsFrame.getFilmDuur()) {
            String gekozenDuur = (String) settingsFrame.getFilmDuur().getSelectedItem();
            System.out.println("Filmduur veranderd naar: " + gekozenDuur);

            TijdsDuur filmDuur = switch (gekozenDuur) {
                case "Normaal" -> TijdsDuur.NORMAAL;
                case "Lang"    -> TijdsDuur.LANG;
                case "Kort"    -> TijdsDuur.KORT;
                case null, default -> TijdsDuur.NORMAAL;
            };

            for (settingsListener listener : listeners) {
                listener.filmDuurVeranderd(filmDuur);
            }
        }
    }

    // acties van de sliders
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider) e.getSource();

        // als de gebruiker de slider loslaat wordt er gekeken wat er is geselecteerd en waar
        if (!slider.getValueIsAdjusting()) {
            int waarde = slider.getValue();

            if (slider == settingsFrame.getAantalSchoonmakers()) {
                System.out.println("Aantal Schoonmakers aangepast naar: " + waarde);
                for(settingsListener listener : listeners) {
                    listener.aantalSchoonmakersVeranderd(waarde);
                }
            } else if (slider == settingsFrame.getRestaurantCapaciteit()) {
                System.out.println("Restaurant Capaciteit aangepast naar: " + waarde);
                for(settingsListener listener : listeners) {
                    listener.restaurantCapaciteitVeranderd(waarde);
                }
            } else if (slider == settingsFrame.getTrapLoopDuur()) {
                System.out.println("Trap-Loop duur aangepast naar: " + waarde);
                for(settingsListener listener : listeners) {
                    listener.trapLoopDuurVeranderd(waarde);
                }
            } else if (slider == settingsFrame.getGastMaxWachttijd()) {
                System.out.println("Gast Max Wachttijd aangepast naar: " + waarde);
                for(settingsListener listener : listeners) {
                    listener.gastMaxWachttijdVeranderd(waarde);
                }
            }
        }
    }

    // getters & setters
    public SettingsView getSettingsFrame() {
        return settingsFrame;
    }

    public void addListener(settingsListener listener) {
        this.listeners.add(listener);
    }
}