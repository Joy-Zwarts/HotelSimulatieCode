public class SimulatieSysteem {
    private Layout layout;
    private HotelEvent event;
    private Receptie receptie;
    private Persoon persoon;

    public SimulatieSysteem(Layout layout, HotelEvent event, Receptie receptie, Persoon persoon) {
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
