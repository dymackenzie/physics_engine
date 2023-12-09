package persistence;

import model.Entity;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * CREDIT: JsonSerializationDemo
 */
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ArrayList<Entity> entities = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyEntities() {
        JsonReader reader = new JsonReader("./data/testEmptyEntities.json");
        try {
            ArrayList<Entity> entities = reader.read();
            assertEquals(0, entities.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralEntities() {
        JsonReader reader = new JsonReader("./data/testGeneralEntities.json");
        try {
            ArrayList<Entity> entities = reader.read();
            assertEquals(2, entities.size());
            checkEntity(39.0, 19.0, 0, 0, 0, 0,
                    0.5, 0.5, 0.5, 0.5, 0.5, "CIRCLE",
                    entities.get(0));
            checkEntity(56.0, 20.0, 0, 0, 0, 0,
                    0.5, 0.5, 0.5, 0.5, 0.5, "BOX",
                    entities.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderInvalidEntities() {
        JsonReader reader = new JsonReader("./data/testInvalidEntities.json");
        try {
            ArrayList<Entity> entities = reader.read();
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
