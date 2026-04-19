package Model.Layout;

import java.util.Objects;

public class Locatie {

    // attributen
    private int x;
    private int y;

    // constructor
    public Locatie(int X, int Y) {
        this.x = X;
        this.y = Y;
    }

    // getters en setters
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int X) { this.x = X; }
    public void setY(int Y) { this.y = Y; }

    // helpers voor locatie als de key in de grid hashmap
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locatie locatie = (Locatie) o;
        return x == locatie.x && y == locatie.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    // geeft de locatie terug als string
    @Override
    public String toString() {
        return x + "," + y;
    }
}