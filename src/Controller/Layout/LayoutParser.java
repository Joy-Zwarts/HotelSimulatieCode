package Controller.Layout;

import Model.Layout.RuimteData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LayoutParser {

    private static final long DEFAULT_CAPACITY = 9999;

    // parsed de json file en maakt data aan gebaseerd op de waarden uit de file en slaat deze op in een array
    public List<RuimteData> parse(String file) throws Exception {

        List<RuimteData> ruimtes = new ArrayList<>();

        JSONParser parser = new JSONParser();
        JSONArray layoutArray = (JSONArray) parser.parse(new FileReader(file));

        for (Object obj : layoutArray) {

            JSONObject ruimte = (JSONObject) obj;

            RuimteData data = new RuimteData();

            data.areaType = (String) ruimte.get("AreaType");
            String position = (String) ruimte.get("Position");

            String[] xPos = position.split(",");
            int x = Integer.parseInt(xPos[0].trim());

            String[] yPos = position.split(",");
            int y = Integer.parseInt(yPos[1].trim());

            data.position = new Locatie(x, y);

            data.dimension = (String) ruimte.get("Dimension");

            Object capObj = ruimte.get("Capacity");
            data.capacity = (capObj instanceof Number)
                    ? ((Number) capObj).longValue()
                    : DEFAULT_CAPACITY;

            data.classification = (String) ruimte.get("Classification");

            ruimtes.add(data);
        }

        return ruimtes; // returned al de gemaakte ruimtes uit de array als hij klaar is
    }
}