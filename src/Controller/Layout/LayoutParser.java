package Controller.Layout;

import Model.Ruimtes.RuimteData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LayoutParser {

    private static final long DEFAULT_CAPACITY = 9999;

    public List<RuimteData> parse(String file) throws Exception {

        List<RuimteData> ruimtes = new ArrayList<>();

        JSONParser parser = new JSONParser();
        JSONArray layoutArray = (JSONArray) parser.parse(new FileReader(file));

        for (Object obj : layoutArray) {

            JSONObject ruimte = (JSONObject) obj;

            RuimteData data = new RuimteData();

            data.areaType = (String) ruimte.get("AreaType");
            data.position = (String) ruimte.get("Position");
            data.dimension = (String) ruimte.get("Dimension");

            Object capObj = ruimte.get("Capacity");
            data.capacity = (capObj instanceof Number)
                    ? ((Number) capObj).longValue()
                    : DEFAULT_CAPACITY;

            data.classification = (String) ruimte.get("Classification");

            ruimtes.add(data);
        }

        return ruimtes;
    }
}