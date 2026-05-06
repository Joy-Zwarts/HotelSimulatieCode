import Controller.Layout.LayoutGeladen;
import Controller.Layout.LayoutLoader;
import Controller.Layout.LayoutController;
import Controller.GastManagement.NewKamer;
import Model.Layout.LayoutModel;
import Model.Layout.Locatie;
import Model.Layout.RuimteData;
import Model.Ruimtes.KamerType;
import Model.Systeem.DarkModeModel;
import View.Systeem.HotelSimulatieView;
import hotelevents.HotelEventManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TestLayoutLoader {

    private LayoutLoader layoutLoader;
    private HotelEventManager manager;
    private HotelSimulatieView view;
    private LayoutModel model;

    @BeforeEach
    void setUp() {
        manager = new HotelEventManager();
        view = new HotelSimulatieView(new DarkModeModel());
        model = new LayoutModel();
        layoutLoader = new LayoutLoader(manager, view, model, null, view);
    }

    @Test
    void testLoadLayoutSuccess() {
        LayoutLoader spyLoader = new LayoutLoader(manager, view, model, null, view) {
            @Override
            protected File getFileFromPicker() {
                File tempFile = new File("test_layout.json");
                try {
                    // De parser verwacht blijkbaar strings voor Position en Dimension
                    // We gebruiken "1,1" in plaats van een object {x:1, y:1}
                    String jsonContent = "[" +
                            "{" +
                            "\"AreaType\": \"Room\"," +
                            "\"Position\": \"1,1\"," + // Veranderd naar String
                            "\"Dimension\": \"1,1\"," +
                            "\"Classification\": \"1 Star\"," +
                            "\"Capacity\": 1" +
                            "}" +
                            "]";
                    java.nio.file.Files.writeString(tempFile.toPath(), jsonContent);
                    tempFile.deleteOnExit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return tempFile;
            }
            @Override
            protected void showMessage(String msg) {}
        };

        LayoutModel result = spyLoader.loadLayout();

        assertNotNull(result, "Model zou niet null moeten zijn bij een geldig JSON bestand");
        assertFalse(result.getRuimtes().isEmpty(), "Er zou minimaal één kamer geladen moeten zijn");
    }

    @Test
    void testLayoutGeladenListener() {
        AtomicInteger callCount = new AtomicInteger(0);

        LayoutLoader spyLoader = new LayoutLoader(manager, view, model, null, view) {
            @Override
            protected File getFileFromPicker() {
                File tempFile = new File("test_layout_listener.json");
                try {
                    java.nio.file.Files.writeString(tempFile.toPath(), "[]");
                    tempFile.deleteOnExit();
                } catch (Exception e) {}
                return tempFile;
            }
            @Override
            protected void showMessage(String msg) {}
        };

        spyLoader.setNewLayoutListener(controller -> callCount.incrementAndGet());
        spyLoader.loadLayout();

        assertNotNull(spyLoader.getController(), "Controller mag niet null zijn na laden");
        assertEquals(1, callCount.get(), "Listener moet aangeroepen zijn");
    }

    @Test
    void testLoadLayoutWithException() {
        // We maken een Spy die een bestand geeft dat niet bestaat om de catch-block te testen
        LayoutLoader spyLoader = new LayoutLoader(manager, view, model, null, view) {
            @Override
            protected File getFileFromPicker() {
                return new File("niet_bestaand_bestand.json");
            }
            @Override
            protected void showMessage(String msg) {}
        };

        LayoutModel result = spyLoader.loadLayout();
        assertNull(result, "Resultaat moet null zijn bij een fout");
    }

    @Test
    void testMaakKamerAlleTypes() throws Exception {
        Method method = LayoutLoader.class.getDeclaredMethod("maakKamer", RuimteData.class);
        method.setAccessible(true);

        String[] types = {"Room", "Restaurant", "Fitness", "Cinema"};

        for (String type : types) {
            RuimteData data = new RuimteData();
            data.areaType = type;
            data.position = new Locatie(1, 1);
            data.dimension = "1,1";
            data.classification = "1 Star";
            data.capacity = 1;

            method.invoke(layoutLoader, data);
        }
    }

    @Test
    void testNewRoomListener() throws Exception {
        AtomicBoolean listenerCalled = new AtomicBoolean(false);
        layoutLoader.setNewRoomListener(ruimte -> {
            if (ruimte.getAreaType() == KamerType.ROOM) {
                listenerCalled.set(true);
            }
        });

        RuimteData data = new RuimteData();
        data.areaType = "Room";
        data.position = new Locatie(0,0);
        data.classification = "3 Star";

        Method method = LayoutLoader.class.getDeclaredMethod("maakKamer", RuimteData.class);
        method.setAccessible(true);
        method.invoke(layoutLoader, data);

        assertTrue(listenerCalled.get());
    }

    @Test
    void testMaakKamerUnknownType() {
        RuimteData data = new RuimteData();
        data.areaType = "Unknown";

        try {
            Method method = LayoutLoader.class.getDeclaredMethod("maakKamer", RuimteData.class);
            method.setAccessible(true);
            method.invoke(layoutLoader, data);
            fail("Zou een exception moeten gooien");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }
}
