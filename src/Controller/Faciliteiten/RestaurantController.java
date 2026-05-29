package Controller.Faciliteiten;

import Controller.Events.Interfaces.needFoodEvent;
import Controller.Faciliteiten.Interfaces.restaurantOver;
import Controller.Layout.Intefaces.LayoutGeladen;
import Controller.Layout.LayoutController;
import Controller.PersoonManagement.Interfaces.NewGast;
import Controller.Timer.WachtTimer;
import Model.Layout.Locatie;
import Model.Personen.Activiteit;
import Model.Personen.GastModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RestaurantModel;
import Model.Ruimtes.RuimteModel;
import View.Systeem.TijdsDuur;
import View.Systeem.settingsListener;
import hotelevents.HotelEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RestaurantController implements needFoodEvent, NewGast, settingsListener, LayoutGeladen {

    private final ArrayList<restaurantOver> listeners = new ArrayList<>();
    private final Random rand = new Random();
    public final WachtTimer wachtTimer;
    public HashMap<String, RestaurantModel> restaurants = new HashMap<>();

    public void addListeners(restaurantOver listener) {
        listeners.add(listener);
    }

    public RestaurantController(WachtTimer timer) {
        this.wachtTimer = timer;
    }

    @Override
    public void needFood(HotelEvent hotelEvent) {
        // Logica om gast naar restaurant te sturen (Pathfinding triggeren)
    }

    public void stuurGastWeg(int gastId, RestaurantModel restaurant) {
        // Verwijder de gast uit het specifieke restaurant model
        restaurant.verwijderGast(gastId);

        for (restaurantOver listener : listeners) {
            listener.gaWegUitRestaurant(gastId);
        }
        System.out.println("Gast " + gastId + " heeft het restaurant verlaten.");
    }

    @Override
    public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {
        int gastId = gast.getID();

        // zoek het restaurant waar de gast zich nu bevindt
        for (RestaurantModel restaurant : restaurants.values()) {
            if (gast.getLocatie().equals(restaurant.getPosition())) {

                // controleer  of er wel plek is
                if (!restaurant.isVol()) {
                    restaurant.voegGastToe(gastId);

                    int verblijfTijd = rand.nextInt(15, 31);
                    String uniekeID = gast.getTypePersoon().name() + "-" + gastId;

                    // start de timer en geef het restaurant mee zodat we weten waar ze weggaan
                    wachtTimer.startTimer(uniekeID, () -> stuurGastWeg(gastId, restaurant), verblijfTijd);

                    System.out.println("Gast " + gastId + " eet in restaurant " + restaurant.getID() + " voor " + verblijfTijd + " ticks.");
                    gast.setActivity(Activiteit.ETEN);
                } else {
                    System.out.println("Gast " + gastId + " wilde eten, maar restaurant " + restaurant.getID() + " is vol!");
                    for (restaurantOver listener : listeners) {
                        listener.gaWegUitRestaurant(gastId);
                    }
                }
                break; // Gast kan maar in één restaurant tegelijk zijn
            }
        }
    }

    @Override
    public void restaurantCapaciteitVeranderd(int restaurantCapaciteit) {
        for (RestaurantModel restaurant : restaurants.values()) {
            restaurant.setCapacity(restaurantCapaciteit);
        }
    }

    @Override
    public void onLayoutGeladen(LayoutController layoutController) {
        for (RuimteModel ruimte : layoutController.getModel().getRuimtes()) {
            if (ruimte.getAreaType().equals(KamerType.RESTAURANT)){
                RestaurantModel restaurant = (RestaurantModel) ruimte;
                restaurants.put(restaurant.getID(), restaurant);
                System.out.println("Restaurant " + restaurant.getID() + " succesvol gekoppeld aan de controller.");
            }
        }
    }

    @Override public void onGastAangemaakt(GastModel gast) {}
    @Override public void onGastVertrokken(GastModel gast) {}
    @Override public void onGastVerplaatst(GastModel gast, Locatie oudeLocatie) {}
    @Override public void onGastGaatWegUitKamer(GastModel gast, Locatie oudeLocatie) {}
    @Override public void schoonmaakTijdVeranderd(TijdsDuur tijdsDuur) {}
    @Override public void filmDuurVeranderd(TijdsDuur tijdsDuur) {}
    @Override public void aantalSchoonmakersVeranderd(int aantalSchoonmakers) {}
    @Override public void trapLoopDuurVeranderd(int trapLoopDuur) {}
    @Override public void gastMaxWachttijdVeranderd(int gastMaxWachttijd) {}
}