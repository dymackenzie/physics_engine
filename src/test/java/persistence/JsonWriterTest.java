package persistence;

import model.Entity;
import model.helpers.Vector2;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * CREDIT: JsonSerializationDemo
 */
public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            ArrayList<Entity> entities = new ArrayList<>();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyEntities() {
        try {
            ArrayList<Entity> entities = new ArrayList<>();
            JsonWriter writer = new JsonWriter("./data/testEmptyEntities.json");
            writer.open();
            writer.writeEntities(entities);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmptyEntities.json");
            entities = reader.read();
            assertEquals(0, entities.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralEntities() {
        JsonReader reader = new JsonReader("./data/testGeneralEntities.json");
        try {

            ArrayList<Entity> entities = new ArrayList<>();
            Entity c = new Entity(new Vector2(39.0, 19.0),
                    0.5, 0.5, 0.5, 0.5, 0.5, Entity.EntityType.CIRCLE);
            Entity b = new Entity(new Vector2(56.0, 20.0),
                    0.5, 0.5, 0.5, 0.5, 0.5, Entity.EntityType.BOX);
            entities.add(c);
            entities.add(b);
            JsonWriter writer = new JsonWriter("./data/testGeneralEntities.json");
            writer.open();
            writer.writeEntities(entities);
            writer.close();

            ArrayList<Entity> read = reader.read();
            assertEquals(2, read.size());
            checkEntity(39.0, 19.0, 0, 0, 0, 0,
                    0.5, 0.5, 0.5, 0.5, 0.5, "CIRCLE",
                    read.get(0));
            checkEntity(56.0, 20.0, 0, 0, 0, 0,
                    0.5, 0.5, 0.5, 0.5, 0.5, "BOX",
                    read.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
