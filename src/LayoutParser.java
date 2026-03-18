import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LayoutParser {
    public Layout parse(String file) throws IOException, ParseException {
        Layout layout = new Layout();
        JSONParser parser = new JSONParser();

        for(Object obj : (JSONArray)parser.parse(new FileReader(file))) {
            JSONObject ruimte = (JSONObject)obj;
            String areaType = (String)ruimte.get("AreaType");
            String position = (String)ruimte.get("Position");
            String dimension = (String)ruimte.get("Dimension");
            long capacity = ruimte.get("Capacity") != null ? (Long)ruimte.get("Capacity") : 9999L;
            String classification = (String)ruimte.get("Classification");
            switch (areaType) {
                case "Room":
                    switch (classification) {
                        case "1 Star":
                            Ruimte kamer1 = new Kamer(areaType, position, dimension, RoomClassificatie.eenSter);
                            layout.addKamer(kamer1);
                            continue;
                        case "2 Star":
                            Ruimte kamer2 = new Kamer(areaType, position, dimension, RoomClassificatie.tweeSterren);
                            layout.addKamer(kamer2);
                            continue;
                        case "3 Star":
                            Ruimte kamer3 = new Kamer(areaType, position, dimension, RoomClassificatie.drieSterren);
                            layout.addKamer(kamer3);
                            continue;
                        case "4 Star":
                            Ruimte kamer4 = new Kamer(areaType, position, dimension, RoomClassificatie.vierSterren);
                            layout.addKamer(kamer4);
                            continue;
                        case "5 Star":
                            Ruimte kamer5 = new Kamer(areaType, position, dimension, RoomClassificatie.vijfSterren);
                            layout.addKamer(kamer5);
                        default:
                            continue;
                    }
                case "Restaurant":
                    Ruimte restaurant = new Restaurant(areaType, position, dimension, capacity);
                    layout.addKamer(restaurant);
                    break;
                case "Fitness":
                    Ruimte fitness = new Fitness(areaType, position, dimension);
                    layout.addKamer(fitness);
                    break;
                case "Cinema":
                    Ruimte bios = new Bioscoop(areaType, position, dimension, 20);
                    layout.addKamer(bios);
            }
        }

        return layout;
    }
}
