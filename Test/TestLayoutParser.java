import Controller.Layout.LayoutParser;
import Model.Layout.RuimteData;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestLayoutParser {

    @Test
    void testParse_validJson() throws Exception {

        // JSON testdata (zelfde structuur als parser)
        String json = """
        [
          {
            "AreaType": "Room",
            "Position": "2,3",
            "Dimension": "4,2",
            "Capacity": 5,
            "Classification": "viersterren"
          }
        ]
        """;

        // tijdelijke file maken
        File tempFile = File.createTempFile("layout_test", ".json");

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(json);
        }

        // parser uitvoeren
        LayoutParser parser = new LayoutParser();
        List<RuimteData> ruimtes = parser.parse(tempFile.getAbsolutePath());


        assertNotNull(ruimtes);
        assertEquals(1, ruimtes.size());

        RuimteData data = ruimtes.get(0);

        assertEquals("Room", data.areaType);
        assertEquals(2, data.position.getX());
        assertEquals(3, data.position.getY());
        assertEquals("4,2", data.dimension);
        assertEquals(5, data.capacity);
        assertEquals("viersterren", data.classification);
    }

    @Test
    void testParse_defaultCapacity() throws Exception {

        // JSON zonder capacity de default word dan 9999
        String json = """
        [
          {
            "AreaType": "Fitness",
            "Position": "1,1",
            "Dimension": "2,2",
          }
        ]
        """;

        File tempFile = File.createTempFile("layout_test2", ".json");

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(json);
        }

        LayoutParser parser = new LayoutParser();
        List<RuimteData> ruimtes = parser.parse(tempFile.getAbsolutePath());

        assertEquals(1, ruimtes.size());

        RuimteData data = ruimtes.get(0);

        assertEquals("Fitness", data.areaType);
        assertEquals(9999, data.capacity); // DEFAULT_CAPACITY check
    }
}