package Controller;

import Model.HotelEvent;
import Model.LayoutModel;
import Model.Persoon;

public class SimulatieSysteem {
    private LayoutModel layout;
    private HotelEvent event;
    private Receptie receptie;
    private Persoon persoon;

    public SimulatieSysteem(LayoutModel layout, HotelEvent event, Receptie receptie, Persoon persoon) {
        this.layout = layout;
        this.event = event;
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
