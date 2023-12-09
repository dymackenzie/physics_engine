package persistence;

import model.Entity;
import model.helpers.Vector2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Reads JSON files and returns them as an ArrayList of type Entity
 *
 * Credit: JsonSerializationDemo
 */
public class JsonReader {

    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads entities from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ArrayList<Entity> read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseEntities(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses entities from JSON object and returns it
    private ArrayList<Entity> parseEntities(JSONObject jsonObject) {
        ArrayList<Entity> entities = new ArrayList<>();
        addEntities(entities, jsonObject);
        return entities;
    }

    // MODIFIES: entities
    // EFFECTS: parses entities from JSON object and adds them to an ArrayList of type Entity
    private void addEntities(ArrayList<Entity> entities, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("entities");
        for (Object json : jsonArray) {
            JSONObject nextEntity = (JSONObject) json;
            try {
                addEntity(entities, nextEntity);
            } catch (NoSuchFieldException e) {
                // no such field therefore cannot add entity
            }
        }
    }

    // MODIFIES: entities
    // EFFECTS: parses entity from JSON object and adds them to an ArrayList of type Entity
    private void addEntity(ArrayList<Entity> entities, JSONObject jsonObject) throws NoSuchFieldException {

        Vector2 position = new Vector2(jsonObject.getDouble("positionX"), jsonObject.getDouble("positionY"));
        Vector2 velocity = new Vector2(jsonObject.getDouble("velocityX"), jsonObject.getDouble("velocityY"));
        Vector2 force = new Vector2(jsonObject.getDouble("forceX"), jsonObject.getDouble("forceY"));

        double restitution = jsonObject.getDouble("restitution");
        double mass = jsonObject.getDouble("mass");
        double radius = jsonObject.getDouble("radius");
        double width = jsonObject.getDouble("width");
        double height = jsonObject.getDouble("height");
        String entityType = jsonObject.getString("entityType");

        Entity entity;

        if (entityType.equals("CIRCLE")) {
            entity = new Entity(position, restitution, mass, radius, width, height, Entity.EntityType.CIRCLE);
        } else if (entityType.equals("BOX")) {
            entity = new Entity(position, restitution, mass, radius, width, height, Entity.EntityType.BOX);
        } else {
            throw new NoSuchFieldException();
        }

        entity.setVelocity(velocity);
        entity.setForce(force);
        entities.add(entity);
    }

}
