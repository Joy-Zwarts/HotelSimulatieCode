package Controller;
import View.OverzichtView;
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
        if (!paused) {
            manager.pauze();
            view.show();
            paused = true;
        }
    }

    public void resume() {
        if (paused) {
            manager.pauze();
            manager.setHte(1000);
            view.hide();
            paused = false;
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void setView(OverzichtView overzichtView) {
        this.view = overzichtView;
    }
}