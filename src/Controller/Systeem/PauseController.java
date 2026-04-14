package Controller.Systeem;
import View.Systeem.OverzichtView;
import hotelevents.HotelEventManager;
public class PauseController {
    private final HotelEventManager manager;
    private OverzichtView view;
    private boolean paused = false;

    public PauseController(HotelEventManager manager, OverzichtView view) {
        this.manager = manager;
        this.view = view;
    }

    public void pause() {
        manager.pauze();
        view.show();
    }

    public void resume() {
        manager.pauze();
        view.hide();
    }

    public boolean isPaused() {
        return paused;
    }

    public void setView(OverzichtView overzichtView) {
        this.view = overzichtView;
    }
}