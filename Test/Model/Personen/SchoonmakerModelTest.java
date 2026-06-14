package Model.Personen;

import Model.Entiteiten.SchoonmakerModel;
import Model.Entiteiten.TypePersoon;
import Model.Layout.Locatie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchoonmakerModelTest {

    SchoonmakerModel schoonmakerModel;

    @BeforeEach
    void setUp() {
        schoonmakerModel = new SchoonmakerModel(1, new Locatie(6, 9), new Locatie(6, 7), TypePersoon.SCHOONMAKER, new Locatie(9, 11));
    }

    @Test
    void isCleaningfalseTest() {

        //assign
        schoonmakerModel.setCleaning(false);

        //assert + act
        assertFalse(schoonmakerModel.isCleaning());

    }
    @Test
    void isCleaningNULLTest(){

        //assert + act
        assertFalse(schoonmakerModel.isCleaning());
    }
    @Test
    void isCleaningtrueTest(){
        //assign
        schoonmakerModel.setCleaning(true);

        //assert + act
        assertTrue(schoonmakerModel.isCleaning());
    }
}
