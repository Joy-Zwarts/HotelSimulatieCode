public class Ruimte {
    private String areaType;
    private String position;
    private String dimension;

    public Ruimte(String areaType, String position, String dimension) {
        this.areaType = areaType;
        this.position = position;
        this.dimension = dimension;
    }

    public void printInfo() {
        System.out.println(this);
        System.out.println("Area: " + this.areaType);
        System.out.println("Position: " + this.position);
        System.out.println("Dimension: " + this.dimension);
        System.out.println("-----------------");
    }

    public String getAreaType() {
        return this.areaType;
    }

    public int getPositionX() {
        String[] pos = this.position.split(",");
        int x = Integer.parseInt(pos[0].trim());
        return x;
    }

    public int getPositionY() {
        String[] pos = this.position.split(",");
        int y = Integer.parseInt(pos[1].trim());
        return y;
    }

    public void setPositionY(int y) {
        int var10000 = this.getPositionX();
        String newpositionY = var10000 + "," + y;
        this.position = newpositionY;
    }

    public int getDimensionW() {
        String[] dim = this.dimension.split(",");
        int w = Integer.parseInt(dim[0].trim());
        return w;
    }

    public int getDimensionH() {
        String[] dim = this.dimension.split(",");
        int h = Integer.parseInt(dim[1].trim());
        return h;
    }
}
