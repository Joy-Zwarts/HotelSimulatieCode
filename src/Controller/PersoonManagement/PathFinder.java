package Controller.PersoonManagement;

import Controller.Layout.LayoutController;
import Model.Entiteiten.LiftModel;
import Model.Layout.Locatie;
import java.util.LinkedList;
import java.util.Random; // 1. Random import toegevoegd

public class PathFinder {
    // attributen
    private final LinkedList<Locatie> walkQueue;
    private final LayoutController layoutController;
    private Locatie currentLocatie;
    private final Locatie targetLocatie;
    private final Random random; // 2. Random attribuut toegevoegd

    // constructor
    public PathFinder(Locatie start, Locatie target, LayoutController controller) {
        this.currentLocatie = start;
        this.targetLocatie = target;
        this.layoutController = controller;
        this.walkQueue = new LinkedList<>();
        this.random = new Random(); // 3. Random geïnitialiseerd

        berekenRoute();
    }

    public void berekenRoute() {
        walkQueue.clear();

        // Check of we naar een andere verdieping moeten
        if (currentLocatie.getY() != targetLocatie.getY()) {

            int trapX = layoutController.getView().getGridBreedte() - 1;
            int liftX = 0;

            // --- DYNAMISCHE ROUTE BEREKENING ---
            boolean kiesTrap = true; // Default fallback

            // Bereken ticks voor de trap
            int verticaleAfstand = Math.abs(currentLocatie.getY() - targetLocatie.getY());
            int trapTicks = verticaleAfstand * BeweegHelper.getTrapVertragingTicks();
            // Voeg de horizontale loopafstand naar de trap toe (1 tick per stap)
            trapTicks += Math.abs(currentLocatie.getX() - trapX) + Math.abs(trapX - targetLocatie.getX());

            // Bereken ticks voor de lift
            if (layoutController.getLiftController() != null && layoutController.getLiftController().getLiftModel() != null) {
                LiftModel lift = layoutController.getLiftController().getLiftModel();

                // Ticks die de lift erover doet + horizontale loopafstand naar/van de lift
                int liftTicks = lift.berekenVerwachteTicks(currentLocatie.getY(), targetLocatie.getY());
                liftTicks += Math.abs(currentLocatie.getX() - liftX) + Math.abs(liftX - targetLocatie.getX());

                // Kies de snelste optie
                if (liftTicks < trapTicks) {
                    kiesTrap = false;
                }
            }

            // Bepaal de transport X op basis van de berekening
            int transportX = kiesTrap ? trapX : liftX;

            Locatie transportHuidigeVerdieping = new Locatie(transportX, currentLocatie.getY());
            Locatie transportTargetVerdieping = new Locatie(transportX, targetLocatie.getY());

            // Plan de gekozen route
            planHorizontaalPad(currentLocatie, transportHuidigeVerdieping);
            planVerticaalPad(transportHuidigeVerdieping, transportTargetVerdieping);
            planHorizontaalPad(transportTargetVerdieping, targetLocatie);

        } else {
            // Als je al op de juiste verdieping bent, loop direct horizontaal
            planHorizontaalPad(currentLocatie, targetLocatie);
        }
    }

    // plan het horizontale pad stap voor stap van de beginlocatie naar de eindlocatie
    private void planHorizontaalPad(Locatie start, Locatie eind) {
        int tempX = start.getX();
        int y = start.getY();

        while (tempX != eind.getX()) {
            if (tempX < eind.getX()) tempX++;
            else tempX--;
            walkQueue.add(new Locatie(tempX, y));
        }
    }

    // plan het verticale pad stap voor stap van de beginlocatie naar de eindlocatie
    private void planVerticaalPad(Locatie start, Locatie eind) {

        int x = start.getX();
        int tempY = start.getY();

        while (tempY != eind.getY()) {
            if (tempY < eind.getY()) tempY++;
            else tempY--;
            walkQueue.add(new Locatie(x, tempY));
        }
    }

    // getters & setters


    public LayoutController getLayoutController() {
        return this.layoutController;
    }

    // krijg de eerstvolgende stap in de walk queue
    public Locatie getNextStep() {
        if (walkQueue.isEmpty()) return null;

        // update de huidige locatie naar de stap die we nu gaan zetten
        this.currentLocatie = walkQueue.poll();
        return currentLocatie;
    }

    // je kan hiermee de volgende stap bekijken zonder dat de stap uit de wachtrij weg wordt gehaald
    public Locatie peekNextStep() {
        return walkQueue.peek();
    }

    public boolean isBestemmingBereikt() {
        return walkQueue.isEmpty();
    }
}