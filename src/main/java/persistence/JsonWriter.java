package persistence;

import model.Entity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Writes JSON files with an ArrayList of type Entity
 *
 * Credit: JsonSerializationDemo
 */
public class JsonWriter {

    private static final int TAB = 4;
    private final String destination;
    private PrintWriter writer;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of ArrayList of type Entity to file
    public void writeEntities(ArrayList<Entity> entities) {

        JSONArray jsonArray = new JSONArray();
        for (Entity entity : entities) {
            jsonArray.put(entity.toJson());
        }

        JSONObject json = new JSONObject();
        json.put("entities", jsonArray);
        saveToFile(json.toString(TAB));

    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

}
