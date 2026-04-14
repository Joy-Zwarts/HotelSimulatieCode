package View.Systeem;

import javax.swing.*;

public class ScenarioPickerView {

    // attributen

    private final JComboBox<String> comboBox;
    private final JButton confirmButton;
    private final JFrame frame;

    // constructor
    public ScenarioPickerView() {
        frame = new JFrame("Scenario Picker");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(null);

        String[] options = {"Scenario 1", "Scenario 2", "Scenario 3", "Scenario 4"};
        comboBox = new JComboBox<>(options);
        comboBox.setBounds(100, 50, 200, 30);
        frame.add(comboBox);

        confirmButton = new JButton("Confirm");
        confirmButton.setBounds(150, 100, 100, 30);
        frame.add(confirmButton);

        frame.setVisible(true);
    }

    // getters & setters

    public JComboBox<String> getComboBox() {
        return comboBox;
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public void close() {
        frame.dispose();
    }
}