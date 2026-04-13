package Model;

import java.io.File;

public class FilePickerFilter {

    public static File filterJsonFile(File file) {
        if (file == null) {
            return null;
        }

        // filter op niet-json bestanden
        if (!file.getName().toLowerCase().endsWith(".json")) {
            return null;
        }

        return file;
    }
}