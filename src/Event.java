import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Event implements ActionListener {
    private String description;

    public Event(String description, ActionListener actionListener) {
        this.description = description;
    }

    public void executeEvent() {
    }

    public void kiesEvent() {
    }

    public String getDescription() {
        return this.description;
    }

    public void actionPerformed(ActionEvent e) {
    }
}
