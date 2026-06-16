package Model.Entiteiten;

import Model.Layout.Locatie;

import java.util.LinkedList;
import java.util.Queue;

public class LiftModel extends EntiteitenModel {
    // attributen
    private int verdieping;
    private boolean beschikbaar;
    private final Queue<Integer> targetYRequests;

    // constructors
    public LiftModel(int id, Locatie position, Locatie targetLocatie, int verdieping, boolean beschikbaar) {
        super(id, position, targetLocatie);
        this.verdieping = verdieping;
        this.beschikbaar = beschikbaar;
        this.targetYRequests = new LinkedList<>();
    }

    public void voegVerzoekToe(int yVerdieping) {
        if (!targetYRequests.contains(yVerdieping)) {
            targetYRequests.add(yVerdieping);
        }
    }
    public Integer peekVolgendeVerzoek() {
        return targetYRequests.peek();
    }
    public void verwijderHuidigeVerzoek() {
        targetYRequests.poll();
    }
    public boolean heeftVerzoeken() {
        return !targetYRequests.isEmpty();
    }

    // getters en setters
    public int getVerdieping() {
        return this.verdieping;
    }

    public void setVerdieping(int verdieping) {
        this.verdieping = verdieping;
    }

    public boolean isBeschikbaar() {
        return this.beschikbaar;
    }

    public void setBeschikbaar(boolean beschikbaar) {
        this.beschikbaar = beschikbaar;
    }

}
