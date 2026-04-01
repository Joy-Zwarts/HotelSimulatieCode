package Controller;

import Model.LayoutModel;
import Model.Persoon;

public class SimulatieSysteem {
    private LayoutModel layout;
    private Receptie receptie;
    private Persoon persoon;

    public SimulatieSysteem(LayoutModel layout, Receptie receptie, Persoon persoon) {
        this.layout = layout;
        this.receptie = receptie;
        this.persoon = persoon;
    }

    public void startSimulatie() {
    }

    public void PauseSimulatie() {
    }

    public void StopSimulatie() {
    }
}
