package Controller;

import java.io.FileReader;
import java.io.IOException;

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


            // maak een nieuwe kamer aan met de attributen die we net hebben opgehaald

            switch (areaType) {
                case "Room":
                    switch (classification) {
                        case "1 Star":
                            Ruimte kamer1 = new Kamer(areaType, position, dimension, RoomClassificatie.eenSter);
                            // zet de kamer in de layout
                            layout.addKamer(kamer1);
                            break;
                        case "2 Star":
                            Ruimte kamer2 = new Kamer(areaType, position, dimension, RoomClassificatie.tweeSterren);
                            layout.addKamer(kamer2);
                            break;
                        case "3 Star":
                            Ruimte kamer3 = new Kamer(areaType, position, dimension, RoomClassificatie.drieSterren);
                            // zet de kamer in de layout
                            layout.addKamer(kamer3);
                            break;
                        case "4 Star":
                            Ruimte kamer4 = new Kamer(areaType, position, dimension, RoomClassificatie.vierSterren);
                            layout.addKamer(kamer4);
                            break;
                        case "5 Star":
                            Ruimte kamer5 = new Kamer(areaType, position, dimension, RoomClassificatie.vijfSterren);
                            layout.addKamer(kamer5);
                            break;
                    }
                    break;
                case "Model.Restaurant":
                    Ruimte restaurant = new Restaurant(areaType, position, dimension, capacity);
                    layout.addKamer(restaurant);
                    break;
                case "Model.Fitness":
                    Ruimte fitness = new Fitness(areaType, position, dimension);
                    layout.addKamer(fitness);
                    break;
                case "Cinema":
                    Ruimte bios = new Bioscoop(areaType, position, dimension, 20);
                    layout.addKamer(bios);
                    break;
                default:
                    break;
            }

        }
        return layout; // return de layout
    }
}