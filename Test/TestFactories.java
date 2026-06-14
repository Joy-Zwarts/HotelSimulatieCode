import Controller.PersoonFactory.EntiteitenFactory;
import Controller.PersoonFactory.GastCreator;
import Model.Entiteiten.GastModel;
import Model.Entiteiten.TypePersoon;
import Model.Layout.Locatie;
import Model.Ruimtes.KamerClassificatie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Controller.RuimteFactory.*;
import Model.Ruimtes.*;

import java.util.ArrayList;

public class TestFactories {

    private ArrayList<GastModel> gasten;

    @BeforeEach
    public void setUp() {
        gasten = new ArrayList<>();
    }

    @Test
    public void testGastCreatie() {
        EntiteitenFactory factory = new GastCreator();

        gasten.add((GastModel) factory.createEntiteit(1, new Locatie(0,0), new Locatie(1,1), 1, null, TypePersoon.GAST));
        gasten.add((GastModel) factory.createEntiteit(2, new Locatie(0,0), new Locatie(1,1), 2, null, TypePersoon.GAST));
        gasten.add((GastModel) factory.createEntiteit(3, new Locatie(0,0), new Locatie(1,1), 3, null, TypePersoon.GAST));
        gasten.add((GastModel) factory.createEntiteit(4, new Locatie(0,0), new Locatie(1,1), 4, null, TypePersoon.GAST));
        gasten.add((GastModel) factory.createEntiteit(5, new Locatie(0,0), new Locatie(1,1), 5, null, TypePersoon.GAST));

        Assertions.assertEquals(5, gasten.size());
        Assertions.assertEquals(KamerClassificatie.eenSter, gasten.get(0).getWensen());
        Assertions.assertEquals(KamerClassificatie.tweeSterren, gasten.get(1).getWensen());
        Assertions.assertEquals(KamerClassificatie.drieSterren, gasten.get(2).getWensen());
        Assertions.assertEquals(KamerClassificatie.vierSterren, gasten.get(3).getWensen());
        Assertions.assertEquals(KamerClassificatie.vijfSterren, gasten.get(4).getWensen());
    }

//    @Test
//    public void testSchoonmakerCreatie() {
//        PersoonFactory factory = new SchoonmakerCreator();
//        Locatie loc = new Locatie(5, 5);
//        Locatie target = new Locatie(10, 10);
//
//        SchoonmakerModel schoonmaker = (SchoonmakerModel) factory.createPersoon(99, loc, target, 0, null);
//
//        Assertions.assertNotNull(schoonmaker);
//        Assertions.assertEquals(10, schoonmaker.getLocatie().getX());
//        Assertions.assertEquals(10, schoonmaker.getLocatie().getY());
//    }

    @Test
    public void testRoomCreatorAlleClassificaties() {
        RuimteFactory factory = new KamerCreator();
        Locatie loc = new Locatie(1, 1);
        String dim = "1,1";

        KamerModel star1 = (KamerModel) factory.createRuimte(loc, dim, 0, "1 Star");
        KamerModel star2 = (KamerModel) factory.createRuimte(loc, dim, 0, "2 Star");
        KamerModel star3 = (KamerModel) factory.createRuimte(loc, dim, 0, "3 Star");
        KamerModel star4 = (KamerModel) factory.createRuimte(loc, dim, 0, "4 Star");
        KamerModel star5 = (KamerModel) factory.createRuimte(loc, dim, 0, "5 Star");

        Assertions.assertEquals(KamerClassificatie.eenSter, star1.getClassification());
        Assertions.assertEquals(KamerClassificatie.tweeSterren, star2.getClassification());
        Assertions.assertEquals(KamerClassificatie.drieSterren, star3.getClassification());
        Assertions.assertEquals(KamerClassificatie.vierSterren, star4.getClassification());
        Assertions.assertEquals(KamerClassificatie.vijfSterren, star5.getClassification());

        Assertions.assertEquals(KamerType.ROOM, star1.getAreaType());
    }

    @Test
    public void testRoomCreatorUnknownClassification() {
        RuimteFactory factory = new KamerCreator();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createRuimte(new Locatie(0,0), "1,1", 0, "99 Stars"));
    }

    @Test
    public void testCinemaCreator() {
        RuimteFactory factory = new BioscoopCreator();
        RuimteModel cinema = factory.createRuimte(new Locatie(2, 2), "2,2", 0, "");

        Assertions.assertInstanceOf(BioscoopModel.class, cinema);
        Assertions.assertEquals(KamerType.CINEMA, cinema.getAreaType());
        Assertions.assertTrue(((BioscoopModel) cinema).getId().startsWith("C"), "ID moet beginnen met C");
    }

    @Test
    public void testFitnessCreator() {
        RuimteFactory factory = new FitnessCreator();
        RuimteModel fitness = factory.createRuimte(new Locatie(3, 3), "1,2", 0, "");

        Assertions.assertInstanceOf(FitnessModel.class, fitness);
        Assertions.assertEquals(KamerType.FITNESS, fitness.getAreaType());
        Assertions.assertTrue(((FitnessModel) fitness).getId().startsWith("F"), "ID moet beginnen met F");
    }

    @Test
    public void testRestaurantCreator() {
        RuimteFactory factory = new RestaurantCreator();
        RuimteModel restaurant = factory.createRuimte(new Locatie(4, 4), "3,2", 1, "");

        Assertions.assertInstanceOf(RestaurantModel.class, restaurant);
        Assertions.assertEquals(KamerType.RESTAURANT, restaurant.getAreaType());
        Assertions.assertTrue(((RestaurantModel) restaurant).getID().startsWith("R"), "ID moet beginnen met R");
        Assertions.assertEquals(0, (restaurant).getAantalGasten());
    }
}