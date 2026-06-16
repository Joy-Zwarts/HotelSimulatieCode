package Model.Entiteiten;

import Model.Layout.Locatie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiftModel extends EntiteitenModel {
    private int verdieping;
    private boolean beschikbaar;

    // VERANDERD: Van Queue naar een List om vrij te kunnen zoeken en sorteren
    private final List<Integer> verzoeken;
    private boolean richtingOmhoog; // Onthoudt welke kant de lift op reist

    public LiftModel(int id, Locatie position, Locatie targetLocatie, int verdieping, boolean beschikbaar) {
        super(id, position, targetLocatie);
        this.verdieping = verdieping;
        this.beschikbaar = beschikbaar;
        this.verzoeken = new ArrayList<>();
        this.richtingOmhoog = true; // We beginnen met de aanname omhoog
    }

    public void voegVerzoekToe(int yVerdieping) {
        if (!verzoeken.contains(yVerdieping)) {
            verzoeken.add(yVerdieping);
        }
    }

    public boolean heeftVerzoeken() {
        return !verzoeken.isEmpty();
    }

    // NIEUW: Bepaal de logische VOLGENDE verdieping op basis van de huidige rijrichting
    public Integer bepaalVolgendeBestemming() {
        if (verzoeken.isEmpty()) return null;

        int huidigeY = getLocatie().getY();

        // Filter verzoeken in de huidige richting
        List<Integer> verzoekenInRichting = new ArrayList<>();
        for (int reqY : verzoeken) {
            if (richtingOmhoog && reqY < huidigeY) { // Omhoog = Y wordt kleiner
                verzoekenInRichting.add(reqY);
            } else if (!richtingOmhoog && reqY > huidigeY) { // Omlaag = Y wordt groter
                verzoekenInRichting.add(reqY);
            }
        }

        // Als er verzoeken zijn in de huidige richting, pak de SCHEERSTE (dichtstbijzijnde)
        if (!verzoekenInRichting.isEmpty()) {
            if (richtingOmhoog) {
                // Omhoog: we willen de HOOGSTE Y die kleiner is dan huidigeY (dus het dichtstbij)
                return Collections.max(verzoekenInRichting);
            } else {
                // Omlaag: we willen de LAAGSTE Y die groter is dan huidigeY (dus het dichtstbij)
                return Collections.min(verzoekenInRichting);
            }
        }

        // Geen verzoeken meer in deze richting? Draai de richting om!
        richtingOmhoog = !richtingOmhoog;

        // Kijk opnieuw (omgekeerde richting). Als er nog iets is, pak de uiterste daarvan
        if (!verzoeken.isEmpty()) {
            if (richtingOmhoog) {
                return Collections.max(verzoeken); // Dichtstbijzijnde van onderen
            } else {
                return Collections.min(verzoeken); // Dichtstbijzijnde van boven
            }
        }

        return null;
    }

    // NIEUW: Verwijder specifiek de verdieping waar de lift NU is aangekomen
    public void verwijderVerzoek(int yVerdieping) {
        verzoeken.remove(Integer.valueOf(yVerdieping));
    }

    // Getters & Setters
    public boolean isRichtingOmhoog() { return richtingOmhoog; }
    public void setRichtingOmhoog(boolean richtingOmhoog) { this.richtingOmhoog = richtingOmhoog; }
    public int getVerdieping() { return this.verdieping; }
    public void setVerdieping(int verdieping) { this.verdieping = verdieping; }
    public boolean isBeschikbaar() { return this.beschikbaar; }
    public void setBeschikbaar(boolean beschikbaar) { this.beschikbaar = beschikbaar; }
}