package Model;

public class Ruimte {

        private String areaType;
        private String position;
        private String dimension;

        // constructor

        public Ruimte(String areaType, String position, String dimension) {
            this.areaType = areaType; // zet areatype
            this.position = position; // zet position
            this.dimension = dimension; // zet dimension
        }

        //  print alle info over de kamer
        public void printInfo() {
            System.out.println(this);
            System.out.println("Area: " + areaType);
            System.out.println("Position: " + position);
            System.out.println("Dimension: " + dimension);
            System.out.println("-----------------");

        }

        // getters en setters

        public String getAreaType() {
            return areaType;
        }

        public int getPositionX() {
            String[] pos = position.split(",");
            int x = Integer.parseInt(pos[0].trim());
            return x;
        }

        public int getPositionY() {
            String[] pos = position.split(",");
            int y = Integer.parseInt(pos[1].trim());
            return y;
        }

        public void setPositionY(int y) {
            String newpositionY = getPositionX() + "," + y;
            this.position = newpositionY;
        }

        public int getDimensionW() {
            String[] dim = dimension.split(",");
            int w = Integer.parseInt(dim[0].trim());
            return w;
        }

        public int getDimensionH() {
            String[] dim = dimension.split(",");
            int h = Integer.parseInt(dim[1].trim());
            return h;
        }
    }