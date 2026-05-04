package Controller.GastManagement;

import Controller.Layout.LayoutController;
import Model.Layout.Locatie;
import java.util.LinkedList;

public class PathFinder {
    // attributen
    private final LinkedList<Locatie> walkQueue;
    private final LayoutController layoutController;
    private Locatie currentLocatie;
    private final Locatie targetLocatie;

    // constructor
    public PathFinder(Locatie start, Locatie target, LayoutController controller) {
        this.currentLocatie = start;
        this.targetLocatie = target;
        this.layoutController = controller;
        this.walkQueue = new LinkedList<>();

        berekenRoute();
    }

    // bereken de route van de locatie nu naar de target locatie
    public void berekenRoute() {
        walkQueue.clear();

        // check of we naar een andere verdieping moeten
        if (currentLocatie.getY() != targetLocatie.getY()) {


            // locatie van de trap ophalen
            int trapX = layoutController.getView().getGridBreedte() - 1;

            Locatie trapHuidigeVerdieping = new Locatie(trapX, currentLocatie.getY());
            Locatie trapTargetVerdieping = new Locatie(trapX, targetLocatie.getY());

            // loop horizontaal naar de trap op de current verdieping
            planHorizontaalPad(currentLocatie, trapHuidigeVerdieping);

            // ga verticaal (met de trap) naar de target verdieping
            planVerticaalPad(trapHuidigeVerdieping, trapTargetVerdieping);

            // loop vanaf de trap op de nieuwe verdieping naar de target kamer
            planHorizontaalPad(trapTargetVerdieping, targetLocatie);

        } else {
            // als je al op de juiste verdieping bent loop direct horizontaal naar de target kamer
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

    // krijg de eerstvolgende stap in de walk queue
    public Locatie getNextStep() {
        if (walkQueue.isEmpty()) return null;

        // update de huidige locatie naar de stap die we nu gaan zetten
        this.currentLocatie = walkQueue.poll();
        return currentLocatie;
    }

    public boolean isBestemmingBereikt() {
        return walkQueue.isEmpty();
    }
}