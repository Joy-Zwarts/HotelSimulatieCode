package Controller.GastManagement;

import Controller.Layout.LayoutController;
import Controller.Layout.Locatie;
import java.util.LinkedList;

public class PathFinder {
    private final LinkedList<Locatie> walkQueue;
    private final LayoutController layoutController;
    private Locatie currentLocatie;
    private final Locatie targetLocatie;

    public PathFinder(Locatie start, Locatie target, LayoutController controller) {
        this.currentLocatie = start;
        this.targetLocatie = target;
        this.layoutController = controller;
        this.walkQueue = new LinkedList<>();

        berekenRoute();
    }

    public void berekenRoute() {
        walkQueue.clear();

        // Stap 1: Check of we naar een andere verdieping moeten
        if (currentLocatie.getY() != targetLocatie.getY()) {

            // De trap is altijd het meest rechter vakje.
            // We halen de breedte op uit de controller/view.
            int trapX = layoutController.getView().getGridBreedte() - 1;

            Locatie trapHuidigeVerdieping = new Locatie(trapX, currentLocatie.getY());
            Locatie trapTargetVerdieping = new Locatie(trapX, targetLocatie.getY());

            // 1a. Loop horizontaal naar de trap op de huidige verdieping
            planHorizontaalPad(currentLocatie, trapHuidigeVerdieping);

            // 1b. Ga verticaal (met de trap) naar de target verdieping
            planVerticaalPad(trapHuidigeVerdieping, trapTargetVerdieping);

            // 1c. Loop vanaf de trap op de nieuwe verdieping naar de target kamer
            planHorizontaalPad(trapTargetVerdieping, targetLocatie);

        } else {
            // We zijn al op de juiste verdieping, loop direct horizontaal
            planHorizontaalPad(currentLocatie, targetLocatie);
        }
    }


    private void planHorizontaalPad(Locatie start, Locatie eind) {
        int tempX = start.getX();
        int y = start.getY();

        while (tempX != eind.getX()) {
            if (tempX < eind.getX()) tempX++;
            else tempX--;
            walkQueue.add(new Locatie(tempX, y));
        }
    }

    private void planVerticaalPad(Locatie start, Locatie eind) {
        int x = start.getX();
        int tempY = start.getY();

        while (tempY != eind.getY()) {
            if (tempY < eind.getY()) tempY++;
            else tempY--;
            walkQueue.add(new Locatie(x, tempY));
        }
    }

    public Locatie getNextStep() {
        if (walkQueue.isEmpty()) return null;

        // Update de huidige locatie naar de stap die we nu gaan zetten
        this.currentLocatie = walkQueue.poll();
        return currentLocatie;
    }

    public boolean isBestemmingBereikt() {
        return walkQueue.isEmpty();
    }
}