package Controller.Systeem;

import Controller.SimulatieController;
import View.Systeem.HotelSimulatieView;
import View.Systeem.ScenarioPickerView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScenarioPicker implements ActionListener {

    private final ScenarioPickerView scenarioPickerView;
    private final SimulatieController simulatieManager;
    private String selected;

    // constructor
    public ScenarioPicker(HotelSimulatieView view, SimulatieController simulatieManager) {
        this.simulatieManager = simulatieManager;
        this.scenarioPickerView = new ScenarioPickerView(view);
        this.scenarioPickerView.getConfirmButton().addActionListener(this);
        this.selected = "Scenario 1";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selected = (String) scenarioPickerView.getComboBox().getSelectedItem();
        System.out.println("Selected scenario: " + selected);

        int scenarioNumber = getSelectedNumber();
        System.out.println("Scenario number: " + scenarioNumber);

        simulatieManager.setScenario(scenarioNumber);

        scenarioPickerView.close();
    }

    private int getSelectedNumber() {
        return switch (selected) {
            case "Scenario 2" -> 2;
            case "Scenario 3" -> 3;
            case "Scenario 4" -> 4;
            default -> 1;
        };
    }
}