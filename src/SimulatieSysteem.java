public class SimulatieSysteem {
    private Layout layout;
    private Event event;
    private Klok klok;
    private Receptie receptie;
    private Persoon persoon;

    public SimulatieSysteem(Layout layout, Event event, Klok klok, Receptie receptie, Persoon persoon) {
        this.layout = layout;
        this.event = event;
        this.klok = klok;
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
