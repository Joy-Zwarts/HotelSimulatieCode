package Controller.Faciliteiten;

import Controller.Faciliteiten.Interfaces.restaurantOver;
import Controller.Layout.Interfaces.LayoutGeladen;
import Controller.Layout.LayoutController;
import Controller.PersoonManagement.Interfaces.NewGast;
import Controller.Timer.WachtTimer;
import Model.Layout.Locatie;
import Model.Entiteiten.Activiteit;
import Model.Entiteiten.GastModel;
import Model.Ruimtes.KamerType;
import Model.Ruimtes.RestaurantModel;
import Model.Ruimtes.RuimteModel;
import View.Systeem.TijdsDuur;
import Controller.Systeem.Interfaces.settingsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RestaurantController implements NewGast, settingsListener, LayoutGeladen {

    private final ArrayList<restaurantOver> listeners = new ArrayList<>();
    private final Random rand = new Random();
    public final WachtTimer wachtTimer;
    public HashMap<String, RestaurantModel> restaurants = new HashMap<>();
    public LayoutController layoutController;

    public void addListeners(restaurantOver listener) {
        listeners.add(listener);
    }

    public RestaurantController(WachtTimer timer) {
        this.wachtTimer = timer;
    }

    // als gasten klaar zijn met eten
    public void stuurGastWeg(int gastId, RestaurantModel restaurant) {
        restaurant.verwijderGast(gastId);

        for (restaurantOver listener : listeners) {
            listener.gaWegUitRestaurant(gastId);
        }
        System.out.println("Gast " + gastId + " heeft het restaurant verlaten.");
    }

    // als er geen plek is in het restaurant
    public void gastGeweigerd(int gastId) {
        for (restaurantOver listener : listeners) {
            listener.gastGeweigerd(gastId);
        }
    }

    // kijkt of er plek is in het restaurant, zo ja, dan mogen ze blijven eten, zo nee, worden ze weggestuurd
    @Override
    public void onGastAangekomenInKamer(GastModel gast, Locatie behaaldeLocatie) {
        int gastId = gast.getID();

        // welk restaurant is het
        for (RestaurantModel restaurant : restaurants.values()) {
            if ((gast.getLocatie().getX() == (restaurant.getPosition().getX())) &&
                    (gast.getLocatie().getY() == (restaurant.getPosition().getY() - 1))) {

                // als er nog plek is
                if (!restaurant.isVol()) {
                    restaurant.voegGastToe(gastId);

                    // bereken eettijd
                    int verblijfTijd = rand.nextInt(15, 31);
                    String uniekeID = gast.getTypePersoon().name() + "-" + gastId;

                    // start timer voor hun
                    wachtTimer.startTimer(uniekeID, () -> stuurGastWeg(gastId, restaurant), verblijfTijd);

                    System.out.println("Gast " + gastId + " eet in restaurant " + restaurant.getID() + " voor " + verblijfTijd + " ticks.");
                    gast.setActiviteit(Activiteit.ETEN);
                } else {
                    System.out.println("Gast " + gastId + " wilde eten, maar restaurant " + restaurant.getID() + " is vol!");
                    String uniekeWeigerID = "WEIGER-" + gast.getTypePersoon().name() + "-" + gastId;
                    // start de timer zodat het swing safe is en roep de weiger-event aan
                    wachtTimer.startTimer(uniekeWeigerID, () -> gastGeweigerd(gastId), 0);
                }
                break;
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
    public void onLayoutGeladen(LayoutController controller) {
        this.layoutController = controller;
        for (RuimteModel ruimte : layoutController.getModel().getRuimtes()) {
            if (ruimte.getAreaType().equals(KamerType.RESTAURANT)) {
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
    @Override public void showFactuurBonnen(boolean bool) {}
    @Override public void trapLoopDuurVeranderd(int trapLoopDuur) {}
    @Override public void gastMaxWachttijdVeranderd(int gastMaxWachttijd) {}
}