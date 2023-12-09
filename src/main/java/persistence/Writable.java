package persistence;

import org.json.JSONObject;

/**
 * Interface to make sure class is writable to a JSON file.
 *
 * Credit: JsonSerializationDemo
 */
public interface Writable {

    // EFFECTS: returns this as a JSON object
    JSONObject toJson();

}
