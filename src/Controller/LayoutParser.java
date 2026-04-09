package Controller;

import java.io.FileReader;
import java.io.IOException;

import Controller.RuimteFactory.*;
import Model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LayoutParser {

    public LayoutModel parse(String file) throws IOException, ParseException {

        LayoutModel layout = new LayoutModel();

        // parser en layout array aanmaken
        JSONParser parser = new JSONParser();
        JSONArray layoutArray = (JSONArray) parser.parse(new FileReader(file));

        for (Object obj : layoutArray) { // voor aantal objecten (ruimten) in de file

            JSONObject ruimte = (JSONObject) obj; // object ruimte aanmaken

            String areaType = (String) ruimte.get("AreaType"); // get AreaType
            String position = (String) ruimte.get("Position"); // get Position
            String dimension = (String) ruimte.get("Dimension"); // get Dimension

            long capacity = ruimte.get("Capacity") != null // als de capaciteit niet null is
                    ? (long) ruimte.get("Capacity") // get capaciteit
                    : 9999; // anders zet de capaciteit op 9999

            String classification = (String) ruimte.get("Classification"); // get classificatie

            RuimteFactory factory;

            // switch op area type en maak het aan via de ruimte factory
            switch (areaType) {
                case "Room":
                    factory = new RoomCreator();
                    break;
                case "Restaurant":
                    factory = new RestaurantCreator();
                    break;
                case "Fitness":
                    factory = new FitnessCreator();
                    break;
                case "Cinema":
                    factory = new CinemaCreator();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown type");
            }

            RuimteModel ruimteObj = factory.createRuimte(areaType, position, dimension, capacity, classification);
            layout.addKamer(ruimteObj);

        }
        return layout; // return de layout
    }
}