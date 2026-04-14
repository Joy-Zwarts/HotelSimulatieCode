package Model.Systeem;

import java.awt.*;

public class DarkModeModel {

    // attributen
    private boolean darkMode;

    // constructor
    public DarkModeModel() {
        this.darkMode = false;
    }

    // getters & setters

    public boolean isDarkMode() {
        return darkMode;
    }
    public void setDarkMode(boolean darkmode) {
        this.darkMode = darkmode;
    }

    // hard coded kleuren voor light en dark mode
    public Color getBackgroundColor() {
        return darkMode ? new Color(0x1e1f1f) : Color.WHITE;
    }
    public Color getForegroundColor() {
        return darkMode ? Color.WHITE : new Color(0x1e1f1f);
    }
    public Color getButtonBackgroundColor() {
        return darkMode ? Color.WHITE : new Color(0x1e1f1f);
    }
    public Color getButtonFgColor() {
        return darkMode ? new Color(0x1e1f1f) : Color.WHITE;
    }
}
