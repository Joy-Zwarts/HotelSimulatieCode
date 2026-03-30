import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestFrame {

    @Test
    public void testFrameOpent() throws Exception {
        // Start Main handmatig
        Main.main(new String[]{});
        // Selecteer handmatig een bestand in de JFileChooser

        Thread.sleep(5000); // Wacht even

        boolean frameIsVisible = false;
        for (Frame f : Frame.getFrames()) {
            if (f.isVisible() && "Hotel Layout".equals(f.getTitle())) {
                frameIsVisible = true;
            }
        }

        assertTrue(frameIsVisible, "Het frame moet zichtbaar zijn");
    }
}