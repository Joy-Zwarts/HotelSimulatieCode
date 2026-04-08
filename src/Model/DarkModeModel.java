package Model;

import java.awt.*;

public class DarkModeModel {

    // attributen

    private boolean darkMode;
    private Color bg;
    private Color fg;
    private Color buttonBg;
    private Color buttonFg;

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
        return bg = darkMode ? new Color(0x1e1f1f) : Color.WHITE;
    }
    public Color getForegroundColor() {
        return fg = darkMode ? Color.WHITE : new Color(0x1e1f1f);
    }
    public Color getButtonBackgroundColor() {
        return buttonBg = darkMode ? Color.WHITE : new Color(0x1e1f1f);
    }
    public Color getButtonFgColor() {
        return buttonFg = darkMode ? new Color(0x1e1f1f) : Color.WHITE;
    }
}
