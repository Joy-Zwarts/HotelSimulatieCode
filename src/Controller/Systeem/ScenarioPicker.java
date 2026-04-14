package Controller.Systeem;

import View.Systeem.ScenarioPickerView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScenarioPicker implements ActionListener {

    // attributen

    private final ScenarioPickerView scenarioPickerView;
    private String selected;

    // constructor
    public ScenarioPicker() {
        scenarioPickerView = new ScenarioPickerView();
        scenarioPickerView.getConfirmButton().addActionListener(this);
        selected = "Scenario 1";
    }

    // als er wordt geklikt op de confirm button, registreer het gekozen scenario
    @Override
    public void actionPerformed(ActionEvent e) {
        selected = (String) scenarioPickerView.getComboBox().getSelectedItem();
        System.out.println("Selected scenario: " + selected);

        int scenarioNumber = getSelected();
        System.out.println("Scenario number: " + scenarioNumber);

        scenarioPickerView.close(); // close frame na de keuze
    }

    // geef het nummer van het scenario door
    public int getSelected() {
        int selectedNumber = 1;
        switch (selected) {
            case "Scenario 1":
                break;
            case "Scenario 2":
                selectedNumber = 2;
                break;
            case "Scenario 3":
                selectedNumber = 3;
                break;
            case "Scenario 4":
                selectedNumber = 4;
                break;
        }
        return selectedNumber;
    }
}