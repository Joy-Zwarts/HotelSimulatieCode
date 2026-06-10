package Controller.PersoonManagement;

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
    }

    // bereken de route van de locatie nu naar de target locatie
    // bereken de route van de locatie nu naar de target locatie
    // bereken de route van de locatie nu naar de target locatie
    public void berekenRoute() {
        walkQueue.clear();

        // check of we naar een andere verdieping moeten
        if (currentLocatie.getY() != targetLocatie.getY()) {

            // We dwingen de gasten om ALTIJD de lift te pakken (kolom 0)
            int liftX = 0;

            System.out.println("PathFinder: Gast MOET de lift nemen op X: " + liftX);

            // Maak de locaties aan voor de lift op beide verdiepingen
            Locatie liftHuidigeVerdieping = new Locatie(liftX, currentLocatie.getY());
            Locatie liftTargetVerdieping = new Locatie(liftX, targetLocatie.getY());

            // 1. Loop eerst horizontaal vanaf de huidige kamer naar de lift (X = 0)
            planHorizontaalPad(currentLocatie, liftHuidigeVerdieping);

            // 2. Ga verticaal met de lift naar de juiste verdieping
            planVerticaalPad(liftHuidigeVerdieping, liftTargetVerdieping);

            // 3. Loop vanaf de lift op de nieuwe verdieping horizontaal naar de target kamer
            planHorizontaalPad(liftTargetVerdieping, targetLocatie);

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

    // je kan hiermee de volgende stap bekijken zonder dat de stap uit de wachtrij weg wordt gehaald
    public Locatie peekNextStep() {
        return walkQueue.peek();
    }

    public boolean isBestemmingBereikt() {
        return walkQueue.isEmpty();
    }
}